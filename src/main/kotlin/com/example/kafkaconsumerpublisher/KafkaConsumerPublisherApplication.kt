package com.example.kafkaconsumerpublisher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaConsumerPublisherApplication

fun main(args: Array<String>) {
	runApplication<KafkaConsumerPublisherApplication>(*args)
}
