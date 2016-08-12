package nl.esciencecenter.e3dchem.knime.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import nl.esciencecenter.e3dchem.knime.ws.server.HelloServer;

public class HelloServerTest {
	private HelloServer server;

	@Before
	public void setUp() throws Exception {
		server = new HelloServer("mynode.html");
		server.start();
		// wait for server to spin up
//		Thread.sleep(500);
	}
	
	@Test
	public void cleanUp() throws Exception {
		server.stop();
	}
	
	@Test
	public void testCurrentUri() {
		URI uri = server.getCurrentUri();
		assertEquals("localhost", uri.getHost());
		assertEquals("/assets/mynode.html", uri.getPath());
	}
	
//	@Test
//	public void testHandler() throws ClientProtocolException, IOException {
//		String uri = viewer.getCurrent_uri().toString() + "hello";
//		String response = Request.Get(uri).execute().returnContent().asString();
//		assertEquals("<h1>Hello World</h1>", response);
//	}
//	
	@Test
	public void testStatic() throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = server.getBaseUri().resolve("/assets/index.html");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertTrue(response.contains("KNIME WebGL demo"));
	}

	@Test
	public void testRest() throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = server.getBaseUri().resolve("/api/ligands");
		HttpResponse response = Request.Get(uri).addHeader("Accept", "application/json").execute().returnResponse();
		String content = EntityUtils.toString(response.getEntity());
		assertEquals("[]", content);
	}

//	@Test
//	public void testWebSocket() {
//		fail("Todo");
//	}
	
	@Test
	public void testSwaggerYaml() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.yaml");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertTrue(response.contains("3DMol.js"));
		assertTrue(response.contains("ligands"));
		assertTrue(response.contains("hilite"));
		assertTrue(response.contains("event-stream"));
	}

	@Test
	public void testSwaggerJson() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.json");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertTrue(response.contains("3DMol.js"));
		assertTrue(response.contains("ligands"));
		assertTrue(response.contains("hilite"));
		assertTrue(response.contains("event-stream"));
	}
	
}
