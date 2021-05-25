package com.caiyanjia.Bean;

public class Email {
    private String SendTo;
    private String Subject;
    private String Content;
    private String Sender;
    private String Password;
    private String MailDebug;
    private String MailSmtpAuth;
    private String MailHost;
    private String MailTransportProtocol;

    public String getSendTo() {
        return SendTo;
    }

    public void setSendTo(String sendTo) {
        SendTo = sendTo;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMailDebug() {
        return MailDebug;
    }

    public void setMailDebug(String mailDebug) {
        MailDebug = mailDebug;
    }

    public String getMailSmtpAuth() {
        return MailSmtpAuth;
    }

    public void setMailSmtpAuth(String mailSmtpAuth) {
        MailSmtpAuth = mailSmtpAuth;
    }

    public String getMailHost() {
        return MailHost;
    }

    public void setMailHost(String mailHost) {
        MailHost = mailHost;
    }

    public String getMailTransportProtocol() {
        return MailTransportProtocol;
    }

    public void setMailTransportProtocol(String mailTransportProtocol) {
        MailTransportProtocol = mailTransportProtocol;
    }
}
