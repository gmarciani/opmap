package model.application;

import java.util.Random;
import control.random.Randomizer;
import model.application.operator.Operational;
import model.application.operator.Role;

public class ApplicationFactory {
	
	private static final String DEFAULT_NAME = "";
	private static final String DEFAULT_DESCRIPTION = "";
	private static final int DEFAULT_SRCNODES = 1;
	private static final int DEFAULT_PIPNODES = 1;
	private static final int DEFAULT_SNKNODES = 1;
	
	private Random rnd;
	
	private String name;
	private String description;
	private int srcnodes;
	private int pipnodes;
	private int snknodes;

	public ApplicationFactory() {
		this.clear();
	}
	
	public ApplicationFactory setName(final String name) {
		this.name = name;
		return this;
	}
	
	public ApplicationFactory setDescription(final String description) {
		this.description = description;
		return this;
	}
	
	public ApplicationFactory setNodes(final int pipnodes) {
		this.pipnodes = pipnodes;
		return this;
	}
	
	public Application create() {
		Application app = new Application();
		
		app.setName(this.name);
		app.setDescription(this.description);
		
		int nxtid = 0;
		
		for (int srcnode = 1; srcnode <= this.srcnodes; srcnode++, nxtid++) {
			Operational node = new Operational(nxtid, Role.SRC, "source" + srcnode, x -> new Long(1000), 1, Randomizer.rndDouble(this.rnd, 1.0, 100.0));
			app.addOperational(node);
		}
		
		for (int pipnode = 1; pipnode <= this.pipnodes; pipnode++, nxtid++) {
			Operational node = new Operational(nxtid, Role.PIP, "operator" + pipnode, x -> Math.round(x * 0.95), 1,	Randomizer.rndDouble(this.rnd, 1.0, 100.0));
			app.addOperational(node);
		}		
		
		for (int snknode = 1; snknode <= this.snknodes; snknode++, nxtid++) {
			Operational node = new Operational(nxtid, Role.SNK, "sink" + snknode, x -> new Long(1), 1, Randomizer.rndDouble(this.rnd, 1.0, 100.0));
			app.addOperational(node);
		}		
		
		for (Operational pipnodeSRC : app.getPipes()) {
			for (Operational pipnodeDST : app.getPipes()) {
				if (pipnodeSRC.getId() == pipnodeDST.getId())
					continue;
				app.addStream(pipnodeSRC, pipnodeDST);
			}
		}
		
		for (Operational srcnode : app.getSources())
			app.addStream(srcnode, Randomizer.rndItem(this.rnd, app.getPipes()));
		
		for (Operational snknode : app.getSinks())
			app.addStream(Randomizer.rndItem(this.rnd, app.getPipes()), snknode);
		
		return app;		
	}
	
	private void clear() {
		this.rnd = new Random();
		this.name = DEFAULT_NAME;
		this.description = DEFAULT_DESCRIPTION;
		this.srcnodes = DEFAULT_SRCNODES;
		this.pipnodes = DEFAULT_PIPNODES;
		this.snknodes = DEFAULT_SNKNODES;
	}

}
