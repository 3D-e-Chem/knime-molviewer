package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import java.util.Set;

import javax.ws.rs.Path;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Ligands")
@Path("/ligands/hilite")
public class LigandsHiLiteResource extends HiLiteResource {

	@Override
	@ApiOperation(value = "List of highlighted row keys", nickname = "getHighlightedLigandsKeys")
	public Set<String> getHighlightedKeys() {
		return super.getHighlightedKeys();
	}

	@Override
	@ApiOperation(value = "Select list of highlighted row keys, others are unselected", nickname = "setHighlightedLigandsKeys")
	public void setHighlightedKeys(Set<String> highlightedKeys) {
		super.setHighlightedKeys(highlightedKeys);
	}
}
