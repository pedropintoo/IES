# IES Lab 05 - Event-Driven and Message Queue with Apache Kafka

In this lab, we explore **Apache Kafka** for event-driven architectures. Apache Kafka is a distributed event-streaming platform widely used for building real-time data pipelines and streaming applications.

## Table of Contents
1. [Configure Apache Kafka with Docker](#1-configure-apache-kafka-with-docker)
2. [Create a producer and consumer](#2-create-a-producer-and-consumer)
3. [Create a Consumer in Java Integrated with Spring Boot](#3-create-a-consumer-in-java-integrated-with-spring-boot)
4. [Wrapping-Up and Integrating Concepts](#4-wrapping-up-and-integrating-concepts)

---

## 1. Configure Apache Kafka with Docker

This section covers deploying Apache Kafka using Docker Compose, creating and managing Kafka topics, and understanding the behavior of multiple consumers and producers.

### Steps

### 1.1. Deploy Kafka Using Docker Compose - Running the Kafka Setup

Use the following Docker Compose file to set up Kafka, Zookeeper, and a Kafka management tool (Kafdrop):

```yaml
version: "3"
name: lab05
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.4
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - 22181:2181

  kafka:
    image: confluentinc/cp-kafka:7.4.4
    depends_on:
      - zookeeper
    ports:
      - 29092:29092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_LOG_RETENTION_MS: 10000
      KAFKA_LOG_RETENTION_CHECK_INTERVAL_MS: 5000

  kafdrop:
    image: obsidiandynamics/kafdrop:4.0.2
    ports:
      - "9009:9000"
    environment:
      KAFKA_BROKERCONNECT: "kafka:9092" # Kafka Broker Network Addresses (host:port,host:port)
      SERVER_SERVLET_CONTEXTPATH: "/"
```

To set up and run Kafka along with Zookeeper and Kafdrop, use the following command:

```bash
docker compose up --build
```

Once the setup is complete, your Kafka cluster will be running along with Zookeeper and Kafdrop. You can access Kafdrop at `http://localhost:9009`.

---

### Core Kafka Concepts

#### **What is a Cluster?**

- A **Kafka cluster** consists of multiple Kafka brokers working together to manage topics and messages.
- **Zookeeper** coordinates the brokers and ensures the cluster's state remains consistent.

#### **What is Kafdrop?**

- **Kafdrop** is a web-based UI for Kafka, allowing you to:
  - Monitor Kafka topics, partitions, and messages.
  - View and manage consumer groups and configurations.
- Access it at `http://localhost:9009` for a convenient management experience.

#### **What is a Topic?**

- A **topic** is a logical category or feed where messages are published by producers.
- Kafka topics:
  - Are used to group related messages.
  - Allow multiple consumers to subscribe and process the messages.

#### **What is a Partition?**

- Kafka divides topics into **partitions** for scalability and parallel processing:
  - Each partition is an ordered, immutable sequence of messages.
  - Messages within a partition are assigned offsets (unique identifiers).
  - Partitions are distributed across brokers, enabling load balancing.

#### **What is a Broker?**

- A **Kafka broker** is a server that:
  - Stores and manages Kafka topics and partitions.
  - Handles message production and consumption requests.
  - Works with other brokers in the cluster to distribute the load.

#### **What is Zookeeper?**

- **Zookeeper** is a centralized service used by Kafka for:
  - Managing configuration and cluster metadata.
  - Coordinating Kafka brokers.
  - Tracking topic partitions and their replicas.

#### **What is a Consumer Group?**

- A **consumer group** is a set of consumers that collaborate to consume messages from one or more topics:
  - Each consumer in a group processes a subset of the messages in the topic partitions.
  - Enables load distribution and fault tolerance.

#### **What is a Consumer?**

- A **Kafka consumer** is an application that reads messages from Kafka topics:
  - It can belong to a consumer group or work independently.
  - Consumers commit offsets to track which messages they’ve processed.

#### **What is a Producer?**

- A **Kafka producer** is an application that sends messages to Kafka topics:
  - Producers specify which topics to send messages to.
  - They can configure settings like delivery guarantees and partitioning strategies.

---

### 1.2. Create a Kafka Topic

Run the following command to create a topic named `lab05`:

```bash
docker exec lab05-kafka-1 kafka-topics --create --topic lab05 --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092
```

### 1.3. Consume Messages

Start a Kafka consumer to read messages from the `lab05` topic:

```bash
docker exec lab05-kafka-1 kafka-console-consumer --topic lab05 --from-beginning --bootstrap-server kafka:9092
```

### 1.4. Produce Messages

Start a Kafka producer to send messages to the topic:

```bash
docker exec -it lab05-kafka-1 bash
kafka-console-producer --topic lab05 --broker-list kafka:9092
```

Type any message in the producer terminal. These messages will appear in the consumer terminal.

---

## 2. Create a producer and consumer

To create a producer and consumer, we will use Python and the `kafka-python` library.

When a Kafka consumer reads messages, it tracks the offset of the last message processed. Committing offsets ensures that the consumer does not reprocess messages if it restarts.

### 2.1. Consumer Commit

The code is in the `Lab05_2` directory.

```
|
|-- `Lab05_2`
|   |-- `Lab05_2`
|   |   |-- `__init__.py`
|   |   |-- `consumer_example.py`
|   |   |-- `produter_example.py`
|   |-- `pyproject.toml`
```

#### Types of Commits

1. **Automatic Commit**:

   - Enabled by `enable_auto_commit=True`.
   - Commits offsets at regular intervals.
   - Default interval: `5000ms` (`auto.commit.interval.ms`).

2. **Manual Commit**:
   - Provides precise control over when offsets are committed.
   - Use `commit()` to explicitly commit the current offset after processing a batch of messages.

#### Advantages

- **Automatic Commit**:

  - Simplifies consumer configuration.
  - Suitable for stateless consumers or scenarios where reprocessing is acceptable.

- **Manual Commit**:
  - Ensures offsets are only committed after successful processing.
  - Reduces the risk of losing messages in case of failures.

---

### 2.2. Retention Time

Kafka topics retain messages for a configurable duration, after which messages are deleted to free up space.

#### Key Settings

- **`retention.ms`**: Defines the maximum retention period for messages.
- **`log.retention.bytes`**: Specifies the maximum size of the topic before old messages are deleted.

#### Current Configuration

From the provided Docker Compose file:

- **Retention Period**: `10000ms` (10 seconds).
- **Check Interval**: `5000ms` (5 seconds).

#### Implications

- Messages are available for consumption for only 10 seconds.
- Consumers must process messages quickly or risk missing them.

---

### 2.3. Observations

#### Consumer Behavior with Retention and Commit

1. **Messages and Retention Time**:

   - Messages older than 10 seconds are deleted.
   - Consumers started after messages are deleted will not receive them.

2. **Offset Tracking**:

   - If a consumer commits an offset, it will only process messages after the last committed offset on subsequent restarts.

3. **Multiple Consumers**:
   - **Same Group**: Consumers in the same group share partitions and process messages collectively.
   - **Different Groups**: Each consumer group processes all messages independently.

### **The consumer should read all the messages from the topic. What is the last message?**

The last message in the topic will be the final message produced by the producer before it stops. For example, if the producer is generating Fibonacci sequence messages until a specific value (e.g., `115304`), the last message will be:

```json
{
  "nMec": "115304",
  "generatedNumber": 65536,
  "type": "fibonacci"
}
```

Here, `65536` is the largest Fibonacci number less than or equal to `115304`, depending on the producer's logic.

---

### **d. If you run the consumer multiple times, does it read all the messages? Why?**

It depends on **when** the consumer runs and how it is configured:

#### **1. If the consumer runs after messages are produced:**

- **Retention time matters**: Kafka retains messages for a limited duration (configured as `retention.ms`).
- In this setup, messages are retained for 10 seconds (`10000ms`). If the consumer starts after this period, it will not read older messages because they are deleted.
- **Last message retrieval**: If the consumer starts while messages are still in the topic (within the retention time), it can read all messages using the `auto_offset_reset='earliest'` setting.

#### **2. If the consumer runs multiple times (with `enable_auto_commit=True`):**

- **First run**: The consumer reads all messages and commits offsets automatically.
- **Subsequent runs**: The consumer starts reading from the last committed offset. Since offsets are committed, it will not reprocess old messages unless retention allows and `auto_offset_reset` is set to `'earliest'`.

#### **3. If the consumer runs multiple times (with `enable_auto_commit=False`):**

- Without committing offsets, the consumer reads from the beginning each time, depending on the `auto_offset_reset` configuration.

---

## 3 Create a Consumer in Java Integrated with Spring Boot

Apache Kafka is a versatile distributed event-streaming platform that allows seamless communication between applications written in different programming languages. In this exercise, we create a Spring Boot application that acts as a Kafka consumer and integrates it with the Python producer from **Section 3.2**. This setup demonstrates the interoperability of Kafka across platforms.

---

#### 3.1 Create a New Spring Boot Project

- Use Spring Initializr to create a new Spring Boot project with the following dependencies:
  - Spring for Apache Kafka
  - Spring Boot DevTools (optional, for development)

The following files implement the consumer functionality:

---

#### 3.2 Listening to Messages (Kafka Consumer)

The **`Consumer.java`** file contains a Kafka listener to receive messages sent to the topic.

##### `Consumer.java`:

```java
package ies.lab.app;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ies.lab.app.dto.Message;

@Configuration
public class Consumer {

    @Bean
    public NewTopic consumerBean() {
        return TopicBuilder.name("lab05_115304")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(topics = "lab05_115304", containerFactory = "messageKafkaListenerContainerFactory")
    public void listen(Message in) {
        System.out.println("Received message: " + in);
    }
}
```

---

#### 3.3 Spring Kafka Configuration

The **`KafkaConfig.java`** file sets up the necessary configurations for the Kafka consumer, including deserialization of JSON messages into Java objects.

##### `KafkaConfig.java`:

```java
package ies.lab.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import ies.lab.app.dto.Message;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.properties.spring.json.trusted.packages}")
    private String trustedPackages;

    // Consumer Factory for Message
    @Bean
    public ConsumerFactory<String, Message> messageConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
            consumerProps(Message.class.getName()),
            new StringDeserializer(),
            new ErrorHandlingDeserializer<>(new JsonDeserializer<>(Message.class))
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Message> messageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageConsumerFactory());
        return factory;
    }

    // Consumer Properties
    private  Map<String, Object> consumerProps(String deserializerClassString) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId + "." + deserializerClassString);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, deserializerClassString);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPackages);
        return props;
    }
}
```

---

#### 3.4 Serialization and Deserialization

To deserialize JSON messages from the Python producer, create the **`Message.java`** class.

##### `Message.java`:

```java
package ies.lab.app.dto;

public class Message {
    private String nMec;
    private int generatedNumber;
    private String type;

    public Message() {} // Required by Jackson

    public Message(String nMec, int generatedNumber, String type) {
        this.nMec = nMec;
        this.generatedNumber = generatedNumber;
        this.type = type;
    }

    public String getnMec() {
        return nMec;
    }

    public void setnMec(String nMec) {
        this.nMec = nMec;
    }

    public int getGeneratedNumber() {
        return generatedNumber;
    }

    public void setGeneratedNumber(int generatedNumber) {
        this.generatedNumber = generatedNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "nMec='" + nMec + '\'' +
                ", generatedNumber=" + generatedNumber +
                ", type='" + type + '\'' +
                '}';
    }
}
```

---

#### 3.5 Spring Boot Application

The **`Application.java`** serves as the entry point for the Spring Boot application.

##### `Application.java`:

```java
package ies.lab.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

---

#### 3.6 Properties Configuration

Update the `application.properties` file with the following configurations:

```properties
spring.application.name=app
spring.kafka.bootstrap-servers=localhost:29092
spring.kafka.consumer.group-id=lab05-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
```

---

### Steps to Verify

1. **Run the Spring Boot Application**:
   Start the Spring Boot application.

2. **Produce Messages Using `producer.py`**:
   Use the Python script from **Section 3.2** to produce messages in JSON format:

   ```python
   import json
   from kafka import KafkaProducer

   producer = KafkaProducer(bootstrap_servers=['localhost:29092'],
                            value_serializer=lambda v: json.dumps(v).encode('utf-8'))

   producer.send('lab05_115304', {'nMec': '115304', 'generatedNumber': 123, 'type': 'test'})
   ```

3. **Verify Messages in the Consumer**:
   Check the Spring Boot application logs for received messages:
   ```
   Received message: Message{nMec='115304', generatedNumber=123, type='test'}
   ```

## 4 Wrapping-Up and Integrating Concepts

In this exercise, we build upon the concepts from previous sections to create a fully integrated application that supports real-time updates for movie quotes. The complete system consists of a Python producer, a Spring Boot backend consuming Kafka messages and updating a database, and a React frontend displaying the latest quotes dynamically using WebSocket.

---

### 4.1 Python Script to Generate and Publish Quotes

The Python script generates random quotes every 5 to 10 seconds and sends them to a Kafka topic.

#### Python Script: `producer.py`

```python
from lorem import lorem
import os
import sys
import sys, types
import time
import random

m = types.ModuleType('kafka.vendor.six.moves', 'Mock module')
setattr(m, 'range', range)
sys.modules['kafka.vendor.six.moves'] = m

from kafka import KafkaProducer
from kafka.errors import KafkaError
import json

producer = KafkaProducer(bootstrap_servers='localhost:29092')

# Produce messages
TOPIC = 'quotes'
MOVIES = ['The Godfather', 'The Shawshank Redemption', 'The Dark Knight', 'The Lord of the Rings', 'Pulp Fiction', 'Fight Club', 'Forrest Gump', 'Inception', 'The Matrix', 'The Silence of the Lambs', 'The Lion King', 'The Avengers', 'The Terminator', 'The Shining', 'The Sixth Sense', 'The Social Network', 'The Green Mile', 'The Wizard of Oz', 'The Exorcist', 'The Graduate', 'The Godfather Part II', 'The Great Dictator', 'The Grand Budapest Hotel', 'The Good, the Bad and the Ugly', 'The Departed', 'The Dark Knight Rises', 'The Curious Case of Benjamin Button', 'The Chronicles of Narnia', 'The Breakfast']
YEARS =  [           1972,                       1994,              2008,                    2001,           1994,         1999,           1994,        2010,         1999,                       1991,            1994,           2012,             1984,          1980,              1999,                 2010,             1999,               1939,           1973,           1967,                    1974,                 1940,                       2014,                             1966,           2006,                    2012,                                  2008,                       2005,           1939]

while True:
    quote = lorem.get_sentence()
    idx = random.randint(0, len(MOVIES) - 1)
    movie = MOVIES[idx]
    year = YEARS[idx]
    # print(f"[{movie}]: {quote}")
    message = json.dumps({'quote': quote, 'movie': movie, 'year': year}).encode('utf-8')
    print(f"Sending message: {message}")
    future = producer.send(TOPIC, message)
    try:
        record_metadata = future.get(timeout=10)
        print(f"Message sent to topic {record_metadata.topic} with partition {record_metadata.partition} and offset {record_metadata.offset}")
    except KafkaError as e:
        print(f"Failed to send message: {e}")

    time.sleep(random.randint(5, 10))
```

---

### 4.2 Spring Boot: Consuming Quotes from Kafka

The Spring Boot application consumes quotes from the Kafka topic and inserts them into a PostgreSQL database.

#### Kafka Consumer: `QuotesConsumer.java`

```java
@Configuration
@Service
public class QuotesConsumer {

    private final QuoteService quoteService;
    private final MovieService movieService;

    public QuotesConsumer(QuoteService quoteService, MovieService movieService) {
        this.quoteService = quoteService;
        this.movieService = movieService;
    }

    @Bean
    public NewTopic quoteConsumerBean() {
        return TopicBuilder.name("quotes")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(topics = "quotes", containerFactory = "quoteKafkaListenerContainerFactory")
    public void listenQuotes(QuoteConsumerRecord in) {
        Movie movie = new Movie();
        movie.setTitle(in.getMovie());
        movie.setYear(in.getYear());
        try {
            movie = movieService.getMovieByTitleAndYear(movie.getTitle(), movie.getYear());
        } catch (ResourceNotFoundException e) {
            movie = movieService.createMovie(movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Quote quote = new Quote();
        quote.setQuote(in.getQuote());
        quote.setMovie(movie);
        quoteService.createQuote(quote);

        System.out.println("Received: " + in);
    }
}
```

---

### 4.3 React Frontend: Display Latest Quotes

The React frontend fetches and displays the latest 5 quotes dynamically, updating in real-time using WebSocket.

#### WebSocket Configuration: `WebSocketConfig.java`

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/quotes-api").setAllowedOriginPatterns("*");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
      registry.enableSimpleBroker("/quotes");
      registry.setApplicationDestinationPrefixes("/app");
      
  }

}
```

---

#### React Frontend Code

The React app displays the latest quotes and listens to real-time updates.

Add Stomp Session Provider to the root component:

```typescript
const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  
  <React.StrictMode>
    <StompSessionProvider
      url={"ws://localhost:8080/quotes-api"}
    >
      <App />
    </StompSessionProvider>
  </React.StrictMode>
);
```

Secondly, use the `useSubscription` hook to listen for real-time updates:

```typescript
let limit = 5;
  useSubscription("/quotes", (message) => {
    setRealTimeQuotes((prevQuotes) => {
      const newQuotes = [...prevQuotes, JSON.parse(message.body)];
      return newQuotes.length > limit ? newQuotes.slice(-limit) : newQuotes;
    });
  });
```

---

### 4.4 Deploying the Full Application with Docker

#### Docker Compose File: `docker-compose.yml`

```yaml
services:
  
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.4
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - 22181:2181

  kafka:
    image: confluentinc/cp-kafka:7.4.4
    depends_on:
      - zookeeper
    ports:
      - 29092:29092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      # kafka:9092 is inter-container communication / localhost:29092 is external communication
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_LOG_RETENTION_MS: 10000
      KAFKA_LOG_RETENTION_CHECK_INTERVAL_MS: 5000
  
  postgres:
    image: postgres:12
    container_name: postgres
    env_file: ./.env
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
    ports:
      - "${POSTGRES_LOCAL_PORT}:${POSTGRES_DOCKER_PORT}"
    restart: always

  quotes:
    depends_on: 
      - postgres
      - kafka
    build: 
      context: ./quotes
    env_file: ./.env
    ports:
      - "${QUOTES_LOCAL_PORT}:${QUOTES_DOCKER_PORT}"
    environment:
      SPRING_APPLICATION_JSON: >-
        {
          "spring.datasource.url"  : "jdbc:postgresql://postgres:${POSTGRES_DOCKER_PORT}/${POSTGRES_DB}",
          "spring.datasource.username" : "${POSTGRES_USER}",
          "spring.datasource.password" : "${POSTGRES_PASSWORD}",
          "spring.jpa.properties.hibernate.dialect" : "org.hibernate.dialect.PostgreSQLDialect",
          "spring.jpa.hibernate.ddl-auto" : "update"
        }
      NEW_QUOTES_TOPIC: "${NEW_QUOTES_TOPIC}"
    stdin_open: true
    tty: true

  frontend-quotes:
    depends_on: 
      - quotes
    build: ./frontend-quotes
    ports:
      - "${FRONTEND_LOCAL_PORT}:${FRONTEND_DOCKER_PORT}"
    stdin_open: true
    tty: true
    environment:
      - NODE_ENV=development
```

The only thing left is to run the following command:

```bash
docker compose up --build
```

However, since the producer is separated from the rest of the application, you need to run it manually. Simulate a real-world scenario by running the producer in a separate terminal: (like a sensor or IoT device generating data)

```bash
cd quotes-generation
poetry run python producer.py
```


#### Key Files for Backend:

- **`Dockerfile`**: Spring Boot backend build configuration
- **`wait-for-it.sh`**: Ensures dependencies are ready before starting the backend

#### Key Files for Frontend:

- React app is containerized and served on port `3000`.

---

### 4.5 Summary

- **Python Producer**: Generates random movie quotes and sends them to Kafka.
- **Spring Boot Backend**:
  - Consumes Kafka messages.
  - Inserts quotes into PostgreSQL.
  - Updates the React frontend in real time using WebSocket.
- **React Frontend**: Displays the latest 5 quotes dynamically.
- **Deployment**: All components are containerized and orchestrated using Docker Compose.


---

# References

- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring for Apache Kafka Reference](https://docs.spring.io/spring-kafka/docs/current/reference/html/)
- [React Documentation](https://reactjs.org/docs/getting-started.html)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Confluent Documentation](https://docs.confluent.io/)
