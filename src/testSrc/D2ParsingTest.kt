package org.jetbrains.plugins.d2

import org.junit.jupiter.api.Test

class D2ParsingTest : D2ParsingTestCase() {
    @Test
    fun parsing() {
        test()
    }

    @Test
    fun connections() {
        test()
    }

    @Test
    fun `connection reference`() {
        test()
    }

    @Test
    fun `no connection reference`() {
        test()
    }

    @Test
    fun shapeIds() {
        test()
    }

    @Test
    fun code() {
        test()
    }

    @Test
    fun `values and labels`() {
        test()
    }

    @Test
    fun `style object`() {
        test(
            """
      x -> y: {
        style: {
          stroke-dash: 10
        }
      }
      x.style.stroke-dash: 3
    """
        )
    }

    @Test
    fun `classes and array`() {
        test(
            """
classes: {
  x: "qweqwr"
  "324:": {
    label: ""
    icon: https://play.d2lang.com/assets/icons/d2-logo.svg
  }
  sphere: {
    shape: circle
    style.stroke-width: 0
  }
}

logo.class: [
  324:
  "sphere"
]
    """
        )
    }


    @Test
    fun `just id`() {
        test(
            """
      s
    """
        )
    }

    @Test
    fun `implicit semicolon`() {
        test(
            """
       a: b
       {}
      """,
            ensureNoErrorElements = false
        )
    }

    @Test
    fun `no implicit semicolon`() {
        test(
            """
      a: b \
      {}
    """
        )
    }

    @Test
    fun `top-level direction`() {
        test(
            """
      direction: down
    """
        )
    }

    @Test
    fun `label with dot`() {
        test(
            """
      s: 1.2
    """
        )
    }

    @Test
    fun `id with spaces`() {
        test(
            """
      logs foo bar -> logs
    """
        )
    }

    @Test
    fun `id with spaces and dash`() {
        test(
            """
      logs-foo bar - dash -> logs
    """
        )
    }

    @Test
    fun idAndReservedKeyword() {
        test(
            """
      logs.style.stroke: "#694024"
    """
        )
    }

    @Test
    fun `markdown pipe as marker`() {
        test(
            """
        explanation: |md
          # I can do headers
          - lists
          - lists
        
          And other normal markdown stuff
        |
    """
        )
    }

    @Test
    fun `markdown double pipe as marker`() {
        test(
            """
        explanation: ||md
          # I can do headers
          - lists
          - lists
        
          And other normal markdown stuff
        ||
    """
        )
    }

    @Test
    fun `markdown double pipe and quote as marker`() {
        test(
            """
      # Much cleaner!
      my_code: |`ts
        declare function getSmallPet(): Fish | Bird;
        const works = (a > 1) || (b < 2)
      `|
    """
        )
    }
}