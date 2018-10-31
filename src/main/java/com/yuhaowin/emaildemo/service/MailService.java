package com.yuhaowin.emaildemo.service;

public interface MailService {
    void sendSimpleMail(String to, String subject, String content);

    void sendHtmlMail(String to, String subject, String content);

    void sendTemplateMail(Object params, String title, String templateName);
}
