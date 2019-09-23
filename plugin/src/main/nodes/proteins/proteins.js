window.molviewerProteins = function () {
    const MolviewerProteins = {};
    MolviewerProteins.init = function (representation, value, modules) {
        debugger
        // const Molviewer = modules[0];

        const div = document.createElement('div');
        div.setAttribute('id', 'viewport');
        document.body.append(div);

        const app = new window.MolViewer.ProteinsViewerApp(div);
        app.render();

        const proteinsPort = representation.inObjects[0];
        const proteinColumnName = value.options.pdbs;
        const proteinColumnIndex = proteinsPort.spec.colNames.findIndex(n => n === proteinColumnName);

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
        app.setProteins(proteins);

        // Hookup selection events
        // From outside to inside
        const tableId = proteinsPort.id;
        knimeService.subscribeToSelection(tableId, e => {
            if ('added' in e.changeSet) {
                e.changeSet.added.forEach(app.toggleVisibility);
            }
            if ('removed' in e.changeSet) {
                e.changeSet.removed.forEach(app.toggleVisibility);
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
            // TODO bi-directional selection causes infinite loop
            // knimeService.publishSelection(tableId, selection, true) 
        })
    }

    MolviewerProteins.getPNG = function () {
        console.log('Return image');
    }

    MolviewerProteins.getComponentValue = function () {
        return {};
    }

    MolviewerProteins.validate = function () {

    }

    MolviewerProteins.setValidationError = function () {

    }

    return MolviewerProteins;
}();

