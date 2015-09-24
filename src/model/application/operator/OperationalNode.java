package model.application.operator;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import model.application.operator.Operator;
import model.application.operator.OperatorRole;

public class OperationalNode implements Comparable<OperationalNode>, Serializable {

	private static final long serialVersionUID = -8266724889784176862L;
	
	private long id;
	private Operator operator;
	private ComputationalDemand computationalDemand;
	private Set<Long> pinnables;			

	public OperationalNode(long id, Operator operator, ComputationalDemand computationalDemand, Set<Long> pinnables) {
		this.setId(id);
		this.setOperator(operator);
		this.setComputationalDemand(computationalDemand);
		this.setPinnables(pinnables);
	}
	
	public OperationalNode(Operator operator, ComputationalDemand computationalDemand, Set<Long> pinnables) {
		this.setOperator(operator);
		this.setComputationalDemand(computationalDemand);
		this.setPinnables(pinnables);
	}
	
	public OperationalNode(Operator operator, ComputationalDemand computationalDemand) {
		this.setOperator(operator);
		this.setComputationalDemand(computationalDemand);
		this.setPinnables(new LinkedHashSet<Long>());
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public Operator getOperator() {
		return this.operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public ComputationalDemand getComputationalDemand() {
		return this.computationalDemand;
	}

	public void setComputationalDemand(ComputationalDemand computationalDemand) {
		this.computationalDemand = computationalDemand;
	}
	
	public Set<Long> getPinnables() {
		return this.pinnables;
	}

	public void setPinnables(Set<Long> pinnables) {
		if (this.pinnables == null)
			this.pinnables = new LinkedHashSet<Long>();
		else if (!this.pinnables.isEmpty())
			this.pinnables.clear();
		this.pinnables.addAll(pinnables);
	}
	
	public void addPinnables(Set<Long> pinnables) {
		this.pinnables.addAll(pinnables);
	}
	
	public void addPinnable(Long nodeId) {
		this.getPinnables().add(nodeId);
	}
	
	public boolean isSource() {
		return this.getOperator().getRole().equals(OperatorRole.SRC);
	}

	public boolean isSink() {
		return this.getOperator().getRole().equals(OperatorRole.SNK);
	}	
	
	public boolean isPipe() {
		return this.getOperator().getRole().equals(OperatorRole.PIP);
	}	
	
	@Override 
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		
		OperationalNode other = (OperationalNode) obj;
		
		return (this.getId() == other.getId());
	}
	
	@Override public int compareTo(OperationalNode other) {
		return this.getComputationalDemand().compareTo(other.getComputationalDemand());
	}	
	
	@Override 
	public String toString() {
		return "OperationalNode(" +
			   "id:" + this.getId() + ";" + 
			   "operator:" + this.getOperator() + "; " +
			   "compDemand:" + this.getComputationalDemand() + "; " +
			   "pinnables:" + this.getPinnables() + ")";
	}
	
	@Override public int hashCode() {
		return Objects.hash(this.getId());
	}	

}
