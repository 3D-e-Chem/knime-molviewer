package nl.esciencecenter.e3dchem.knime.molviewer.server.api;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

public class Molecule implements Serializable {
    public Molecule(String data, String format) {
        this.data = data;
        this.format = format;
    }

    public Molecule() {
        super();
    }

    private static final long serialVersionUID = 8195661028979524114L;

    @ApiModelProperty(required = true, value = "Data format", allowableValues = "mol2, pdb, phar, sdf")
    public String format = null;
    @ApiModelProperty(required = true, value = "Data of molecule aka atoms/bonds in specified format")
    public String data = null;
    @ApiModelProperty(required = false, value = "Identifier")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String id = null;
    @ApiModelProperty(required = false, value = "Label")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String label = null;

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
