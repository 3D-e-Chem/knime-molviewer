KNIME node which launches a web browser with a molecule viewer powered by [NGL](https://github.com/arose/ngl).

[![Build Status Linux](https://travis-ci.org/3D-e-Chem/knime-molviewer.svg?branch=master)](https://travis-ci.org/3D-e-Chem/knime-molviewer)
[![Build status Windows](https://ci.appveyor.com/api/projects/status/595y9gh13d69y61q?svg=true)](https://ci.appveyor.com/project/3D-e-Chem/knime-molviewer)
[![SonarCloud Gate](https://sonarcloud.io/api/badges/gate?key=nl.esciencecenter.e3dchem.knime.molviewer:nl.esciencecenter.e3dchem.knime.molviewer)](https://sonarcloud.io/dashboard?id=nl.esciencecenter.e3dchem.knime.molviewer:nl.esciencecenter.e3dchem.knime.molviewer)
[![SonarCloud Coverage](https://sonarcloud.io/api/badges/measure?key=nl.esciencecenter.e3dchem.knime.molviewer:nl.esciencecenter.e3dchem.knime.molviewer&metric=coverage)](https://sonarcloud.io/component_measures/domain/Coverage?id=nl.esciencecenter.e3dchem.knime.molviewer:nl.esciencecenter.e3dchem.knime.molviewer)
[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.168569.svg)](https://doi.org/10.5281/zenodo.168569)

If you are using KNIME workflows with large molecules and you want to view them in 3D then the molviewer nodes are able to handle this.
* Provides cheminformatics trying to model proteins within KNIME a way to view them
* Adds nodes to KNIME to visualize proteins, small molecules and pharmacophores
* Adds support to KNIME to  handle viewing big molecules
* Used to compare pharmacophore generation tools
* Used to check ligand repurposing

This project uses a web user interface based on https://github.com/3D-e-Chem/molviewer-tsx .

![From KNIME launch web browser with molviewer inside](https://raw.githubusercontent.com/3D-e-Chem/knime-molviewer/master/docs/molviewer-composite.png)

# Installation

Requirements:

* KNIME, https://www.knime.org, version 4.0 or higher

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
4. Give molviewer nodes Proteins, Ligands and/or Pharmacophores as input. 
5. Open the view of the molviewer node, this will launch a web browser with the molecules visualized.
6. In web browser you can

   * Rotate/translate/zoom molecules with mouse
   * Toggle which molecules are visible
   * Transmit visible molecules as HiLite selection back to KNIME or do the reverse from KNIME to molviewer
   * and more

See example workflow in `examples/` directory.

# Build

```
mvn verify
```

An Eclipse update site will be made in `p2/target/repository` repository.
The update site can be used to perform a local installation.

# Development

Steps to get development environment setup based on https://github.com/knime/knime-sdk-setup#sdk-setup:

1. Install Java 8
2. Install Eclipse for [RCP and RAP developers](https://www.eclipse.org/downloads/packages/release/2018-12/r/eclipse-ide-rcp-and-rap-developers)
3. Configure Java 8 inside Eclipse Window > Preferences > Java > Installed JREs
4. Import this repo as an Existing Maven project
5. Activate target platform by going to Window > Preferences > Plug-in Development > Target Platform and check the `KNIME Analytics Platform (4.0) - nl.esciencecenter.e3dchem.knime.molviewer.targetplatform/KNIME-AP-4.0.target` target definition.

During import the Tycho Eclipse providers must be installed.


## Update plugin libs directory

The `server/libs/` directory are filled with dependencies specified in the `server/pom.libs.xml` file.
Update libs directory with
```
mvn -f server/pom.libs.xml dependency:copy-dependencies -DoutputDirectory=libs
```
The jars in the libs directory should be listed in the Bundle-ClassPath property of the `server/META-INF/MANIFEST.MF` file.

TODO incorporate fill libs/ command in mvn package


## Web interface

The web interface in the `server/src/main/resources/webapp` directory. Is a distribution from the https://github.com/3D-e-Chem/molviewer-tsx repository.

## Tests

Tests for the node are in `tests/src` directory.
Tests can be executed with `mvn verify`, they will be run in a separate KNIME environment.
Test results will be written to `test/target/surefire-reports` directory.

### Unit tests

Unit tests written in Junit4 format can be put in `tests/src/java`.

### Workflow tests

See https://github.com/3D-e-Chem/knime-testflow#3-add-test-workflow

# New release

1. Update versions in pom files with `mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=<version>-SNAPSHOT` command.
2. Commit and push changes
3. Create package with `mvn package`, will create update site in `p2/target/repository`
4. Test node by installing it from local update site
5. Append new release to an update site
  1. Make clone of an update site repo
  2. Append release to the update site with `mvn install -Dtarget.update.site=<path to update site>`
6. Add files, commit and push changes of update site repo.
7. Create a Github release
8. Update Zenodo entry, fix authors and license
9. Make nodes available to 3D-e-Chem KNIME feature by following steps at https://github.com/3D-e-Chem/knime-node-collection#new-release

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
