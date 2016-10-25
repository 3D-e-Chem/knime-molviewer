package nl.esciencecenter.e3dchem.knime.ws.server.api;

import io.swagger.annotations.ApiModelProperty;


public class Molecule {
	@ApiModelProperty(required=true, value="Identifier")
	public String id;
	@ApiModelProperty(required=true, value="Label")
	public String label;
	@ApiModelProperty(required=true, value="Data format", allowableValues="sdf, pdb")
	public String format;
	@ApiModelProperty(required=true, value="Data of molecule aka atoms/bonds in specified format")
	public String data;
	
	public Molecule() {
		
	}
}
