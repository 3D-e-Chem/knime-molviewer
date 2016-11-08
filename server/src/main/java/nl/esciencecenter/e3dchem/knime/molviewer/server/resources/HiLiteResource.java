package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.knime.core.data.RowKey;
import org.knime.core.node.property.hilite.HiLiteHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "hilite")
@Produces("application/json")
public class HiLiteResource {
	private HiLiteHandler m_hiliteHandler;

	@GET
	@ApiOperation(value = "List of highlighted row keys")
	public Set<String> getHighlightedKeys() {
		return m_hiliteHandler.getHiLitKeys().stream().map(d -> d.getString()).collect(Collectors.toSet());
	}

	@POST
	@ApiOperation(value = "Select list of highlighted row keys")
	public void setHighlightedKeys(Set<String> highlightedKeys) {
		Set<RowKey> ids = highlightedKeys.stream().map(d -> new RowKey(d)).collect(Collectors.toSet());
		m_hiliteHandler.fireHiLiteEvent(ids);
	}

	@POST
	@Path("/undo")
	@ApiOperation(value = "Unselect list of highlighted row keys")
	public void unsetHighlightedKeys(Set<String> unhighlightedKeys) {
		Set<RowKey> ids = unhighlightedKeys.stream().map(d -> new RowKey(d)).collect(Collectors.toSet());
		m_hiliteHandler.fireUnHiLiteEvent(ids);
	}

	@DELETE
	@ApiOperation(value = "Deselect all highlighted row keys")
	public void unHiLiteAll() {
		m_hiliteHandler.fireClearHiLiteEvent();
	}

	public void setHiLiteHandler(HiLiteHandler hiliteHandler) {
		m_hiliteHandler = hiliteHandler;
	}
}
