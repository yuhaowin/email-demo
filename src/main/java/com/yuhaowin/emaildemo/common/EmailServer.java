package com.yuhaowin.emaildemo.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 发送邮件服务器.目前支持SMTP协议邮件
 *
 * @author changwen@telincn.com
 * @date 2016-10-8
 */
public class EmailServer {
    private final static Logger logger = LoggerFactory.getLogger(Authentication.class);

    /**
     * 是否开启debug调试模式
     **/
    private static boolean isDebug = false;
    /**
     * 是否启用身份验证
     **/
    private static boolean isAuth = true;
    /**
     * 验证类型
     **/
    private static String authType = "auth";

    /**
     * 私有化默认构造器,使外部不可实例化
     */
    private EmailServer() {
    }

    public static void main(String[] args) throws Exception {
        // EmailServer.sendQQCorpHtmlMail("admin@4006123579.com",
        // "Heweiwuzhu123", "你好啊", "你好啊", "449120183@qq.com",
        // "特宽流量");
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
        sb.append("<head><title>江苏电信微信服务号运营指标日报</title>");
        sb.append("<style>");
        sb.append("table { border-collapse: collapse;border: none; }");
        sb.append("table tr td { border:solid 1px #CCCCCC; width:100px; }");
        sb.append("table thead tr th { border:solid 1px #CCCCCC; background-color:#DDDD39; width:100px; }");
        sb.append(".tip { background-color:#9EEF26; }");
        sb.append(".warning { background-color:#FB957E; }");
        sb.append(".error { background-color:#FC224D; }");
        sb.append(".tblName { font-size: 25px; }");
        sb.append("</style></head>");
        sb.append("<body><div align=\"center\">您好！江苏电信微信服务号运营指标日报 生成时间：2016-10-08 07:06:53</div><br/>");
        sb.append("<a class=\"tblName\" href=\"#\">用户概况</a>");
        sb.append("<table id=\"tblTablespaces\">");
        sb.append("<thead>");
        sb.append(
                "<tr><th>日期</th><th>新增用户</th><th>取消用户</th><th>净增用户</th><th>总用户数</th><th>新增绑定</th><th>总绑定数</th><th>绑定率</th><th>活跃人数</th><th>活跃度</th></tr></thead>");
        sb.append("<tbody>");
        sb.append(
                "<tr class=\"\"><td>2016-10-07</td><td>812</td><td>496</td><td>316</td><td>1085581</td><td>675</td><td>774024</td><td>71.30%</td><td>28691</td><td>2.64%</td></tr>");
        sb.append(
                "<tr class=\"\"><td>2016-10-06</td><td>793</td><td>502</td><td>291</td><td>1085167</td><td>683</td><td>773521</td><td>71.28%</td><td>34749</td><td>3.20%</td></tr>");
        sb.append("</tbody></table><br /><div align=\"center\">2016.10 beta版本</div></body>");
        sb.append("");
        EmailServer.sendQQCorpHtmlMail("changwen@telincn.com", "Qq5418718", "send test", sb.toString(), "5418718@qq.com", "");
    }

    /**
     * 发送普通邮件(单个接收人)
     *
     * @param username 发件人用户名
     * @param password 发件人密码
     * @param title    邮件标题
     * @param content  邮件正文
     * @param receiver 单个接收人
     * @return
     * @throws NoAuthExpcetion
     */
    public boolean sendTextMail(String username, String password, String title, String content, String receiver) {
        return sendTextMail(username, password, title, content, Collections.singletonList(receiver), null);
    }

    /**
     * 发送普通邮件(单个接收人)
     *
     * @param username    发件人用户名
     * @param password    发件人密码
     * @param title       邮件标题
     * @param content     邮件正文
     * @param receiver    单个接收人
     * @param displayName 昵称
     * @return
     * @throws NoAuthExpcetion
     */
    public static boolean sendTextMail(String username, String password, String title, String content, String receiver, String displayName) {
        return sendTextMail(username, password, title, content, Collections.singletonList(receiver), displayName);
    }

    /**
     * 发送普通邮件(多个接收人)
     *
     * @param username  发件人用户名
     * @param password  发件人密码
     * @param title     邮件标题
     * @param content   邮件正文
     * @param receivers 多个接收人
     * @return
     * @throws NoAuthExpcetion
     */
    public static boolean sendTextMail(String username, String password, String title, String content, List<String> receivers) {
        return sendTextMail(username, password, title, content, receivers, null);
    }

