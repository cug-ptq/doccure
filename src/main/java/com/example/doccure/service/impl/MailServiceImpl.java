package com.example.doccure.service.impl;

import com.example.doccure.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String SENDER;

    /**
     * 发送普通邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public String sendSimpleMailMessage(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("发送简单邮件时发生异常!");
        }
        return "";
    }

    /**
     * 发送 HTML 邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public String sendMimeMessage(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper;
            try {
                helper = new MimeMessageHelper(message, true);
                helper.setFrom(SENDER);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);
                mailSender.send(message);
            } catch (javax.mail.MessagingException e) {
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            System.out.println("发送MimeMessage时发生异常！");
        }
        return "";
    }

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件路径
     */
    @Override
    public String sendMimeMessage(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper;
            try {
                helper = new MimeMessageHelper(message, true);
                helper.setFrom(SENDER);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);

                FileSystemResource file = new FileSystemResource(new File(filePath));
                String fileName = file.getFilename();
                assert fileName != null;
                helper.addAttachment(fileName, file);
            } catch (javax.mail.MessagingException e) {
                e.printStackTrace();
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("发送带附件的MimeMessage时发生异常！");
        }
        return "";
    }

    /**
     * 发送带静态文件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param rscIdMap 需要替换的静态文件
     */
    @Override
    public String sendMimeMessage(String to, String subject, String content, Map<String, String> rscIdMap) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper;
            try {
                helper = new MimeMessageHelper(message, true);
                helper.setFrom(SENDER);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);

                for (Map.Entry<String, String> entry : rscIdMap.entrySet()) {
                    FileSystemResource file = new FileSystemResource(new File(entry.getValue()));
                    helper.addInline(entry.getKey(), file);
                }
                mailSender.send(message);
            } catch (javax.mail.MessagingException e) {
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            System.out.println("发送带静态文件的MimeMessage时发生异常！");
        }
        return "";
    }
}
