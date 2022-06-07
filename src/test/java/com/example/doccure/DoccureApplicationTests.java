package com.example.doccure;

import com.example.doccure.service.IdentityCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
class DoccureApplicationTests {

    @Autowired
    private IdentityCodeService identityCodeService;

    private static final String TO = "360385776@qq.com";
    private static final String SUBJECT = "主题 - 测试邮件";
    private static final String CONTENT = "你是个傻狗";

    @Test
    void contextLoads() {
        identityCodeService.sendSimpleMailMessage(TO, SUBJECT, CONTENT);
    }

}
