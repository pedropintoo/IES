package ies.lab.app;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Consumer {

    @Bean
    public NewTopic consumerBean() {
        return TopicBuilder.name("lab05_115304")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(id = "consumerId", topics = "lab05_115304")
    public void listen(String in) {
        System.out.println("Received message: " + in);
    }
}
