package control.exceptions;

public class GeneratorException extends Exception {

	private static final long serialVersionUID = -4678276519656258276L;

	public GeneratorException() {}

	public GeneratorException(String message) {
		super(message);
	}

	public GeneratorException(Throwable cause) {
		super(cause);
	}

	public GeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
