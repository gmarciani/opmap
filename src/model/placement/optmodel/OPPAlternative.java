package model.placement.optmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import control.exceptions.ModelException;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
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
import model.application.operator.OperationalPath;
import model.architecture.Architecture;
import model.architecture.link.LogicalLink;
import model.architecture.node.Computational;

public class OPPAlternative extends AbstractOPPModel {
	
	private double Rmax;
	private double Amin;
	
	private double Rw;
	private double Aw;

	public OPPAlternative(Application app, Architecture arc,
			   				double Rmax, double Amin,
			   				double Rw, double Aw) throws ModelException {
		super(app, arc);
		super.getCPlex().setName("OPP MP Alternative");
		this.setRmax(Rmax);
		this.setAmin(Amin);
		this.setRw(Rw);
		this.setAw(Aw);
		this.compile(super.getApplication(), super.getArchitecture());
	}

	public OPPAlternative(Application app, Architecture arc) throws ModelException {
		this(app, arc, 10000.0, 0.009, 0.5, 0.5);
	}

	public double getRmax() {
		return this.Rmax;
	}

	public void setRmax(final double rmax) {
		this.Rmax = rmax;
	}

	public double getAmin() {
		return this.Amin;
	}

	public void setAmin(final double amin) {
		this.Amin = amin;
	}

	public double getRw() {
		return this.Rw;
	}

	public void setRw(final double rw) {
		this.Rw = rw;
	}

	public double getAw() {
		return this.Aw;
	}

	public void setAw(final double aw) {
		this.Aw = aw;
	}

