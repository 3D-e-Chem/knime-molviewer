package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import java.util.List;

import javax.ws.rs.Path;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;

@Path("/ligands")
@Api(value = "Ligands")
public class LigandsResource extends MoleculeResource {

	@Override
	@ApiOperation(value = "List of ligands", nickname = "getLigands", notes = "Ligands from the ligand input port of the KNIME node")
	public List<Molecule> getMolecules() {
		return super.getMolecules();
	}
}
