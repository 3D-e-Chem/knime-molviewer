package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Test with js 
 * https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events
 *
 * Docs see https://jersey.java.net/documentation/latest/sse.html
 */
@Path("/broadcast")
@Api(value="broadcast")
public class BroadcasterResource {
	private SseBroadcaster broadcaster = new SseBroadcaster();
	
	public void broadcastMessage(String message) {
        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        OutboundEvent event = eventBuilder.name("message")
            .mediaType(MediaType.TEXT_PLAIN_TYPE)
            .data(String.class, message)
            .build();
 
        broadcaster.broadcast(event);
    }
	
	@GET
	@ApiOperation(value="Server sent events endpoint", response=String.class)
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput listenToBroadcast() {
        final EventOutput eventOutput = new EventOutput();
        this.broadcaster.add(eventOutput);
        return eventOutput;
    }
}
