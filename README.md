# How to run Kafka

## Docker

### Spotify Kafka   
Spotify provide Kafka docker image (https://hub.docker.com/r/spotify/kafka/) that contain Kafka + Zookeeper, to run it execute
```
docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=`docker-machine ip \`docker-machine active\`` --env ADVERTISED_PORT=9092 spotify/kafka
```

### Wurstmeister Kafka
Gives separate images for Apache Zookeeper and Apache Kafka. You can use `docker-compose` to run signle node or Kafka cluster (https://jaceklaskowski.gitbooks.io/apache-kafka/kafka-docker.html#wurstmeister-kafka)

Run single node
```
export DOCKERHOST=$(ifconfig | grep -E "([0-9]{1,3}\.){3}[0-9]{1,3}" | grep -v 127.0.0.1 | awk '{ print $2 }' | cut -f2 -d: | head -n1)
docker-compose -f docker-compose-single-broker.yml up
```

Run kafka cluster defined in `docker-compose.yml`
```
export DOCKERHOST=$(ifconfig | grep -E "([0-9]{1,3}\.){3}[0-9]{1,3}" | grep -v 127.0.0.1 | awk '{ print $2 }' | cut -f2 -d: | head -n1)
docker-compose up -d
```

# Q&A

## Is Kafka Streams store some information on server side e.g. in case of window aggregations

Based on my current knowledge yes, see:
- https://docs.confluent.io/current/streams/architecture.html#state
- https://docs.confluent.io/current/streams/developer-guide/running-app.html#streams-developer-guide-execution-scaling-state-restoration
- https://docs.confluent.io/current/streams/architecture.html#fault-tolerance

```
$ bash kafka-topics.sh --list --bootstrap-server localhost:9092
__consumer_offsets
input
kafka-streams-example-KSTREAM-REDUCE-STATE-STORE-0000000002-changelog <---
kafka-streams-example-KSTREAM-REDUCE-STATE-STORE-0000000002-repartition <---
kafka-streams-example-KTABLE-SUPPRESS-STATE-STORE-0000000008-changelog <---
output
```

## Is Kafka Streams support asynchronous operations

No, parallelism is achieved through the partitioning of topics, within a partition of a topic, processing is strongly ordered, one-at-a-time.