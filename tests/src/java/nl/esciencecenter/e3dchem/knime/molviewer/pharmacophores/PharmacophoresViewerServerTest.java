package nl.esciencecenter.e3dchem.knime.molviewer.pharmacophores;

import static org.junit.Assert.*;
import static org.hamcrest.core.StringContains.containsString;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.property.hilite.HiLiteHandler;

import nl.esciencecenter.e3dchem.knime.molviewer.server.PharmacophoresViewerServer;

public class PharmacophoresViewerServerTest {
	private PharmacophoresViewerServer server;

	@Before
	public void setUp() throws Exception {
		server = new PharmacophoresViewerServer("SomeViewer");
		HiLiteHandler hiLiteHandler = new HiLiteHandler();
		server.setHiLiteHandler(hiLiteHandler);
		server.start();
		// wait for server to spin up
		// Thread.sleep(500);
	}

	@After
	public void cleanUp() throws Exception {
		server.stop();
	}

	@Test
	public void testFetchPharmacophores() throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = server.getBaseUri().resolve("/api/pharmacophores");
		HttpResponse response = Request.Get(uri).addHeader("Accept", "application/json").execute().returnResponse();
		String content = EntityUtils.toString(response.getEntity());
		assertEquals("[]", content);
	}

	@Test
	public void testFetchPharmacophoresHiLite() throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = server.getBaseUri().resolve("/api/pharmacophores/hilite");
		HttpResponse response = Request.Get(uri).addHeader("Accept", "application/json").execute().returnResponse();
		String content = EntityUtils.toString(response.getEntity());
		assertEquals("[]", content);
	}

	@Test
	public void testSwaggerYaml() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.yaml");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("pharmacophores"));
		assertThat(response, containsString("hilite"));
	}

	@Test
	public void testSwaggerJson() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.json");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("pharmacophores"));
		assertThat(response, containsString("hilite"));
	}
}
