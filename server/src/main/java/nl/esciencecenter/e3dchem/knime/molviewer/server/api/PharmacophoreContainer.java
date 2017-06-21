package nl.esciencecenter.e3dchem.knime.molviewer.server.api;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

public class PharmacophoreContainer implements Serializable {
	private static final long serialVersionUID = -5922466426587485779L;

	@ApiModelProperty(required = true, value = "Identifier")
	public String id;
	@ApiModelProperty(required = true, value = "Label")
	public String label;
	@ApiModelProperty(required = true, value = "Pharmacophore")
	public AnonymousMolecule pharmacophore;
	@ApiModelProperty(required = false, value = "Protein")
	public AnonymousMolecule protein;
	@ApiModelProperty(required = false, value = "Ligand")
	public AnonymousMolecule ligand;
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PharmacophoreContainer other = (PharmacophoreContainer) obj;
		return Objects.equals(this.id, other.id) && Objects.equals(this.label, other.label)
				&& Objects.equals(this.pharmacophore, other.pharmacophore) && Objects.equals(this.protein, other.protein) &&
				Objects.equals(this.ligand, other.ligand);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, label, pharmacophore, protein, ligand);
	}
}
