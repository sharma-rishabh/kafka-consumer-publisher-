package com.example.kafkaconsumerpublisher

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.protocol.types.Field.Str
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer
import org.springframework.boot.runApplication
import java.time.Duration
import java.util.*

//@SpringBootApplication
//class KafkaConsumerPublisherApplication

fun main(args: Array<String>) {
	val bootstrapServer = "localhost:29092"
	val topic = "PRICES"
//	val client = AdminClient.create(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer))
//	client.createTopics(listOf(NewTopic(topic, 1, 1.toShort())))
//	client.close()
//
	val producerProps = Properties().apply {
		put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
		put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
		put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
	}
	val producer = KafkaProducer<String, String>(producerProps)

	val producerRecord = ProducerRecord(topic, "carPrice", "100000")
	producer.send(producerRecord){metadata, error ->
		if (error == null) {
			println("Produced an event for ${metadata.topic()}")
		} else {
			println("Couldn't produce event ${error.message}")
		}
	}

	producer.flush()
	producer.close()


	val consumerProps = Properties().apply {
		put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
		put(ConsumerConfig.GROUP_ID_CONFIG, "local-group")
		put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
		put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
		put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
	}
	val consumer = KafkaConsumer<String, String>(consumerProps)
	consumer.subscribe(listOf(topic))

	while (true) {
		val records = consumer.poll(Duration.ofMillis(100))
		for (record in records) {
			println("Received Message on ${record.topic()}, key = ${record.key()}, value = ${record.value()}")
		}

	}
}
