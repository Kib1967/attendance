package attendance.service;

public class ApprovalRequestNotSentException extends BusinessLayerException {

	public ApprovalRequestNotSentException() {
		super();
	}

	public ApprovalRequestNotSentException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApprovalRequestNotSentException(String message) {
		super(message);
	}

	public ApprovalRequestNotSentException(Throwable cause) {
		super(cause);
	}
}