    /**
     * 发送普通邮件(多个接收人)
     *
     * @param username    发件人用户名
     * @param password    发件人密码
     * @param title       邮件标题
     * @param content     邮件正文
     * @param receivers   多个接收人
     * @param displayName 昵称
     * @return
     * @throws NoAuthExpcetion
     */
    public static boolean sendTextMail(String username, String password, String title, String content, List<String> receivers, String displayName) {
        try {
            Authentication auth = null;
            if (isAuth()) {
                // 如果需要身份认证，则创建一个密码验证器
                auth = new Authentication(username, password);
            }
            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", isAuth() ? "true" : "false");
            props.setProperty("mail.transport.protocol", "auth");
            EmailConfig config = getEmailConfig(username);
            props.setProperty("mail.smtp.host", config.getSmtp());
            props.setProperty("mail.smtp.port", config.getPort() + "");

            // 根据邮件会话属性和密码验证器构造一个发送邮件的session
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(isDebug);

            Message message = makeTextMail(session, title, content, username, receivers, false, displayName);
            Transport.send(message);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 发送QQ企业HTML邮件(单个接收人)
     *
     * @param username    发件人用户名
     * @param password    发件人密码
     * @param title       邮件标题
     * @param content     邮件正文
     * @param receiver    单个接收人 ·
     * @param displayName 昵称
     * @return
     */
    public static boolean sendQQCorpHtmlMail(String username, String password, String title, String content, String receiver, String displayName) {
        return sendQQCorpHtmlMail(username, password, title, content, Collections.singletonList(receiver), displayName);
    }

    /**
     * 发送QQ企业HTML邮件(多个接收人)
     *
     * @param username  发件人用户名
     * @param password  发件人密码
     * @param title     邮件标题
     * @param content   邮件正文
     * @param receivers 多个接收人
     * @return
     * @throws NoAuthExpcetion
     */
    public static boolean sendQQCorpHtmlMail(final String username, final String password, String title, String content, List<String> receivers,
                                             String displayName) {

        try {
            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", isAuth() ? "true" : "false");
            props.setProperty("mail.transport.protocol", "auth");
            props.setProperty("mail.smtp.host", "smtp.exmail.qq.com");
            props.setProperty("mail.smtp.port", "25");

            // 根据邮件会话属性和密码验证器构造一个发送邮件的session
            Session session = Session.getInstance(props, new Authenticator() {// 用户连接认证
                // 如果需要身份认证，则创建一个密码验证器
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            // Session session = Session.getDefaultInstance(props, auth);

            session.setDebug(isDebug);

            Message message = makeTextMail(session, title, content, username, receivers, true, displayName);

            Transport.send(message);
            logger.info("------邮件发送成功------");
            return true;
        } catch (MessagingException e) {
            logger.error(e.getMessage() + "发送帐号/密钥为:" + username + "/" + password, e);
            return false;
        }
    }

    /**
     * 发送HTML邮件(多个接收人)
     *
     * @param username  发件人用户名
     * @param password  发件人密码
     * @param title     邮件标题
     * @param content   邮件正文
     * @param receivers 多个接收人
     * @return
     * @throws NoAuthExpcetion
     */
    public static boolean sendHtmlMail(final String username, final String password, String title, String content, List<String> receivers,
                                       String displayName) {

        try {
            // Authentication auth = null;
            // if (isAuth()) {
            // // 如果需要身份认证，则创建一个密码验证器
            // auth = new Authentication(username, password);
            // }

            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", isAuth() ? "true" : "false");
            props.setProperty("mail.transport.protocol", "auth");
            EmailConfig config = getEmailConfig(username);
            props.setProperty("mail.smtp.host", config.getSmtp());
            props.setProperty("mail.smtp.port", config.getPort() + "");

            // 根据邮件会话属性和密码验证器构造一个发送邮件的session
            Session session = Session.getInstance(props, new Authenticator() {// 用户连接认证
                // 如果需要身份认证，则创建一个密码验证器
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            // Session session = Session.getDefaultInstance(props, auth);

            session.setDebug(isDebug);

            Message message = makeTextMail(session, title, content, username, receivers, true, displayName);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            logger.error(e.getMessage() + "发送帐号/密钥为:" + username + "/" + password, e);
            return false;
        }
    }

    /**
     * 发送HTML邮件(单个接收人)
     *
     * @param username 发件人用户名
     * @param password 发件人密码
     * @param title    邮件标题
     * @param content  邮件正文
     * @param receiver 单个接收人
     * @return
     */
    public static boolean sendHtmlMail(String username, String password, String title, String content, String receiver) {
        return sendHtmlMail(username, password, title, content, Collections.singletonList(receiver), null);
    }

    /**
     * 发送HTML邮件(单个接收人)
     *
     * @param username    发件人用户名
     * @param password    发件人密码
     * @param title       邮件标题
     * @param content     邮件正文
     * @param receiver    单个接收人 ·
     * @param displayName 昵称
     * @return
     */
    public static boolean sendHtmlMail(String username, String password, String title, String content, String receiver, String displayName) {
        return sendHtmlMail(username, password, title, content, Collections.singletonList(receiver), displayName);
    }

    /**
     * 获取邮件服务器配置
     *
     * @param email 邮箱地址
     * @return
     */
    private static EmailConfig getEmailConfig(String email) {
        String mailServiceDomainName = getMailServiceDomainName(email);
        EmailConfig emailConfig = new EmailConfig(mailServiceDomainName, "smtp." + mailServiceDomainName, 25, "邮箱的配置");
        return emailConfig;
    }

    /**
     * 创建邮件message
     *
     * @param session     根据配置获得的session
     * @param title       邮件主题
     * @param content     邮件的内容
     * @param from        发件者
     * @param receivers   收件者
     * @param isHtmlMail  是否是html邮件
     * @param displayName 昵称
     */
    private static Message makeTextMail(Session session, String title, String content, String from, List<String> receivers, boolean isHtmlMail,
                                        String displayName) {
        return makeTextMail(session, title, content, from, receivers, null, null, isHtmlMail, displayName);
    }

    /**
     * 创建邮件message
     *
     * @param session     根据配置获得的session
     * @param title       邮件主题
     * @param content     邮件的内容
     * @param from        发件者
     * @param receivers   收件者
     * @param cclist      抄送者
     * @param mslist      密送者
     * @param isHtmlMail  是否是html邮件
     * @param displayName 昵称
     */
    private static Message makeTextMail(Session session, String title, String content, String from, List<String> receivers, List<String> cclist,
                                        List<String> mslist, boolean isHtmlMail, String displayName) {
        Message message = new MimeMessage(session);
        try {
            /** 标题 **/
            logger.info("this mail's title is {" + title + "}");
            message.setSubject(title);
            /** 内容 **/
            logger.info("this mail's content is {" + content + "}");
            if (isHtmlMail) {
                // 是html邮件
                message.setContent(content, "text/html;charset=utf-8");
            } else {
                // 普通邮件
                message.setText(content);
            }
            /** 发件者地址 **/
            logger.info("this mail's sender is {" + from + "}");

            String nick = "";

            /** 昵称 **/
            if (StringUtils.isNotBlank(displayName)) {
                try {
                    nick = javax.mail.internet.MimeUtility.encodeText(displayName);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            Address fromAddress = new InternetAddress(nick + " <" + from + ">");
            message.setFrom(fromAddress);

            /** 接收者邮箱 **/
            if (receivers != null && !receivers.isEmpty()) {
                Address[] tos = new InternetAddress[receivers.size()];
                for (int i = 0; i < receivers.size(); i++) {
                    String receiver = receivers.get(i);
                    if (ValidataUtils.Email(receiver)) {
                        tos[i] = new InternetAddress(receiver);
                    }
                }
                /** 接收人 **/
                message.setRecipients(Message.RecipientType.TO, tos);
            }

            /** 抄送者邮箱 **/
            if (cclist != null && !cclist.isEmpty()) {
                Address[] tos = new InternetAddress[cclist.size()];
                for (int i = 0; i < cclist.size(); i++) {
                    String ccStr = cclist.get(i);
                    if (ValidataUtils.Email(ccStr)) {
                        tos[i] = new InternetAddress(ccStr);
                    }
                }
                /** 抄送人 **/
                message.setRecipients(Message.RecipientType.CC, tos);
            }

            /** 密送者邮箱 **/
            if (mslist != null && !mslist.isEmpty()) {
                Address[] tos = new InternetAddress[mslist.size()];
                for (int i = 0; i < mslist.size(); i++) {
                    String ccStr = cclist.get(i);
                    if (ValidataUtils.Email(ccStr)) {
                        tos[i] = new InternetAddress(ccStr);
                    }
                }
                /** 密送人 **/
                message.setRecipients(Message.RecipientType.BCC, tos);
            }

            /** 发件时间 **/
            message.setSentDate(new Date());
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        return message;
    }

    /**
     * 获取邮箱域名
     *
     * @param email 邮箱
     * @return
     */
    private static String getMailServiceDomainName(String email) {
        if (StringUtils.isEmpty(email)) {
            return "";
        } else {
            int firstIndex = StringUtils.indexOf(email, "@");
            return StringUtils.substring(email, firstIndex + 1);
        }
    }

    public static boolean isDebug() {
        return EmailServer.isDebug;
    }

    public static void setDebug(boolean debug) {
        EmailServer.isDebug = debug;
    }

    public static boolean isAuth() {
        return EmailServer.isAuth;
    }

    public void setAuth(boolean auth) {
        EmailServer.isAuth = auth;
    }

    public static String getAuthType() {
        return EmailServer.authType;
    }

    public static void setAuthType(String authType) {
        EmailServer.authType = authType;
    }
}
