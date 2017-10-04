package nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import nl.esciencecenter.e3dchem.knime.molviewer.ligandsandproteins.LigandsAndProteinsViewerModel;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Molecule;

public class LigandsAndProteinsViewerModelTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testSaveAndLoad() throws IOException {
		List<Molecule> molecules = new ArrayList<Molecule>();
		Molecule mol = new Molecule();
		mol.data = "Some data to render later";
		mol.id = "Row1";
		mol.label = "Label1";
		mol.format = "sdf";
		molecules.add(mol);

		LigandsAndProteinsViewerModel node = new LigandsAndProteinsViewerModel();
		File file = folder.newFile();
		node.saveInternalsMolecules(file, molecules);
		List<Molecule> result = node.loadInternalsMolecules(file);

		assertThat(result, is(molecules));
	}

    @Test
    public void testLoadInternalsMolecules() throws IOException {
        LigandsAndProteinsViewerModel node = new LigandsAndProteinsViewerModel();
        InputStream stream = getClass().getResourceAsStream("/src/resources/molViewerInternals.ligands.ser.gz");
        List<Molecule> result = node.loadInternalsMolecules(stream);

        assertThat(result.get(0).format, is(notNullValue()));
    }
}
