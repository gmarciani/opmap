package opmap.view;

import static org.fusesource.jansi.Ansi.ansi;

public class Output {
	
private static Output instance;
	
	private Output() {}
	
	public static Output getInstance() {
		if (instance == null)
			instance = new Output();
		return instance;
	}
	
	public void onDefault(String msg) {
		System.out.println(msg);
	}
	
	public void onResult(String msg) {
		String result = "[ssp] " + msg;
		System.out.println(ansi().fg(AppColor.RESULT_COLOR).a(result).reset());
	}
	
	public void onWarning(String message) {
		String warning = "[WARNING] " + message;
		System.out.println(ansi().fg(AppColor.WARNING_COLOR).a(warning).reset());
	}
	
	public void onException(String message) {
		String warning = "[EXCEPTION] " + message;
		System.out.println(ansi().fg(AppColor.EXCEPTION_COLOR).a(warning).reset());
	}
	
	public void onUnrecognizedArguments(String[] arguments) {
		String unrecognizedArgs = "";
		int uArgsNo = 0;
		for (String arg : arguments) {
			if (arg.charAt(0) != '-') {
				unrecognizedArgs += arg + " ";
				uArgsNo ++;
			}
		}		
		String warning = "Unrecognized argument" + ((uArgsNo > 1) ? "s" : "") + ": " + unrecognizedArgs;		
		this.onWarning(warning);
	}

}
