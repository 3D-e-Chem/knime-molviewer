package nl.esciencecenter.e3dchem.knime.ws.server.resources;
//
//import java.util.Optional;
//import java.util.concurrent.atomic.AtomicLong;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//
//import nl.esciencecenter.e3dchem.knime.ws.server.api.Saying;
//
//@Path("/hoi")
//@Produces("application/json")
//public class HelloWorldResource {
//    private final String template;
//    private final String defaultName;
//    private final AtomicLong counter;
//
//    public HelloWorldResource() {
//    	this("Hello constructor %s!!!", "boss");
//	}
//
//	public HelloWorldResource(String template, String defaultName) {
//        this.template = template;
//        this.defaultName = defaultName;
//        this.counter = new AtomicLong();
//    }
//	
//    @GET
//	public Saying sayHello() {
//        final String value = String.format(template, defaultName);
//        return new Saying(counter.incrementAndGet(), value);
//    }
//    
////    @GET
////	public Saying sayHello(@QueryParam("name") String name) {
////        final String value = String.format(template, name);
////        return new Saying(counter.incrementAndGet(), value);
////    }
////
//    // TODO register https://github.com/FasterXML/jackson-datatype-jdk8 so Optional can be used
////    @GET
////	public Saying sayHello(@QueryParam("name") Optional<String> name) {
////        final String value = String.format(template, name.orElse(defaultName));
////        return new Saying(counter.incrementAndGet(), value);
////    }
//
//}
