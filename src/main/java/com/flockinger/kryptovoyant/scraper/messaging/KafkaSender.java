package com.flockinger.kryptovoyant.scraper.messaging;

import com.flockinger.kryptovoyant.scraper.exception.MessagingException;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender implements Sender {

  private final KafkaTemplate<String, Object> messageTemplate;
  private final static Logger LOG = LoggerFactory.getLogger(KafkaSender.class);

  public KafkaSender(KafkaTemplate<String, Object> messageTemplate) {
    this.messageTemplate = messageTemplate;
  }

  @Override
  public <T extends Object> void sendMessage(T message, String topic) {
    if (Objects.isNull(message) || StringUtils.isEmpty(topic)) {
      throw new MessagingException("Message and Topic must not be empty/null!");
    }

    messageTemplate.send(topic, message).completable()
        .handle((it, exception) -> {
          if (Objects.nonNull(exception)) {
            LOG.error(String.format("Sending messgae to topic %s failed!", topic), exception);
          } else {
            LOG.debug("Message successfully sent to {}.", topic);
          }
          return it;
        });
  }

}
