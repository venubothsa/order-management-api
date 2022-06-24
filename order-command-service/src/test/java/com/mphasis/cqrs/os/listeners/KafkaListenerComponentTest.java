package com.mphasis.cqrs.os.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.cqrs.os.command.dtos.PaymentMessage;
import com.mphasis.cqrs.os.command.listeners.KafkaListenerComponent;
import com.mphasis.cqrs.os.command.utils.PaymentStatus;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@EmbeddedKafka(partitions = 1, topics = {"payment_status_events"})
@SpringBootTest
public class KafkaListenerComponentTest {

    private static final String TOPIC = "payment_status_events";
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    @SpyBean
    private KafkaListenerComponent kafkaListenerComponent;

    @Captor
    ArgumentCaptor<String> messageArgumentCaptor;
    @Captor
    ArgumentCaptor<String> topicArgumentCaptor;
    @Captor
    ArgumentCaptor<Integer> partitionArgumentCaptor;
    @Captor
    ArgumentCaptor<Long> offsetArgumentCaptor;
    Producer<Integer, String> kafkaProducer;

    @Before
    public void init() {
        Map<String, Object> stringObjectMap = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        kafkaProducer = new DefaultKafkaProducerFactory<Integer, String>(stringObjectMap).createProducer();
    }

    @Test
    public void listenPaymentEvent() throws JsonProcessingException, InterruptedException {
        Consumer<Integer, String> consumer = configureConsumer();
        ObjectMapper objectMapper = new ObjectMapper();
        PaymentMessage paymentMessage = new PaymentMessage();
        paymentMessage.setOrderId(UUID.randomUUID().toString());
        paymentMessage.setStatus(PaymentStatus.PAYMENT_SUCCESS.name());
        paymentMessage.setOrderAmount(100.0);
        paymentMessage.setTransactionId(UUID.randomUUID().toString());
        kafkaProducer.send(new ProducerRecord<>(TOPIC, 1, objectMapper.writeValueAsString(paymentMessage)));
        kafkaProducer.flush();
        ConsumerRecord<Integer, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
        assertNotNull(singleRecord);
        consumer.close();
        kafkaProducer.close();
    }

    private Consumer<Integer, String> configureConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("payment-events-1", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<Integer, String>(consumerProps)
                .createConsumer();
        consumer.subscribe(Collections.singleton(TOPIC));
        return consumer;
    }

}
