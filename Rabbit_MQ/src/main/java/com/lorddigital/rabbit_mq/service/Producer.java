package com.lorddigital.rabbit_mq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class Producer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Метод для отправки сообщений в очередь
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("DriverMateQueue", message);  // Отправляем сообщение в очередь
        System.out.println("Message sent: " + message);
    }
}