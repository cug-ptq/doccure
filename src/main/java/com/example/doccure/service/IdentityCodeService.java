package com.example.doccure.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface IdentityCodeService {
    String sendSimpleMailMessage(String to, String subject, String content);

    String sendMimeMailMessage(String to, String subject, String content);

    String sendMimeMailMessage(String to, String subject, String content, String filePath);

    String sendMimeMailMessage(String to, String subject, String content, Map<String, String> rscIdMap);

    String sendMimeTeleMessage(String to);
}
