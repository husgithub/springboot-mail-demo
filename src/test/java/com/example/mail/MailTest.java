package com.example.mail;

import com.example.mail.config.MyMailConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.ResourceUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootMailDemoApplication.class)
@WebAppConfiguration
public class MailTest {

    @Autowired
    protected MyMailConfig myMailConfig;

    @Test
    public void sendMail() {
        Transport transport = null;
        try {
            Properties javaMailProperties = new Properties();
            javaMailProperties.setProperty("mail.debug", "true");
            javaMailProperties.setProperty("mail.transport.protocol", myMailConfig.getProtocol());
            javaMailProperties.setProperty("mail.host", myMailConfig.getHost());
            //设置发送者
            javaMailProperties.setProperty("mail.from", myMailConfig.getFrom());
            Session session = Session.getInstance(javaMailProperties);
            MimeMessage mimeMessage = new MimeMessage(session);
            /**
             * 设置邮件接收者
             * MimeMessage.RecipientType.TO:  发送
             * MimeMessage.RecipientType.CC： 抄送
             * MimeMessage.RecipientType.BCC：密送
             */
            InternetAddress[] recipientAddress = new InternetAddress[myMailConfig.getTo().size()];
            for (int i = 0; i < myMailConfig.getTo().size(); i++) {
                recipientAddress[i] = new InternetAddress(myMailConfig.getTo().get(i));
            }
            mimeMessage.setRecipients(Message.RecipientType.TO, recipientAddress);
            //设置主题
            mimeMessage.setSubject("测试邮件", String.valueOf(myMailConfig.getDefaultEncoding()));
            MimeMultipart rootMixedMultipart = new MimeMultipart("mixed");
            mimeMessage.setContent(rootMixedMultipart);

            MimeMultipart nestedRelatedMultipart = new MimeMultipart("related");
            MimeBodyPart relatedBodyPart = new MimeBodyPart();
            relatedBodyPart.setContent(nestedRelatedMultipart);

            //添加 文本/超文本 内容
            MimeBodyPart textBodyPart = new MimeBodyPart();
            String text = getHtml("测试邮件发送成功 !");
            textBodyPart.setContent(text, "text/html;charset=" + myMailConfig.getDefaultEncoding());
            nestedRelatedMultipart.addBodyPart(textBodyPart);

            String path = ResourceUtils.getURL("classpath:static/img/").getPath();

            //添加 内嵌图片
            MimeBodyPart imgBodyPart = new MimeBodyPart();
            // 读取本地文件
            imgBodyPart.setDataHandler(new DataHandler(new FileDataSource(path + "success.jpg")));
            imgBodyPart.setContentID("pic");
            nestedRelatedMultipart.addBodyPart(imgBodyPart);

            //添加附件
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.setDisposition("attachment");
            attachmentBodyPart.setFileName(MimeUtility.encodeText("success.jpg"));
            attachmentBodyPart.setDataHandler(new DataHandler(new FileDataSource(path + "success.jpg")));
            rootMixedMultipart.addBodyPart(relatedBodyPart);
            rootMixedMultipart.addBodyPart(attachmentBodyPart);

            /*transport = session.getTransport(myMailConfig.getProtocol());
            transport.connect(myMailConfig.getHost(), myMailConfig.getUsername(), myMailConfig.getPassword());*/
            transport = session.getTransport();
            transport.connect(myMailConfig.getUsername(), myMailConfig.getPassword());
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getHtml(String words) {
        String html = "<html><body>" +
                words +
                "<img src='cid:pic'style=\"width: 80px\"/></body></html>";
        return html;
    }
}
