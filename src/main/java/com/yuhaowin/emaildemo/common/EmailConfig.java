package com.yuhaowin.emaildemo.common;

import java.io.Serializable;

/**
 * 邮箱服务器的配置.
 *
 * @author changwen@telincn.com
 * @date 2016-10-8
 */
public class EmailConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 邮件名称
     */
    private String name;

    /**
     * smtp服务器名称
     */
    private String smtp;

    /**
     * 端口号
     */
    private int port;

    /**
     * 邮件描述
     */
    private String description;

    public EmailConfig() {
    }

    public EmailConfig(String name, String smtp, int port, String description) {
        super();
        this.name = name;
        this.smtp = smtp;
        this.port = port;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
