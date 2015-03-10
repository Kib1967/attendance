package attendance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	
	@Value("#{'${mail.server.host}'}")
	private String mailHost;

	@Value("#{'${mail.server.port}'}")
	private int mailPort;

	@Value("#{'${mail.server.protocol}'}")
	private String mailProtocol;

	@Value("#{'${mail.server.username}'}")
	private String mailUsername;
	
	//@Value("#{systemEnvironment['SPRING_MAIL_SERVER_PASSWORD']}")
	@Value("#{'${mail.server.password}'}")
	private String mailPassword;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setProtocol(mailProtocol);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        
        return mailSender;
    }

}