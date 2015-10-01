package model.application.operator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import model.application.operator.OPRole;
import model.architecture.node.EXNode;

public class OPNode implements Comparable<OPNode>, Serializable {

	private static final long serialVersionUID = -8266724889784176862L;
	
	private int id;
	private OPRole role;
	private String name;
	private Function<Long, Long> transformation;
	private int resources;	
	private double speed;	
	private long flowIn;
	private long flowOut;	
	private Set<EXNode> pinnables;
	
	public OPNode(int id, OPRole role, String name, Function<Long, Long> transformation, 
			   			int resources, double speed, Set<EXNode> pinnables) {
		this.setId(id);
		this.setRole(role);
		this.setName(name);
		this.setTransformation(transformation);		
		this.setResources(resources);
		this.setSpeed(speed);
		
		if (this.isSource())
		this.setFlowOut(this.getTransformation().apply(new Long(0)));
		else if (this.isSink())
		this.setFlowOut(0);
		
		this.setPinnables(pinnables);
	}

	public OPNode(int id, OPRole role, String name, Function<Long, Long> transformation, 
						int resources, double speed) {
		this(id, role, name, transformation, resources, speed, null);
	}
	
	public int getId() {
		return this.id;
	}
	
	private void setId(int id) {
		this.id = id;
	}

	public OPRole getRole() {
		return this.role;
	}
	
	private void setRole(OPRole role) {
		this.role = role;
	}

	public String getName() {
		return this.name;
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	public Function<Long, Long> getTransformation() {
		return this.transformation;
	}
	
	private void setTransformation(Function<Long, Long> transformation) {
		this.transformation = transformation;
	}
	
	public int getResources() {
		return this.resources;
	}

	public void setResources(int resources) {
		this.resources = resources;
	}

	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public long getFlowIn() {
		return this.flowIn;
	}

	public void setFlowIn(long flowIn) {
		this.flowIn = flowIn;
		this.flowOut = this.transformation.apply(this.flowIn);
	}

	public long getFlowOut() {
		return this.flowOut;
	}

	public void setFlowOut(long flowOut) {
		this.flowOut = flowOut;
	}
	
	public Set<EXNode> getPinnables() {
		return this.pinnables;
	}
	
	private void setPinnables(Set<EXNode> pinnables) {
		this.pinnables = pinnables;
	}
	
	public boolean addPinnable(final EXNode exnode) {
		if (this.pinnables == null) {
			this.pinnables = new HashSet<EXNode>();
		}
		return this.pinnables.add(exnode);
	}
	
	public boolean isSource() {
		return this.getRole().equals(OPRole.SRC);
	}

	public boolean isSink() {
		return this.getRole().equals(OPRole.SNK);
	}	
	
	public boolean isPipe() {
		return this.getRole().equals(OPRole.PIP);
	}	
	
	public void addFlowIn(long flow) {
		this.setFlowIn(this.getFlowIn() + flow);
	}
	
	public boolean isPinnable(EXNode exnode) {
		if (this.pinnables == null)
			return true;
		else
			return this.pinnables.contains(exnode);
	}
	
	@Override 
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		OPNode other = (OPNode) obj;
		
		return (this.getId() == other.getId());
	}
	
	@Override 
	public int compareTo(OPNode other) {
		return Double.valueOf(this.getSpeed()).compareTo(other.getSpeed());
	}	
	
	public String toPrettyString() {
		String str = "#opnode# ";
		
		str += "id:" + this.getId() + "|";
		str += "operator:" + this.getName() + "|";
		str += "role:" + this.getRole() + "|";
		str += "resources:" + this.getResources() + "|"; 
		str += "speed:" + this.getSpeed() + "|"; 
		str += "flowIn:" + this.getFlowIn() + "|"; 
		str += "flowOut:" + this.getFlowOut() + "|"; 
		str += "pinnables:" + ((this.getPinnables()!=null)?(this.getPinnables().isEmpty()?"none":this.getPinnables()):"every");
		
		return str;
	}
	
	@Override 
	public String toString() {
		return "OPNode(" +
			   "id:" + this.getId() + ";" + 
			   "operator:" + this.getName() + ";" +
			   "role:" + this.getRole() + ";" +
			   "resources:" + this.getResources() + ";" + 
			   "speed:" + this.getSpeed() + ";" + 
			   "flowIn:" + this.getFlowIn() + ";" + 
			   "flowOut:" + this.getFlowOut() + ";" + 
			   "pinnables:" + this.getPinnables() + ")";
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getId());
	}	

}
