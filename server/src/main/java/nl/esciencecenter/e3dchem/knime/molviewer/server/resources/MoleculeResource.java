package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import io.swagger.annotations.ApiOperation;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;


@Produces("application/json")
public class MoleculeResource {
	private List<Molecule> molecules;

	public MoleculeResource() {
		this.molecules = new ArrayList<Molecule>();
	}
	
	public MoleculeResource(List<Molecule> molecules) {
		this.molecules = molecules;
	}
	
	@GET
	@ApiOperation(value = "List of molecules")
	public List<Molecule> getMolecules() {
		return molecules;
	}

	public void setMolecules(List<Molecule> molecules) {
		this.molecules = molecules;
	}
}
