window.molviewerLigandsAndProteins = function () {
    const MolviewerLigandsAndProteins = {};
    MolviewerLigandsAndProteins.init = function (representation, value, modules) {
        debugger
        // const Molviewer = modules[0];

        const div = document.createElement('div');
        div.setAttribute('id', 'viewport');
        document.body.append(div);

        const app = new window.MolViewer.LigandsAndProteinsViewerApp(div);
        app.render();

        const ligandsPort = representation.inObjects[0];
        const ligandsColumnName = value.options.ligands;
        const ligandsColumnIndex = ligandsPort.spec.colNames.findIndex(n => n === ligandsColumnName);

        let ligandLabelColumnIndex = undefined;
        if ('liglabels' in value.options) {
            const ligandLabelColumnName = value.options.liglabels;
            ligandLabelColumnIndex = ligandsPort.spec.colNames.findIndex(n => n === ligandLabelColumnName);
        }

        const ligands = ligandsPort.rows.map(row => {
            return {
                id: row.rowKey,
                label: ligandLabelColumnIndex !== undefined ? row.data[ligandLabelColumnIndex] : row.rowKey,
                data: row.data[ligandsColumnIndex],
                format: ligandsPort.spec.knimeTypes[ligandsColumnIndex] === 'SDF' ? 'sdf' : 'mol2'
            };
        })

        // Hookup selection events
        // From outside to inside
        const ligandTableId = ligandsPort.id;
        knimeService.subscribeToSelection(ligandTableId, e => {
            // TODO bi-directional selection causes infinite loop
            // disabled recieving selections
            if ('added' in e.changeSet) {
                // e.changeSet.added.forEach(app.toggleLigandVisibility);
            }
            if ('removed' in e.changeSet) {
                // e.changeSet.removed.forEach(app.toggleLigandVisibility);
            }
        });
        // From inside to outside
        app.subscribeToLigandVisibilityToggle((toggles) => {
            const selection = {
                selectionMethod: "selection",
                changeSet: {
                    added: [],
                    removed: []
                }
            };
            toggles.forEach(toggle => {
                if (toggle.visible) {
                    selection.changeSet.added.push(toggle.id);
                } else {
                    selection.changeSet.removed.push(toggle.id);
                }
            });
            knimeService.publishSelection(ligandTableId, selection, true) 
        })
        
        app.setLigands(ligands);

        const proteinsPort = representation.inObjects[1];
        const proteinColumnName = value.options.pdbs;
        const proteinColumnIndex = proteinsPort.spec.colNames.findIndex(n => n === proteinColumnName);

        let proteinLabelColumnIndex = undefined;
        if ('protlabels' in value.options) {
            const proteinLabelColumnName = value.options.protlabels;
            proteinLabelColumnIndex = proteinsPort.spec.colNames.findIndex(n => n === proteinLabelColumnName);
        }

        const proteins = proteinsPort.rows.map(row => {
            return {
                id: row.rowKey,
                label: proteinLabelColumnIndex !== undefined ? row.data[proteinLabelColumnIndex] : row.rowKey,
                data: row.data[proteinColumnIndex],
                format: proteinsPort.spec.knimeTypes[proteinColumnIndex] === 'PDB' ? 'pdb' : 'mol2'
            };
        })

        // Hookup selection events
        // From outside to inside
        const proteinTableId = proteinsPort.id;
        knimeService.subscribeToSelection(proteinTableId, e => {
            if ('added' in e.changeSet) {
                e.changeSet.added.forEach(app.toggleProteinVisibility);
            }
            if ('removed' in e.changeSet) {
                e.changeSet.removed.forEach(app.toggleProteinVisibility);
            }
        });
        // From inside to outside
        app.subscribeToProteinVisibilityToggle((toggles) => {
            const selection = {
                selectionMethod: "selection",
                changeSet: {
                    added: [],
                    removed: []
                }
            };
            toggles.forEach(toggle => {
                if (toggle.visible) {
                    selection.changeSet.added.push(toggle.id);
                } else {
                    selection.changeSet.removed.push(toggle.id);
                }
            });
            // TODO bi-directional selection causes infinite loop
            // disabled recieving selections
            // knimeService.publishSelection(proteinTableId, selection, true) 
        })

        app.setProteins(proteins);
    }

    MolviewerLigandsAndProteins.getPNG = function () {
        console.log('Return image');
    }

    MolviewerLigandsAndProteins.getComponentValue = function () {
        return {};
    }

    MolviewerLigandsAndProteins.validate = function () {
        return true;
    }

    MolviewerLigandsAndProteins.setValidationError = function () {

    }

    return MolviewerLigandsAndProteins;
}();

