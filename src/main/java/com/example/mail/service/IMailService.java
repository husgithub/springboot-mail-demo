package com.example.mail.service;

import com.example.mail.model.Email;

public interface IMailService {
    void sendHtml(Email mail) throws Exception;

    void sendHtmlWithTemplate(Email mail) throws Exception;
}
