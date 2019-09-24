window.molviewerLigandsAndProteins = function () {
    const MolviewerLigandsAndProteins = {};
    MolviewerLigandsAndProteins.init = function (representation, value, modules) {
        debugger
        // const Molviewer = modules[0];

        const div = document.createElement('div');
        div.setAttribute('id', 'viewport');
        document.body.append(div);

        const app = new window.MolViewer.LigandsAndProteinsViewerApp(div);
        this.app = app;

        app.render();

        const ligandsPort = representation.inObjects[0];
        const ligandsColumnName = value.options.ligands;
        const ligandsColumnIndex = ligandsPort.spec.colNames.findIndex(n => n === ligandsColumnName);

        this.value = value;

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
            const currently_shown = new Set(this.app.store.getState().ligands.filter(m => m.visible).map(m => m.id));
            if ('added' in e.changeSet) {
                e.changeSet.added.filter(id => !currently_shown.has(id)).forEach(app.toggleLigandVisibility);
            }
            if ('removed' in e.changeSet) {
                e.changeSet.removed.filter(id => currently_shown.has(id)).forEach(app.toggleLigandVisibility);
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

        const initialLigandSelection = {
            selectionMethod: "selection",
            changeSet: {
                added: this.app.store.getState().ligands.filter(m => m.visible).map(m => m.id),
                removed: this.app.store.getState().ligands.filter(m => !m.visible).map(m => m.id)
            }
        };
        knimeService.publishSelection(ligandTableId, initialLigandSelection, true)

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
            const currently_shown = new Set(this.app.store.getState().proteins.filter(m => m.visible).map(m => m.id));
            if ('added' in e.changeSet) {
                e.changeSet.added.filter(id => !currently_shown.has(id)).forEach(app.toggleProteinVisibility);
            }
            if ('removed' in e.changeSet) {
                e.changeSet.removed.filter(id => currently_shown.has(id)).forEach(app.toggleProteinVisibility);
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
            knimeService.publishSelection(proteinTableId, selection, true) 
        })

        app.setProteins(proteins);

        const initialSelection = {
            selectionMethod: "selection",
            changeSet: {
                added: this.app.store.getState().proteins.filter(m => m.visible).map(m => m.id),
                removed: this.app.store.getState().proteins.filter(m => !m.visible).map(m => m.id)
            }
        };
        knimeService.publishSelection(proteinTableId, initialSelection, true)
    }

    MolviewerLigandsAndProteins.getPNG = function () {
        console.log('Return image');
    }

    MolviewerLigandsAndProteins.getComponentValue = function () {
        const state = this.app.store.getState();
        const ligandselection = {};
        state.ligands.forEach(m => {
            ligandselection[m.id] = m.visible;
        });
        const proteinselection = {};
        state.proteins.forEach(m => {
            proteinselection[m.id] = m.visible;
        });
        this.value.outColumns = {
            ligandselection,
            proteinselection
        };
        return this.value;
    }

    MolviewerLigandsAndProteins.validate = function () {
        return true;
    }

    MolviewerLigandsAndProteins.setValidationError = function () {

    }

    return MolviewerLigandsAndProteins;
}();

