package experiments;

import java.util.stream.Collectors;

import control.exceptions.GeneratorException;
import model.application.Application;
import model.application.ApplicationGenerator;
import model.architecture.Architecture;
import model.architecture.ArchitectureGenerator;
import model.placement.optmodel.cplex.OPPConservative;
import model.placement.optmodel.cplex.OPPRestricted;
import model.placement.optmodel.cplex.OPPStandard;

public final class Experiments {

	private Experiments() {}
	
	public static final String RESULTS_DIR = "./test/experiments/results";	
	
	public static enum UNIT {
		SECOND("s"),
		MILLIS("ms");
		
		private String name;
		
		private UNIT(final String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.getName();
		}
	}
	
	/********************************************************************************
	 * Experiment: Random gaussian generation 		
	 ********************************************************************************/	
	public static final class RandomGeneration {		
		public static final class WRT_Variance {
			public static final double MIN = 0.0;
			public static final double MAX = 10.0;
			
			public static final double VARS[] = {0.1, 0.2, 0.5, 5.0};			
		}
	}
	
	/********************************************************************************
	 * Experiment: Model Creation 		
	 ********************************************************************************/		
	public static final class Creation {
		
		public static final Class<?> CMP_MODELS[] = {OPPStandard.class, OPPRestricted.class, OPPConservative.class};
		public static final int REPETITIONS = 20;
		
		
		/********************************************************************************
		 * Model creation with respect to the number of exnodes		
		 ********************************************************************************/
		public static final class WRT_EXNodes {
			public static final int OPNODES = 30;			
			public static final int EXMIN = 5;
			public static final int EXMAX = 100;
			public static final int EXPAS = 5;			
			
			public static final UNIT MEASURE = UNIT.MILLIS;
			
