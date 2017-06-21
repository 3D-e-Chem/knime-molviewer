package nl.esciencecenter.e3dchem.knime.molviewer.pharmacophores;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;

import nl.esciencecenter.e3dchem.knime.molviewer.server.api.AnonymousMolecule;
import nl.esciencecenter.e3dchem.knime.molviewer.server.api.Pharmacophore;

public class PharmacophoresViewerModelTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Test
	public void testSaveAndLoad() throws IOException, CanceledExecutionException {
		PharmacophoresViewerModel node = new PharmacophoresViewerModel();
		Pharmacophore phar = new Pharmacophore();
		phar.id = "id1";
		phar.label = "label1";
		phar.pharmacophore = new AnonymousMolecule("...", "phar");
		phar.ligand = new AnonymousMolecule("...", "sdf");
		phar.protein = new AnonymousMolecule("...", "pdb");
		node.getPharmacophores().add(phar);

		ExecutionMonitor exec = null;
		node.saveInternals(folder.getRoot(), exec);
		node.getPharmacophores().clear();
		node.loadInternals(folder.getRoot(), exec);
		
		List<Pharmacophore> expected = Arrays.asList(phar);
		assertEquals(expected , node.getPharmacophores());
	}

}
