All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.2.0] - 2024-01-06

### Features

- Support style object syntax

### Miscellaneous Tasks

- Do not use *** gradle to generate lexer and grammar, as gradle ***

## [1.1.1] - 2024-01-04

### Bug Fixes

- D2Viewer must implement DumbAware

### Documentation

- Add note about support for older IDE versions

## [1.1.0] - 2024-01-03

### Bug Fixes

- 2024.1 compatibility
- A lot of space around d2 icon

### Features

- Spellchecker for unquoted string, simplify parser
- If Dark IntelliJ theme is used, use dark D2 theme for preview by default
- Viewer toolbar to change layout, export
- Support direction keyword on top-level

## [1.0.0] - 2023-12-31
- Highlighting works for most D2 syntax.
- Fix element color provider (`logs.style.stroke: "#f4a261"`).
- Reference&Resolve is implemented for shapes.
- Basic completion (keywords).
