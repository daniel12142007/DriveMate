package com.lorddigital.rabbit_mq;

import com.lorddigital.rabbit_mq.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMqApplication implements CommandLineRunner {
    @Autowired
    private Producer producer;

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Отправляем несколько сообщений
        producer.sendMessage("Hello, RabbitMQ!");
        producer.sendMessage("Spring Boot is awesome!");
    }
}