			public static final Application app() {
				ApplicationGenerator appGen = new ApplicationGenerator();
				Application app = null;
				try {
					app = appGen.setName("Sample Application")
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
			public static final int OPMIN = 5;
			public static final int OPMAX = 100;
			public static final int OPPAS = 5;
			
			public static final UNIT MEASURE = UNIT.MILLIS;
			
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
				ApplicationGenerator appGen = new ApplicationGenerator();
				Application app = null;
				try {
					app = appGen.setName("Sample Application")
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
		 * Model creation with respect to the opnodes pinnability factor	
		 ********************************************************************************/
		public static final class WRT_PINFactor {
			public static final int EXNODES = 60;
			public static final int OPNODES = 30;
			
			public static final double PINMIN = 0.05;
			public static final double PINMAX = 1.0;
			public static final double PINPAS = 0.05;
			
			public static final UNIT MEASURE = UNIT.MILLIS;
			
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
			
			public static final Application app(final Architecture arc, final double pinfact) {
				ApplicationGenerator appGen = new ApplicationGenerator();
				Application app = null;
				try {
					app = appGen.setName("Sample Application")
									.setDescription("Created randomly (uniform)")
									.setSRC(1)
									.setPIP(OPNODES - 2)
									.setSNK(1)	
									.setOPNodeConnectivity(1.0, 1.0)
									.setOPNodePinnability(arc.vertexSet().stream().map(node -> node.getId()).collect(Collectors.toSet()), pinfact, pinfact)
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
	public static final class Resolution {		
		
		public static final Class<?> CMP_MODELS[] = {OPPStandard.class, OPPRestricted.class, OPPConservative.class};
		public static final int REPETITIONS = 3;
		
		
		/********************************************************************************
		 * Model resolution with respect to the number of exnodes		
		 ********************************************************************************/
		public static final class WRT_EXNodes {
			public static final int OPNODES = 20;			
			public static final int EXMIN = 5;
			public static final int EXMAX = 100;
			public static final int EXPAS = 5;
			
			public static final UNIT MEASURE = UNIT.SECOND;
			
			public static final Application app() {
				ApplicationGenerator appGen = new ApplicationGenerator();
				Application app = null;
				try {
					app = appGen.setName("Sample Application")
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
				 				.setEXNodeResources(4, 16)
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
		 * Model resolution with respect to the number of opnodes	 		
		 ********************************************************************************/
		public static final class WRT_OPNodes {
			public static final int EXNODES = 20;
			public static final int OPMIN = 5;
			public static final int OPMAX = 80;
			public static final int OPPAS = 5;
			
			public static final UNIT MEASURE = UNIT.SECOND;
			
			public static final Architecture arc() {
				ArchitectureGenerator arcGen = new ArchitectureGenerator();				
				Architecture arc = null;
				try {
					arc = arcGen.setName("Random Architecture")
								.setDescription("Created randomly (uniform)")
				 				.setEXNodes(EXNODES)
				 				.setEXNodeResources(4, 16)
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
				ApplicationGenerator appGen = new ApplicationGenerator();
				Application app = null;
				try {
					app = appGen.setName("Sample Application")
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
		 * Model resolution with respect to the opnodes pinnability factor 		
		 ********************************************************************************/
		public static final class WRT_PINFactor {
			public static final int EXNODES = 100;
			public static final int OPNODES = 20;
			
			public static final double PINMIN = 0.1;
			public static final double PINMAX = 1.0;
			public static final double PINPAS = 0.1;
			
			public static final UNIT MEASURE = UNIT.SECOND;
			
			public static final Architecture arc() {
				ArchitectureGenerator arcGen = new ArchitectureGenerator();				
				Architecture arc = null;
				try {
					arc = arcGen.setName("Random Architecture")
								.setDescription("Created randomly (uniform)")
				 				.setEXNodes(EXNODES)
				 				.setEXNodeResources(2, 4)
				 				.setEXNodeSpeedup(2.0, 4.0)
				 				.setEXNodeAvailability(0.9, 1.0)
				 				.setLinkDelay(30.0, 300.0)
				 				.setLinkBandwidth(10000.0, 100000.0)
				 				.setLinkAvailability(0.9, 1.0)
				 				.create();
				} catch (GeneratorException exc) {
					exc.printStackTrace();
				}
				
				return arc;
			};
			
			public static final Application app(final Architecture arc, final double pinfact) {
				ApplicationGenerator appGen = new ApplicationGenerator();
				Application app = null;
				try {
					app = appGen.setName("Sample Application")
									.setDescription("Created randomly (uniform)")
									.setSRC(1)
									.setPIP(OPNODES - 2)
									.setSNK(1)	
									.setOPNodeConnectivity(1.0, 1.0)
									.setOPNodePinnability(arc.vertexSet().stream().map(node -> node.getId()).collect(Collectors.toSet()), pinfact, pinfact)
									.setSRCProd(10000.0, 20000.0)
									.setPIPCons(0.7, 0.9)
									.setSNKCons(0.5, 0.7)
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
		 * Model resolution with respect to the opnodes diversity factor 		
		 ********************************************************************************/
		public static final class WRT_DIVFactor {
			public static final int EXNODES = 40;
			public static final int OPNODES = 20;
			
			public static final double DIVMIN = 0.0;
			public static final double DIVMAX = 40.0;
			public static final double DIVPAS = 4.0;
			
			public static final UNIT MEASURE = UNIT.SECOND;
			
			public static final Architecture arc(final double divfact) {
				ArchitectureGenerator arcGen = new ArchitectureGenerator();				
				Architecture arc = null;
				try {
					arc = arcGen.setName("Random Architecture")
								.setDescription("Created randomly (uniform)")
				 				.setEXNodes(EXNODES)
				 				.setEXNodeResources(2, 12, divfact)
				 				.setEXNodeSpeedup(2.0, 8.0, divfact)
				 				.setEXNodeAvailability(0.5, 0.9, divfact)
				 				.setLinkDelay(30.0, 300.0, divfact)
				 				.setLinkBandwidth(1000000.0, 1000000000.0, divfact)
				 				.setLinkAvailability(0.5, 0.9, divfact)
				 				.create();
				} catch (GeneratorException exc) {
					exc.printStackTrace();
				}
				
				return arc;
			};
			
			public static final Application app() {
				ApplicationGenerator appGen = new ApplicationGenerator();
				Application app = null;
				try {
					app = appGen.setName("Sample Application")
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
			}
		}
	}	

}
