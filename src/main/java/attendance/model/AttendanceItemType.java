package attendance.model;

public enum AttendanceItemType {
	ANNUAL_LEAVE(true),
	BUSINESS_TRAVEL(true),
	SICK(false),
	WORKING_FROM_HOME(true),
	TRAINING(true),
	MATERNITY_LEAVE(true);
	
	private final boolean needsApproval;
	
	private AttendanceItemType(boolean needsApproval) {
		this.needsApproval = needsApproval;
	}

	public boolean isNeedsApproval() {
		return needsApproval;
	}
}
