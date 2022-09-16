package com.application.library.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;

    public void sendMail(String to, String subject, String text, byte[] attachmentByteZip) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = addToMail(username, to, text, subject, mimeMessage);

        mimeMessageHelper.addAttachment("latestChanges.zip",
                new ByteArrayDataSource(attachmentByteZip, "application/x-7z-compressed"));
        javaMailSender.send(mimeMessage);
    }

    private MimeMessageHelper addToMail(String username, String to, String text, String subject, MimeMessage mimeMessage)
            throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setText(text);
        mimeMessageHelper.setSubject(subject);
        return mimeMessageHelper;
    }
}
