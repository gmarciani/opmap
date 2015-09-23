package model.architecture.node;

import java.io.Serializable;

public class ComputationalNode implements Comparable<ComputationalNode>, Serializable {
	
	private static final long serialVersionUID = 4664378299375410058L;
	
	private long id;
	private String name;
	private ComputationalOffer computationalOffer;

	public ComputationalNode(long id, String name, ComputationalOffer computationalOffer) {
		this.setId(id);
		this.setName(name);
		this.setComputationalOffer(computationalOffer);
	}
	
	public ComputationalNode(long id, ComputationalOffer computationalOffer) {
		this.setId(id);
		this.setName(null);
		this.setComputationalOffer(computationalOffer);
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ComputationalOffer getComputationalOffer() {
		return this.computationalOffer;
	}

	public void setComputationalOffer(ComputationalOffer computationalOffer) {
		this.computationalOffer = computationalOffer;
	}	
	
	@Override public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public int compareTo(ComputationalNode other) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override public String toString() {
		// TODO Auto-generated method stub
		return "";
	}

}
