package nl.esciencecenter.e3dchem.knime.molviewer;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.esciencecenter.e3dchem.knime.molviewer.server.LigandsAndProteinsViewerServer;
import nl.esciencecenter.e3dchem.knime.molviewer.server.MolViewerServer;

public class MolViewerServerTest {
	private MolViewerServer server;

	@Before
	public void setUp() throws Exception {
		// MolViewerServer is abstract, so test against a implementation
		server = new LigandsAndProteinsViewerServer("SomeViewer");
		server.start();
		// wait for server to spin up
		// Thread.sleep(500);
	}

	@After
	public void cleanUp() throws Exception {
		server.stop();
	}
	
	@Test
	public void testCurrentUri() {
		URI uri = server.getCurrentUri();
		assertEquals("localhost", uri.getHost());
		assertEquals("/SomeViewer", uri.getPath());
	}

	@Test
	public void testStatic_index() throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = server.getBaseUri().resolve("/index.html");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("Molviewer"));
	}

	@Test
	public void testStatic_root() throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = server.getBaseUri().resolve("/");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("Molviewer"));
	}
	
	@Test
	public void testSwaggerYaml() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.yaml");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("WebGL"));
		assertThat(response, containsString("event-stream"));
	}

	@Test
	public void testSwaggerJson() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.json");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("WebGL"));
		assertThat(response, containsString("event-stream"));
	}
}
