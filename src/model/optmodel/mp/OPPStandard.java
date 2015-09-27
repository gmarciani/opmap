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
import model.application.dstream.DataStream;
import model.application.operator.Operational;
import model.architecture.Architecture;
import model.architecture.link.LogicalLink;
import model.architecture.node.Computational;
import model.optmodel.AbstractOPPModel;

public class OPPStandard extends AbstractOPPModel {
	
	private double Rmax;
	private double Rmin;
	private double Amax;
	private double Amin;
	
	private double wR;
	private double wA;

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
			for (Operational opnode : app.vertexSet())
				for (Computational exnode : arc.vertexSet())
					super.getVariables().add(modeler.boolVar("X[" + opnode.getId() + "]" + 
															  "[" + exnode.getId() + "]"));
			for (DataStream dstream : app.edgeSet())
				for (LogicalLink link : arc.edgeSet())
					super.getVariables().add(modeler.boolVar("Y[" + app.getEdgeSource(dstream).getId() + "]" + 
															  "[" + app.getEdgeTarget(dstream).getId() + "]" + 
															  "[" + arc.getEdgeSource(link).getId() + "]" + 
															  "[" + arc.getEdgeTarget(link).getId() + "]"));
		} catch (IloException exc) {
			throw new ModelException("Error while defining variables: " + exc.getMessage());
		}		
		
		// Response Time
		IloNumVar R;
		try {
			R = modeler.numVar(0.0, Double.MAX_VALUE);
		} catch (IloException exc) {
			throw new ModelException("Error while defining response time: " + exc.getMessage());
		}
		
		// Availability
		IloNumVar A;
		try {
			A = modeler.numVar(0.0, 1.0);
		} catch (IloException exc) {
			throw new ModelException("Error while defining availability: " + exc.getMessage());
		}

		// Objective	
		IloNumExpr objRExpr, objAExpr, objExpr;
		try {
			objRExpr = modeler.prod(modeler.sum(this.Rmax, modeler.negative(R)), wR / (Rmax - Rmin));
			objAExpr = modeler.prod(modeler.sum(A, -Math.log(this.Amin)), wA / (Math.log(Amax) - Math.log(Amin)));
			objExpr = modeler.sum(objRExpr, objAExpr);
			IloObjective obj = modeler.maximize(objExpr);
			super.setObjective(obj);
		} catch (IloException exc) {
			throw new ModelException("Error while defining objective: " + exc.getMessage());
		}		
		
		// Capacity Bound		
		try {
			for (Computational exnode : arc.vertexSet()) {
				IloLinearNumExpr exprCapacity = modeler.linearNumExpr();
				for (Operational opnode : app.vertexSet()) {
					exprCapacity.addTerm(opnode.getResources(), super.getXVariable(opnode.getId(), exnode.getId()));
				}
				IloRange cnsCapacity = modeler.le(exnode.getResources(), exprCapacity);
				super.getConstraints().add(cnsCapacity);
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining capacity bound: " + exc.getMessage());
		}	
		
		// Unicity Bound		
		try {
			for (Operational opnode : app.vertexSet()) {
				IloLinearNumExpr exprUnicity = modeler.linearNumExpr();
				for (Computational exnode : arc.vertexSet()) {
					exprUnicity.addTerm(1.0, super.getXVariable(opnode.getId(), exnode.getId()));
				}
				IloRange cnsUnicity = modeler.eq(1.0, exprUnicity);
				super.getConstraints().add(cnsUnicity);
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining unicity bound: " + exc.getMessage());
		}
		
		// Connectivity Bound
		try {
			for (DataStream dstream : app.edgeSet()) {
				for (LogicalLink link : arc.edgeSet()) {
					IloLinearNumExpr exprConnectivityOne = modeler.linearNumExpr();
					IloLinearNumExpr exprConnectivityTwo = modeler.linearNumExpr();
					long i = app.getEdgeSource(dstream).getId();
					long j = app.getEdgeTarget(dstream).getId();
					long u = arc.getEdgeSource(link).getId();
					long v = arc.getEdgeTarget(link).getId();
					
					exprConnectivityOne.addTerm(1.0, super.getXVariable(i, u));
					exprConnectivityOne.addTerm(1.0, super.getXVariable(j, v));
					exprConnectivityOne.addTerm(-1.0, super.getYVariable(i, j, u, v));
					IloRange cnsConnectivityOne = modeler.le(1.0, exprConnectivityOne);
					super.getConstraints().add(cnsConnectivityOne);
					
					exprConnectivityTwo.addTerm(1.0, super.getXVariable(i, u));
					exprConnectivityTwo.addTerm(1.0, super.getXVariable(j, v));
					exprConnectivityTwo.addTerm(-2.0, super.getYVariable(i, j, u, v));
					IloRange cnsConnectivityTwo = modeler.ge(1.0, exprConnectivityTwo);
					super.getConstraints().add(cnsConnectivityTwo);
				}				
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining conectivity bound (one): " + exc.getMessage());
		}
	}

}
