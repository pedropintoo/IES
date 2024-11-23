import sys
import sys, types

m = types.ModuleType('kafka.vendor.six.moves', 'Mock module')
setattr(m, 'range', range)
sys.modules['kafka.vendor.six.moves'] = m

from kafka import KafkaProducer
from kafka.errors import KafkaError
import json

producer = KafkaProducer(bootstrap_servers=['localhost:29092'])

# Produce messages
number = 1
nmec = 115304
topic = 'lab05_115304'

while number < nmec:
    
    message = json.dumps({'nMec': '115304', 'generatedNumber': number, 'type': 'fibonacci'}).encode('utf-8')
    future = producer.send(topic, message)
    try:
        record_metadata = future.get(timeout=10)
        print(f"Topic: {record_metadata.topic}, Partition: {record_metadata.partition}, Offset: {record_metadata.offset}, Value: {message.decode('utf-8')}")
    except KafkaError as e:
        print(f"Failed to send message: {e}")

    number += number
    
