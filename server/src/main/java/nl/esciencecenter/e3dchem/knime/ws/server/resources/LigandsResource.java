package nl.esciencecenter.e3dchem.knime.ws.server.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/ligands")
@Api(value="ligands")
@Produces("application/json")
public class LigandsResource {
	private List<String> ligands;

	public LigandsResource() {
		this.ligands = new ArrayList<String>();
	}
	
	public LigandsResource(List<String> ligands) {
		this.ligands = ligands;
	}
	
	@GET
	@ApiOperation(value = "List of ligands")
	public List<String> getLigands() {
		return ligands;
	}

	public void setLigands(List<String> ligands) {
		this.ligands = ligands;
	}
}
