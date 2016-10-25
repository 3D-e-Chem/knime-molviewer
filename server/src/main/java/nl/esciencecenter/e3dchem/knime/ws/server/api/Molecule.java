package nl.esciencecenter.e3dchem.knime.ws.server.api;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;


public class Molecule implements Serializable {
	private static final long serialVersionUID = 8195661028979524114L;
	
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
