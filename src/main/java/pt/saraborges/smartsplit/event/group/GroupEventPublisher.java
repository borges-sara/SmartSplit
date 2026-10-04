package pt.saraborges.smartsplit.event.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pt.saraborges.smartsplit.event.KafkaTopics;

@Component
public class GroupEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(GroupEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public GroupEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onGroupCreated(GroupCreatedEvent event) {
        kafkaTemplate.send(KafkaTopics.GROUP_CREATED, event.groupId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish GroupCreatedEvent for group {}", event.groupId(), ex);
                    } else {
                        log.info("Published GroupCreatedEvent for group {}", event.groupId());
                    }
                });
    }
}