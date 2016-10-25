package nl.esciencecenter.e3dchem.knime.ws.server.websocket;

//import java.io.IOException;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//
//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
//import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
//import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//
//
//@WebSocket
//public class HelloServerEndpoint {
//	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
//
//	@OnWebSocketConnect
//	public void onOpen(final Session session) {
//		sessions.add(session);
//	}
//
//	@OnWebSocketConnect
//	public void onClose(final Session session) {
//		sessions.remove(session);
//	}
//
//	@OnWebSocketMessage
//	public void onMessage(Session session, String message) throws IOException {
//		// TODO pass message to NodeModel hilite listener
//		sendMessage(message);
//	}
//	
//	public void sendMessage(String message) throws IOException {
//		for( final Session session: sessions ) {
//            session.getRemote().sendString(message);
//        }
//	}
//}
