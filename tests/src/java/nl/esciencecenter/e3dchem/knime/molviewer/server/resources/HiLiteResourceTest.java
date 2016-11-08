package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.knime.core.data.RowKey;
import org.knime.core.node.property.hilite.HiLiteHandler;

public class HiLiteResourceTest {
	private HiLiteResource resource;
	private HiLiteHandler hiliteHandler;

	@Before
	public void setUp() {
	    resource = new HiLiteResource();
		hiliteHandler = new HiLiteHandler();
		hiliteHandler.fireHiLiteEvent(new RowKey("Row0"));
		hiliteHandler.fireHiLiteEvent(new RowKey("Row1"));
		hiliteHandler.fireUnHiLiteEvent(new RowKey("Row2"));
		resource.setHiLiteHandler(hiliteHandler);
	}
	
	@Test
	public void testGetHighlightedKeys() {
		Set<String> result = resource.getHighlightedKeys();
		
		Set<String> expected = Stream.of("Row0", "Row1").collect(Collectors.toSet());
		assertEquals(expected, result);
	}

	@Test
	public void testSetHighlightedKeys() {
		Set<String> query = Stream.of("Row1", "Row2").collect(Collectors.toSet());

		resource.setHighlightedKeys(query);
		
		Set<RowKey> result = hiliteHandler.getHiLitKeys();
		Set<RowKey> expected = Stream.of("Row1", "Row2").map(d -> new RowKey(d)).collect(Collectors.toSet());
		assertEquals(expected, result);
	}
	
	@Test 
	public void testSetHighlightedKeys_bothempty_nochange() {
		resource = new HiLiteResource();
		hiliteHandler = new HiLiteHandler();
		resource.setHiLiteHandler(hiliteHandler);
		Set<String> query = new HashSet<String>();
		
		resource.setHighlightedKeys(query);
		
		Set<RowKey> result = hiliteHandler.getHiLitKeys();
		Set<RowKey> expected = new HashSet<RowKey>();
		assertEquals(expected, result);
	}
	
	@Test 
	public void testSetHighlightedKeys_set2emptyHandlefilled_zerohilites() {
		Set<String> query = new HashSet<String>();
		
		resource.setHighlightedKeys(query);
		
		Set<RowKey> result = hiliteHandler.getHiLitKeys();
		Set<RowKey> expected = new HashSet<RowKey>();
		assertEquals(expected, result);
	}

	
	@Test 
	public void testSetHighlightedKeys_same_nochange() {
		resource = new HiLiteResource();
		hiliteHandler = new HiLiteHandler();
		resource.setHiLiteHandler(hiliteHandler);
		Set<String> query = Stream.of("Row0", "Row1").collect(Collectors.toSet());
		
		resource.setHighlightedKeys(query);
		
		Set<RowKey> result = hiliteHandler.getHiLitKeys();
		Set<RowKey> expected = Stream.of("Row0", "Row1").map(d -> new RowKey(d)).collect(Collectors.toSet());
		assertEquals(expected, result);
	}
}
