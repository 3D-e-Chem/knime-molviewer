window.molviewerPharmacophores = function () {
    const MolviewerPharmacophores = {};
    MolviewerPharmacophores.init = function (representation, value, modules) {
        const div = document.createElement('div');
        div.setAttribute('id', 'viewport');
        document.body.append(div);

        const app = new window.MolViewer.PharmacophoresViewerApp(div);
        this.app = app;

        app.render();

        const pharmacophoresPort = representation.inObjects[0];
        const pharColumnName = value.options.pharmacophores;
        const pharColumnIndex = pharmacophoresPort.spec.colNames.findIndex(n => n === pharColumnName);
        const tableId = pharmacophoresPort.id;

        this.value = value;

        let labelColumnIndex = undefined;
        if ('labels' in value.options) {
            const labelColumnName = value.options.labels;
            labelColumnIndex = pharmacophoresPort.spec.colNames.findIndex(n => n === labelColumnName);
        }

        let proteinColumnIndex = undefined;
        if ('proteins' in value.options) {
            const proteinColumnName = value.options.proteins;
            proteinColumnIndex = pharmacophoresPort.spec.colNames.findIndex(n => n === proteinColumnName);
        }

        let ligandColumnIndex = undefined;
        if ('ligands' in value.options) {
            const ligandColumnName = value.options.ligands;
            ligandColumnIndex = pharmacophoresPort.spec.colNames.findIndex(n => n === ligandColumnName);
        }

        let transformColumnIndex = undefined;
        if ('transform' in value.options) {
            const transformColumnName = value.options.transform;
            transformColumnIndex = pharmacophoresPort.spec.colNames.findIndex(n => n === transformColumnName);
        }

        const Pharmacophores = pharmacophoresPort.rows.map(row => {
            const r = {
                id: row.rowKey,
                label: labelColumnIndex !== undefined ? row.data[labelColumnIndex] : row.rowKey,
                pharmacophore: {
                    data: row.data[pharColumnIndex],
                    format: 'phar'
                }
            };
            if (proteinColumnIndex !== undefined) {
                r.protein = {
                    data: row.data[proteinColumnIndex],
                    format: pharmacophoresPort.spec.knimeTypes[proteinColumnIndex] === 'PDB' ? 'pdb' : 'mol2'
                };
            }
            if (ligandColumnIndex !== undefined) {
                r.ligand = {
                    data: row.data[ligandColumnIndex],
                    format: pharmacophoresPort.spec.knimeTypes[ligandColumnIndex] === 'SDF' ? 'sdf' : 'mol2'
                };
            }
            if (transformColumnIndex !== undefined) {
                r.transform = row.data[transformColumnIndex];
            }
            return r;
        })

        // Hookup selection events
        // From outside to inside
        knimeService.subscribeToSelection(tableId, e => {
            const currently_shown = new Set(this.app.store.getState().pharmacophores.filter(m => m.visible).map(m => m.id));
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
        app.setPharmacophores(Pharmacophores);

        const initialSelection = {
            selectionMethod: "selection",
            changeSet: {
                added: this.app.store.getState().pharmacophores.items.filter(m => m.visible).map(m => m.id),
                removed: this.app.store.getState().pharmacophores.items.filter(m => !m.visible).map(m => m.id)
            }
        };
        knimeService.publishSelection(tableId, initialSelection, true)
    }

    MolviewerPharmacophores.getPNG = function () {
        console.log('Return image');
    }

    MolviewerPharmacophores.getComponentValue = function () {
        const state = this.app.store.getState();
        const selection = {};
        state.pharmacophores.items.forEach(m => {
            selection[m.id] = m.visible;
        });
        this.value.outColumns = {
            selection
        };
        return this.value;
    }

    MolviewerPharmacophores.validate = function () {
        return true;
    }

    MolviewerPharmacophores.setValidationError = function () {

    }

    return MolviewerPharmacophores;
}();

