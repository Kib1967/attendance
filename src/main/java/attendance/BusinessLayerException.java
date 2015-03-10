package attendance;

public class BusinessLayerException extends Exception {

	public BusinessLayerException() {
		super();
	}

	public BusinessLayerException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessLayerException(String message) {
		super(message);
	}

	public BusinessLayerException(Throwable cause) {
		super(cause);
	}
}
