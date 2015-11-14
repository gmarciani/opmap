/**********************************************************************************
 * OPL 12.6.2.0 Model
 * 
 * Project: Opmap
 * Version: 1.0
 * Author: Giacomo Marciani
 *
 * Description: Operator Placement Problem (OPP): alternative PL{01} formulation 
 *				in Mathematical Programming, as stated in "Optimal DSP Deployment"
 *				by prof. Francesco Lo Presti.
 *********************************************************************************/


 /*********************************************
 * Resources Graph
 *********************************************/
 
tuple NodeRES {
	key int id; 		// [identifier] unique identifier	
	int capacity; 		// [cores] resources offer
	float speedup; 		// [ratio] speedup (wrt. referenced processor)
	float availability; // [0,1] probability to be up&running
};

tuple EdgeRES {
	key NodeRES src; 	// [identifier] source node
	key NodeRES dst; 	// [identifier] destination node	
	float delay; 		// [seconds] round-trip-time
	float bandwidth; 	// [bps] (unused for now)
	float availability; // [0,1] probability to be up&running
};

{NodeRES} nodesRES = ...;

{EdgeRES} edgesRES with src in nodesRES, dst in nodesRES = ...;


/*********************************************
 * DSP Operators
 *********************************************/
 
tuple Operator {
	key string name;	// [identifier] operator name
};
 
{Operator} operators = ...;


/*********************************************
 * DSP Application Graph
 *********************************************/

tuple NodeDSP {
	key int id; 		// [identifier] unique identifier	
	Operator operator; 	// [identifier] operator
	int resources; 		// [cores] resources demand
	int execution; 		// [seconds] execution time (wrt. referenced processor)	
	float flowIn; 		// [tuples] tuples flowing in
	float flowOut; 		// [tuples] tuples flowing out
};

tuple EdgeDSP {
	key NodeDSP src; 	// [identifier] source node
	key NodeDSP dst; 	// [identifier] destination node	
	float flow; 		// [tuples] tuple flowing through
};

tuple PathDSP {
	ordered {int} nodes;
};

{NodeDSP} nodesDSP  with operator in operators = ...;

{EdgeDSP} edgesDSP with src in nodesDSP, dst in nodesDSP = ...;

{PathDSP} pathsDSP = ...; 

{NodeDSP} srcNodes = {nodeDSP|nodeDSP in nodesDSP: nodeDSP.operator.name == "src"};

{NodeDSP} snkNodes = {nodeDSP|nodeDSP in nodesDSP: nodeDSP.operator.name == "snk"};


/*********************************************
 * Eligible Pins/Links
 *********************************************/

int eligiblePins[nodesDSP][nodesRES] = ...;


/*********************************************
 * Response Time
 *********************************************/
 
float wR = ...;
float maxR = ...;


/*********************************************
 * Availability
 *********************************************/
 
float wA = ...;
float minA = ...;


/*********************************************
 * Decision Variables
 *********************************************/

dvar boolean placeNode[nodesDSP][nodesRES];
dvar boolean placeEdge[edgesDSP][edgesRES];


/*********************************************
 * Decision Expressions for Response Time
 *********************************************/

// Execution component
dexpr float Rex_p[pathDSP in pathsDSP] = 
sum (nodeDSP in nodesDSP: nodeDSP.id in pathDSP.nodes)
	sum (nodeRES in nodesRES: eligiblePins[nodeDSP][nodeRES] == 1)
		placeNode[nodeDSP][nodeRES] * (nodeDSP.execution / nodeRES.speedup);

// Transmission component
dexpr float Rtx_p[pathDSP in pathsDSP] = 
sum (edgeDSP in edgesDSP: edgeDSP.src.id in pathDSP.nodes diff {last(pathDSP.nodes)} && edgeDSP.dst.id in pathDSP.nodes diff {first(pathDSP.nodes)})
	sum (edgeRES in edgesRES: eligiblePins[edgeDSP.src][edgeRES.src] == 1 && eligiblePins[edgeDSP.dst][edgeRES.dst] == 1)
		placeEdge[edgeDSP][edgeRES] * edgeRES.delay;
							
dexpr float R_p[pathDSP in pathsDSP] = Rex_p[pathDSP] + Rtx_p[pathDSP];
 
dexpr float R = max (pathDSP in pathsDSP) R_p[pathDSP];


/*********************************************
 * Decision Expressions for Availability
 *********************************************/

// Execution component
dexpr float Aex = 
sum (nodeDSP in nodesDSP)
	sum (nodeRES in nodesRES: eligiblePins[nodeDSP][nodeRES] == 1)
		placeNode[nodeDSP][nodeRES] * log(nodeRES.availability);

// Transmission component							
dexpr float Atx = 
sum (edgeDSP in edgesDSP)
	sum (edgeRES in edgesRES: eligiblePins[edgeDSP.src][edgeRES.src] == 1 && eligiblePins[edgeDSP.dst][edgeRES.dst] == 1)
		placeEdge[edgeDSP][edgeRES] * log(edgeRES.availability);
								
