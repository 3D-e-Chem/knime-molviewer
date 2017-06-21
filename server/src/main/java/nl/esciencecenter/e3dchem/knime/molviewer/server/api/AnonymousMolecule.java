package nl.esciencecenter.e3dchem.knime.molviewer.server.api;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

public class AnonymousMolecule implements Serializable {
	private static final long serialVersionUID = 6270265632979848068L;
	@ApiModelProperty(required = true, value = "Data format", allowableValues = "mol2, pdb, phar, sdf")
	public String format;
	@ApiModelProperty(required = true, value = "Data of molecule aka atoms/bonds in specified format")
	public String data;

	public AnonymousMolecule(String data, String format) {
		super();
		this.data = data;
		this.format = format;
	}

	public AnonymousMolecule() {

	}

	@Override
	public int hashCode() {
		return Objects.hash(format, data);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AnonymousMolecule other = (AnonymousMolecule) obj;
		return Objects.equals(this.format, other.format) && Objects.equals(this.data, other.data);
	}
}
