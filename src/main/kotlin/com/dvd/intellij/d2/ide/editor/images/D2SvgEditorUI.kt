package com.dvd.intellij.d2.ide.editor.images

import com.dvd.intellij.d2.ide.utils.*
import com.intellij.ide.CopyPasteDelegator
import com.intellij.ide.CopyPasteSupport
import com.intellij.ide.CopyProvider
import com.intellij.ide.DeleteProvider
import com.intellij.ide.util.DeleteHandler
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.ui.JBColor
import com.intellij.ui.PopupHandler
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBLayeredPane
import com.intellij.ui.components.Magnificator
import com.intellij.ui.components.panels.NonOpaquePanel
import com.intellij.util.ObjectUtils
import com.intellij.util.ui.JBUI
import org.intellij.images.ImagesBundle
import org.intellij.images.editor.ImageDocument
import org.intellij.images.editor.ImageEditor
import org.intellij.images.editor.ImageZoomModel
import org.intellij.images.options.Options
import org.intellij.images.options.OptionsManager
import org.intellij.images.options.ZoomOptions
import org.intellij.images.thumbnail.actionSystem.ThumbnailViewActions
import org.intellij.images.thumbnail.actions.ShowBorderAction
import org.intellij.images.ui.ImageComponent
import org.intellij.images.ui.ImageComponentDecorator
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Dimension
import java.awt.Point
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.event.*
import java.awt.image.BufferedImage
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

private const val IMAGE_PANEL = "image"
private const val ERROR_PANEL = "error"
private const val ZOOM_FACTOR_PROP = "D2Editor.zoomFactor"

