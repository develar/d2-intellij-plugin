All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Resolving of ambiguous shape references (shapes with the same names but declared in different containers).
- Support [parent reference](https://d2lang.com/tour/containers#reference-parent).
- Completion for `shape` keyword.

### Fixed
- Proper highlighting for `vars`, `classes`, `style` object syntax.
- Parse property `style.*` in a non-shape map.

## [1.2.0] - 2024-01-06

### Added
- Support `style`, `classes` and `vars` object syntax.

## [1.1.1] - 2024-01-04

### Fixed
- `D2Viewer` must implement `DumbAware` ([GH-2](https://github.com/develar/d2-intellij-plugin/issues/2)).

## [1.1.0] - 2024-01-03

### Added
- Spellchecker for unquoted string, simplify parser.
- If Dark IntelliJ theme is used, use dark D2 theme for preview by default.
- Viewer toolbar to change layout, export.
- Support direction keyword on top-level.

### Fixed
- A lot of space around the D2 file icon.
- IJ Platform 2024.1 compatibility.

## [1.0.0] - 2023-12-31

### Added
- Highlighting works for most D2 syntax.
- Reference&Resolve is implemented for shapes.
- Basic completion (keywords).

### Fixed
- Fix element color provider (`logs.style.stroke: "#f4a261"`).