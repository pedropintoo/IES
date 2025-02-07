import sys, types

m = types.ModuleType('kafka.vendor.six.moves', 'Mock module')
setattr(m, 'range', range)
sys.modules['kafka.vendor.six.moves'] = m

from kafka import KafkaConsumer, TopicPartition

# Kafka Configuration
bootstrap_servers = 'localhost:29092'
topic_name = 'lab05_115304'
group_id = 'consumer1'

# Create Kafka consumer instance
consumer = KafkaConsumer(
    topic_name,
    bootstrap_servers=bootstrap_servers,
    group_id=group_id,
    enable_auto_commit=True,  # Automatically commit offsets
    auto_offset_reset='earliest'  # Start from the beginning if no offset is stored
)

# List topic partitions
PARTITIONS = []
for partition in consumer.partitions_for_topic(topic_name):
    PARTITIONS.append(TopicPartition(topic_name, partition))

# Print end offsets
end_offsets = consumer.end_offsets(PARTITIONS)
print("End Offsets:", end_offsets)

# Consume messages
for message in consumer:
    print(f"Received message: {message.value.decode('utf-8')}")  # Decode message from bytes