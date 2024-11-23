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

while True:
    quote = lorem.get_sentence()
    movie = MOVIES[random.randint(0, len(MOVIES) - 1)]
    # print(f"[{movie}]: {quote}")
    message = json.dumps({'quote': quote, 'movie': movie}).encode('utf-8')
    print(f"Sending message: {message}")
    future = producer.send(TOPIC, message)
    try:
        record_metadata = future.get(timeout=10)
        print(f"Message sent to topic {record_metadata.topic} with partition {record_metadata.partition} and offset {record_metadata.offset}")
    except KafkaError as e:
        print(f"Failed to send message: {e}")

    time.sleep(random.randint(5, 10))
