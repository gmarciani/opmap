package model.application.opnode;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import model.application.opnode.OPRole;
import model.architecture.exnode.EXNode;

public class OPNode implements Comparable<OPNode>, Serializable {

	private static final long serialVersionUID = -8266724889784176862L;
	
	private int id;
	private OPRole role;
	private String name;
	private Function<Double, Double> transformation;
	private int resources;	
	private double speed;	
	private double flowIn;
	private double flowOut;	
	private Set<Integer> pinnables;
	private boolean alwaysPinnable;
	
	public OPNode(final int id, final OPRole role, final String name, final Function<Double, Double> transformation, 
			   	  final int resources, final double speed) {
		this.setId(id);
		this.setRole(role);
		this.setName(name);
		this.setTransformation(transformation);		
		this.setResources(resources);
		this.setSpeed(speed);
		this.setPinnables(new HashSet<Integer>());
		this.setAlwaysPinnable(true);
		
		if (this.isSource())
			this.setFlowOut(this.getTransformation().apply(0.0));
		else if (this.isSink())
			this.setFlowOut(0);
	}
	
	public int getId() {
		return this.id;
	}
	
	private void setId(final int id) {
		this.id = id;
	}

	public OPRole getRole() {
		return this.role;
	}
	
	private void setRole(final OPRole role) {
		this.role = role;
	}

	public String getName() {
		return this.name;
	}
	
	private void setName(final String name) {
		this.name = name;
	}
	
	public Function<Double, Double> getTransformation() {
		return this.transformation;
	}
	
	private void setTransformation(final Function<Double, Double> transformation) {
		this.transformation = transformation;
	}
	
	public int getResources() {
		return this.resources;
	}

	public void setResources(final int resources) {
		this.resources = resources;
	}

	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(final double speed) {
		this.speed = speed;
	}

	public double getFlowIn() {
		return this.flowIn;
	}

	public void setFlowIn(final double flowIn) {
		this.flowIn = flowIn;
		this.flowOut = this.transformation.apply(this.flowIn);
	}

	public double getFlowOut() {
		return this.flowOut;
	}

	public void setFlowOut(final double flowOut) {
		this.flowOut = flowOut;
	}
	
	public Set<Integer> getPinnables() {
		return this.pinnables;
	}
	
	public void setPinnables(final Set<Integer> pinnables) {
		this.pinnables = pinnables;
	}
	
	public boolean addPinnable(final int exnodeid) {
		this.alwaysPinnable = false;
		return this.pinnables.add(exnodeid);
	}
	
	public boolean addPinnable(final EXNode exnode) {
		return this.addPinnable(exnode.getId());
	}
	
	public void setAlwaysPinnable(final boolean alwaysPinnable) {
		this.alwaysPinnable = alwaysPinnable;
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
	
	public boolean isAlwaysPinnable() {
		return this.alwaysPinnable;
	}
	
	public void addFlowIn(final double flow) {
		this.setFlowIn(this.getFlowIn() + flow);
	}
	
	public boolean isPinnableOn(final EXNode exnode) {
		if (this.isAlwaysPinnable())
			return true;
		else
			return this.pinnables.contains(exnode.getId());
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
		return Integer.valueOf(this.getId()).compareTo(other.getId());
	}	
	
	public String toPrettyString() {
		String str = String.format("#opnode# id:%d|opr:%s|role:%s|resources:%d|speed:%f:fIn:%f|fOut:%f|pinnables:%s",
				this.getId(),
				this.getName(),
				this.getRole(),
				this.getResources(),
				this.getSpeed(),
				this.getFlowIn(),
				this.getFlowOut(),
				this.isAlwaysPinnable()?"every":this.getPinnables());
		
		return str;
	}
	
	@Override 
	public String toString() {
		String str = String.format("OPNode(id:%d|opr:%s|role:%s|resources:%d|speed:%f:fIn:%f|fOut:%f|pinnables:%s)",
				this.getId(),
				this.getName(),
				this.getRole(),
				this.getResources(),
				this.getSpeed(),
				this.getFlowIn(),
				this.getFlowOut(),
				this.isAlwaysPinnable()?"every":this.getPinnables());
		
		return str;
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getId());
	}	

}
