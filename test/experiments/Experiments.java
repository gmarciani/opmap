package experiments;

import java.util.stream.Collectors;

import control.exceptions.GeneratorException;
import model.application.Application;
import model.application.ApplicationGenerator;
import model.architecture.Architecture;
import model.architecture.ArchitectureGenerator;
import model.placement.optmodel.cplex.OPPAlternative;
import model.placement.optmodel.cplex.OPPConservative;
import model.placement.optmodel.cplex.OPPRestricted;
import model.placement.optmodel.cplex.OPPStandard;

public final class Experiments {

	private Experiments() {}
	
	public static final String RESULTS_DIR = "./test/experiments/results/";			
	
	/********************************************************************************
	 * Experiment: Model Creation 		
	 ********************************************************************************/		
	public static final class ModelCreation {
		
		public static final Class<?> CMP_MODELS[] = {OPPStandard.class, OPPRestricted.class, OPPConservative.class, OPPAlternative.class};
		public static final int REPETITIONS = 10;
		
		/********************************************************************************
		 * Model creation with respect to the number of exnodes		
		 ********************************************************************************/
		public static final class WRT_EXNodes {
			public static final int OPNODES = 30;			
			public static final int EXMIN = 10;
			public static final int EXMAX = 100;
			public static final int EXPAS = 5;
			
			public static final Application app() {
				ApplicationGenerator appFactory = new ApplicationGenerator();
				Application app = null;
				try {
					app = appFactory.setName("Sample Application")
									.setDescription("Created randomly (uniform)")
									.setSRC(1)
									.setPIP(OPNODES - 2)
									.setSNK(1)	
									.setOPNodeConnectivity(1.0, 1.0)
									.setSRCProd(1000.0, 10000.0)
									.setPIPCons(0.3, 0.5)
									.setSNKCons(0.2, 0.4)
									.setOPNodeResources(1, 2)
									.setOPNodeSpeed(1000.0, 2000.0)							
									.create();
				} catch (GeneratorException exc) {
					exc.printStackTrace();
				}
				
				return app;
			};
			
			public static final Architecture arc(final int exnodes) {
				ArchitectureGenerator arcGen = new ArchitectureGenerator();				
				Architecture arc = null;
				try {
					arc = arcGen.setName("Random Architecture")
								.setDescription("Created randomly (uniform)")
				 				.setEXNodes(exnodes)
				 				.setEXNodeResources(2, 4)
				 				.setEXNodeSpeedup(2.0, 8.0)
				 				.setEXNodeAvailability(0.6, 0.7)
				 				.setLinkDelay(30.0, 300.0)
				 				.setLinkBandwidth(1000000.0, 1000000000.0)
				 				.setLinkAvailability(0.7, 0.9)
				 				.create();
				} catch (GeneratorException exc) {
					exc.printStackTrace();
				}
				
				return arc;
			}
		}
		
		
		/********************************************************************************
		 * Model creation with respect to the number of opnodes	 		
		 ********************************************************************************/
		public static final class WRT_OPNodes {
			public static final int EXNODES = 30;
			public static final int OPMIN = 10;
			public static final int OPMAX = 100;
			public static final int OPPAS = 5;
			
			public static final Architecture arc() {
				ArchitectureGenerator arcGen = new ArchitectureGenerator();				
				Architecture arc = null;
				try {
					arc = arcGen.setName("Random Architecture")
								.setDescription("Created randomly (uniform)")
				 				.setEXNodes(EXNODES)
				 				.setEXNodeResources(2, 4)
				 				.setEXNodeSpeedup(2.0, 8.0)
				 				.setEXNodeAvailability(0.6, 0.7)
				 				.setLinkDelay(30.0, 300.0)
				 				.setLinkBandwidth(1000000.0, 1000000000.0)
				 				.setLinkAvailability(0.7, 0.9)
				 				.create();
				} catch (GeneratorException exc) {
					exc.printStackTrace();
				}
				
				return arc;
			};
			
			public static final Application app(final int opnodes) {
				ApplicationGenerator appFactory = new ApplicationGenerator();
				Application app = null;
				try {
					app = appFactory.setName("Sample Application")
									.setDescription("Created randomly (uniform)")
									.setSRC(1)
									.setPIP(opnodes - 2)
									.setSNK(1)	
									.setOPNodeConnectivity(1.0, 1.0)
									.setSRCProd(1000.0, 10000.0)
									.setPIPCons(0.3, 0.5)
									.setSNKCons(0.2, 0.4)
									.setOPNodeResources(1, 2)
									.setOPNodeSpeed(1000.0, 2000.0)							
									.create();
				} catch (GeneratorException exc) {
					exc.printStackTrace();
				}
				
				return app;
			}
		}
		
		/********************************************************************************
		 * Model creation with respect to the opnodes pinnability	 		
		 ********************************************************************************/
		public static final class WRT_PinnDegree {
			public static final int EXNODES = 100;
			public static final int OPNODES = 30;
			
			public static final double PINMIN = 0.1;
			public static final double PINMAX = 1.0;
			public static final double PINPAS = 0.05;
			
			public static final Architecture arc() {
				ArchitectureGenerator arcGen = new ArchitectureGenerator();				
				Architecture arc = null;
				try {
					arc = arcGen.setName("Random Architecture")
								.setDescription("Created randomly (uniform)")
				 				.setEXNodes(EXNODES)
				 				.setEXNodeResources(2, 4)
				 				.setEXNodeSpeedup(2.0, 8.0)
				 				.setEXNodeAvailability(0.6, 0.7)
				 				.setLinkDelay(30.0, 300.0)
				 				.setLinkBandwidth(1000000.0, 1000000000.0)
				 				.setLinkAvailability(0.7, 0.9)
				 				.create();
				} catch (GeneratorException exc) {
					exc.printStackTrace();
				}
				
				return arc;
			};
			
			public static final Application app(final Architecture arc, final double pinDeg) {
				ApplicationGenerator appFactory = new ApplicationGenerator();
				Application app = null;
				try {
					app = appFactory.setName("Sample Application")
									.setDescription("Created randomly (uniform)")
									.setSRC(1)
									.setPIP(OPNODES - 2)
									.setSNK(1)	
									.setOPNodeConnectivity(1.0, 1.0)
									.setOPNodePinnability(arc.vertexSet().stream().map(node -> node.getId()).collect(Collectors.toSet()), pinDeg, pinDeg)
									.setSRCProd(1000.0, 10000.0)
									.setPIPCons(0.3, 0.5)
									.setSNKCons(0.2, 0.4)
									.setOPNodeResources(1, 2)
									.setOPNodeSpeed(1000.0, 2000.0)							
									.create();
				} catch (GeneratorException exc) {
					exc.printStackTrace();
				}
				
				return app;
			}
		}
		
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
