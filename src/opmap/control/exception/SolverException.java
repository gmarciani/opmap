package opmap.control.exception;

public class SolverException extends Exception {

	private static final long serialVersionUID = -2321960714145129175L;

	public SolverException() {}

	public SolverException(String message) {
		super(message);
	}

	public SolverException(Throwable cause) {
		super(cause);
	}

	public SolverException(String message, Throwable cause) {
		super(message, cause);
	}

	public SolverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
