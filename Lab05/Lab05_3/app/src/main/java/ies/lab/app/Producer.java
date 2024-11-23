package ies.lab.app;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

import ies.lab.app.dto.Message;

@Configuration
public class Producer {
    
    @Bean
    public NewTopic producerBean() {
        return TopicBuilder.name("lab05_115304")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public ApplicationRunner runner(KafkaTemplate<String, String> template) {
        return args -> {
            try {
                Message message = new Message("0", 0, "Hello World!");
                String messageJson = new ObjectMapper().writeValueAsString(message);
                template.send("lab05_115304", messageJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}

