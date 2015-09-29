package model.optmodel.mp;

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
import model.optmodel.AbstractOPPModel;

public class OPPStandard extends AbstractOPPModel {
	
	private double Rmax;
	private double Rmin;
	private double Amax;
	private double Amin;
	
	private double Rw;
	private double Aw;

	public OPPStandard(Application app, Architecture arc,
					   double Rmax, double Rmin, double Amax, double Amin,
					   double Rw, double Aw) throws ModelException {
		super(app, arc);
		super.getCPlex().setName("OPP MP Standard");
		this.setRmax(Rmax);
		this.setRmin(Rmin);
		this.setAmax(Amax);
		this.setAmin(Amin);
		this.setRw(Rw);
		this.setAw(Aw);
		this.compile(super.getApplication(), super.getArchitecture());
	}
	
	public OPPStandard(Application app, Architecture arc) throws ModelException {
		this(app, arc, 10000.0, 1.0, 1.0, 0.009, 0.5, 0.5);
	}	
	
	public double getRmax() {
		return this.Rmax;
	}
	
	private void setRmax(final double Rmax) {
		this.Rmax = Rmax;
	}
	
	public double getRmin() {
		return this.Rmin;
	}
	
	private void setRmin(final double Rmin) {
		this.Rmin = Rmin;
	}
	
	public double getAmax() {
		return this.Amax;
	}
	
	private void setAmax(final double Amax) {
		this.Amax = Amax;
	}
	
	public double getAmin() {
		return this.Amin;
	}
	
	private void setAmin(final double Amin) {
		this.Amin = Amin;
	}
	
	public double getRw() {
		return this.Rw;
	}
	
	private void setRw(final double Rw) {
		this.Rw = Rw;
	}
	
	public double getAw() {
		return this.Aw;
	}
	
	private void setAw(final double Aw) {
		this.Aw = Aw;
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
				for (Operational opnode : path.subList(0, path.size() - 2)) {
					for (LogicalLink link : arc.edgeSet()) {
						int i = opnode.getId();
						int j = path.get(i + 1).getId();
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
			objRExpr = modeler.prod(modeler.sum(this.getRmax(), modeler.negative(R)), this.getRw() / (this.getRmax() - this.getRmin()));
			objAExpr = modeler.prod(modeler.sum(Alg, -Math.log(this.getAmin())), this.getAw() / (Math.log(this.getAmax()) - Math.log(this.getAmin())));
			objExpr = modeler.sum(objRExpr, objAExpr);
			IloObjective obj = modeler.maximize(objExpr);
			super.getCPlex().addObjective(obj.getSense(), obj.getExpr(), "Standard Objective");
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
