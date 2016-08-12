package nl.esciencecenter.e3dchem.knime.ws.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NetworkConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.property.hilite.HiLiteHandler;

import nl.esciencecenter.e3dchem.knime.ws.server.resources.HelloBroadcasterResource;
import nl.esciencecenter.e3dchem.knime.ws.server.resources.HiLiteResource;
import nl.esciencecenter.e3dchem.knime.ws.server.resources.LigandsResource;

public class HelloServer {
	private static final NodeLogger logger = NodeLogger
            .getLogger(HelloServer.class);
    private Server server;
	private URI current_uri;
	private HelloBroadcasterResource sse_res;
	private LigandsResource ligands_res;
	private String page = "";
	private HiLiteResource hilite_res;

	public HelloServer(String page) {
		// use random port, can be retrieved after start
		this(page, 0);
	}
			
	public HelloServer(String page, int port) {
		this.page = page;
		server = new Server(port);
		
//		logger.warn("setupServer: HelloWorldHandler");
//        ContextHandler hello_handler = new ContextHandler();
//        hello_handler.setHandler(new HelloWorldHandler());
//        hello_handler.setContextPath("/hello");
        
//		logger.warn("setupServer: ResourceHandler");
//		logger.warn("x");
//		logger.warn(HelloServer.class.getResource("/assets/"));
//		logger.warn("x1");
//		logger.warn(HelloServer.class.getResource("/assets"));
//		logger.warn("x2");
//		logger.warn(getClass().getResource("/assets"));
//		logger.warn("x3");
//		logger.warn(getClass().getResource("/assets/"));
//		logger.warn("x4");
//		logger.warn(Resource.newClassPathResource("/assets/"));
//		logger.warn("x5");
//		logger.warn(Resource.newClassPathResource("/assets"));
//		try {
//			logger.warn(Resource.newResource("/assets/"));
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logger.warn("x6");
//		logger.warn(Resource.newClassPathResource("/assets/", true, true));
//		logger.warn("x7");
//		logger.warn(Resource.newClassPathResource("/assets", true, true));
		
        // handle static files
		logger.warn("setupServer: assets");
		String welcome_file = "index.html";
		String class_path ="/assets/";
		String web_path = "/assets";
        ContextHandler resource_context = staticDirHandler(welcome_file, class_path, web_path);
        ContextHandler swagger_ui_static = staticDirHandler("index.html", "/swagger-ui/", "/swagger-ui");
        
//        logger.warn("setupServer: HelloServlet");
//        HelloServlet hello_servlet = new HelloServlet();
//        ServletHandler hello_servlet_handler = new ServletHandler();
//        hello_servlet_handler.addServletWithMapping(new ServletHolder(hello_servlet), "/hello-api");
                
//        logger.warn("setupServer: CXFNonSpringJaxrsServlet");
//        CXFNonSpringJaxrsServlet cxf = new CXFNonSpringJaxrsServlet();
//        ServletHandler cxf_servlet_handler = new ServletHandler();
//        ServletHolder cxf_holder = new ServletHolder(cxf);
//        cxf_holder.setInitParameter("javax.ws.rs.Application", "nl.esciencecenter.e3dchem.knime.ws.server.HelloApplication");
//        cxf_servlet_handler.addServletWithMapping(cxf_holder, "/cxf-api/*");
//        

        logger.warn("setupServer: Jersey");
        ResourceConfig rc = new ResourceConfig();
        rc.register(io.swagger.jaxrs.listing.ApiListingResource.class);
        rc.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        rc.register(SwaggerInfo.class);
//        HelloWorldResource res = new HelloWorldResource();
//        rc.register(res);
        sse_res = new HelloBroadcasterResource();
        rc.register(sse_res);
        ligands_res = new LigandsResource();
        rc.register(ligands_res);
        hilite_res = new HiLiteResource();
        rc.register(hilite_res);
        ServletContainer sc = new ServletContainer(rc);
        ServletHolder holder = new ServletHolder(sc);
        ServletContextHandler rest_handler = new ServletContextHandler();
        rest_handler.setContextPath("/api");
        rest_handler.addServlet(holder, "/*");
               
//        logger.warn("setupServer: WebSocket");
//        HelloWebSocketServlet hellows_servlet = new HelloWebSocketServlet();
////        hellows_servlet.configure(new WebSocketServerFactory());
//        ServletContextHandler hellows_servlet_handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
////        hellows_servlet_handler.setClassLoader(Thread.currentThread().getContextClassLoader());
//        hellows_servlet_handler.addServlet(new ServletHolder(hellows_servlet), "/*");
//        hellows_servlet_handler.setContextPath("/websocket");
        
       
        logger.warn("setupServer: ContextHandlerCollection");
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { 
//        		hello_handler, 
        		resource_context, 
//        		hello_servlet_handler, 
//        		cxf_servlet_handler, 
        		rest_handler,
//        		hellows_servlet_handler,
        		swagger_ui_static,
        		new DefaultHandler() 
        		});
        
        server.setHandler(contexts);
	}

	private ContextHandler staticDirHandler(String welcome_file, String class_path, String web_path) {
		ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[] {welcome_file} );
        Resource base = Resource.newClassPathResource(class_path);
//        logger.warn(base);
        resource_handler.setBaseResource(base);
        ContextHandler resource_context = new ContextHandler();
        resource_context.setHandler(resource_handler);
        resource_context.setContextPath(web_path);
		return resource_context;
	}

	public void stop() throws Exception {
		sendMessage("stop");
		server.stop();
    	logger.warn("Service stopped");
	}

	public void start() throws Exception {
		server.start();
    	int port  = ((NetworkConnector) server.getConnectors()[0]).getLocalPort();
        current_uri = new URI("http://localhost:" + port + "/assets/" + page);
    	logger.warn("Service started on " + current_uri.toString());
    	logger.warn(server.getState());
	}

	public URI getCurrentUri() {
		return current_uri;
	}
	
	public URI getBaseUri() throws URISyntaxException {
		return new URI("http://localhost:" + current_uri.getPort());
	}

	public void sendMessage(String message) {
		sse_res.broadcastMessage(message);
	}

	public void updateLigands(List<String> ligands) {
		ligands_res.setLigands(ligands);
	}

	public void setHiLiteHandler(HiLiteHandler hiliteHandler) {
		hilite_res.setHiLiteHandler(hiliteHandler);
	}
}
