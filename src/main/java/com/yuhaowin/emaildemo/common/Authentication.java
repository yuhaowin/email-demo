package com.yuhaowin.emaildemo.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 发送邮件时的身份认证器.
 */
public class Authentication extends Authenticator {

    private final static Logger logger = LoggerFactory.getLogger(Authentication.class);

    /**
     * 用户名(发送者邮箱地址)
     **/
    private String username;
    /**
     * 发送者邮箱密码
     **/
    private String password;

    public Authentication(String username, String password) throws NoAuthExpcetion {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.error("username and password is required!");
            throw new NoAuthExpcetion("username and password is required!");
        }
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
