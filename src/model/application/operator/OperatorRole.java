package model.application.operator;

public enum OperatorRole {
	SRC("Source"),
	SNK("Sink"),
	PIP("Pipe");
	
	private String name;
	
	private OperatorRole(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override 
	public String toString() {
		return this.getName();
	}
}
