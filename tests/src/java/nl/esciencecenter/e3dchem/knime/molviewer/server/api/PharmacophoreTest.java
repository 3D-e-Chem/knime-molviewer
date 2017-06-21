package nl.esciencecenter.e3dchem.knime.molviewer.server.api;

import static org.junit.Assert.*;

import org.junit.Test;

public class PharmacophoreTest {

	@Test
	public void testHashCode() {
		Pharmacophore phar = new Pharmacophore();
		assertEquals(phar.hashCode(), 28629151L);
	}

	@Test
	public void testEqualsNull() {
		Pharmacophore phar = new Pharmacophore();
		assertFalse(phar.equals(null));
	}

	@Test
	public void testEqualsUnsetProps() {
		Pharmacophore phar = new Pharmacophore();
		Pharmacophore other = new Pharmacophore();
		assertTrue(phar.equals(other));
	}
	
	@Test
	public void testEqualsFilledProps_otherUnset() {
		Pharmacophore phar = new Pharmacophore();
		phar.id = "id1";
		phar.label = "label1";
		phar.pharmacophore = new AnonymousMolecule("...", "phar");
		phar.ligand = new AnonymousMolecule("...", "sdf");
		phar.protein = new AnonymousMolecule("...", "pdb");
		
		Pharmacophore other = new Pharmacophore();
		assertFalse(phar.equals(other));
	}

	@Test
	public void testEqualsFilledProps_otherSameFill() {
		Pharmacophore phar = new Pharmacophore();
		phar.id = "id1";
		phar.label = "label1";
		phar.pharmacophore = new AnonymousMolecule("...", "phar");
		phar.ligand = new AnonymousMolecule("...", "sdf");
		phar.protein = new AnonymousMolecule("...", "pdb");
		
		Pharmacophore other = new Pharmacophore();
		other.id = "id1";
		other.label = "label1";
		other.pharmacophore = new AnonymousMolecule("...", "phar");
		other.ligand = new AnonymousMolecule("...", "sdf");
		other.protein = new AnonymousMolecule("...", "pdb");
		assertTrue(phar.equals(other));
	}

}
