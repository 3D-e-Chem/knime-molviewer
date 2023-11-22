# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).
The file is formatted as described on http://keepachangelog.com/.

## [Unreleased]

## [2.0.3] - 2023-11-22

### Changed

- Require KNIME 5.1

Workflows using v1 molviewer nodes will need to replace them with the v2 molviewer nodes.

## [2.0.2] - 2019-11-07

### Fixed

- Unable to show second pharmacophore ([#31](https://github.com/3D-e-Chem/knime-molviewer/issues/31))

## [2.0.1] - 2019-09-25

### Fixed

- Pharmacphore viewer gave syntax error

## [2.0.0] - 2019-09-24

### Changed

- Replaced Java based nodes via server with Dynamic js nodes ([#27](https://github.com/3D-e-Chem/knime-molviewer/issues/27))

## [1.1.3] - 2019-06-27

### Changed

- Compatible with KNIME 4 ([#25](https://github.com/3D-e-Chem/knime-molviewer/issues/25))

## [1.1.2] - 2017-12-11

### Added

- Centering on current scene (#21)

### Fixed

- Handle missing values (#19)

## [1.1.1] - 2017-04-10

### Fixed

- Executed node contains no data (#24)

## [1.1.0] - 2017-09-08

### Added

- Node to view pharmacophores with optional protein/ligand (#11)
- Node to view only ligands (#20)
- Node to view only proteins (#22)
- Color picker for ligands (#21)

## [1.0.2] - 2017-02-27

### Added

- Pocket as ball+stick toggle
- Added slider for adjusting pocket selection radius

## [1.0.1] - 2017-01-25

### Changed

* Use xdg-open on Linux to open url on web-browser (#16)

### Removed

* KNIME crash workaround in node description

## [1.0.0] - 2017-01-23

### Added

* KNIME crash workaround in node description (#16)

### Changed

* If KNIME is unable to open url in web browser then give a warning

## [0.1.5] - 2016-12-02

### Changed

* Allow proteins to be in mol2 format (#14)
* Switched from 3Dmol to NGL (https://github.com/3D-e-Chem/molviewer-tsx/issues/4)

## [0.1.4] - 2016-11-25

### Added

* Example workflow (#2)
* Test workflow (#4)
* Auto open web browser checkbox (#5)

### Changed

* Replace default node icon (#3)

### Fixed

* UI hosted as / (#1)
* Error if protein input is from Vernalis PDB Downloader node (#12) 
