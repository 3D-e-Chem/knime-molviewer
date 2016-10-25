package nl.esciencecenter.e3dchem.knime.ws.server.websocket;

//import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
//import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
//import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
//
//public class HelloCreator implements WebSocketCreator {
//	private HelloServerEndpoint helloSocket;
//
//	public HelloCreator() {
//		super();
//		helloSocket = new HelloServerEndpoint();
//	}
//
//	@Override
//	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
//		return new HelloServerEndpoint();
////		for (String subprotocol : req.getSubProtocols()) {
////            if ("text".equals(subprotocol)) {
////                resp.setAcceptedSubProtocol(subprotocol);
////                return helloSocket;
////            }
////        }
////        return null;
//	}
//
//}
