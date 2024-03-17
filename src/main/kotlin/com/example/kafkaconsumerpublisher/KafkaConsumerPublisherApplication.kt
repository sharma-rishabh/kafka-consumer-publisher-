package com.example.kafkaconsumerpublisher

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate


@SpringBootApplication
class KafkaConsumerPublisherApplication{
	@KafkaListener(id = "consumer", topics = ["AUTO_PRICES"])
	fun listen(message: String) {
		println(">>> MESSAGE $message")
	}

	@Bean
	fun runner(kafkaTemplate: KafkaTemplate<String, String>) =
		ApplicationRunner {
			kafkaTemplate.send("AUTO_PRICES", "ALTO", "100000")
		}
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<KafkaConsumerPublisherApplication>(*args)
		}
	}
}

