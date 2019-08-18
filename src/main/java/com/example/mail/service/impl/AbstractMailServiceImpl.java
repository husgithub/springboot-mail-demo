package com.example.mail.service.impl;

import com.example.mail.config.MyMailConfig;
import com.example.mail.model.Email;
import com.example.mail.service.IMailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;

import javax.mail.internet.MimeMessage;
import java.util.Iterator;

@Service
public class AbstractMailServiceImpl implements IMailService {

    @Autowired
    protected MyMailConfig myMailConfig;

    @Autowired
    protected JavaMailSender mailSender;

    @Autowired
    protected Configuration freemarkerConf;

    @Override
    public void sendHtml(Email mail) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(myMailConfig.getFrom());
        helper.setTo(mail.getEmail());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent(), true);
        if (!CollectionUtils.isEmpty(mail.getAttachments())) {
            Iterator<Email.Attachment> attachmentIterator = mail.getAttachments().iterator();
            while (attachmentIterator.hasNext()) {
                Email.Attachment attachment = attachmentIterator.next();
                helper.addAttachment(attachment.getFileName(), attachment.getInputStreamSource());
            }
        }
        mailSender.send(message);
    }

    @Override
    public void sendHtmlWithTemplate(Email mail) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(myMailConfig.getFrom());
        helper.setTo(mail.getEmail());
        helper.setSubject(mail.getSubject());
        Template template = freemarkerConf.getTemplate(mail.getTemplate() + ".ftl");
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(
                template, mail.getExtension());
        helper.setText(text, true);
        mailSender.send(message);
    }
}
