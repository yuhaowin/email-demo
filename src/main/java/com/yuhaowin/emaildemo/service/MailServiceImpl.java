package com.yuhaowin.emaildemo.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送邮件的模板引擎
     */
    @Autowired
    private FreeMarkerConfigurer configurer;

    /**
     * 发件人
     */
    @Value("${mail.fromMail.addr}")
    private String from;

    /**
     * 解决附件名称过长会被分割处理,导致一些邮件客户端附件
     * 名显示乱码问题
     */
    static {
        System.setProperty("mail.mime.splitlongparameters", "false");
    }

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
            logger.info("简单邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常！", e);
        }

    }


    @Override
    public void sendHtmlMail(String to, String subject, String html) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
            logger.info("html邮件发送成功");
        } catch (MessagingException e) {
            logger.error("发送html邮件时发生异常！", e);
        }
    }

    @Override
    public void sendAttachmentsMail(String[] to, String[] copyto, String subject, String content, String[] filePaths) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            //设置多附件
            for (String filePath : filePaths) {
                FileSystemResource file = new FileSystemResource(new File(filePath));
                String filename = file.getFilename();
                helper.addAttachment(filename, file);
            }
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendTemplateMail(Object model, String title, String templateName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            //发送给谁
            helper.setTo(InternetAddress.parse("yuhaowin@126.com"));
            //邮件标题
            helper.setSubject("【" + title + "-" + LocalDate.now() + " " + LocalTime.now().withNano(0) + "】");

            FileSystemResource file = new FileSystemResource(new File("/Users/yuhao/Pictures/Personal/yuhao.jpg"));

            helper.addAttachment("测试", file);
            try {
                Template template = configurer.getConfiguration().getTemplate(templateName);
                try {
                    String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

                    helper.setText(text, true);
                    mailSender.send(mimeMessage);
                } catch (TemplateException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendTemplateMail(Object data, String[] to, String[] copyto, String subject, String templateName, String[] filePaths) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //设置发件人
            helper.setFrom(from);
            //设置收件人
            helper.setTo(to);
            //设置抄送人
            helper.setCc(copyto);
            //邮件标题
            helper.setSubject(subject);
            //设置多附件
            for (String filePath : filePaths) {
                FileSystemResource file = new FileSystemResource(new File(filePath));
                String filename = file.getFilename();
                helper.addAttachment(filename, file);
            }
            Template template = configurer.getConfiguration().getTemplate(templateName);
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
