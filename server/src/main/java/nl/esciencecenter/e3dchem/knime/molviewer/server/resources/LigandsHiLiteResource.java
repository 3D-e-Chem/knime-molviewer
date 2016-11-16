package nl.esciencecenter.e3dchem.knime.molviewer.server.resources;

import javax.ws.rs.Path;

import io.swagger.annotations.Api;

@Api(value = "Ligands")
@Path("/ligands/hilite")
public class LigandsHiLiteResource extends HiLiteResource {

}
