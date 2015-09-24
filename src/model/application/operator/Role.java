package model.application.operator;

public enum Role {
	SRC("Source"),
	SNK("Sink"),
	PIP("Pipe");
	
	private String name;
	
	private Role(final String name) {
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
