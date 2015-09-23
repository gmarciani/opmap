package model.optmodel;

import control.exceptions.ModelException;
import model.application.Application;
import model.architecture.Architecture;

public abstract class AbstractOPPCPlexModel extends AbstractCPlexModel implements OPPCPlexModel {
	
	private Application app;
	private Architecture arc;	

	public AbstractOPPCPlexModel(Application app, Architecture arc) throws ModelException {
		super();
		this.app = app;
		this.arc = arc;
		this.compile(this.app, this.arc);
	}
	
	public AbstractOPPCPlexModel() {}
	
	public Application getApplication() {
		return this.app;
	}
	
	public Architecture getArchitecture() {
		return this.arc;
	}
	
	public abstract void compile(Application app, Architecture arc) throws ModelException;

}
