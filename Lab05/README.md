

Run the container:
```
docker exec lab05-kafka-1 kafka-topics --create --topic lab05 --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092 
```


Run the consumer:
```
docker exec lab05-kafka-1 kafka-console-consumer --topic lab05 --from-beginning --bootstrap-server kafka:9092
```

Client:
```
docker exec -it lab05-kafka-1 bash
kafka-console-producer --topic lab05 --broker-list kafka:9092
```
Type any message


# Multiple consumers & producers

When dealing with multiple consumers and producers in a messaging or queuing system, the behavior can vary depending on the specific implementation of the system. Here’s a general explanation:

 - **Multiple Consumers**
    - When you open multiple consumer terminals, each consumer will receive all messages from the queue. This means that each consumer will process the same set of messages, which can be useful for scenarios where all consumers need to see all messages.

 - **Multiple Producers**
    - When you open multiple producer terminals, each producer will send messages to the queue. The queue will receive messages from all producers and store them until they are consumed. This can increase the rate at which messages are added to the queue, potentially leading to a higher throughput.

 - **Old Messages**
    - Old messages are lost when a consumer is started after messages have been sent to the queue. This is because the queue does not store messages for consumers that are not connected. If you need to process old messages, you can use a persistent queue that stores messages until they are consumed.
   

Additionally: we can add a timeout for old messages, so they are not stored indefinitely. This can be useful for scenarios where messages need to be processed within a certain time frame. If a message is not consumed within the timeout period, it can be removed from the queue to free up resources.



# Consumer Commit
When a consumer reads messages from a Kafka topic, it keeps track of the offset (position) of the messages it has read. The consumer can commit these offsets to Kafka to mark them as processed. There are two types of commits:

Automatic Commit: The consumer automatically commits offsets at regular intervals. This is controlled by the enable_auto_commit and auto_commit_interval_ms settings.
Manual Commit: The consumer manually commits offsets using the commit() method. This gives more control over when offsets are committed, which can be useful for ensuring that messages are processed successfully before committing.

# Retention Time
Kafka topics have a retention time, which is the duration for which messages are retained in the topic. After this time, messages are deleted to free up space. The retention time is configured per topic using the retention.ms setting. 

Since in docker compose we have:
```dockerfile
KAFKA_LOG_RETENTION_MS: 10000
KAFKA_LOG_RETENTION_CHECK_INTERVAL_MS: 5000
```

This means that messages are retained for 10 seconds (10000 ms) and the retention check interval is 5 seconds (5000 ms). This can be useful for scenarios where messages need to be processed quickly and do not need to be stored for long periods of time.

# Kafka Producer

The `KafkaTemplate` wraps a producer and provides convenience methods to send data to Kafka topics.
The following listing shows the relevant methods from `KafkaTemplate`:

```java
CompletableFuture<SendResult<K, V>> sendDefault(V data);

CompletableFuture<SendResult<K, V>> sendDefault(K key, V data);

CompletableFuture<SendResult<K, V>> sendDefault(Integer partition, K key, V data);

CompletableFuture<SendResult<K, V>> sendDefault(Integer partition, Long timestamp, K key, V data);

CompletableFuture<SendResult<K, V>> send(String topic, V data);

CompletableFuture<SendResult<K, V>> send(String topic, K key, V data);

CompletableFuture<SendResult<K, V>> send(String topic, Integer partition, K key, V data);

CompletableFuture<SendResult<K, V>> send(String topic, Integer partition, Long timestamp, K key, V data);

CompletableFuture<SendResult<K, V>> send(ProducerRecord<K, V> record);

CompletableFuture<SendResult<K, V>> send(Message<?> message);

Map<MetricName, ? extends Metric> metrics();

List<PartitionInfo> partitionsFor(String topic);

<T> T execute(ProducerCallback<K, V, T> callback);

<T> T executeInTransaction(OperationsCallback<K, V, T> callback);

// Flush the producer.
void flush();

interface ProducerCallback<K, V, T> {

    T doInKafka(Producer<K, V> producer);

}

interface OperationsCallback<K, V, T> {

    T doInOperations(KafkaOperations<K, V> operations);

}
```

See the [Javadoc](https://docs.spring.io/spring-kafka/api/org/springframework/kafka/core/KafkaTemplate.html) for more detail.

# Kafka Consumer

When you use a [message listener container](https://docs.spring.io/spring-kafka/reference/html/#message-listener-container), you must provide a listener to receive data.
There are currently eight supported interfaces for message listeners.
The following listing shows these interfaces:

```java
public interface MessageListener<K, V> {

    void onMessage(ConsumerRecord<K, V> data);

}

public interface AcknowledgingMessageListener<K, V> {

    void onMessage(ConsumerRecord<K, V> data, Acknowledgment acknowledgment);

}

public interface ConsumerAwareMessageListener<K, V> extends MessageListener<K, V> {

    void onMessage(ConsumerRecord<K, V> data, Consumer<?, ?> consumer);

}

public interface AcknowledgingConsumerAwareMessageListener<K, V> extends MessageListener<K, V> {

    void onMessage(ConsumerRecord<K, V> data, Acknowledgment acknowledgment, Consumer<?, ?> consumer);

}

public interface BatchMessageListener<K, V> {

    void onMessage(List<ConsumerRecord<K, V>> data);

}

public interface BatchAcknowledgingMessageListener<K, V> {

    void onMessage(List<ConsumerRecord<K, V>> data, Acknowledgment acknowledgment);

}

public interface BatchConsumerAwareMessageListener<K, V> extends BatchMessageListener<K, V> {

    void onMessage(List<ConsumerRecord<K, V>> data, Consumer<?, ?> consumer);

}

public interface BatchAcknowledgingConsumerAwareMessageListener<K, V> extends BatchMessageListener<K, V> {

    void onMessage(List<ConsumerRecord<K, V>> data, Acknowledgment acknowledgment, Consumer<?, ?> consumer);

}
```



Poetry:
```
poetry new quotes-generation
cd quotes-generation
poetry add kafka-python python-lorem six
```

Activate the virtual environment:
```
poetry shell
```

Build the project:
```
poetry build
```

