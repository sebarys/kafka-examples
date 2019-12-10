package com.sebarys.app

import com.typesafe.config.ConfigFactory

package object configuration {

  case class TopicConfig(
      name: String,
      nrOfPartitions: Int,
      replicationFactor: Int
  )
  case class KafkaConfig(
      applicationId: String,
      brokerAddresses: String,
      sourceTopic: TopicConfig,
      sinkTopic: TopicConfig
  )

  case class Config(kafka: KafkaConfig)

  def load(configFile: String = "application.conf"): Config = {
    val config = ConfigFactory.load(configFile)
    val applicationId: String = config.getString("kafka.applicationId")
    val brokerAddresses: String = config.getString("kafka.addresses")
    val sourceTopicName: String = config.getString("kafka.source.name")
    val sourceTopicPartitionNumber: Int = config.getInt("kafka.source.partitions")
    val sourceTopicReplicationFactor: Int = config.getInt("kafka.source.replicationFactor")
    val sinkTopicName: String = config.getString("kafka.sink.name")
    val sinkTopicPartitionNumber: Int = config.getInt("kafka.sink.partitions")
    val sinkTopicReplicationFactor: Int = config.getInt("kafka.sink.replicationFactor")
    Config(
      KafkaConfig(
        applicationId = applicationId,
        brokerAddresses = brokerAddresses,
        sourceTopic =  TopicConfig(sourceTopicName, sourceTopicPartitionNumber, sourceTopicReplicationFactor),
        sinkTopic = TopicConfig(sinkTopicName, sinkTopicPartitionNumber, sinkTopicReplicationFactor)
      )
    )
  }
}
