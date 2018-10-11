package com.yuhaowin.emaildemo.common;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.ast.NullLiteral;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * 发送邮件并附带附件
 */
public class SendWithAttachments {

    private static Logger logger = LoggerFactory.getLogger(SendWithAttachments.class);

    /**
     * 系统环境变量
     */
    private Properties props;

    /**
     * 邮件会话对象
     */
    private Session session;

    /**
     * MIME邮件对象
     */
    private MimeMessage mimeMsg;

    /**
     * Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
     */
    private Multipart mp;


    /**
     * 配置邮件发送服务器
     */
    public SendWithAttachments() {
        props = System.getProperties();
        props.put("mail.smtp.auth", "false");
        session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        mimeMsg = new MimeMessage(session);
        mp = new MimeMultipart();
    }

    /**
     * 配置邮件发送服务器
     *
     * @param auth
     * @param smtp
     * @param port
     * @param username
     * @param password
     */
    public SendWithAttachments(String auth, String smtp, String port, String username, String password) {
        props = System.getProperties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.port", port);
        props.put("username", username);
        props.put("password", password);
        session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        mimeMsg = new MimeMessage(session);
        mp = new MimeMultipart();
    }

    /**
     * 发送邮件
     *
     * @param from    发送人
     * @param to      接收人
     * @param copyto  抄送者
     * @param subject 主题
     * @param content 正文
     * @param files   附件
     * @return
     */
    public boolean sendMail(String from, String[] to, String[] copyto, String subject, String content, String[] files) {
        try {
            // 设置发信人
            mimeMsg.setFrom(new InternetAddress(from));
            // 设置接收人
            for (int i = 0; i < to.length; i++) {
                mimeMsg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to[i]));
            }
            // 设置抄送人
            for (int i = 0; i < copyto.length; i++) {
                mimeMsg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto[i]));
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            BodyPart bp = new MimeBodyPart();
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // 多个附件
            if (files != null) {
                for (String file : files) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(file);
                    bp.setDataHandler(new DataHandler(fds));
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp);
                }
            }
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            // 发送邮件
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp");
                transport.connect((String) props.get("mail.smtp.host"), Integer.valueOf((String) props.get("mail.smtp.port")),
                        (String) props.get("username"), (String) props.get("password"));
                transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
                if (mimeMsg.getRecipients(Message.RecipientType.CC) != null) {
                    transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
                }
                transport.close();
            } else {
                Transport.send(mimeMsg);
            }
            logger.info("------邮件发送成功------");
        } catch (Exception e) {
            logger.error("日报发送异常，原因 = {}", ExceptionUtils.getMessage(e));
        }
        return true;
    }

    public static void main(String[] args) {
        String smtp = "smtp.126.com";
        String port = "25";
        String username = "yuhaowin@126.com";
        String password = "YUHAO4455343507";
        String from = "yuhaowin@126.com";
        String[] to = {
                "742396846@qq.com"
        };
        String[] copyto = {
                "742396846@qq.com"
        };
        String subject = "发送附件测试";
        String content = "请查收附件";
        String filename = "D://1.txt|D://2.txt";
        String[] files = filename.split("\\|");
        SendWithAttachments email = new SendWithAttachments("true", smtp, port, username, password);
        email.sendMail(from, to, copyto, subject, content, null);
    }
}