// https://github.com/JetBrains/intellij-community/blob/master/images/src/org/intellij/images/editor/impl/ImageEditorUI.java
class D2SvgEditorUI(
  val editor: ImageEditor,
  private val isEmbedded: Boolean,
  isOpaque: Boolean,
  private val fileEditor: D2FileEditorImpl,
) : JPanel(), DataProvider, CopyProvider, ImageComponentDecorator, Disposable {

  private val deleteProvider: DeleteProvider
  private val copyPasteSupport: CopyPasteSupport?
  internal val zoomModel: ImageZoomModel = ImageZoomModelImpl()
  private val wheelAdapter: ImageWheelAdapter = ImageWheelAdapter()
  private val changeListener: ChangeListener = DocumentChangeListener()
  private val imageComponent = ImageComponent()
  private val contentPanel: JPanel
  private var infoLabel: JLabel? = null
  private val myScrollPane: JScrollPane

  init {
    imageComponent.addPropertyChangeListener(ZOOM_FACTOR_PROP) {
      imageComponent.zoomFactor = getZoomModel().zoomFactor
    }
    val options = OptionsManager.getInstance().options
    val editorOptions = options.editorOptions
    options.addPropertyChangeListener(OptionsChangeListener(), this)

    copyPasteSupport = CopyPasteDelegator(editor.project, this)
    deleteProvider = DeleteHandler.DefaultDeleteProvider()

    val document = imageComponent.document
    document.addChangeListener(changeListener)

    // Set options
    val chessboardOptions = editorOptions.transparencyChessboardOptions
    val gridOptions = editorOptions.gridOptions
    imageComponent.transparencyChessboardCellSize = chessboardOptions.cellSize
    imageComponent.transparencyChessboardWhiteColor = chessboardOptions.whiteColor
    imageComponent.setTransparencyChessboardBlankColor(chessboardOptions.blackColor)
    imageComponent.gridLineZoomFactor = gridOptions.lineZoomFactor
    imageComponent.gridLineSpan = gridOptions.lineSpan
    imageComponent.gridLineColor = gridOptions.lineColor
    imageComponent.isBorderVisible = ShowBorderAction.isBorderVisible()

    // Create layout
    val view = ImageContainerPane(imageComponent)
    PopupHandler.installPopupMenu(view, D2_GROUP_POPUP, D2_ACTION_PLACE)
    view.addMouseListener(FocusRequester())

    myScrollPane = ScrollPaneFactory.createScrollPane(view, true)
    myScrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
    myScrollPane.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED

    // Zoom by wheel listener
    myScrollPane.addMouseWheelListener(wheelAdapter)

    // Construct UI
    layout = BorderLayout()

    var toolbarPanel: JComponent? = null
    if (!isEmbedded) {
      val actionManager = ActionManager.getInstance()
      val actionGroup = actionManager.getAction(D2_GROUP_TOOLBAR) as ActionGroup
      val actionToolbar = actionManager.createActionToolbar(D2_ACTION_PLACE, actionGroup, true)
      actionToolbar.targetComponent = this

      toolbarPanel = actionToolbar.component
      toolbarPanel.background = JBColor.lazy { background }
      toolbarPanel.addMouseListener(FocusRequester())
    }
    val errorLabel = JLabel(
      ImagesBundle.message("error.broken.image.file.format"),
      Messages.getErrorIcon(), SwingConstants.CENTER
    )

    val errorPanel = NonOpaquePanel(BorderLayout())
    errorPanel.add(errorLabel, BorderLayout.CENTER)

    contentPanel = JPanel(CardLayout())
    contentPanel.add(myScrollPane, IMAGE_PANEL)
    contentPanel.add(errorPanel, ERROR_PANEL)

    val topPanel = NonOpaquePanel(BorderLayout())
    if (!isEmbedded) {
      toolbarPanel?.let { topPanel.add(it, BorderLayout.WEST) }
      infoLabel = JLabel(null as String?, SwingConstants.RIGHT).also {
        it.border = JBUI.Borders.emptyRight(2)
        topPanel.add(it, BorderLayout.EAST)
      }
    }

    add(topPanel, BorderLayout.NORTH)
    add(contentPanel, BorderLayout.CENTER)

    myScrollPane.addComponentListener(object : ComponentAdapter() {
      override fun componentResized(e: ComponentEvent) {
        updateZoomFactor()
      }
    })

    if (!isOpaque) {
      // isOpaque = false
      contentPanel.isOpaque = false
      myScrollPane.isOpaque = false
      myScrollPane.viewport.isOpaque = false
    }

    background = JBColor.lazy {
      ObjectUtils.notNull(
        EditorColorsManager.getInstance().globalScheme.getColor(EditorColors.PREVIEW_BACKGROUND),
        EditorColorsManager.getInstance().globalScheme.defaultBackground,
      )
    }

    updateInfo()
  }

  private fun updateInfo() {
    if (isEmbedded) return

    val document = imageComponent.document
    val image = document.value
    if (image != null) {
      val file = fileEditor.generatedFile ?: return
      infoLabel?.text = D2Bundle.message("d2.image.info", image.width, image.height, StringUtil.formatFileSize(file.length))
    } else {
      infoLabel?.text = null
    }
  }

  override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

  @Suppress("unused")
  fun getContentComponent(): JComponent = contentPanel
  fun getImageComponent(): ImageComponent = imageComponent

  override fun dispose() {
    imageComponent.removeMouseWheelListener(wheelAdapter)
    imageComponent.document.removeChangeListener(changeListener)

    removeAll()
  }

  override fun setTransparencyChessboardVisible(visible: Boolean) {
    imageComponent.isTransparencyChessboardVisible = visible
    repaint()
  }

  override fun isTransparencyChessboardVisible(): Boolean = imageComponent.isTransparencyChessboardVisible

  // Disable for thumbnail action
  override fun isEnabledForActionPlace(place: String): Boolean = ThumbnailViewActions.ACTION_PLACE != place

  override fun setGridVisible(visible: Boolean) {
    imageComponent.isGridVisible = visible
    repaint()
  }

  override fun isGridVisible(): Boolean = imageComponent.isGridVisible
  override fun getZoomModel(): ImageZoomModel = zoomModel

  fun setImageProvider(imageProvider: ImageDocument.ScaledImageProvider?, format: String?) {
    val document = imageComponent.document
    val previousImage = document.value
    document.setValue(imageProvider)
    if (imageProvider == null) return
    document.format = format

    if (previousImage == null || !zoomModel.isZoomLevelChanged) {
      val zoomOptions = zoomOptions
      if (!(zoomOptions.isSmartZooming && updateZoomFactor())) {
        zoomModel.zoomFactor = 1.0
      }
    }
  }

  private fun updateZoomFactor(): Boolean {
    val zoomOptions = zoomOptions
    if (zoomOptions.isSmartZooming && !zoomModel.isZoomLevelChanged) {
      val smartZoomFactor = getSmartZoomFactor(zoomOptions)
      if (smartZoomFactor != null) {
        zoomModel.zoomFactor = smartZoomFactor
        return true
      }
    }
    return false
  }

  private val zoomOptions: ZoomOptions
    get() = editor.zoomModel.customZoomOptions ?: OptionsManager.getInstance().options.editorOptions.zoomOptions

  private inner class ImageContainerPane(private val imageComponent: ImageComponent) : JBLayeredPane() {
    init {
      add(imageComponent)
      putClientProperty(Magnificator.CLIENT_PROPERTY_KEY, Magnificator { scale, at ->
        val locationBefore = imageComponent.location
        val model = editor.zoomModel
        val factor = model.zoomFactor
        model.zoomFactor = scale * factor
        Point(
          ((at.x - max(if (scale > 1.0) locationBefore.x else 0, 0)) * scale).toInt(),
          ((at.y - max(if (scale > 1.0) locationBefore.y else 0, 0)) * scale).toInt()
        )
      })
    }

    private fun centerComponents() {
      val bounds = bounds
      val point = imageComponent.location
      // in embedded mode images should be left-side aligned
      point.x = (bounds.width - imageComponent.width) / 2
      point.y = (bounds.height - imageComponent.height) / 2
      imageComponent.location = point
    }

    override fun invalidate() {
      centerComponents()
      super.invalidate()
    }

    override fun getPreferredSize(): Dimension = imageComponent.size
  }

  private inner class ImageWheelAdapter : MouseWheelListener {
    override fun mouseWheelMoved(e: MouseWheelEvent) {
      val options = OptionsManager.getInstance().options
      val editorOptions = options.editorOptions
      val zoomOptions = editorOptions.zoomOptions
      if (zoomOptions.isWheelZooming && e.isControlDown) {
        val rotation = e.wheelRotation
        val oldZoomFactor = zoomModel.zoomFactor
        val oldPosition = myScrollPane.viewport.viewPosition
        if (rotation > 0) {
          zoomModel.zoomOut()
        } else if (rotation < 0) {
          zoomModel.zoomIn()
        }

        // reset view, otherwise view size is not obtained correctly sometimes
        val view = myScrollPane.viewport.view
        myScrollPane.viewport = null
        myScrollPane.setViewportView(view)
        if (oldZoomFactor > 0 && rotation != 0) {
          val mousePoint = e.point
          val zoomChange = zoomModel.zoomFactor / oldZoomFactor
          val newPosition = Point(
            max(0.0, (oldPosition.getX() + mousePoint.getX()) * zoomChange - mousePoint.getX()).toInt(),
            max(0.0, (oldPosition.getY() + mousePoint.getY()) * zoomChange - mousePoint.getY()).toInt()
          )
          myScrollPane.viewport.viewPosition = newPosition
        }
        e.consume()
      }
    }
  }

  private inner class ImageZoomModelImpl : ImageZoomModel {
    private var myCustomZoomOptions: ZoomOptions? = null
    private var myZoomLevelChanged = false
    private val IMAGE_MAX_ZOOM_FACTOR = Double.MAX_VALUE
    private var zoomFactor = 0.0

    override fun getZoomFactor(): Double = zoomFactor

    override fun setZoomFactor(zoomFactor: Double) {
      val oldZoomFactor = getZoomFactor()
      if (oldZoomFactor.compareTo(zoomFactor) == 0) return
      this.zoomFactor = zoomFactor

      // Change current size
      updateImageComponentSize()
      revalidate()
      repaint()
      myZoomLevelChanged = false
      imageComponent.firePropertyChange(ZOOM_FACTOR_PROP, oldZoomFactor, zoomFactor)
    }

    private val maximumZoomFactor: Double
      get() {
        val factor = IMAGE_MAX_ZOOM_FACTOR
        return min(factor, ImageZoomModel.MACRO_ZOOM_LIMIT)
      }
    private val minimumZoomFactor: Double
      get() {
        val bounds = imageComponent.document.bounds
        val factor = if (bounds != null) 1.0 / bounds.getWidth() else 0.0
        return max(factor, ImageZoomModel.MICRO_ZOOM_LIMIT)
      }

    override fun fitZoomToWindow() {
      val zoomOptions: ZoomOptions = zoomOptions
      val smartZoomFactor = getSmartZoomFactor(zoomOptions)
      if (smartZoomFactor != null) {
        zoomModel.zoomFactor = smartZoomFactor
      } else {
        zoomModel.zoomFactor = 1.0
      }
      myZoomLevelChanged = false
    }

    override fun zoomOut() {
      setZoomFactor(nextZoomOut)
      myZoomLevelChanged = true
    }

    override fun zoomIn() {
      setZoomFactor(nextZoomIn)
      myZoomLevelChanged = true
    }// Micro

    // Macro
    private val nextZoomOut: Double
      get() {
        var factor = getZoomFactor()
        if (factor > 1.0) {
          // Macro
          factor /= ImageZoomModel.MACRO_ZOOM_RATIO
          factor = max(factor, 1.0)
        } else {
          // Micro
          factor /= ImageZoomModel.MICRO_ZOOM_RATIO
        }
        return max(factor, minimumZoomFactor)
      }// Micro

    // Macro
    private val nextZoomIn: Double
      get() {
        var factor = getZoomFactor()
        if (factor >= 1.0) {
          // Macro
          factor *= ImageZoomModel.MACRO_ZOOM_RATIO
        } else {
          // Micro
          factor *= ImageZoomModel.MICRO_ZOOM_RATIO
          factor = min(factor, 1.0)
        }
        return min(factor, maximumZoomFactor)
      }

    override fun canZoomOut(): Boolean = // Ignore small differences caused by floating-point arithmetic.
      getZoomFactor() - 1.0e-14 > minimumZoomFactor

    override fun canZoomIn(): Boolean = getZoomFactor() < maximumZoomFactor

    override fun setZoomLevelChanged(value: Boolean) {
      myZoomLevelChanged = value
    }

    override fun isZoomLevelChanged(): Boolean = myZoomLevelChanged

    override fun getCustomZoomOptions(): ZoomOptions? = myCustomZoomOptions

    override fun setCustomZoomOptions(zoomOptions: ZoomOptions?) {
      myCustomZoomOptions = zoomOptions
    }
  }

  private fun getSmartZoomFactor(zoomOptions: ZoomOptions): Double? {
    val bounds = imageComponent.document.bounds ?: return null
    if (bounds.getWidth() == 0.0 || bounds.getHeight() == 0.0) return null
    val width = bounds.width
    val height = bounds.height
    val preferredMinimumSize = zoomOptions.prefferedSize
    if (width < preferredMinimumSize.width &&
      height < preferredMinimumSize.height
    ) {
      val factor = (preferredMinimumSize.getWidth() / width.toDouble() +
              preferredMinimumSize.getHeight() / height.toDouble()) / 2.0
      return ceil(factor)
    }
    val canvasSize = myScrollPane.viewport.extentSize
    canvasSize.height -= ImageComponent.IMAGE_INSETS * 2
    canvasSize.width -= ImageComponent.IMAGE_INSETS * 2
    if (canvasSize.width <= 0 || canvasSize.height <= 0) return null
    return if (canvasSize.width < width ||
      canvasSize.height < height
    ) {
      min(
        canvasSize.height.toDouble() / height,
        canvasSize.width.toDouble() / width
      )
    } else 1.0
  }

  private fun updateImageComponentSize() {
    val bounds = imageComponent.document.bounds
    if (bounds != null) {
      val zoom = getZoomModel().zoomFactor
      imageComponent.setCanvasSize(ceil(bounds.width * zoom).toInt(), ceil(bounds.height * zoom).toInt())
    }
  }

  private inner class DocumentChangeListener : ChangeListener {
    override fun stateChanged(e: ChangeEvent) {
      updateImageComponentSize()
      val document = imageComponent.document
      val value = document.value
      val layout = contentPanel.layout as CardLayout
      layout.show(contentPanel, if (value != null) IMAGE_PANEL else ERROR_PANEL)
      updateInfo()
      revalidate()
      repaint()
    }
  }

  private inner class FocusRequester : MouseAdapter() {
    override fun mousePressed(e: MouseEvent) {
      IdeFocusManager.getGlobalInstance()
        .doWhenFocusSettlesDown { IdeFocusManager.getGlobalInstance().requestFocus(this@D2SvgEditorUI, true) }
    }
  }

  override fun getData(dataId: String): Any? = when {
    CommonDataKeys.PROJECT.`is`(dataId) -> editor.project
    CommonDataKeys.VIRTUAL_FILE.`is`(dataId) -> editor.file
    CommonDataKeys.VIRTUAL_FILE_ARRAY.`is`(dataId) -> arrayOf(editor.file)
    PlatformDataKeys.COPY_PROVIDER.`is`(dataId) -> this
    PlatformDataKeys.CUT_PROVIDER.`is`(dataId) && copyPasteSupport != null -> copyPasteSupport.cutProvider
    PlatformDataKeys.DELETE_ELEMENT_PROVIDER.`is`(dataId) -> deleteProvider
    ImageComponentDecorator.DATA_KEY.`is`(dataId) -> editor
    PlatformCoreDataKeys.BGT_DATA_PROVIDER.`is`(dataId) -> DataProvider { getSlowData(it) }
    else -> null
  }

  private fun getSlowData(dataId: String): Any? = when {
    CommonDataKeys.PSI_FILE.`is`(dataId) -> findPsiFile()
    CommonDataKeys.PSI_ELEMENT.`is`(dataId) -> findPsiFile()
    PlatformCoreDataKeys.PSI_ELEMENT_ARRAY.`is`(dataId) -> {
      val psi = findPsiFile()
      arrayOf(psi)
    }

    else -> null
  }

  private fun findPsiFile(): PsiFile? {
    val file = editor.file
    return if (file.isValid) PsiManager.getInstance(editor.project).findFile(file) else null
  }

  override fun performCopy(dataContext: DataContext) {
    val document = imageComponent.document
    val image = document.value
    CopyPasteManager.getInstance().setContents(ImageTransferable(image))
  }

  override fun isCopyEnabled(dataContext: DataContext): Boolean = true

  override fun isCopyVisible(dataContext: DataContext): Boolean = true

  private class ImageTransferable(private val myImage: BufferedImage) : Transferable {
    override fun getTransferDataFlavors(): Array<DataFlavor> = arrayOf(DataFlavor.imageFlavor)

    override fun isDataFlavorSupported(dataFlavor: DataFlavor): Boolean = DataFlavor.imageFlavor.equals(dataFlavor)

    @Throws(UnsupportedFlavorException::class)
    override fun getTransferData(dataFlavor: DataFlavor): Any {
      if (!DataFlavor.imageFlavor.equals(dataFlavor)) {
        throw UnsupportedFlavorException(dataFlavor)
      }
      return myImage
    }
  }

  private inner class OptionsChangeListener : PropertyChangeListener {
    override fun propertyChange(evt: PropertyChangeEvent) {
      val options = evt.source as Options
      val editorOptions = options.editorOptions
      val chessboardOptions = editorOptions.transparencyChessboardOptions
      val gridOptions = editorOptions.gridOptions

      imageComponent.transparencyChessboardCellSize = chessboardOptions.cellSize
      imageComponent.transparencyChessboardWhiteColor = chessboardOptions.whiteColor
      imageComponent.setTransparencyChessboardBlankColor(chessboardOptions.blackColor)
      imageComponent.gridLineZoomFactor = gridOptions.lineZoomFactor
      imageComponent.gridLineSpan = gridOptions.lineSpan
      imageComponent.gridLineColor = gridOptions.lineColor
    }
  }

}