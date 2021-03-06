package opmap.model.placement.optmodel.cplex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloModeler;
import ilog.concert.IloNumExpr;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplexModeler;
import opmap.control.exception.ModelException;
import opmap.model.application.Application;
import opmap.model.application.DStream;
import opmap.model.application.OPNode;
import opmap.model.application.OPPath;
import opmap.model.architecture.Architecture;
import opmap.model.architecture.EXNode;
import opmap.model.architecture.Link;
import opmap.model.placement.optmodel.AbstractOPPStandard;
import opmap.model.placement.variable.standard.StandardPlacementX;
import opmap.model.placement.variable.standard.StandardPlacementY;

public class OPPStandard extends AbstractOPPStandard {

	public OPPStandard(Application app, Architecture arc,
					   double Rmax, double Rmin, double Amax, double Amin, double Rw, double Aw) throws ModelException {
		super("OPP Standard", app, arc, Rmax, Rmin, Amax, Amin, Rw, Aw);
		this.compile(super.getApplication(), super.getArchitecture());
	}
	
	public OPPStandard(Application app, Architecture arc) throws ModelException {
		this(app, arc, DEFAULT_RMAX, DEFAULT_RMIN, DEFAULT_AMAX, DEFAULT_AMIN, DEFAULT_RW, DEFAULT_AW);
	}	

	@Override
	public void compile(Application app, Architecture arc) throws ModelException {		
		IloModeler modeler = new IloCplexModeler();	
		
		/********************************************************************************
		 * Decision Variables		
		 ********************************************************************************/
		try {
			this.X = new StandardPlacementX(app, arc);
			this.Y = new StandardPlacementY(app, arc);
		} catch (IloException exc) {
			throw new ModelException("Error while defining decision variables X and Y: " + exc.getMessage());
		}
		
		/********************************************************************************
		 * Response-Time		
		 ********************************************************************************/
		Set<OPPath> paths = this.getApplication().getAllOperationalPaths();
		List<IloNumExpr> Rpaths = new ArrayList<IloNumExpr>(paths.size());
		IloNumExpr R;
		try {
			for (OPPath path : paths) {
				IloLinearNumExpr Rpex = modeler.linearNumExpr();	
				IloLinearNumExpr Rptx = modeler.linearNumExpr();
				
				for (OPNode opnode : path.getNodes()) {
					for (EXNode exnode : this.getArchitecture().vertexSet()) {
						int i = opnode.getId();
						int u = exnode.getId();
						Rpex.addTerm(opnode.getSpeed() / exnode.getSpeedup(), X.get(i, u));
					}						
				}		
				
				for (DStream dstream : path) {
					for (Link link : arc.edgeSet()) {
						int i = dstream.getSrc().getId();
						int j = dstream.getDst().getId();
						int u = link.getSrc().getId();
						int v = link.getDst().getId();
						Rptx.addTerm(link.getDelay(), Y.get(i, j, u, v));
					}
				}				
				
				IloNumExpr Rp = modeler.sum(Rpex, Rptx);
				Rpaths.add(Rp);
			}				
			R = modeler.max(Rpaths.toArray(new IloNumExpr[Rpaths.size()]));
		} catch (IloException exc) {
			throw new ModelException("Error while defining Response-Time: " + exc.getMessage());
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
					Aex.addTerm(Math.log(exnode.getAvailability()), X.get(i, u));
				}					
			}				
			
			for (DStream dstream : this.getApplication().edgeSet()) {
				for (Link link : this.getArchitecture().edgeSet()) {
					int i = dstream.getSrc().getId();
					int j = dstream.getDst().getId();
					int u = link.getSrc().getId();
					int v = link.getDst().getId();
					Atx.addTerm(Math.log(link.getAvailability()), Y.get(i, j, u, v));
				}					
			}
					
