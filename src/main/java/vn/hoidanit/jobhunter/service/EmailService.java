package vn.hoidanit.jobhunter.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final MailSender mailSender;

    public EmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail() {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("lochoang2101@gmail.com");
        mail.setSubject("Testing from Spring boot");
        mail.setText("Hello world");
        this.mailSender.send(mail);
    }
}
