package com.sebarys.app

import java.util.Properties

import com.sebarys.app.configuration.KafkaConfig
import org.apache.kafka.clients.admin.{AdminClient, CreateTopicsResult, NewTopic}
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream

package object kafka {

  def createKafkaProps(kafkaConfig: KafkaConfig): Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaConfig.applicationId)
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.brokerAddresses)
    p
  }

  def createTopics(config: KafkaConfig): CreateTopicsResult = {
    val adminClient: AdminClient = AdminClient.create(kafka.createKafkaProps(config))
    val sourceConf = config.sourceTopic
    val sourceTopic = new NewTopic(sourceConf.name, sourceConf.nrOfPartitions, sourceConf.replicationFactor.toShort)
    val sinkConf = config.sinkTopic
    val sinkTopic = new NewTopic(sinkConf.name, sinkConf.nrOfPartitions, sinkConf.replicationFactor.toShort)

    import scala.collection.JavaConverters._
    adminClient.createTopics(List(sourceTopic, sinkTopic).asJava)
  }

  def createKafkaStream[Key, Value](streamsBuilder: StreamsBuilder, sourceTopicName: String)
                                (implicit inSerde: Serde[Key], outSerde: Serde[Value]): KStream[Key, Value] = {
    import org.apache.kafka.streams.scala.ImplicitConversions._

    streamsBuilder.stream[Key, Value](sourceTopicName)
  }

}