	@Override
	public void compile(Application app, Architecture arc) throws ModelException {
		IloModeler modeler = new IloCplexModeler();	
		
		/********************************************************************************
		 * Decision Variables		
		 ********************************************************************************/
		IloNumVar X[][] 	= new IloIntVar[app.vertexSet().size()][arc.vertexSet().size()];
		IloNumVar Y[][][][] = new IloIntVar[app.vertexSet().size()][app.vertexSet().size()][arc.vertexSet().size()][arc.vertexSet().size()];
		
		try {
			for (Operational opnode : this.getApplication().vertexSet()) {
				for (Computational exnode : this.getArchitecture().vertexSet()) {
					int i = opnode.getId();
					int u = exnode.getId();
					X[i][u] = modeler.boolVar("X[" + i + "][" + u + "]");
				}					
			}				
		} catch (IloException exc) {
			throw new ModelException("Error while defining X variables: " + exc.getMessage());
		}
		
		try {
			for (DataStream dstream : this.getApplication().edgeSet()) {
				for (LogicalLink link : this.getArchitecture().edgeSet()) {
					int i = app.getEdgeSource(dstream).getId();
					int j = app.getEdgeTarget(dstream).getId();
					int u = arc.getEdgeSource(link).getId();
					int v = arc.getEdgeTarget(link).getId();
					Y[i][j][u][v] = modeler.boolVar("Y[" + i + "][" + j + "][" + u + "][" + v + "]");									 																			   
				}					
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining Y variables: " + exc.getMessage());
		}		
		
		/********************************************************************************
		 * Response Time		
		 ********************************************************************************/
		Set<OperationalPath> paths = this.getApplication().getAllOperationalPaths();
		List<IloNumExpr> Rpaths = new ArrayList<IloNumExpr>(paths.size());
		IloNumExpr R;
		try {
			for (OperationalPath path : paths) {
				IloLinearNumExpr Rpex = modeler.linearNumExpr();				
				for (Operational opnode : path) {
					for (Computational exnode : this.getArchitecture().vertexSet()) {
						int i = opnode.getId();
						int u = exnode.getId();
						Rpex.addTerm(opnode.getSpeed() / exnode.getSpeedup(), X[i][u]);
					}						
				}					
				
				IloLinearNumExpr Rptx = modeler.linearNumExpr();
				for (int k = 0; k < path.size() - 1; k++) {
					for (LogicalLink link : arc.edgeSet()) {
						int i = path.get(k).getId();
						int j = path.get(k + 1).getId();
						int u = arc.getEdgeSource(link).getId();
						int v = arc.getEdgeTarget(link).getId();
						Rptx.addTerm(link.getDelay(), Y[i][j][u][v]);
					} 
				}				
				
				IloNumExpr Rp = modeler.sum(Rpex, Rptx);
				Rpaths.add(Rp);
			}				
			R = modeler.max(Rpaths.toArray(new IloNumExpr[Rpaths.size()]));
		} catch (IloException exc) {
			throw new ModelException("Error while defining response time: " + exc.getMessage());
		}
		
		/********************************************************************************
		 * Availability		
		 ********************************************************************************/
		IloNumExpr Alg;
		IloLinearNumExpr Aex, Atx;
		try {
			Alg = modeler.numExpr();
			Aex = modeler.linearNumExpr();
			Atx = modeler.linearNumExpr();
			
			for (Operational opnode : this.getApplication().vertexSet()) {
				for (Computational exnode : this.getArchitecture().vertexSet()) {
					int i = opnode.getId();
					int u = exnode.getId();
					Aex.addTerm(Math.log(exnode.getAvailability()), X[i][u]);
				}					
			}				
			
			for (DataStream dstream : this.getApplication().edgeSet()) {
				for (LogicalLink link : this.getArchitecture().edgeSet()) {
					int i = app.getEdgeSource(dstream).getId();
					int j = app.getEdgeTarget(dstream).getId();
					int u = arc.getEdgeSource(link).getId();
					int v = arc.getEdgeTarget(link).getId();
					Atx.addTerm(Math.log(link.getAvailability()), Y[i][j][u][v]);
				}					
			}
					
			Alg = modeler.sum(Aex, Atx);
		} catch (IloException exc) {
			throw new ModelException("Error while defining availability: " + exc.getMessage());
		}

		/********************************************************************************
		 * Objective		
		 ********************************************************************************/	
		IloNumExpr objRExpr, objAExpr, objExpr;
		try {			
			objRExpr = modeler.prod(R, this.getRw() / this.getRmax());
			objAExpr = modeler.prod(Alg, this.getAw() / Math.log(this.getAmin()));
			objExpr = modeler.sum(modeler.negative(objRExpr), modeler.negative(objAExpr));
			IloObjective obj = modeler.maximize(objExpr);
			super.getCPlex().addObjective(obj.getSense(), obj.getExpr(), "Alternative Objective");
		} catch (IloException exc) {
			throw new ModelException("Error while defining objective: " + exc.getMessage());
		}		
		
		/********************************************************************************
		 * Capacity		
		 ********************************************************************************/
		try {
			for (Computational exnode : arc.vertexSet()) {
				IloLinearNumExpr exprCapacity = modeler.linearNumExpr();
				for (Operational opnode : app.vertexSet()) {
					int i = opnode.getId();
					int u = exnode.getId();
					exprCapacity.addTerm(opnode.getResources(), X[i][u]);
				}					
				IloRange cnsCapacity = modeler.ge(exnode.getResources(), exprCapacity);
				super.getCPlex().addRange(cnsCapacity.getLB(), cnsCapacity.getExpr(), cnsCapacity.getUB(), 
						"Capacity Bound " + exnode.getName());
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining capacity bound: " + exc.getMessage());
		}	
		
		/********************************************************************************
		 * Uniqueness		
		 ********************************************************************************/		
		try {
			for (Operational opnode : app.vertexSet()) {
				IloLinearNumExpr exprUnicity = modeler.linearNumExpr();
				for (Computational exnode : arc.vertexSet()) {
					int i = opnode.getId();
					int u = exnode.getId();
					exprUnicity.addTerm(1.0, X[i][u]);
				}
				IloRange cnsUnicity = modeler.eq(1.0, exprUnicity);
				super.getCPlex().addRange(cnsUnicity.getLB(), cnsUnicity.getExpr(), cnsUnicity.getUB(), 
						"Unicity Bound " + opnode.getName());
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining unicity bound: " + exc.getMessage());
		}
		
		/********************************************************************************
		 * Connectivity		
		 ********************************************************************************/
		try {
			for (DataStream dstream : app.edgeSet()) {
				for (LogicalLink link : arc.edgeSet()) {
					IloLinearNumExpr exprConn1 = modeler.linearNumExpr();
					IloLinearNumExpr exprConn2 = modeler.linearNumExpr();
					int i = app.getEdgeSource(dstream).getId();
					int j = app.getEdgeTarget(dstream).getId();
					int u = arc.getEdgeSource(link).getId();
					int v = arc.getEdgeTarget(link).getId();
					
					exprConn1.addTerm(1.0, X[i][u]);
					exprConn1.addTerm(1.0, X[j][v]);
					exprConn1.addTerm(-1.0, Y[i][j][u][v]);
					IloRange cnsConn1 = modeler.ge(1.0, exprConn1);
					super.getCPlex().addRange(cnsConn1.getLB(), cnsConn1.getExpr(), cnsConn1.getUB(), 
							"Connectivity Bound (1) [" + i + "][" + j + "][" + u + "][" + v + "]");
					
					exprConn2.addTerm(1.0, X[i][u]);
					exprConn2.addTerm(1.0, X[j][v]);
					exprConn2.addTerm(-2.0, Y[i][j][u][v]);
					IloRange cnsConnectivityTwo = modeler.le(0.0, exprConn2);
					super.getCPlex().addRange(cnsConnectivityTwo.getLB(), cnsConnectivityTwo.getExpr(), cnsConnectivityTwo.getUB(), 
							"Connectivity Bound (2) [" + i + "][" + j + "][" + u + "][" + v + "]");
				}				
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining conectivity bound (one): " + exc.getMessage());
		}
	}

}
