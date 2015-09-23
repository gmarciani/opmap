package model.application.operator;

public enum OperatorType {
	SRC("Source"),
	SNK("Sink"),
	PIP("Pipe");
	
	private String name;
	
	private OperatorType(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override public String toString() {
		return this.getName();
	}
}
