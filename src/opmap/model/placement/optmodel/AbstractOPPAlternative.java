package opmap.model.placement.optmodel;

import opmap.control.exception.ModelException;
import opmap.model.application.Application;
import opmap.model.architecture.Architecture;

public abstract class AbstractOPPAlternative extends AbstractOPPModel {
	
	protected static final double DEFAULT_RMAX 	= 10000.0;
	protected static final double DEFAULT_AMIN 	= 0.5;
	protected static final double DEFAULT_RW   	= 0.5;
	protected static final double DEFAULT_AW	= 0.5;
	
	protected double Rmax;
	protected double Amin;
	
	protected double Rw;
	protected double Aw;

	public AbstractOPPAlternative(String name, Application app, Architecture arc,
								  double Rmax, double Amin, double Rw, double Aw) throws ModelException {
		super(name, app, arc);
	}
	
	public double getRmax() {
		return this.Rmax;
	}

	protected void setRmax(final double rmax) {
		this.Rmax = rmax;
	}

	public double getAmin() {
		return this.Amin;
	}

	protected void setAmin(final double amin) {
		this.Amin = amin;
	}

	public double getRw() {
		return this.Rw;
	}

	protected void setRw(final double rw) {
		this.Rw = rw;
	}

	public double getAw() {
		return this.Aw;
	}

	protected void setAw(final double aw) {
		this.Aw = aw;
	}

}
