package nl.esciencecenter.e3dchem.knime.molviewer.server.api;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class MoleculeTest {

	@Test
	public void testEqualsNull() {
		Molecule mol = new Molecule();
		
		assertFalse(mol.equals(null));
	}

	@Test
	public void testEqualsClass() throws URISyntaxException {
		Molecule mol = new Molecule();
		URI other = new URI("https://3d-e-chem.github.io");
		assertFalse(mol.equals(other));
	}
	
	@Test
	public void testEqualsFilledProps() {
		String data = "some data";
		String id = "id1";
		String label = "ID1";
		String format = "sdf";
		
		Molecule mol = new Molecule();
		mol.data = data;
		mol.id = id;
		mol.label = label;
		mol.format = format;

		Molecule other = new Molecule();
		other.data = data;
		other.id = id;
		other.label = label;
		other.format = format;
		
		assertTrue(mol.equals(other));
	}
	
	@Test
	public void testEqualsFilledPropsData() {
		String data = "some data";
		String id = "id1";
		String label = "ID1";
		String format = "sdf";
		
		Molecule mol = new Molecule();
		mol.data = data;
		mol.id = id;
		mol.label = label;
		mol.format = format;

		Molecule other = new Molecule();
		other.data = "some other data";
		other.id = id;
		other.label = label;
		other.format = format;
		
		assertFalse(mol.equals(other));
	}
	
	@Test
	public void testEqualsFilledPropsId() {
		String data = "some data";
		String id = "id1";
		String label = "ID1";
		String format = "sdf";
		
		Molecule mol = new Molecule();
		mol.data = data;
		mol.id = id;
		mol.label = label;
		mol.format = format;

		Molecule other = new Molecule();
		other.data = data;
		other.id = "id2";
		other.label = label;
		other.format = format;
		
		assertFalse(mol.equals(other));
	}
	
	@Test
	public void testEqualsFilledPropsDataLabel() {
		String data = "some data";
		String id = "id1";
		String label = "ID1";
		String format = "sdf";
		
		Molecule mol = new Molecule();
		mol.data = data;
		mol.id = id;
		mol.label = label;
		mol.format = format;

		Molecule other = new Molecule();
		other.data = data;
		other.id = id;
		other.label = "ID2";
		other.format = format;
		
		assertFalse(mol.equals(other));
	}
	
	@Test
	public void testEqualsFilledPropsFormat() {
		String data = "some data";
		String id = "id1";
		String label = "ID1";
		String format = "sdf";
		
		Molecule mol = new Molecule();
		mol.data = data;
		mol.id = id;
		mol.label = label;
		mol.format = format;

		Molecule other = new Molecule();
		other.data = data;
		other.id = id;
		other.label = label;
		other.format = "pdb";
		
		assertFalse(mol.equals(other));
	}
	
	@Test
	public void testHashCodeSame() {
		String data = "some data";
		String id = "id1";
		String label = "ID1";
		String format = "sdf";
		
		Molecule mol = new Molecule();
		mol.data = data;
		mol.id = id;
		mol.label = label;
		mol.format = format;

		Molecule other = new Molecule();
		other.data = data;
		other.id = id;
		other.label = label;
		other.format = format;
		
		assertEquals(mol.hashCode(), other.hashCode());
	}

	@Test
	public void testHashCodeDiff() {
		String data = "some data";
		String id = "id1";
		String label = "ID1";
		String format = "sdf";
		
		Molecule mol = new Molecule();
		mol.data = data;
		mol.id = id;
		mol.label = label;
		mol.format = format;

		Molecule other = new Molecule();
		other.data = data;
		other.id = "id2";
		other.label = label;
		other.format = format;
		
		assertNotEquals(mol.hashCode(), other.hashCode());
	}

    @Test
    public void test_anonymousConstructor() {
        Molecule mol = new Molecule();
        assertNull(mol.format);
        assertNull(mol.data);
    }

    @Test
    public void test_constructor() {
        Molecule mol = new Molecule("data", "format");
        assertEquals("data", mol.data);
        assertEquals("format", mol.format);
    }

    @Test
    public void testEqualsNull_dataConstructor() {
        Molecule mol = new Molecule("data", "format");

        assertFalse(mol.equals(null));
    }

    @Test
    public void testEqualsString() {
        Molecule mol = new Molecule("data", "format");

        assertFalse(mol.equals("no mol"));
    }

    @Test
    public void testHashCode() {
        Molecule mol = new Molecule("data", "format");
        assertEquals(-673444332L, mol.hashCode());
    }

    @Test
    public void testEqualsFilledProps_dataConstructor() {
        Molecule mol = new Molecule("data1", "format1");
        Molecule other = new Molecule("data1", "format1");
        assertTrue(mol.equals(other));
    }
}
