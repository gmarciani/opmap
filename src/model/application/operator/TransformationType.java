package model.application.operator;

public enum TransformationType {
	EQUAL("Equality"),
	RATIO("Ratio");
	
	private String name;
	
	private TransformationType(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override public String toString() {
		return this.getName();
	}
}
