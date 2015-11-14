package opmap.control.exception;

public class ModelException extends Exception {

	private static final long serialVersionUID = -4804624933254566993L;

	public ModelException() {}

	public ModelException(String message) {
		super(message);
	}

	public ModelException(Throwable cause) {
		super(cause);
	}

	public ModelException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
