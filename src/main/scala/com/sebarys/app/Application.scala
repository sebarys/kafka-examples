package com.sebarys.app

import java.util.concurrent.TimeUnit

import com.sebarys.app.configuration.Config
import com.sebarys.app.processing.Processor
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}

object Application extends App with LazyLogging {

  val config: Config = configuration.load()

  logger.info("Creating topics")

  kafka.createTopics(config.kafka)

  val kafkaStreamsBuilder = new StreamsBuilder

  implicit val stringSerde: Serde[String] = Serdes.String
  import org.apache.kafka.streams.scala.ImplicitConversions._

  val source: KStream[String, String] =
    kafka.createKafkaStream[String, String](kafkaStreamsBuilder, config.kafka.sourceTopic.name)
  val res: KStream[String, String] = Processor.process(source)
  res.to(config.kafka.sinkTopic.name)

  val streams: KafkaStreams = new KafkaStreams(
    kafkaStreamsBuilder.build(),
    kafka.createKafkaProps(config.kafka)
  )

  logger.info("Starting stream")

  streams.start()

  sys.ShutdownHookThread {
    streams.close(10, TimeUnit.SECONDS)
  }
}
