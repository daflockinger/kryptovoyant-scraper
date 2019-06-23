package com.flockinger.kryptovoyant.scraper.scrape;

import com.flockinger.kryptovoyant.scraper.client.metadata.CryptoMetaDataClient;
import com.flockinger.kryptovoyant.scraper.client.metadata.dto.CryptoMetaData;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetaDataScraper {

  private final CryptoMetaDataClient client;
  private final String topic;
  private final KafkaTemplate<String, Object> messageTemplate;
  private final static Logger LOG = LoggerFactory.getLogger(MetaDataScraper.class);

  public MetaDataScraper(
      CryptoMetaDataClient client,
      @Value("${topics.metadata}") String topic,
      KafkaTemplate<String, Object> messageTemplate) {
    this.client = client;
    this.topic = topic;
    this.messageTemplate = messageTemplate;
  }

  @Scheduled(cron = "${update-schedules.metadata}")
  public void scrape() {
    List<CryptoMetaData> metaData = client.fetchMetaData();
    messageTemplate.send(topic, metaData);
    LOG.info("MetaData scraped and sent successfully having {} records!", metaData.size());
  }

}
