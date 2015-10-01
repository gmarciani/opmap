package model.application.operator;

public enum OPRole {
	SRC("source"),
	SNK("sink"),
	PIP("pipe");
	
	private String name;
	
	private OPRole(final String name) {
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
