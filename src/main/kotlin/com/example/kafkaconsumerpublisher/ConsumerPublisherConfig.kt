package com.example.kafkaconsumerpublisher

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory


@Configuration
@EnableKafka
class ConsumerPublisherConfig(
    @Value("\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String,
    @Value("\${kafka.topic.name}")
    private val topicName: String
) {
    private val adminConfig = mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress)

    @Bean
    fun admin(): KafkaAdmin {
        return KafkaAdmin(adminConfig)
    }

    @Bean
    fun createTopic() = NewTopic(topicName, 1, 1.toShort())

    val producerProps = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
    )

    @Bean
    fun producerFactory() = DefaultKafkaProducerFactory<String, String>(producerProps)

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, String>) = KafkaTemplate(producerFactory)
    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, String>) = ConcurrentKafkaListenerContainerFactory<String, String>().also { it.consumerFactory = consumerFactory }

    val consumerProps = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
        ConsumerConfig.GROUP_ID_CONFIG to "local-group",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
    )

    @Bean
    fun consumerFactory() : DefaultKafkaConsumerFactory<String, String>{
        return DefaultKafkaConsumerFactory<String, String>(consumerProps)
    }

}
