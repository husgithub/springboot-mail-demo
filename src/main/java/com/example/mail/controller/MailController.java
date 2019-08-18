package com.example.mail.controller;

import com.example.mail.config.MyMailConfig;
import com.example.mail.model.Email;
import com.example.mail.model.EmailFormData;
import com.example.mail.service.impl.AbstractMailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MyMailConfig myMailConfig;

    @Resource(name = "abstractMailServiceImpl")
    private AbstractMailServiceImpl abstractMailService;

    @GetMapping("index")
    public String index() {
        return "index";
    }

    @PostMapping("uploadImg")
    @ResponseBody
    public Map<String, Object> uploadImg(MultipartFile file) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        try {
            file.transferTo(new File(myMailConfig.getUploadPath() + file.getOriginalFilename()));
            resp.put("file_path", request.getContextPath() + "/img/" + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            resp.put("success", false);
            resp.put("msg", false);
        }
        return resp;
    }

    @PostMapping("sendMail")
    @ResponseBody
    public String sendMail(EmailFormData email) {
        try {
            if (!CollectionUtils.isEmpty(email.getFiles())) {
                List<Email.Attachment> attachmentList = new ArrayList<>();
                Iterator<MultipartFile> fileIterator = email.getFiles().iterator();
                while (fileIterator.hasNext()) {
                    MultipartFile multipartFile = fileIterator.next();
                    System.out.println(multipartFile.getOriginalFilename());
                    attachmentList.add(new Email.Attachment(multipartFile.getOriginalFilename(), multipartFile));
                }
                email.setAttachments(attachmentList);
            }
            abstractMailService.sendHtml(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送失败！");
        }
        return "0";
    }

    @GetMapping("regsuccess")
    public String toRegisterSuccessTemplate() {
        return "mail/registerSuccess";
    }

    @GetMapping("jd")
    public String toJdTemplate() {
        return "mail/jd";
    }

    @GetMapping("hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }
}
