window.molviewerLigands = function () {
    const MolviewerLigands = {};
    MolviewerLigands.init = function (representation, value, modules) {
        const div = document.createElement('div');
        div.setAttribute('id', 'viewport');
        document.body.append(div);

        const app = new window.MolViewer.LigandsViewerApp(div);
        this.app = app;

        app.render();

        const ligandsPort = representation.inObjects[0];
        const molColumnName = value.options.ligands;
        const molColumnIndex = ligandsPort.spec.colNames.findIndex(n => n === molColumnName);
        const tableId = ligandsPort.id;

        this.value = value;

        let labelColumnIndex = undefined;
        if ('labels' in value.options) {
            const labelColumnName = value.options.labels;
            labelColumnIndex = ligandsPort.spec.colNames.findIndex(n => n === labelColumnName);
        }

        const ligands = ligandsPort.rows.map(row => {
            return {
                id: row.rowKey,
                label: labelColumnIndex !== undefined ? row.data[labelColumnIndex] : row.rowKey,
                data: row.data[molColumnIndex],
                format: ligandsPort.spec.knimeTypes[molColumnIndex] === 'SDF' ? 'sdf' : 'mol2'
            };
        })

        // Hookup selection events
        // From outside to inside
        knimeService.subscribeToSelection(tableId, e => {
            const currently_shown = new Set(this.app.store.getState().ligands.filter(m => m.visible).map(m => m.id));
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
        app.setLigands(ligands);

        const initialSelection = {
            selectionMethod: "selection",
            changeSet: {
                added: this.app.store.getState().ligands.filter(m => m.visible).map(m => m.id),
                removed: this.app.store.getState().ligands.filter(m => !m.visible).map(m => m.id)
            }
        };
        knimeService.publishSelection(tableId, initialSelection, true)
    }

    MolviewerLigands.getPNG = function () {
        console.log('Return image');
    }

    MolviewerLigands.getComponentValue = function () {
        const state = this.app.store.getState();
        const selection = {};
        state.ligands.forEach(m => {
            selection[m.id] = m.visible;
        });
        this.value.outColumns = {
            selection
        };
        return this.value;
    }

    MolviewerLigands.validate = function () {
        return true;
    }

    MolviewerLigands.setValidationError = function () {

    }

    return MolviewerLigands;
}();

