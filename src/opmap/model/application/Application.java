package opmap.model.application;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Application extends DirectedAcyclicGraph<OPNode, DStream> {

	private static final long serialVersionUID = 7253751791492037927L;
	
	private String name;
	private String description;
	
	public Application(final String name, final String description) {
		super(DStream.class);
		this.setName(name);
		this.setDescription(description);
	}
	
	public Application(final String name) {
		this(name, null);
	}
	
	public Application() {
		this(null, null);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public Set<OPNode> getSources() {
		return super.vertexSet().stream().filter(v -> v.isSource()).collect(Collectors.toSet());
	}
	
	public Set<OPNode> getSinks() {
		return super.vertexSet().stream().filter(v -> v.isSink()).collect(Collectors.toSet());
	}
	
	public Set<OPNode> getPipes() {
		return super.vertexSet().stream().filter(v -> v.isPipe()).collect(Collectors.toSet());
	}
	
	public boolean addOperational(final OPNode opnode) {
		return super.addVertex(opnode);
	}
		
	public boolean addStream(final OPNode src, final OPNode dst) {
		DStream dstream = new DStream(src, dst);
		if (!super.vertexSet().contains(dstream.getSrc()))
			super.addVertex(dstream.getSrc());
		if (!super.vertexSet().contains(dstream.getDst()))
			super.addVertex(dstream.getDst());	
		try {
			if (super.addDagEdge(dstream.getSrc(), dstream.getDst(), dstream)) {
				super.setEdgeWeight(dstream, dstream.getWeight());
				return true;
			}
			return false;
		} catch (CycleFoundException exc) {
			return false;
		}
	}
	
	public Set<OPPath> getAllOperationalPaths() {
		Set<OPPath> paths = new HashSet<OPPath>();
		
		for (OPNode source : this.getSources())
			paths.addAll(this.getOperationalPaths(source));
		
		return paths;
	}
	
	public Set<OPPath> getOperationalPaths(final OPNode srcnode) {
		Set<OPPath> paths = new HashSet<OPPath>();
		
		for (OPNode snknode : this.getSinks()) {
			List<DStream> pathEdge = this.findWorstPath(srcnode, snknode);
			if (pathEdge == null)
				continue;
			OPPath path = new OPPath(pathEdge);
			paths.add(path);
		}			
		
		return paths;
	}
	
	protected List<DStream> findWorstPath(final OPNode srcnode, final OPNode snknode) {
		return BellmanFordShortestPath.findPathBetween(this, srcnode, snknode);
	}
	
	public static Application readJSON(final String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Application app = mapper.readValue(json, Application.class);
		return app;		
	}
	
	public static String writeJSON(final Application app) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(app);	
		return json;
	}
	
	public static boolean isConsistent(final Application app) {
		ConnectivityInspector<OPNode, DStream> inspector = new ConnectivityInspector<OPNode, DStream>(app);
		
		for (OPNode srcnode : app.getSources())
			for (OPNode snknode : app.getSinks())
				if (!inspector.pathExists(srcnode, snknode))
					return false;
		return true;
	}
	
	public String toPrettyString() {
		String str = String.format("#application#\nname:%s\ndesc:%s\nopnodes:\n%s\nstreams:\n%s",
				this.getName(),
				this.getDescription(),
				this.vertexSet().stream().sorted().map(opnode -> opnode.toPrettyString()).collect(Collectors.joining("\n")),
				this.edgeSet().stream().sorted().map(dstream -> dstream.toPrettyString()).collect(Collectors.joining("\n")));
		
		return str;
	}
	
	@Override 
	public String toString() {
		String str = String.format("Application(name:%s|desc:%s|srcs:%s|snks:%s|pips:%s|stream%s)",
				this.getName(),
				this.getDescription(),
				this.getSources(),
				this.getSinks(),
				this.getPipes(),
				this.edgeSet());
		
		return str;
	}

}
