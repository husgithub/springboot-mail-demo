package com.example.mail.model;

import org.springframework.core.io.InputStreamSource;

import javax.activation.DataSource;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Email {

    private String[] email;//接收方邮件
    private String subject;//主题
    private String content;//邮件内容
    private String template;//模板
    private Map<String, Object> extension;// 自定义参数
    private List<Attachment> attachments;   //附件

    public static class Attachment{
        private String fileName;
        private DataSource dataSource;
        InputStreamSource inputStreamSource;

        public Attachment(){};

        public Attachment(String fileName, DataSource dataSource) {
            this.fileName = fileName;
            this.dataSource = dataSource;
        }

        public Attachment(String fileName, InputStreamSource inputStreamSource) {
            this.fileName = fileName;
            this.inputStreamSource = inputStreamSource;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public DataSource getDataSource() {
            return dataSource;
        }

        public void setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        public InputStreamSource getInputStreamSource() {
            return inputStreamSource;
        }

        public void setInputStreamSource(InputStreamSource inputStreamSource) {
            this.inputStreamSource = inputStreamSource;
        }
    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, Object> getExtension() {
        return extension;
    }

    public void setExtension(Map<String, Object> extension) {
        this.extension = extension;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
