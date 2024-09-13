package com.madhesiya.smartcontactmanager.services.Impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.madhesiya.smartcontactmanager.services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender mailSender;
  private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

  @Value("${spring.mail.properties.domain_name}")
  private String domain_name;

  @Override
  public void sendEmail(String to, String subject, String message) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(to);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(message);
    simpleMailMessage.setFrom(domain_name);
    mailSender.send(simpleMailMessage);
    logger.info("Email has been sent");
  }

  @Override
  public void sendEmail(String[] to, String subject, String message) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(to);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(message);
    simpleMailMessage.setFrom(domain_name);
    mailSender.send(simpleMailMessage);
    logger.info("Email has been sent");

  }

  @Override
  public void sendEmailWithHtml(String to, String subject, String htmlContent) {
    MimeMessage simpleMailMessage = mailSender.createMimeMessage();
    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(simpleMailMessage, true, "UTF-8 ");
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setFrom(domain_name);
      mimeMessageHelper.setSubject(subject);
      mimeMessageHelper.setText(htmlContent, true);
      mailSender.send(simpleMailMessage);
      logger.info("Email has been sent");
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void sendEmailWithFile(String to, String subject, String message, File file) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
      mimeMessageHelper.setFrom(domain_name);
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setSubject(subject);
      mimeMessageHelper.setText(message);

      FileSystemResource fileSystemResource = new FileSystemResource(file);
      mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), file);
      mailSender.send(mimeMessage);
      logger.info("Email has been sent");
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void sendEmailWithFileWithStream(String to, String subject, String message, InputStream inputStream) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setFrom(domain_name);
      mimeMessageHelper.setSubject(subject);
      mimeMessageHelper.setText(message);
      File file = new File("/src/main/resources/emailFiles/test.png");
      Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
      FileSystemResource fileSystemResource = new FileSystemResource(file);

      mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), file);

      mailSender.send(mimeMessage);
      logger.info("Email has been sent");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
