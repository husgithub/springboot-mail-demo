package com.example.mail;

import com.example.mail.config.MyMailConfig;
import com.example.mail.model.Email;
import com.example.mail.service.IMailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootMailDemoApplication.class)
@WebAppConfiguration
public class MailServiceTests {

    @Autowired
    protected MyMailConfig myMailConfig;

    @Resource(name = "abstractMailServiceImpl")
    private IMailService mailService;

    @Test
    public void sendMail() {
        //简单邮件
        Email mail = new Email();
        String[] tos = new String[myMailConfig.getTo().size()];
        mail.setEmail(myMailConfig.getTo().toArray(tos));
        mail.setSubject("这是一封测试邮件");
        mail.setTemplate("jd");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", "asdf");
        mail.setExtension(map);
        try {
            mailService.sendHtmlWithTemplate(mail);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

}
