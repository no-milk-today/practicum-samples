KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
echo $KAFKA_CLUSTER_ID
bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties
bin/kafka-server-start.sh config/server.properties


bin/kafka-topics.sh --create --topic visitors --partitions 3 --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic visitors --from-beginning
