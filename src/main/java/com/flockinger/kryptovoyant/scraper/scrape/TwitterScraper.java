package com.flockinger.kryptovoyant.scraper.scrape;

import com.flockinger.kryptovoyant.scraper.client.twitter.TwitterClient;
import com.flockinger.kryptovoyant.scraper.client.twitter.TwitterStats;
import com.flockinger.kryptovoyant.scraper.messaging.Sender;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TwitterScraper {

  private final TwitterClient client;
  private final Sender sender;
  private final String twitterTopic;
  private final static Logger LOG = LoggerFactory.getLogger(TwitterScraper.class);

  public TwitterScraper(
      TwitterClient client,
      Sender sender,
      @Value("${topics.twitter}") String twitterTopic) {
    this.client = client;
    this.sender = sender;
    this.twitterTopic = twitterTopic;
  }

  @Scheduled(cron = "${update-schedules.twitter}")
  public void scrapeTwitterStats() {
    List<TwitterStats> twitterStats = client.fetchStats();
    sender.sendMessage(twitterStats, twitterTopic);
    LOG.info("TwitterStats scraped and sent successfully having {} records!", twitterStats.size());
  }

}
