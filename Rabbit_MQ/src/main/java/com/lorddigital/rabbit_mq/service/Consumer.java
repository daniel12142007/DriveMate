package com.lorddigital.rabbit_mq.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    // Метод для обработки сообщений из очереди
    @RabbitListener(queues = "DriverMateQueue")
    public void receiveMessage(String message) {
        System.out.println("Message received: " + message);
    }
}