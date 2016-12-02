package nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import nl.esciencecenter.e3dchem.knime.molviewer.server.LigandsAndProteinsViewerServer;
import nl.esciencecenter.e3dchem.knime.molviewer.server.MolViewerServer;

public class LigandsAndProteinsViewerServerTest {
	private MolViewerServer server;

	@Before
	public void setUp() throws Exception {
		server = new LigandsAndProteinsViewerServer("SomeViewer");
		server.start();
		// wait for server to spin up
		// Thread.sleep(500);
	}

	@Test
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
		assertTrue(response.contains("Molviewer"));
	}

	@Test
	public void testStatic_root() throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = server.getBaseUri().resolve("/");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertTrue(response.contains("Molviewer"));
	}

	@Test
	public void testStatic_LigandsAndProteinsViewer() throws ClientProtocolException, IOException, URISyntaxException {
		String viewerUrl = "#/ligands-and-proteins";
		URI uri = server.getBaseUri().resolve(viewerUrl);
		String response = Request.Get(uri).execute().returnContent().asString();
		assertTrue(response.contains("Molviewer"));
	}

	@Test
	public void testRest() throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = server.getBaseUri().resolve("/api/ligands");
		HttpResponse response = Request.Get(uri).addHeader("Accept", "application/json").execute().returnResponse();
		String content = EntityUtils.toString(response.getEntity());
		assertEquals("[]", content);
	}

	@Test
	public void testSwaggerYaml() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.yaml");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertTrue(response.contains("WebGL"));
		assertTrue(response.contains("ligands"));
		assertTrue(response.contains("hilite"));
		assertTrue(response.contains("event-stream"));
	}

	@Test
	public void testSwaggerJson() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.json");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertTrue(response.contains("WebGL"));
		assertTrue(response.contains("ligands"));
		assertTrue(response.contains("hilite"));
		assertTrue(response.contains("event-stream"));
	}

}