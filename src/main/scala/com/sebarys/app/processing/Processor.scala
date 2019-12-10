package com.sebarys.app.processing

import java.time.temporal.ChronoUnit

import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.kstream.TimeWindows
import org.apache.kafka.streams.scala.kstream.Suppressed.BufferConfig
import org.apache.kafka.streams.scala.kstream.{KStream, Suppressed}

object Processor {
  def process(source: KStream[String, String])(implicit stringSerde: Serde[String]): KStream[String, String] = {

    import org.apache.kafka.streams.scala.ImplicitConversions._

    source
      // group by message, or we can group by part of message in case of more complex structure
      .groupBy((_, v) => v)
      // https://kafka.apache.org/20/documentation/streams/developer-guide/dsl-api.html#windowing
      .windowedBy(TimeWindows.of(java.time.Duration.of(5, ChronoUnit.SECONDS)))
      .reduce((value1, value2) => {
        println(s"reduce events: $value1, $value2")
        s"$value1, $value2"
      })
      // TODO check if we should use .suppress() https://kafka.apache.org/21/documentation/streams/developer-guide/dsl-api.html#window-final-results
      // TODO https://www.confluent.io/blog/kafka-streams-take-on-watermarks-and-triggers/
      //.suppress(Suppressed.untilTimeLimit(java.time.Duration.of(1, ChronoUnit.SECONDS), BufferConfig.maxBytes(1000000)))
      .toStream((k, _) => k.key())
  }
}
