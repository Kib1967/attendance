package attendance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Well-known loggers.
 * 
 * @author Andy McKibbin
 */
public class Loggers {

	private static final String CONTROLLER_LOGGER_NAME = "controller";
	private static final String SERVICE_LOGGER_NAME = "service";
	
	public static final Logger CONTROLLER_LOGGER = LoggerFactory.getLogger(CONTROLLER_LOGGER_NAME);
	public static final Logger SERVICE_LOGGER = LoggerFactory.getLogger(SERVICE_LOGGER_NAME);
}
