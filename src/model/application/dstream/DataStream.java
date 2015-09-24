package model.application.dstream;

import java.io.Serializable;
import java.util.Objects;

public class DataStream implements Comparable<DataStream>, Serializable {
	
	private static final long serialVersionUID = -5368252928484810293L;
	
	private long flow;	

	public DataStream(long flow) {
		this.setFlow(flow);
	}
	
	public DataStream() {
		this.setFlow(0);
	}

	public long getFlow() {
		return this.flow;
	}

	public void setFlow(long flow) {
		this.flow = flow;
	}	
	
	@Override 
	public int compareTo(DataStream other) {
		return Long.valueOf(this.getFlow()).compareTo(
				Long.valueOf(other.getFlow()));
	}
	
	@Override 
	public String toString() {
		return "DataStream(" + 
			   "flow:" + this.getFlow() + ")";
	}
	
	@Override 
	public int hashCode() {
		return Objects.hash(this.getFlow());
	}

	
}
