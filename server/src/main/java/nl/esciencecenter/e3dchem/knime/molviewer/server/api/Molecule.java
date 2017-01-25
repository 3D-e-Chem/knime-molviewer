package nl.esciencecenter.e3dchem.knime.molviewer.server.api;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

public class Molecule implements Serializable {
	private static final long serialVersionUID = 8195661028979524114L;

	@ApiModelProperty(required = true, value = "Identifier")
	public String id;
	@ApiModelProperty(required = true, value = "Label")
	public String label;
	@ApiModelProperty(required = true, value = "Data format", allowableValues = "sdf, pdb")
	public String format;
	@ApiModelProperty(required = true, value = "Data of molecule aka atoms/bonds in specified format")
	public String data;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Molecule other = (Molecule) obj;
		return Objects.equals(this.id, other.id) && Objects.equals(this.label, other.label)
				&& Objects.equals(this.format, other.format) && Objects.equals(this.data, other.data);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, label, format, data);
	}
}
