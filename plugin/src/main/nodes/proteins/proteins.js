window.molviewerProteins = function () {
    const MolviewerProteins = {};
    MolviewerProteins.init = function (representation, value, modules) {
        const div = document.createElement('div');
        div.setAttribute('id', 'viewport');
        document.body.append(div);

        const app = new window.MolViewer.ProteinsViewerApp(div);
        this.app = app;

        app.render();

        const proteinsPort = representation.inObjects[0];
        const proteinColumnName = value.options.pdbs;
        const proteinColumnIndex = proteinsPort.spec.colNames.findIndex(n => n === proteinColumnName);
        const tableId = proteinsPort.id;

        this.value = value;

        let labelColumnIndex = undefined;
        if ('labels' in value.options) {
            const labelColumnName = value.options.labels;
            labelColumnIndex = proteinsPort.spec.colNames.findIndex(n => n === labelColumnName);
        }

        const proteins = proteinsPort.rows.map(row => {
            return {
                id: row.rowKey,
                label: labelColumnIndex !== undefined ? row.data[labelColumnIndex] : row.rowKey,
                data: row.data[proteinColumnIndex],
                format: proteinsPort.spec.knimeTypes[proteinColumnIndex] === 'PDB' ? 'pdb' : 'mol2'
            };
        })

        // Hookup selection events
        // From outside to inside
        knimeService.subscribeToSelection(tableId, e => {
            const currently_shown = new Set(this.app.store.getState().proteins.filter(m => m.visible).map(m => m.id));
            if ('added' in e.changeSet) {
                e.changeSet.added.filter(id => !currently_shown.has(id)).forEach(app.toggleVisibility);
            }
            if ('removed' in e.changeSet) {
                e.changeSet.removed.filter(id => currently_shown.has(id)).forEach(app.toggleVisibility);
            }
        });
        // From inside to outside
        app.subscribeToVisibilityToggle((toggles) => {
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
            knimeService.publishSelection(tableId, selection, true) 
        })

        app.setProteins(proteins);

        const initialSelection = {
            selectionMethod: "selection",
            changeSet: {
                added: this.app.store.getState().proteins.filter(m => m.visible).map(m => m.id),
                removed: this.app.store.getState().proteins.filter(m => !m.visible).map(m => m.id)
            }
        };
        knimeService.publishSelection(tableId, initialSelection, true)
    }

    MolviewerProteins.getPNG = function () {
        console.log('Return image');
    }

    MolviewerProteins.getComponentValue = function () {
        const state = this.app.store.getState();
        const selection = {};
        state.proteins.forEach(m => {
            selection[m.id] = m.visible;
        });
        this.value.outColumns = {
            selection
        };
        return this.value;
    }

    MolviewerProteins.validate = function () {
        return true;
    }

    MolviewerProteins.setValidationError = function () {

    }

    return MolviewerProteins;
}();

