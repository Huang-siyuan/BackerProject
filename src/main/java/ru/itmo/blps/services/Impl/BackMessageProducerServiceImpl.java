package ru.itmo.blps.services.Impl;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.itmo.blps.services.BackMessageProducerService;

@Service
@AllArgsConstructor
public class BackMessageProducerServiceImpl implements BackMessageProducerService {

    private static final Logger logger = LoggerFactory.getLogger(BackMessageProducerServiceImpl.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendBackMessage(String topic, Object obj) {

        // Time, key and value.
        ProducerRecord<String, Object> producerRecord =
                new ProducerRecord<>(
                        topic,
                        null, // Let Kafka allocate by itself
                        System.currentTimeMillis(),
                        String.valueOf(obj.hashCode()),
                        obj);


        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, producerRecord.toString());
        // send backing message asynchronously
        future.addCallback(
                result -> logger.info("Producer successfully send to" + topic + "-> " + result.getRecordMetadata().topic(), result.getRecordMetadata().partition()),
                ex -> logger.error("Producer send: {} Fail, Reason{}", producerRecord, ex.getMessage()));
    }
}
