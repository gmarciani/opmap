package model.optmodel.mp;

import control.exceptions.ModelException;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloModeler;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplexModeler;
import model.application.Application;
import model.architecture.Architecture;
import model.optmodel.AbstractOPPCPlexModel;

public class OPPStandard extends AbstractOPPCPlexModel {

	public OPPStandard(Application app, Architecture arc) throws ModelException {
		super(app, arc);
		this.compile(app, arc);
	}
	
	public OPPStandard() {
		super();
	}

	@Override
	public void compile(Application app, Architecture arc) throws ModelException {		
		IloModeler modeler = new IloCplexModeler();		
		
		// Variables					
		try {
			super.getVariables().add(modeler.boolVar("X1"));
			super.getVariables().add(modeler.boolVar("X2"));
			super.getVariables().add(modeler.boolVar("X3"));
		} catch (IloException exc) {
			throw new ModelException("Error while defining variables: " + exc.getMessage());
		}		

		// Objective	
		double objFact[] = {1.0, 2.0, 3.0};
		IloNumExpr objExpr;
		try {
			objExpr = modeler.scalProd((IloNumVar[])super.getVariables().toArray(), objFact);
			IloObjective objective = modeler.maximize(objExpr);
			super.setObjective(objective);
		} catch (IloException exc) {
			throw new ModelException("Error while defining objective: " + exc.getMessage());
		}		
		
		// Constraint One
		IloLinearNumExpr cnsExpr1;
		try {
			cnsExpr1 = modeler.linearNumExpr();
			cnsExpr1.addTerm(-1.0, super.getVariables().get(0));
			cnsExpr1.addTerm(1.0, super.getVariables().get(1));
			cnsExpr1.addTerm(1.0, super.getVariables().get(2));
			IloRange cns1 = modeler.range(0.0, cnsExpr1, 20.0, "constraintOne");
			super.getConstraints().add(cns1);
		} catch (IloException exc) {
			throw new ModelException("Error while defining constraintOne: " + exc.getMessage());
		}
		
		// Constraint Two
		IloLinearNumExpr cnsExpr2;
		try {
			cnsExpr2 = modeler.linearNumExpr();
			cnsExpr2.addTerm(1.0, super.getVariables().get(0));
			cnsExpr2.addTerm(-3.0, super.getVariables().get(1));
			cnsExpr2.addTerm(1.0, super.getVariables().get(2));
			IloRange cns2 = modeler.range(0.0, cnsExpr2, 30.0, "constraintOne");
			super.getConstraints().add(cns2);
		} catch (IloException exc) {
			throw new ModelException("Error while defining constraintTwo: " + exc.getMessage());
		}		
	}

}
