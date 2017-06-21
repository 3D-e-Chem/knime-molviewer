package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import org.knime.core.data.RowKey;
import org.knime.core.node.property.hilite.HiLiteHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Resource to send highlighted rows from server to client and to receive
 * highlighted rows from client to server.
 *
 * When extending then override the rest API methods and add a unique nickname
 * property to the @ApiOperation annotation.
 */
@Api(value = "hilite")
@Produces("application/json")
public class HiLiteResource {
	private HiLiteHandler hiliteHandler;

	@GET
	@ApiOperation(value = "List of highlighted row keys")
	public Set<String> getHighlightedKeys() {
		return hiliteHandler.getHiLitKeys().stream().map(d -> d.getString()).collect(Collectors.toSet());
	}

	@POST
	@ApiOperation(value = "Select list of highlighted row keys, others are unselected")
	public void setHighlightedKeys(Set<String> highlightedKeys) {
		Set<RowKey> ids = highlightedKeys.stream().map(d -> new RowKey(d)).collect(Collectors.toSet());
		if (ids.isEmpty()) {
			if (hiliteHandler.getHiLitKeys().isEmpty()) {
				// want to unhilite all, but nothing is hilited so done
				return;
			} else {
				hiliteHandler.fireClearHiLiteEvent();
			}
		}
		Set<RowKey> allIds = hiliteHandler.getHiLitKeys();
		if (allIds.containsAll(ids)) {
			// ids are already selected
			return;
		}
		Set<RowKey> ids2unhilite = hiliteHandler.getHiLitKeys();
		// remove all ids which must be hilited, what remains are ids that must
		// be unhilited
		ids2unhilite.removeAll(ids);

		hiliteHandler.fireHiLiteEvent(ids);
		hiliteHandler.fireUnHiLiteEvent(ids2unhilite);
	}

	public void setHiLiteHandler(HiLiteHandler hiliteHandler) {
		this.hiliteHandler = hiliteHandler;
	}
}
