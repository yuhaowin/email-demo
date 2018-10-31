package com.yuhaowin.emaildemo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class MailServiceImplTest {

    @Autowired
    private MailService mailService;

    @Test
    public void testSimpleMail() throws Exception {
        mailService.sendSimpleMail("yuhaowin@126.com","test simple mail"," hello this is simple mail");
    }

    @Test
    public void testsendHtmlMail(){
        String content="<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封Html邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendHtmlMail("yuhaowin@126.com","test html mail",content);
    }

    @Test
    public void testsendTemplateMail(){

        Map map = new HashMap();
        map.put("messageCode","323232");
        map.put("messageStatus","状态");
        map.put("cause","45544545");

        mailService.sendTemplateMail(map,"测试","message.ftl");
    }
}