package nl.esciencecenter.e3dchem.knime.molviewer.server.api;

import static org.junit.Assert.*;

import org.junit.Test;

public class AnonymousMoleculeTest {

	@Test
	public void test_anonymousConstructor() {
		AnonymousMolecule mol = new AnonymousMolecule();
		assertNull(mol.format);
		assertNull(mol.data);
	}

	@Test
	public void test_constructor() {
		AnonymousMolecule mol = new AnonymousMolecule("data", "format");
		assertEquals("data", mol.data);
		assertEquals("format", mol.format);
	}
	
	@Test
	public void testEqualsNull() {
		AnonymousMolecule mol = new AnonymousMolecule("data", "format");
		
		assertFalse(mol.equals(null));
	}
	
	@Test
	public void testEqualsString() {
		AnonymousMolecule mol = new AnonymousMolecule("data", "format");
		
		assertFalse(mol.equals("no mol"));
	}
	
	@Test
	public void testHashCode() {
		AnonymousMolecule mol = new AnonymousMolecule("data", "format");
		assertEquals(-674366892L, mol.hashCode());
	}
	
	@Test
	public void testEqualsFilledProps() {
		AnonymousMolecule mol = new AnonymousMolecule("data1", "format1");
		AnonymousMolecule other = new AnonymousMolecule("data1", "format1");
		assertTrue(mol.equals(other));
	}
}
