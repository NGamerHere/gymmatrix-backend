package com.coderstack.gymmatrix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.sender_email}")
    private String senderEmail;

    @Value("${application.env.config}")
    private String env;

    @Value("${spring.mail.sample_email}")
    private String sampleEmail;

    @Async
    public void sendWelcomeEmail(String toEmail, String name, String password, String dashboardUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);


            if ("dev".equalsIgnoreCase(env)) {
                helper.setTo(sampleEmail);
            } else {
                helper.setTo(toEmail);
            }

            helper.setSubject("Welcome to GYMmatrix");


            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("email", toEmail);
            context.setVariable("password", password);
            context.setVariable("dashboardUrl", dashboardUrl);


            String htmlContent = templateEngine.process("email/trainer-welcome.html", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("✅ Email sent successfully to " + toEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email to " + toEmail);
            e.printStackTrace();
        }
    }
}
