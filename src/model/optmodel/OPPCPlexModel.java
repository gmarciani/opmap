package model.optmodel;

import control.exceptions.ModelException;
import model.application.Application;
import model.architecture.Architecture;

public interface OPPCPlexModel extends CPlexModel {
	
	public void compile(Application app, Architecture arc) throws ModelException;

}
