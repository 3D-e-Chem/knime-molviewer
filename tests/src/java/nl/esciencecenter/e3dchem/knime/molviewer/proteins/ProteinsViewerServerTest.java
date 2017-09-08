package nl.esciencecenter.e3dchem.knime.molviewer.proteins;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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

import nl.esciencecenter.e3dchem.knime.molviewer.server.ProteinsViewerServer;

public class ProteinsViewerServerTest {
	private ProteinsViewerServer server;

	@Before
	public void setUp() throws Exception {
		server = new ProteinsViewerServer("SomeViewer");
		HiLiteHandler hiLiteHandler = new HiLiteHandler();
		server.setProteinsHiLiteHandler(hiLiteHandler);
		server.start();
	}

	@After
	public void cleanUp() throws Exception {
		server.stop();
	}

	@Test
	public void testStatic_ProteinsViewerServer() throws ClientProtocolException, IOException, URISyntaxException {
		String viewerUrl = "#/proteins";
		URI uri = server.getBaseUri().resolve(viewerUrl);
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("Molviewer"));
	}

	@Test
	public void testFetchProteins() throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = server.getBaseUri().resolve("/api/proteins");
		HttpResponse response = Request.Get(uri).addHeader("Accept", "application/json").execute().returnResponse();
		String content = EntityUtils.toString(response.getEntity());
		assertEquals("[]", content);
	}

	@Test
	public void testSwaggerYaml() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.yaml");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("proteins"));
		assertThat(response, containsString("hilite"));
	}

	@Test
	public void testSwaggerJson() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.json");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("proteins"));
		assertThat(response, containsString("hilite"));
	}

}
