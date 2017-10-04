package nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.knime.core.node.property.hilite.HiLiteHandler;

import nl.esciencecenter.e3dchem.knime.molviewer.server.LigandsAndProteinsViewerServer;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;

public class LigandsAndProteinsViewerServerTest {
	private LigandsAndProteinsViewerServer server;

	@Before
	public void setUp() throws Exception {
		server = new LigandsAndProteinsViewerServer("SomeViewer");
		HiLiteHandler hiLiteHandler = new HiLiteHandler();
		server.setLigandsHiLiteHandler(hiLiteHandler);
		server.start();
	}

	@After
	public void cleanUp() throws Exception {
		server.stop();
	}

	@Test
	public void testStatic_LigandsAndProteinsViewer() throws ClientProtocolException, IOException, URISyntaxException {
		String viewerUrl = "#/ligands-and-proteins";
		URI uri = server.getBaseUri().resolve(viewerUrl);
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("Molviewer"));
	}

	@Test
	public void testFetchLigands() throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = server.getBaseUri().resolve("/api/ligands");
		HttpResponse response = Request.Get(uri).addHeader("Accept", "application/json").execute().returnResponse();
		String content = EntityUtils.toString(response.getEntity());
		assertEquals("[]", content);
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
		assertThat(response, containsString("ligands"));
		assertThat(response, containsString("hilite"));
	}

	@Test
	public void testSwaggerJson() throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = server.getBaseUri().resolve("/api/swagger.json");
		String response = Request.Get(uri).execute().returnContent().asString();
		assertThat(response, containsString("ligands"));
		assertThat(response, containsString("hilite"));
	}

    @Test
    public void test_getLigands_optionalFilled() throws URISyntaxException, ClientProtocolException, IOException {
        List<Molecule> ligands = new ArrayList<>(1);
        Molecule ligand = new Molecule();
        ligand.data = "Some data to render later";
        ligand.id = "Row1";
        ligand.label = "Label1";
        ligand.format = "sdf";
        ligands.add(ligand);
        server.updateLigands(ligands);

        URI uri = server.getBaseUri().resolve("/api/ligands");
        String response = Request.Get(uri).execute().returnContent().asString();
        assertThat(response, containsString("Row1"));
        assertThat(response, containsString("Some data to render later"));
        assertThat(response, containsString("Label1"));
        assertThat(response, containsString("sdf"));
    }

    @Test
    public void test_getLigands_requiredFilled() throws URISyntaxException, ClientProtocolException, IOException {
        List<Molecule> ligands = new ArrayList<>(1);
        Molecule ligand = new Molecule();
        ligand.data = "Some data to render later";
        ligand.format = "sdf";
        ligands.add(ligand);
        server.updateLigands(ligands);

        URI uri = server.getBaseUri().resolve("/api/ligands");
        String response = Request.Get(uri).execute().returnContent().asString();
        assertThat(response, containsString("sdf"));
        assertThat(response, containsString("Some data to render later"));
        assertThat(response, not(containsString("id")));
        assertThat(response, not(containsString("label")));
    }
}
