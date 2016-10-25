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
import org.knime.core.node.property.hilite.HiLiteHandler;

import nl.esciencecenter.e3dchem.knime.ws.server.api.Molecule;
import nl.esciencecenter.e3dchem.knime.ws.server.resources.BroadcasterResource;
import nl.esciencecenter.e3dchem.knime.ws.server.resources.HiLiteResource;
import nl.esciencecenter.e3dchem.knime.ws.server.resources.LigandsHiLiteResource;
import nl.esciencecenter.e3dchem.knime.ws.server.resources.LigandsResource;
import nl.esciencecenter.e3dchem.knime.ws.server.resources.ProteinsResource;

public class MolViewerServer {
    private Server server;
	private URI current_uri;
	private BroadcasterResource sse_res;
	private LigandsResource ligands_res;
	private String page = "";
	private HiLiteResource ligand_hilite_res;
	private ProteinsResource proteins_res;

	static public final String apiBasePath = "/api";

	public MolViewerServer(String page) {
		// use random port, can be retrieved after start
		this(page, 0);
	}
			
	public MolViewerServer(String page, int port) {
		this.page = page;
		server = new Server(port);
		
        // handle static files
		String welcome_file = "index.html";
		String class_path ="/assets/";
		String web_path = "/assets";
        ContextHandler resource_context = staticDirHandler(welcome_file, class_path, web_path);
        ContextHandler swagger_ui_static = staticDirHandler("index.html", "/swagger-ui/", "/swagger-ui");
        
        // setup Jersey
        ResourceConfig rc = new ResourceConfig();
        rc.register(io.swagger.jaxrs.listing.ApiListingResource.class);
        rc.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        rc.register(SwaggerInfo.class);
        sse_res = new BroadcasterResource();
        rc.register(sse_res);
        ligands_res = new LigandsResource();
        rc.register(ligands_res);
        ligand_hilite_res = new LigandsHiLiteResource();
        rc.register(ligand_hilite_res);
        proteins_res = new ProteinsResource();
        rc.register(proteins_res);
        ServletContainer sc = new ServletContainer(rc);
        ServletHolder holder = new ServletHolder(sc);
        ServletContextHandler rest_handler = new ServletContextHandler();
        rest_handler.setContextPath(apiBasePath);
        rest_handler.addServlet(holder, "/*");
               
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { 
        		resource_context, 
        		rest_handler,
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
        resource_handler.setBaseResource(base);
        ContextHandler resource_context = new ContextHandler();
        resource_context.setHandler(resource_handler);
        resource_context.setContextPath(web_path);
		return resource_context;
	}

	public void stop() throws Exception {
		sendMessage("stop");
		server.stop();
	}

	public void start() throws Exception {
		server.start();
    	int port  = ((NetworkConnector) server.getConnectors()[0]).getLocalPort();
        current_uri = new URI("http://localhost:" + port + "/assets/" + page);
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

	public void updateLigands(List<Molecule> ligands) {
		ligands_res.setMolecules(ligands);
	}

	public void updateProteins(List<Molecule> proteins) {
		proteins_res.setMolecules(proteins);
	}

	public void setLigandsHiLiteHandler(HiLiteHandler hiliteHandler) {
		ligand_hilite_res.setHiLiteHandler(hiliteHandler);
	}
}
