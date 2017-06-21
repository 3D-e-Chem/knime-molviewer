package nl.esciencecenter.e3dchem.knime.molviewer.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

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

import io.swagger.jaxrs.config.BeanConfig;
import nl.esciencecenter.e3dchem.knime.molviewer.server.resources.BroadcasterResource;

abstract public class MolViewerServer {
	private Server server;
	private URI current_uri;
	private BroadcasterResource sse_res;
	private String page = "";

	static public final String apiBasePath = "/api";

	public MolViewerServer(String page) {
		// use random port, can be retrieved after start
		this(page, 0);
	}

	abstract protected void setupResources(ResourceConfig rc);

	public MolViewerServer(String page, int port) {
		this.page = page;
		server = new Server(port);

		// handle static files
		String welcome_file = "index.html";
		String class_path = "/webapp";
		String web_path = "/";
		ContextHandler resource_context = staticDirHandler(welcome_file, class_path, web_path);
		ContextHandler swagger_ui_static = staticDirHandler("index.html", "/swagger-ui/", "/swagger-ui");

		// setup Jersey
		ResourceConfig rc = new ResourceConfig();
		rc.register(io.swagger.jaxrs.listing.ApiListingResource.class);
		rc.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		sse_res = new BroadcasterResource();
		rc.register(sse_res);
		setupResources(rc);
		ServletContainer sc = new ServletContainer(rc);
		ServletHolder holder = new ServletHolder(sc);
		ServletContextHandler rest_handler = new ServletContextHandler();
		rest_handler.setContextPath(apiBasePath);
		rest_handler.addServlet(holder, "/*");

		// configure swagger
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion(getVersionFromManifest());
		beanConfig.setDescription("API for WebGL based molecule viewer");
		beanConfig.setTitle("The Molviewer API");
		beanConfig.setLicense("Apache 2.0");
		beanConfig.setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0");
		beanConfig.setBasePath(MolViewerServer.apiBasePath);
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setServletConfig(sc.getServletConfig());
		beanConfig.setResourcePackage("nl.esciencecenter.e3dchem.knime.molviewer.server.resources");
		// force scan so a previous scan is overwritten, if false then a second
		// node will host the swagger.yaml from the first node
		beanConfig.setScan(true);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(new Handler[] { resource_context, rest_handler, swagger_ui_static, new DefaultHandler() });

		server.setHandler(contexts);
	}

	private String getVersionFromManifest() {
		InputStream manifestStream = getClass().getResourceAsStream("/META-INF/MANIFEST.MF");
		try {
			Manifest manifest = new Manifest(manifestStream);
			Attributes attr = manifest.getMainAttributes();
			return attr.getValue("Bundle-Version");
		} catch (IOException e) {
			e.printStackTrace();
			return "0.0.0";
		}
	}

	private ContextHandler staticDirHandler(String welcome_file, String class_path, String web_path) {
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setDirectoriesListed(false);
		resource_handler.setWelcomeFiles(new String[] { welcome_file });
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
		int port = ((NetworkConnector) server.getConnectors()[0]).getLocalPort();
		current_uri = new URI("http://localhost:" + port + "/" + page);
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
}
