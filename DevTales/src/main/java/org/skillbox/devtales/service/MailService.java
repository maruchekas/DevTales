package org.skillbox.devtales.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {

    void sendMail(String sendTo, String subject, String message);
}
