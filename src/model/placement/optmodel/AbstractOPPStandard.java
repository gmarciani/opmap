package model.placement.optmodel;

import control.exceptions.ModelException;
import model.application.Application;
import model.architecture.Architecture;

public abstract class AbstractOPPStandard extends AbstractOPPModel {
	
	protected static final double DEFAULT_RMAX 	= 10000.0;
	protected static final double DEFAULT_RMIN 	= 1.0;
	protected static final double DEFAULT_AMAX 	= 1.0;
	protected static final double DEFAULT_AMIN 	= 0.0;
	protected static final double DEFAULT_RW   	= 0.5;
	protected static final double DEFAULT_AW	= 0.5;
	
	protected double Rmax;
	protected double Rmin;
	protected double Amax;
	protected double Amin;
	
	protected double Rw;
	protected double Aw;

	public AbstractOPPStandard(String name, Application app, Architecture arc,
			   				   double Rmax, double Rmin, double Amax, double Amin, double Rw, double Aw) throws ModelException {
		super(name, app, arc);
		this.setRmax(Rmax);
		this.setRmin(Rmin);
		this.setAmax(Amax);
		this.setAmin(Amin);
		this.setRw(Rw);
		this.setAw(Aw);
	}
	
	public double getRmax() {
		return this.Rmax;
	}
	
	protected void setRmax(final double Rmax) {
		this.Rmax = Rmax;
	}
	
	public double getRmin() {
		return this.Rmin;
	}
	
	protected void setRmin(final double Rmin) {
		this.Rmin = Rmin;
	}
	
	public double getAmax() {
		return this.Amax;
	}
	
	protected void setAmax(final double Amax) {
		this.Amax = Amax;
	}
	
	public double getAmin() {
		return this.Amin;
	}
	
	protected void setAmin(final double Amin) {
		this.Amin = Amin;
	}
	
	public double getRw() {
		return this.Rw;
	}
	
	protected void setRw(final double Rw) {
		this.Rw = Rw;
	}
	
	public double getAw() {
		return this.Aw;
	}
	
	protected void setAw(final double Aw) {
		this.Aw = Aw;
	}

}
