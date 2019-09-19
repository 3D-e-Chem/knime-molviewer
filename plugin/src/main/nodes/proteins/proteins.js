window.molviewerProteins = function() {
    const MolviewerProteins = {};
    MolviewerProteins.init = function(representation, value, modules) {

        const NGL= modules[0];
        debugger
        console.log('Initialized');
        console.log([representation, value]);

        const div = document.createElement('div');
        div.setAttribute('id','viewport');
        document.body.append(div);

        // Create NGL Stage object
        const stage = new NGL.Stage( "viewport", {backgroundColor: "white"} );

        // Handle window resizing
        window.addEventListener( "resize", function( event ){
            stage.handleResize();
        }, false );

        const proteinsPort = representation.inObjects[0];
        const proteinColumnName = value.options.pdbs;
        const proteinColumnIndex = proteinsPort.spec.colNames.findIndex(n => n === proteinColumnName);
        // Load PDB entries from port
        proteinsPort.rows.forEach((row, index) => {
            const pdbData = row.data[proteinColumnIndex];
            const stringBlob = new Blob( [ pdbData ], { type: 'text/plain'} );
            stage.loadFile( stringBlob, { ext: "pdb", defaultRepresentation: true, name: row.rowKey } ).then((comp) => {
                // TODO visibility based on selection
                // comp.setVisibility(false);	
            });
        });       
    }

    MolviewerProteins.getPNG = function () {
        console.log('Return image');
    }

    MolviewerProteins.getComponentValue = function() {
        return {};
    }

    MolviewerProteins.validate = function() {

    }

    MolviewerProteins.setValidationError = function() {

    }

    return MolviewerProteins;
}();

