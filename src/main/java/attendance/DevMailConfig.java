package attendance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Profile("dev")
public class DevMailConfig {
	
	private static final String ATTENDANCE_MAIL_PASSWORD_ENV_VAR = "ATTENDANCE_MAIL_PASSWORD";

	@Value("#{'${mail.server.host}'}")
	private String mailHost;

	@Value("#{'${mail.server.port}'}")
	private int mailPort;

	@Value("#{'${mail.server.protocol}'}")
	private String mailProtocol;

	@Value("#{'${mail.server.username}'}")
	private String mailUsername;
	
    @Bean
    public JavaMailSender mailSender() {
    	
    	String mailPassword = System.getenv(ATTENDANCE_MAIL_PASSWORD_ENV_VAR);
    	
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setProtocol(mailProtocol);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        
        return mailSender;
    }

}