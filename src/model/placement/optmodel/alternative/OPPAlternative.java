package model.placement.optmodel.alternative;

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
import model.application.dstream.DStream;
import model.application.opnode.OPNode;
import model.application.opnode.OPPath;
import model.architecture.Architecture;
import model.architecture.exnode.EXNode;
import model.architecture.link.Link;

public class OPPAlternative extends AbstractOPPAlternative {

	public OPPAlternative(Application app, Architecture arc,
			   				double Rmax, double Amin,
			   				double Rw, double Aw) throws ModelException {
		super(app, arc, Rmax, Amin, Rw, Aw);
		super.getCPlex().setName("OPP MP Alternative");
		this.compile(super.getApplication(), super.getArchitecture());
	}

	public OPPAlternative(Application app, Architecture arc) throws ModelException {
		this(app, arc, DEFAULT_RMAX, DEFAULT_AMIN, DEFAULT_RW, DEFAULT_AW);
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
			for (OPNode opnode : this.getApplication().vertexSet()) {
				for (EXNode exnode : this.getArchitecture().vertexSet()) {
					int i = opnode.getId();
					int u = exnode.getId();
					X[i][u] = modeler.boolVar("X[" + i + "][" + u + "]");
				}					
			}				
		} catch (IloException exc) {
			throw new ModelException("Error while defining X variables: " + exc.getMessage());
		}
		
		try {
			for (DStream dstream : this.getApplication().edgeSet()) {
				for (Link link : this.getArchitecture().edgeSet()) {
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
		Set<OPPath> paths = this.getApplication().getAllOperationalPaths();
		List<IloNumExpr> Rpaths = new ArrayList<IloNumExpr>(paths.size());
		IloNumExpr R;
		try {
			for (OPPath path : paths) {
				IloLinearNumExpr Rpex = modeler.linearNumExpr();				
				for (OPNode opnode : path.getNodes()) {
					for (EXNode exnode : this.getArchitecture().vertexSet()) {
						int i = opnode.getId();
						int u = exnode.getId();
						Rpex.addTerm(opnode.getSpeed() / exnode.getSpeedup(), X[i][u]);
					}						
				}					
				
				IloLinearNumExpr Rptx = modeler.linearNumExpr();
				for (int k = 0; k < path.size() - 1; k++) {
					for (Link link : arc.edgeSet()) {
						int i = path.getNodes().get(k).getId();
						int j = path.getNodes().get(k + 1).getId();
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
			
			for (OPNode opnode : this.getApplication().vertexSet()) {
				for (EXNode exnode : this.getArchitecture().vertexSet()) {
					int i = opnode.getId();
					int u = exnode.getId();
					Aex.addTerm(Math.log(exnode.getAvailability()), X[i][u]);
				}					
			}				
			
			for (DStream dstream : this.getApplication().edgeSet()) {
				for (Link link : this.getArchitecture().edgeSet()) {
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
			for (EXNode exnode : arc.vertexSet()) {
				IloLinearNumExpr exprCapacity = modeler.linearNumExpr();
				for (OPNode opnode : app.vertexSet()) {
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
			for (OPNode opnode : app.vertexSet()) {
				IloLinearNumExpr exprUnicity = modeler.linearNumExpr();
				for (EXNode exnode : arc.vertexSet()) {
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
			for (DStream dstream : app.edgeSet()) {
				for (Link link : arc.edgeSet()) {
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
