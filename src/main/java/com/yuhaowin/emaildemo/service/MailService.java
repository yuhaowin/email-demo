package com.yuhaowin.emaildemo.service;

/**
 * 邮件服务service
 */
public interface MailService {

    /**
     * 发送简单邮件
     *
     * @param to      收件人
     * @param subject 主题(标题)
     * @param content 正文
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送html邮件
     *
     * @param to      收件人
     * @param subject 主题(标题)
     * @param html    html页面
     */
    void sendHtmlMail(String to, String subject, String html);

    /**
     * 发送附件邮件
     *
     * @param to        收件人
     * @param copyto    抄送
     * @param subject   主题(标题)
     * @param content   正文
     * @param filePaths 附件路径
     */
    void sendAttachmentsMail(String[] to, String[] copyto, String subject, String content, String[] filePaths);

    /**
     * 发送模板邮件
     *
     * @param emailData    模板需要的数据源
     * @param subject      主题(标题)
     * @param templateName 模板名称
     */
    void sendTemplateMail(Object emailData, String subject, String templateName);

    /**
     * 发送模板邮件
     *
     * @param emailData    模板需要的数据源
     * @param to           收件人
     * @param copyto       抄送
     * @param subject      主题(标题)
     * @param templateName 模板名称
     * @param filePaths    附件路径
     */
    void sendTemplateMail(Object emailData, String[] to, String[] copyto, String subject, String templateName, String[] filePaths);
}
