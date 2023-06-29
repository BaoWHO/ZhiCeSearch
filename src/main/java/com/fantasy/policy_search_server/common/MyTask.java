package com.fantasy.policy_search_server.common;

import com.fantasy.policy_search_server.utils.EmailUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Component
public class MyTask {
    @Scheduled(cron = "0 0 12 * * ?")
    public void myTaskMethod1() {
        MyThread thread = new MyThread();
        Thread t = new Thread(thread);
        t.start();
    }

    @Scheduled(cron = "0 0 20 * * ?")
    public void myTaskMethod2() {
        MyThread thread = new MyThread();
        Thread t = new Thread(thread);
        t.start();
    }
}

class MyThread implements Runnable {
    @Override
    public void run() {
        final String senderEmail = "2012324801@qq.com";
        final String senderPassword = "nscxvzvctbmydgbd";
        final String receiverEmail = "gyh17377821609@outlook.com";

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        //props.put("mail.debug", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            message.setSubject("Report:");
            StringBuilder htmlContent = new StringBuilder();
            for (String s: EmailUtil.getContent()) {
                htmlContent.append("<pre style=\"tab-size: 4;\"><span style=\"white-space: nowrap;\">").append(s).append("</span>").append("</pre>").append("<br/>");
            }
            Multipart multipart = new MimeMultipart();
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(htmlContent.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Message sent successfully.");
            EmailUtil.clear();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}