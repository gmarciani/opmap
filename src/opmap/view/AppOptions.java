package opmap.view;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

public final class AppOptions extends Options {

	private static final long serialVersionUID = -6826768669197692095L;
	
	public static final String DESCRIPTION_HELP = "OPMap helper.";
	public static final String DESCRIPTION_VERSION = "OPMap version.";
	
	public AppOptions() {
		Option help = this.optHelp();		
		Option version = this.optVersion();
		
		OptionGroup optGroup = new OptionGroup();
		optGroup.addOption(help);
		optGroup.addOption(version);
		
		super.addOptionGroup(optGroup);
	}
	
	private Option optHelp() {
		return Option.builder("h")
		.longOpt("help")
		.desc(AppOptions.DESCRIPTION_HELP)
		.hasArg(false)
		.build();
	}
	
	private Option optVersion() {
		return Option.builder("v")
				.longOpt("version")
				.desc(AppOptions.DESCRIPTION_VERSION)
				.hasArg(false)
				.build();
	}

}
