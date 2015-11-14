package opmap.control.plotter;

public enum PlotType {
	LINEAR("linear"),
	HISTOG("hystogram");
	
	private String name;
	
	private PlotType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override 
	public String toString() {
		return this.getName();
	}
}
