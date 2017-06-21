KNIME node which launches a web browser with a molecule viewer powered by [NGL](https://github.com/arose/ngl).

[![Build Status Linux](https://travis-ci.org/3D-e-Chem/knime-molviewer.svg?branch=master)](https://travis-ci.org/3D-e-Chem/knime-molviewer)
[![Build status Windows](https://ci.appveyor.com/api/projects/status/595y9gh13d69y61q?svg=true)](https://ci.appveyor.com/project/3D-e-Chem/knime-molviewer)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a1a1fd8cd5cf401e8ab5fbb328a7aa60)](https://www.codacy.com/app/3D-e-Chem/knime-molviewer?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=3D-e-Chem/knime-molviewer&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/a1a1fd8cd5cf401e8ab5fbb328a7aa60)](https://www.codacy.com/app/3D-e-Chem/knime-gpcrdb?utm_source=github.com&utm_medium=referral&utm_content=3D-e-Chem/knime-gpcrdb&utm_campaign=Badge_Coverage)
[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.168569.svg)](https://doi.org/10.5281/zenodo.168569)

This project uses a web user interface based on https://github.com/3D-e-Chem/molviewer-tsx .

# Installation

Requirements:

* KNIME, https://www.knime.org, version 3.1 or higher

Steps to get the MolViewer KNIME node inside KNIME:

1. Goto Help > Install new software ... menu
2. Press add button
3. Fill text fields with `https://3d-e-chem.github.io/updates`
4. Select --all sites-- in `work with` pulldown
5. Select the node called `MolViewer nodes for KNIME`
6. Install software
7. Restart KNIME

# Usage

1. Create a new KNIME workflow.
2. Find node in Node navigator panel under Community Nodes > 3D-e-Chem > Molviewer.
3. Drag node to workflow canvas.

See example workflow in `examples/` directory.

# Build

```
mvn verify
```

An Eclipse update site will be made in `p2/target/repository` repository.
The update site can be used to perform a local installation.

# Development

Steps to get development environment setup:

1. Download KNIME SDK from https://www.knime.org/downloads/overview
2. Install/Extract/start KNIME SDK
3. Start SDK
4. Install m2e (Maven integration for Eclipse) + KNIME Testing framework

    1. Goto Help > Install new software ...
    2. Make sure the following Update sites are in the pull down list otherwise add them

        * http://download.eclipse.org/releases/mars
        * http://download.eclipse.org/eclipse/updates/4.5
        * http://update.knime.org/analytics-platform/3.1
        * https://3d-e-chem.github.io/updates

    3. Select --all sites-- in work with pulldown
    4. Select m2e (Maven integration for Eclipse)
    5. Select `KNIME Testing framework`
    6. Select `Splash & node category for 3D-e-Chem KNIME nodes`
    7. Install software & restart

5. Import this repo as an Existing Maven project

During import the Tycho Eclipse providers must be installed.


## Update plugin libs directory

The `server/libs/` directory are filled with dependencies specified in the `server/pom.libs.xml` file.
Update libs directory with
```
mvn -f server/pom.libs.xml dependency:copy-dependencies -DoutputDirectory=libs
```
The jars in the libs directory should be listed in the Bundle-ClassPath property of the `server/META-INF/MANIFEST.MF` file.

TODO incorperate fill libs/ command in mvn package


## Web interface

The webinterface in the `server/src/main/resources` directory. Is a distribution from the https://github.com/3D-e-Chem/molviewer-tsx repository.

## Tests

Tests for the node are in `tests/src` directory.
Tests can be executed with `mvn verify`, they will be run in a separate KNIME environment.
Test results will be written to `test/target/surefire-reports` directory.

### Unit tests

Unit tests written in Junit4 format can be put in `tests/src/java`.

### Workflow tests

See https://github.com/3D-e-Chem/knime-testflow#3-add-test-workflow

# New release

1. Update versions in pom files with `mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=<version>` command.
2. Commit and push changes
3. Create package with `mvn package`, will create update site in `p2/target/repository`
4. Append new release to an update site
  1. Make clone of an update site repo
  2. Append release to the update site with `mvn install -Dtarget.update.site=<path to update site>`
5. Add files, commit and push changes of update site repo.
6. Make nodes available to 3D-e-Chem KNIME feature by following steps at https://github.com/3D-e-Chem/knime-node-collection#new-release

# Technical architecture

In the background a web server is started when the view of the node is opened.
The webserver has
* on /api, a rest interface

  * to retrieve data from KNIME node input ports, /api/ligands
  * to propogate selections in web page to KNIME node using the hilite mechanism of KNIME, /api/ligands/hilite
  * server sent events, /api/broadcast

* on /, ui based on https://github.com/3D-e-Chem/molviewer-tsx
* on /swagger.json and /swagger.yaml, [swagger](http://swagger.io/) endpoints
* on /swagger-ui, swagger ui

Server sent events are used to inform the web page that:

* view closed
* input changed
* hilite changed

This project uses [Eclipse Tycho](https://www.eclipse.org/tycho/) to perform build steps.