			Alg = modeler.sum(Aex, Atx);
		} catch (IloException exc) {
			throw new ModelException("Error while defining Availability: " + exc.getMessage());
		}

		/********************************************************************************
		 * Objective
		 ********************************************************************************/	
		IloObjective obj;
		IloNumExpr objRExpr, objAExpr, objExpr;
		try {			
			objRExpr = modeler.prod(modeler.sum(this.getRmax(), modeler.negative(R)), this.getRw() / (this.getRmax() - this.getRmin()));
			objAExpr = modeler.prod(modeler.sum(Alg, -Math.log(this.getAmin())), this.getAw() / (Math.log(this.getAmax()) - Math.log(this.getAmin())));
			objExpr  = modeler.sum(objRExpr, objAExpr);
			obj 	 = modeler.maximize(objExpr);
			super.getCPlex().addObjective(obj.getSense(), obj.getExpr(), "StandardObj");
		} catch (IloException exc) {
			throw new ModelException("Error while defining Objective Function: " + exc.getMessage());
		}		
		
		/********************************************************************************
		 * Eligibility Bound
		 ********************************************************************************/
		try {
			for (OPNode opnode : app.vertexSet()) {
				for (EXNode exnode : arc.vertexSet()) {
					int i = opnode.getId();
					int u = exnode.getId();
					IloRange cnsCapacity = modeler.ge(opnode.isPinnableOn(exnode)?1:0, X.get(i, u));
					super.getCPlex().addRange(cnsCapacity.getLB(), cnsCapacity.getExpr(), cnsCapacity.getUB(), 
							"EligibilityBound-opnode" + opnode.getId() + "-exnode" + exnode.getId());
				}					
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining Eligibility Bound: " + exc.getMessage());
		}
		
		/********************************************************************************
		 * Capacity Bound	
		 ********************************************************************************/
		try {
			for (EXNode exnode : arc.vertexSet()) {
				IloLinearNumExpr exprCapacity = modeler.linearNumExpr();
				for (OPNode opnode : app.vertexSet()) {
					int i = opnode.getId();
					int u = exnode.getId();
					exprCapacity.addTerm(opnode.getResources(), X.get(i, u));
				}					
				IloRange cnsCapacity = modeler.ge(exnode.getResources(), exprCapacity);
				super.getCPlex().addRange(cnsCapacity.getLB(), cnsCapacity.getExpr(), cnsCapacity.getUB(), 
						"CapacityBound-exnode:" + exnode.getId());
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining Capacity Bound: " + exc.getMessage());
		}	
		
		/********************************************************************************
		 * Uniqueness Bound	
		 ********************************************************************************/		
		try {
			for (OPNode opnode : app.vertexSet()) {
				IloLinearNumExpr exprUniqueness = modeler.linearNumExpr();
				for (EXNode exnode : arc.vertexSet()) {
					int i = opnode.getId();
					int u = exnode.getId();
					exprUniqueness.addTerm(1.0, X.get(i, u));
				}
				IloRange cnsUniqueness = modeler.eq(1.0, exprUniqueness);
				super.getCPlex().addRange(cnsUniqueness.getLB(), cnsUniqueness.getExpr(), cnsUniqueness.getUB(), 
						"UniquenessBound-opnode:" + opnode.getId());
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining Uniqueness Bound: " + exc.getMessage());
		}
		
		/********************************************************************************
		 * Connectivity Bound
		 ********************************************************************************/
		try {
			for (DStream dstream : app.edgeSet()) {
				for (Link link : arc.edgeSet()) {
					IloLinearNumExpr exprConn1 = modeler.linearNumExpr();
					IloLinearNumExpr exprConn2 = modeler.linearNumExpr();
					int i = dstream.getSrc().getId();
					int j = dstream.getDst().getId();
					int u = link.getSrc().getId();
					int v = link.getDst().getId();
					
					exprConn1.addTerm(1.0, X.get(i, u));
					exprConn1.addTerm(1.0, X.get(j, v));
					exprConn1.addTerm(-1.0, Y.get(i, j, u, v));
					IloRange cnsConn1 = modeler.ge(1.0, exprConn1);
					super.getCPlex().addRange(cnsConn1.getLB(), cnsConn1.getExpr(), cnsConn1.getUB(), 
							"ConnectivityBound1-dstream" + i + "," + j + "-link:" + u + "," + v);
					
					exprConn2.addTerm(1.0, X.get(i, u));
					exprConn2.addTerm(1.0, X.get(j, v));
					exprConn2.addTerm(-2.0, Y.get(i, j, u, v));
					IloRange cnsConnectivityTwo = modeler.le(0.0, exprConn2);
					super.getCPlex().addRange(cnsConnectivityTwo.getLB(), cnsConnectivityTwo.getExpr(), cnsConnectivityTwo.getUB(), 
							"ConnectivityBound2-dstream" + i + "," + j + "-link:" + u + "," + v);
				}				
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining Connectivity Bound: " + exc.getMessage());
		}
	}	
	
}
