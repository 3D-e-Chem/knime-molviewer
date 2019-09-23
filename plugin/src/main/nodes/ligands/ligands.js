window.molviewerLigands = function () {
    const MolviewerLigands = {};
    MolviewerLigands.init = function (representation, value, modules) {
        debugger
        // const Molviewer = modules[0];

        const div = document.createElement('div');
        div.setAttribute('id', 'viewport');
        document.body.append(div);

        const app = new window.MolViewer.LigandsViewerApp(div);
        app.render();

        const ligandsPort = representation.inObjects[0];
        const molColumnName = value.options.ligands;
        const molColumnIndex = ligandsPort.spec.colNames.findIndex(n => n === molColumnName);

        let labelColumnIndex = undefined;
        if ('labels' in value.options) {
            const labelColumnName = value.options.labels;
            labelColumnIndex = ligandsPort.spec.colNames.findIndex(n => n === labelColumnName);
        }

        const Ligands = ligandsPort.rows.map(row => {
            return {
                id: row.rowKey,
                label: labelColumnIndex !== undefined ? row.data[labelColumnIndex] : row.rowKey,
                data: row.data[molColumnIndex],
                format: ligandsPort.spec.knimeTypes[molColumnIndex] === 'SDF' ? 'sdf' : 'mol2'
            };
        })

        // Hookup selection events
        // From outside to inside
        const tableId = ligandsPort.id;
        knimeService.subscribeToSelection(tableId, e => {
            // TODO bi-directional selection causes infinite loop
            // disabled recieving selections
            if ('added' in e.changeSet) {
                // e.changeSet.added.forEach(app.toggleVisibility);
            }
            if ('removed' in e.changeSet) {
                // e.changeSet.removed.forEach(app.toggleVisibility);
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
        app.setLigands(Ligands);
    }

    MolviewerLigands.getPNG = function () {
        console.log('Return image');
    }

    MolviewerLigands.getComponentValue = function () {
        return {};
    }

    MolviewerLigands.validate = function () {
        return true;
    }

    MolviewerLigands.setValidationError = function () {

    }

    return MolviewerLigands;
}();

