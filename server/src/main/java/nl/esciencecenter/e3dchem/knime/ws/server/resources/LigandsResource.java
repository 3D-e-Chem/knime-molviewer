package nl.esciencecenter.e3dchem.knime.ws.server.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.esciencecenter.e3dchem.knime.ws.server.api.Molecule;

@Path("/ligands")
@Api(value="ligands")
@Produces("application/json")
public class LigandsResource {
	private List<Molecule> ligands;

	public LigandsResource() {
		this.ligands = new ArrayList<Molecule>();
	}
	
	public LigandsResource(List<Molecule> ligands) {
		this.ligands = ligands;
	}
	
	@GET
	@ApiOperation(value = "List of ligands")
	public List<Molecule> getLigands() {
		return ligands;
	}

	public void setLigands(List<Molecule> ligands) {
		this.ligands = ligands;
	}
}
