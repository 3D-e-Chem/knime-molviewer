package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import java.util.List;

import javax.ws.rs.Path;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;

@Path("/proteins")
@Api(value="Proteins")
public class ProteinsResource extends MoleculeResource {
	@Override
	@ApiOperation(value = "List of proteins", nickname="getProteins", notes="Proteins from the protein input port of the KNIME node")
	public List<Molecule> getMolecules() {
		return super.getMolecules();
	}
}
