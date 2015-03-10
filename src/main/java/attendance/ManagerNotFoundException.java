package attendance;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ManagerNotFoundException extends BusinessLayerException {

	public ManagerNotFoundException(String userGid) {
		super("could not find user '" + userGid + "'.");
	}
}
