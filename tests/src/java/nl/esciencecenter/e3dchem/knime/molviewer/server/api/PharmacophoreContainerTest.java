package nl.esciencecenter.e3dchem.knime.molviewer.server.api;

import static org.junit.Assert.*;

import org.junit.Test;

public class PharmacophoreContainerTest {

	@Test
	public void testHashCode() {
		PharmacophoreContainer phar = new PharmacophoreContainer();
		assertEquals(phar.hashCode(), 28629151L);
	}

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
		phar.pharmacophore = new AnonymousMolecule("...", "phar");
		phar.ligand = new AnonymousMolecule("...", "sdf");
		phar.protein = new AnonymousMolecule("...", "pdb");
		
		PharmacophoreContainer other = new PharmacophoreContainer();
		assertFalse(phar.equals(other));
	}

	@Test
	public void testEqualsFilledProps_otherSameFill() {
		PharmacophoreContainer phar = new PharmacophoreContainer();
		phar.id = "id1";
		phar.label = "label1";
		phar.pharmacophore = new AnonymousMolecule("...", "phar");
		phar.ligand = new AnonymousMolecule("...", "sdf");
		phar.protein = new AnonymousMolecule("...", "pdb");
		
		PharmacophoreContainer other = new PharmacophoreContainer();
		other.id = "id1";
		other.label = "label1";
		other.pharmacophore = new AnonymousMolecule("...", "phar");
		other.ligand = new AnonymousMolecule("...", "sdf");
		other.protein = new AnonymousMolecule("...", "pdb");
		assertTrue(phar.equals(other));
	}

}
