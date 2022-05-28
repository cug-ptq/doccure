package com.example.doccure.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface MailService {
    String sendSimpleMailMessage(String to, String subject, String content);

    String sendMimeMessage(String to, String subject, String content);

    String sendMimeMessage(String to, String subject, String content, String filePath);

    String sendMimeMessage(String to, String subject, String content, Map<String, String> rscIdMap);
}
