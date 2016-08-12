package nl.esciencecenter.e3dchem.knime.ws.server;

import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;

@SwaggerDefinition(
        info = @Info(
                description = "Datastore for 3DMol.js based molecule viewer",
                // TODO read version from pom.xml or MANIFEST.MF
                version="1.0.0",
                title = "The Molviewer API",
                license = @License(
                   name = "Apache 2.0", 
                   url = "http://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        consumes = {"application/json"},
        produces = {"application/json", "text/event-stream"},
        schemes = {SwaggerDefinition.Scheme.HTTP}
)
public interface SwaggerInfo {

}
