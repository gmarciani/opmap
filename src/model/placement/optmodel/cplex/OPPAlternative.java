package model.placement.optmodel.cplex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import control.exceptions.ModelException;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloModeler;
import ilog.concert.IloNumExpr;
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
import model.placement.optmodel.AbstractOPPAlternative;
import model.placement.variable.restricted.RestrictedPlacementX;
import model.placement.variable.restricted.RestrictedPlacementY;

public class OPPAlternative extends AbstractOPPAlternative {

	public OPPAlternative(Application app, Architecture arc,
			   			  double Rmax, double Amin, double Rw, double Aw) throws ModelException {
		super("OPP Alternative", app, arc, Rmax, Amin, Rw, Aw);
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
		try {
			this.X = new RestrictedPlacementX(app, arc);
			this.Y = new RestrictedPlacementY(app, arc);
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
						if (!opnode.isPinnableOn(exnode))
							continue;
						int i = opnode.getId();
						int u = exnode.getId();
						Rpex.addTerm(opnode.getSpeed() / exnode.getSpeedup(), this.X.get(i, u));
					}						
				}							
				
				for (DStream dstream : path) {
					for (Link link : arc.edgeSet()) {
						if (!dstream.isPinnableOn(link))
							continue;
						int i = dstream.getSrc().getId();
						int j = dstream.getDst().getId();
						int u = link.getSrc().getId();
						int v = link.getDst().getId();
						Rptx.addTerm(link.getDelay(), this.Y.get(i, j, u, v));
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
					if (!opnode.isPinnableOn(exnode))
						continue;
					int i = opnode.getId();
					int u = exnode.getId();
					Aex.addTerm(Math.log(exnode.getAvailability()), this.X.get(i, u));
				}					
			}				
			
			for (DStream dstream : this.getApplication().edgeSet()) {
				for (Link link : this.getArchitecture().edgeSet()) {
					if (!dstream.isPinnableOn(link))
						continue;
					int i = dstream.getSrc().getId();
					int j = dstream.getDst().getId();
					int u = link.getSrc().getId();
					int v = link.getDst().getId();
					Atx.addTerm(Math.log(link.getAvailability()), this.Y.get(i, j, u, v));
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
			objRExpr = modeler.prod(R, this.getRw() / this.getRmax());
			objAExpr = modeler.prod(Alg, this.getAw() / Math.log(this.getAmin()));
			objExpr  = modeler.sum(modeler.negative(objRExpr), objAExpr);
			obj 	 = modeler.maximize(objExpr);
			super.getCPlex().addObjective(obj.getSense(), obj.getExpr(), "AlternativeObj");
		} catch (IloException exc) {
			throw new ModelException("Error while defining objective: " + exc.getMessage());
		}	
		
		/********************************************************************************
		 * Capacity Bound	
		 ********************************************************************************/
		try {
			for (EXNode exnode : arc.vertexSet()) {
				IloLinearNumExpr exprCapacity = modeler.linearNumExpr();
				for (OPNode opnode : app.vertexSet()) {
					if (!opnode.isPinnableOn(exnode))
						continue;
					int i = opnode.getId();
					int u = exnode.getId();
					exprCapacity.addTerm(opnode.getResources(), this.X.get(i, u));
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
					if (!opnode.isPinnableOn(exnode))
						continue;
					int i = opnode.getId();
					int u = exnode.getId();
					exprUniqueness.addTerm(1.0, this.X.get(i, u));
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
				for (EXNode exnodeU : arc.vertexSet()) {
					if (!dstream.getSrc().isPinnableOn(exnodeU))
						continue;
					IloLinearNumExpr exprConn = modeler.linearNumExpr();
					int i = dstream.getSrc().getId();
					int j = dstream.getDst().getId();
					int u = exnodeU.getId();
					for (EXNode exnodeV : arc.vertexSet()) {
						if (!dstream.getDst().isPinnableOn(exnodeV)) 
							continue;
						int v = exnodeV.getId();
						exprConn.addTerm(1.0, this.Y.get(i, j, u, v));
					}
					super.getCPlex().addEq(this.X.get(i, u), exprConn, 
							"ConnectivityBound1-dstream" + i + "," + j);
				}
			}
		
			for (DStream dstream : app.edgeSet()) {
				for (EXNode exnodeV : arc.vertexSet()) {
					if (!dstream.getDst().isPinnableOn(exnodeV))
						continue;
					IloLinearNumExpr exprConn = modeler.linearNumExpr();
					int i = dstream.getSrc().getId();
					int j = dstream.getDst().getId();
					int v = exnodeV.getId();
					for (EXNode exnodeU : arc.vertexSet()) {
						if (!dstream.getSrc().isPinnableOn(exnodeU))
							continue;
						int u = exnodeU.getId();
						exprConn.addTerm(1.0, this.Y.get(i, j, u, v));						
					}	
					super.getCPlex().addEq(this.X.get(j, v), exprConn, 
							"ConnectivityBound2-dstream" + i + "," + j);
				}
			}
		} catch (IloException exc) {
			throw new ModelException("Error while defining Connectivity Bound: " + exc.getMessage());
		}		
	}

}
