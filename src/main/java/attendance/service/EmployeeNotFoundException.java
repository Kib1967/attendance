package attendance.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends BusinessLayerException {

	public EmployeeNotFoundException(String gid) {
		super("could not find employee '" + gid + "'.");
	}
}
