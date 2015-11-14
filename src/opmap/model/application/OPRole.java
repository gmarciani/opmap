package opmap.model.application;

public enum OPRole {
	SRC("src"),
	SNK("snk"),
	PIP("pip");
	
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
