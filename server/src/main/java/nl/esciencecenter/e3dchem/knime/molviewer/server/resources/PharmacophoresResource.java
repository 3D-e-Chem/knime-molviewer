package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Pharmacophore;;

@Path("/pharmacophores")
@Api(value = "Pharmacophores")
@Produces("application/json")
public class PharmacophoresResource {
	private List<Pharmacophore> pharmacophores;


	public PharmacophoresResource() {
		this.pharmacophores = new ArrayList<>();
	}

	public PharmacophoresResource(List<Pharmacophore> pharmacophores) {
		this.pharmacophores = pharmacophores;
	}

	@GET
	@ApiOperation(value = "List of pharmacophores")
	public List<Pharmacophore> getPharmacophores() {
		return pharmacophores;
	}

	public void setPharmacophores(List<Pharmacophore> pharmacophores) {
		this.pharmacophores = pharmacophores;
	}
}
