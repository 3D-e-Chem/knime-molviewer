package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import java.util.Set;

import javax.ws.rs.Path;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Pharmacophores")
@Path("/pharmacophores/hilite")
public class PharmacophoresHiLiteResource extends HiLiteResource {
	@Override
	@ApiOperation(value = "List of highlighted row keys", nickname = "getHighlightedPharmacophoresKeys")
	public Set<String> getHighlightedKeys() {
		return super.getHighlightedKeys();
	}

	@Override
	@ApiOperation(value = "Select list of highlighted row keys, others are unselected", nickname = "setHighlightedPharmacophoresKeys")
	public void setHighlightedKeys(Set<String> highlightedKeys) {
		super.setHighlightedKeys(highlightedKeys);
	}
}
