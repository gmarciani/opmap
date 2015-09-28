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
import ilog.cplex.IloCplex;
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
	public IloCplex getCPlexModel() {
		return super.getCPlex();
	}

	@Override
	public void compile(Application app, Architecture arc) throws ModelException {		
		IloModeler modeler = new IloCplexModeler();	
		
		// Variables
		IloNumVar X[][] = new IloIntVar[app.vertexSet().size()][arc.vertexSet().size()];
		IloNumVar Y[][][][] = new IloIntVar[app.vertexSet().size()][app.vertexSet().size()][arc.vertexSet().size()][arc.vertexSet().size()];
		
		try {
			for (Operational opnode : this.getApplication().vertexSet())
				for (Computational exnode : this.getArchitecture().vertexSet())
					X[opnode.getId()][exnode.getId()] = modeler.boolVar();
			for (DataStream dstream : this.getApplication().edgeSet())
				for (LogicalLink link : this.getArchitecture().edgeSet())
					Y[app.getEdgeSource(dstream).getId()][app.getEdgeTarget(dstream).getId()]
					 [arc.getEdgeSource(link).getId()][arc.getEdgeTarget(link).getId()] = modeler.boolVar();
		} catch (IloException exc) {
			throw new ModelException("Error while defining variables: " + exc.getMessage());
		}
		
		/*// Response Time
		Set<OperationalPath> paths = this.getApplication().getAllOperationalPaths();
		IloNumExpr R;
		List<IloNumExpr> Rpaths = new ArrayList<IloNumExpr>(paths.size());
		try {
			R = modeler.numExpr();
			for (OperationalPath path : paths) {
				IloLinearNumExpr Rpex = modeler.linearNumExpr();
				
				for (Operational opnode : path)
					for (Computational exnode : this.getArchitecture().vertexSet())
						Rpex.addTerm(opnode.getSpeed() / exnode.getSpeedup(), X[opnode.getId()][exnode.getId()]);
				
				IloLinearNumExpr Rptx = modeler.linearNumExpr();
				
				//
				IloNumExpr Rp = modeler.sum(0.0, Rptx);
				Rpaths.add(Rp);
			}				
			R = modeler.max(Rpaths.toArray(new IloNumExpr[Rpaths.size()]));
		} catch (IloException exc) {
			throw new ModelException("Error while defining response time: " + exc.getMessage());
		}*/
		
		// Availability
		IloNumExpr A;
		IloLinearNumExpr Aex, Atx;
		try {
			A 	= modeler.numExpr();
			Aex = modeler.linearNumExpr();
			Atx = modeler.linearNumExpr();
			
			for (Operational opnode : this.getApplication().vertexSet())
				for (Computational exnode : this.getArchitecture().vertexSet())
					Aex.addTerm(Math.log(exnode.getAvailability()), X[opnode.getId()][exnode.getId()]);
			
			for (DataStream dstream : this.getApplication().edgeSet())
				for (LogicalLink link : this.getArchitecture().edgeSet())
					Atx.addTerm(Math.log(link.getAvailability()), Y[this.getApplication().getEdgeSource(dstream).getId()]
																   [this.getApplication().getEdgeTarget(dstream).getId()]
																   [this.getArchitecture().getEdgeSource(link).getId()]
																   [this.getArchitecture().getEdgeTarget(link).getId()]);	
			A = modeler.sum(Aex, Atx);
		} catch (IloException exc) {
			throw new ModelException("Error while defining availability: " + exc.getMessage());
		}

		// Objective	
		IloNumExpr objRExpr, objAExpr, objExpr;
		try {
			objRExpr = modeler.numExpr();
			objAExpr = modeler.numExpr();
			objExpr  = modeler.numExpr();
			
			//objRExpr = modeler.prod(modeler.sum(this.getRmax(), modeler.negative(R)), this.getRw() / (this.getRmax() - this.getRmin()));
			objAExpr = modeler.prod(modeler.sum(A, -Math.log(this.getAmin())), this.getAw() / (Math.log(this.getAmax()) - Math.log(this.getAmin())));
			objExpr = modeler.sum(objRExpr, objAExpr);
			IloObjective obj = modeler.maximize(objExpr);
			super.getCPlex().addObjective(obj.getSense(), obj.getExpr());
		} catch (IloException exc) {
			throw new ModelException("Error while defining objective: " + exc.getMessage());
		}		
		
		// Capacity Bound		
		try {
			for (Computational exnode : arc.vertexSet()) {
				IloLinearNumExpr exprCapacity = modeler.linearNumExpr();
				for (Operational opnode : app.vertexSet()) {
					exprCapacity.addTerm(opnode.getResources(), X[opnode.getId()][exnode.getId()]);
				}
				IloRange cnsCapacity = modeler.le(exnode.getResources(), exprCapacity);
				super.getCPlex().addRange(cnsCapacity.getLB(), cnsCapacity.getExpr(), cnsCapacity.getUB());
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining capacity bound: " + exc.getMessage());
		}	
		
		// Unicity Bound		
		try {
			for (Operational opnode : app.vertexSet()) {
				IloLinearNumExpr exprUnicity = modeler.linearNumExpr();
				for (Computational exnode : arc.vertexSet()) {
					exprUnicity.addTerm(1.0, X[opnode.getId()][exnode.getId()]);
				}
				IloRange cnsUnicity = modeler.eq(1.0, exprUnicity);
				super.getCPlex().addRange(cnsUnicity.getLB(), cnsUnicity.getExpr(), cnsUnicity.getUB());
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
					int i = app.getEdgeSource(dstream).getId();
					int j = app.getEdgeTarget(dstream).getId();
					int u = arc.getEdgeSource(link).getId();
					int v = arc.getEdgeTarget(link).getId();
					
					exprConnectivityOne.addTerm(1.0, X[i][u]);
					exprConnectivityOne.addTerm(1.0, X[j][v]);
					exprConnectivityOne.addTerm(-1.0, Y[i][j][u][v]);
					IloRange cnsConnectivityOne = modeler.le(1.0, exprConnectivityOne);
					super.getCPlex().addRange(cnsConnectivityOne.getLB(), cnsConnectivityOne.getExpr(), cnsConnectivityOne.getUB());
					
					exprConnectivityTwo.addTerm(1.0, X[i][u]);
					exprConnectivityTwo.addTerm(1.0, X[j][v]);
					exprConnectivityTwo.addTerm(-2.0, Y[i][j][u][v]);
					IloRange cnsConnectivityTwo = modeler.ge(1.0, exprConnectivityTwo);
					super.getCPlex().addRange(cnsConnectivityTwo.getLB(), cnsConnectivityTwo.getExpr(), cnsConnectivityTwo.getUB());
				}				
			}			
		} catch (IloException exc) {
			throw new ModelException("Error while defining conectivity bound (one): " + exc.getMessage());
		}
	}	
	
}
