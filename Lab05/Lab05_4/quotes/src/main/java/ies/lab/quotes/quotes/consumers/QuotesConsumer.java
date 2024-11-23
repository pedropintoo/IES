package ies.lab.quotes.quotes.controllers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuotesConsumer {

    @Bean
    public NewTopic consumerBean() {
        return TopicBuilder.name("quotes")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(id = "consumerId", topics = "quotes")
    public void listen(String in) {
        System.out.println("Received message: " + in);
    }
}
