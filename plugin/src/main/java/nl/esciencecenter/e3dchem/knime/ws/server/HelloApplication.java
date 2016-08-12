package nl.esciencecenter.e3dchem.knime.ws.server;

//import java.util.HashSet;
//import java.util.Set;
//
//import javax.ws.rs.ApplicationPath;
//import javax.ws.rs.core.Application;
//
//import nl.esciencecenter.e3dchem.knime.ws.MolViewerModel;
//import nl.esciencecenter.e3dchem.knime.ws.server.resources.HelloWorldResource;
//
//public class HelloApplication extends Application {
//	private MolViewerModel nodeModel;
//
//	public HelloApplication(MolViewerModel nodeModel) {
//		super();
//		this.nodeModel = nodeModel;
//	}
//
//	
//	@Override
//	public Set<Class<?>> getClasses() {
//		HashSet<Class<?>> classes = new HashSet<Class<?>>();
//		classes.add(HelloWorldResource.class);
//		return classes;
//	}
//
//	@Override
//	public Set<Object> getSingletons() {
//		HashSet<Object> singletons = new HashSet<Object>();
//		// TODO inject nodeModel things into resources.
////		singletons.add(new HelloWorldResource("Hello %s!", "Nobody"));
//		return singletons;
//	}
//	
//}
