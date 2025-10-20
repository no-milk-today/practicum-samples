KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
echo $KAFKA_CLUSTER_ID
bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties
bin/kafka-server-start.sh config/server.properties


bin/kafka-topics.sh --create --topic visitors --partitions 3 --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic visitors --from-beginning

# В CMD или PowerShell
set "KAFKA_LOG4J_OPTS=-Dlog4j.configurationFile=file:///D:/env/kafka_2.13-4.1.0/config/log4j2.yaml"

.\bin\windows\kafka-storage.bat random-uuid
.\bin\windows\kafka-storage.bat format --standalone -t <UUID> -c config\server.properties
.\bin\windows\kafka-server-start.bat config\server.properties