package com.diletant.diletantmediatest.service;

import com.diletant.diletantmediatest.entity.NotificationEmail;
import com.diletant.diletantmediatest.exception.DiletantMediaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



@Service
public class MailService {

    private JavaMailSender mailSender;
    private MailContentBuilder mailContentBuilder;

    @Autowired
    public MailService(MailContentBuilder mailContentBuilder, JavaMailSender mailSender) {
        this.mailContentBuilder = mailContentBuilder;
        this.mailSender = mailSender;
    }


    @Async
    void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("Diletant.media");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            throw new DiletantMediaException("Exception occurred when sending an email to " + notificationEmail.getRecipient());
        }
    }
}
