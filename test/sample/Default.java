package sample;

import model.placement.optmodel.cplex.OPPAlternative;
import model.placement.optmodel.cplex.OPPConservative;
import model.placement.optmodel.cplex.OPPRestricted;
import model.placement.optmodel.cplex.OPPStandard;

public final class Default {

	private Default() {}
	
	/********************************************************************************
	 * Application Deterministic Generator		
	 ********************************************************************************/
	
	public static final int 	OPNODE_RESOURCES = 1;
	public static final double 	OPNODE_SRC_FLOW = 1000.0;
	public static final double 	OPNODE_SNK_FLOW = 1;
	public static final double 	OPNODE_PIP_TRANS = 0.5;
	public static final long 	OPNODE_SPEED = 1;
	
	/********************************************************************************
	 * Application Random Generator		
	 ********************************************************************************/
	
	public static final int OPNODE_RND = 5;
	
	/********************************************************************************
	 * Architecture Deterministic Generator		
	 ********************************************************************************/
	
	public static final int EXNODE_RESOURCES = 4;
	public static final double EXNODE_SPEEDUP = 10.0;
	public static final double EXNODE_AVAILABILITY = 1.0;
	
	/********************************************************************************
	 * Architecture Random Generator		
	 ********************************************************************************/
	
	/********************************************************************************
	 * Experiments
	 ********************************************************************************/	
	public static final class Experiments {
		public static final String RESULTS_DIR = "./test/experiments/results/";		
		
		
		/********************************************************************************
		 * Experiment: Model Creation 		
		 ********************************************************************************/		
		public static final class ModelCreation {
			public static final Class<?> CMP_MODELS[] = {OPPStandard.class, OPPRestricted.class, OPPConservative.class, OPPAlternative.class};
			/********************************************************************************
			 * Fixed OPNodes 		
			 ********************************************************************************/
			public static final int FIXED_OPNODES = 20;			
			public static final int EXMIN = 10;
			public static final int EXMAX = 100;
			public static final int EXPAS = 2;
			
			/********************************************************************************
			 * Fixed EXNodes 		
			 ********************************************************************************/
			public static final int FIXED_EXNODES = 40;
			public static final int OPMIN = 10;
			public static final int OPMAX = 100;
			public static final int OPPAS = 2;
			
			public static final int REPETITIONS = 10;
		}
		
		/********************************************************************************
		 * Experiment: Model Resolution 		
		 ********************************************************************************/		
		public static final class ModelResolution {
			/********************************************************************************
			 * Fixed OPNodes 		
			 ********************************************************************************/
			public static final int CMP_OPNODES[] = {5, 10, 15};
			public static final int FIXED_OPNODES = 10;
			public static final int EXMIN = 5;
			public static final int EXMAX = 50;
			public static final int EXPAS = 5;
			
			/********************************************************************************
			 * Fixed EXNodes 		
			 ********************************************************************************/		
			public static final int CMP_EXNODES[] = {10, 20, 30};
			public static final int FIXED_EXNODES = 20;
			public static final int OPMIN = 5;
			public static final int OPMAX = 50;
			public static final int OPPAS = 5;	
			
			public static final int REPETITIONS = 5;
		}
	}	

}
