package com.jdc.web2026i.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendPdfReportTo(List<String> to, byte[] pdf, String filename, String subject) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setSubject(subject != null ? subject : "Informe de proyectos");
		helper.setText("Adjunto el informe de proyectos.", false);

		// set recipients
		helper.setTo(to.toArray(new String[0]));

		// attach PDF
		helper.addAttachment(filename != null ? filename : "informe.pdf", new ByteArrayResource(pdf));

		mailSender.send(message);
	}

	public void sendSimpleText(List<String> to, String subject, String text) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
		helper.setSubject(subject != null ? subject : "Notificación");
		helper.setText(text != null ? text : "", false);
		helper.setTo(to.toArray(new String[0]));
		mailSender.send(message);
	}

}



