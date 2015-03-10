package attendance.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import attendance.model.Employee;

@Component
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;

	public void send( Employee recipient, String subject, String body ) throws MessagingException {
		
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		helper.setFrom("admin@attendance.net");
		helper.setTo(recipient.getEmailAddress());
		helper.setSubject(subject);
		helper.setText(body);
		
		mailSender.send(mimeMessage);
	}
}
