package com.flockinger.kryptovoyant.scraper.scrape;

import com.flockinger.kryptovoyant.scraper.client.metadata.CryptoMetaDataClient;
import com.flockinger.kryptovoyant.scraper.client.metadata.dto.Coin;
import com.flockinger.kryptovoyant.scraper.client.metadata.dto.CryptoMetaData;
import com.flockinger.kryptovoyant.scraper.messaging.Sender;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetaDataScraper {

  private final CryptoMetaDataClient client;
  private final String metaTopic;
  private final Sender sender;
  private final String coinsTopic;
  private final static Logger LOG = LoggerFactory.getLogger(MetaDataScraper.class);

  public MetaDataScraper(
      CryptoMetaDataClient client,
      @Value("${topics.metadata}") String metaTopic,
      @Value("${topics.coins}") String coinsTopic,
      Sender sender) {
    this.client = client;
    this.metaTopic = metaTopic;
    this.coinsTopic = coinsTopic;
    this.sender = sender;
  }

  @Scheduled(cron = "${update-schedules.metadata}")
  public void scrapeMetaData() {
    List<CryptoMetaData> metaData = client.fetchMetaData();
    sender.sendMessage(metaData, metaTopic);
    LOG.info("MetaData scraped and sent successfully having {} records!", metaData.size());
  }

  @Scheduled(cron = "${update-schedules.coins}")
  public void scrapeCoins() {
    List<Coin> coins = client.getCoins();
    sender.sendMessage(coins, coinsTopic);
    LOG.info("Coins scraped and sent successfully having {} records!", coins.size());
  }

}
