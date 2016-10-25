package nl.esciencecenter.e3dchem.knime.ws.server.api;

public class Molecule {
	public String id;
	public String label;
	public String data;
	public String format;
	
	public Molecule() {
		
	}
	
	public Molecule(String id, String label, String data, String format) {
		super();
		this.id = id;
		this.label = label;
		this.data = data;
		this.format = format;
	}
	
}
