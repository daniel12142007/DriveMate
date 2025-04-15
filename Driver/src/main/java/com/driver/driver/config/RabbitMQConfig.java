package com.driver.driver.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue driverMateQueue() {
        return QueueBuilder.durable("Driver").build();
    }
}