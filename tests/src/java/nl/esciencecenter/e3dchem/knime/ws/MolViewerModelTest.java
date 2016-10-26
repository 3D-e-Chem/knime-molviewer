package nl.esciencecenter.e3dchem.knime.ws;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import nl.esciencecenter.e3dchem.knime.ws.server.api.Molecule;



public class MolViewerModelTest {

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

		MolViewerModel node = new MolViewerModel();
		File file = folder.newFile();
		node.saveInternalsMolecules(file, molecules);
		List<Molecule> result = node.loadInternalsMolecules(file);

		assertThat(result, is(molecules));
	}
}
