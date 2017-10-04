package nl.esciencecenter.e3dchem.knime.molviewer.server.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PharmacophoreContainerTest {

	@Test
	public void testEqualsNull() {
		PharmacophoreContainer phar = new PharmacophoreContainer();
		assertFalse(phar.equals(null));
	}

	@Test
	public void testEqualsUnsetProps() {
		PharmacophoreContainer phar = new PharmacophoreContainer();
		PharmacophoreContainer other = new PharmacophoreContainer();
		assertTrue(phar.equals(other));
	}

	@Test
	public void testEqualsFilledProps_otherUnset() {
		PharmacophoreContainer phar = new PharmacophoreContainer();
		phar.id = "id1";
		phar.label = "label1";
        phar.pharmacophore = new Molecule("...", "phar");
        phar.ligand = new Molecule("...", "sdf");
        phar.protein = new Molecule("...", "pdb");

		PharmacophoreContainer other = new PharmacophoreContainer();
		assertFalse(phar.equals(other));
	}

	@Test
	public void testEqualsFilledProps_otherSameFill() {
		PharmacophoreContainer phar = new PharmacophoreContainer();
		phar.id = "id1";
		phar.label = "label1";
        phar.pharmacophore = new Molecule("...", "phar");
        phar.ligand = new Molecule("...", "sdf");
        phar.protein = new Molecule("...", "pdb");

		PharmacophoreContainer other = new PharmacophoreContainer();
		other.id = "id1";
		other.label = "label1";
        other.pharmacophore = new Molecule("...", "phar");
        other.ligand = new Molecule("...", "sdf");
        other.protein = new Molecule("...", "pdb");
		assertTrue(phar.equals(other));
	}

}