dexpr float A = Aex + Atx;


/*********************************************
 * Objective
 *********************************************/

dexpr float Objective = 
-wR * (R/maxR) - wA * (A - log(minA));


/*********************************************
 * Assertions
 *********************************************/

// Priorities must be in [0,1] with sum equal to 1
assert 0.0 <= wR <= 1.0;
assert 0.0 <= wA <= 1.0;
assert wR + wA == 1;

// Probabilities must be in [0,1]
assert forall (node in nodesRES) 0 <= node.availability && node.availability <= 1.0; 
assert forall (edge in edgesRES) 0 <= edge.availability && edge.availability <= 1.0;

// Source is a DSP node with no incoming data streams, thus with flowIn = 0
assert forall (edgeDSP in edgesDSP)
	edgeDSP.dst not in srcNodes;
assert forall (nodeDSP in srcNodes)
	nodeDSP.flowIn == 0.0;
	
// Sink is a DSP node with no outgoing data streams, thus with flowOut = 0
assert forall (edgeDSP in edgesDSP)
	edgeDSP.src not in snkNodes;
assert forall (nodeDSP in snkNodes)
	nodeDSP.flowOut == 0.0;

// Source/Sink are DSP nodes that can be mapped only on a specific RES node
assert forall (nodeDSP in srcNodes, nodesDSP in snkNodes)
	sum (nodeRES in nodesRES) (eligiblePins[nodeDSP][nodeRES]) == 1;

// DSP Paths consistency
assert forall (pathDSP in pathsDSP) forall (nodeDSPId in pathDSP.nodes)
	nodeDSPId in {nodeDSP.id| nodeDSP in nodesDSP};


/*********************************************
 * PL{01} Model
 *********************************************/

maximize  
	Objective;
subject to {
	
	/*********************************************
	* Eligibility Bound
	* ********************************************
 	* Each DSP node can be mapped on an eligible
 	* RES node.
 	*********************************************/
	forall (nodeDSP in nodesDSP, nodeRES in nodesRES)
		eligibility:
		placeNode[nodeDSP][nodeRES] <= eligiblePins[nodeDSP][nodeRES];
		
		
	/*********************************************
	* Capacity Bound
	* ********************************************
 	* Each RES node must have enough resources to
 	* meet the demand of DSP nodes mapped on it.
 	*********************************************/
	forall (nodeRES in nodesRES)
		capacity:
		sum (nodeDSP in nodesDSP)
		nodeDSP.resources * placeNode[nodeDSP][nodeRES] <= nodeRES.capacity;
		
		
	/*********************************************
	* Unicity Bound
	* ********************************************
 	* Each DSP node must be mapped on at most 
 	* one RES node.
 	*********************************************/
	forall (nodeDSP in nodesDSP)
		unicity: 
		sum (nodeRES in nodesRES)
		placeNode[nodeDSP][nodeRES] == 1;
		
		
	/*********************************************
	* Connectivity Bound
	* ********************************************
 	* A data stream can be mapped only on an
 	* existent logical link
 	*********************************************/
	forall (edgeDSP in edgesDSP, nodeRES_U in nodesRES: 
	eligiblePins[edgeDSP.src][nodeRES_U] == 1)
		connectivity_one:
		placeNode[edgeDSP.src][nodeRES_U] == 
		sum (nodeRES_V in nodesRES, edgeRES in edgesRES: 
		eligiblePins[edgeDSP.dst][nodeRES_V] == 1 && 
		edgeRES.src == nodeRES_U && edgeRES.dst == nodeRES_V) 
			placeEdge[edgeDSP][edgeRES];
		
	forall (edgeDSP in edgesDSP, nodeRES_V in nodesRES: 
	eligiblePins[edgeDSP.dst][nodeRES_V] == 1)
		connectivity_two:
		placeNode[edgeDSP.dst][nodeRES_V] == 
		sum (nodeRES_U in nodesRES, edgeRES in edgesRES: 
		eligiblePins[edgeDSP.src][nodeRES_U] == 1 && 
		edgeRES.src == nodeRES_U && edgeRES.dst == nodeRES_V) 
			placeEdge[edgeDSP][edgeRES];	
}


/*********************************************
 * Solution
 *********************************************/
 
tuple NodeSolution {
	NodeDSP nodeDSP;
	NodeRES nodeRES;
};

{NodeSolution} mapNodes = {<nodeDSP, nodeRES> | nodeDSP in nodesDSP, nodeRES in nodesRES 
											  : placeNode[nodeDSP][nodeRES] == 1};


/*********************************************
 * Results Display
 *********************************************/
 
execute DISPLAY {
	writeln("\n");
	writeln("********************************");
	writeln("* Operator Placement Solution  *");
	writeln("********************************");
	for (var mapNode in mapNodes)
		writeln("DSP node " + mapNode.nodeDSP.id + " mapped on " + 
				"RES node " + mapNode.nodeRES.id);
}