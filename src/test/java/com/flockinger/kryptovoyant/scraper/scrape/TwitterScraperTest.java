package com.flockinger.kryptovoyant.scraper.scrape;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flockinger.kryptovoyant.scraper.client.twitter.TwitterClient;
import com.flockinger.kryptovoyant.scraper.client.twitter.TwitterStats;
import com.flockinger.kryptovoyant.scraper.messaging.Sender;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TwitterScraper.class})
@Import(ScraperTestConfig.class)
@TestPropertySource(properties = {"update-schedules.twitter=0/1 * * * * ?",
    "topics.twitter=twitter-topic"})
public class TwitterScraperTest {

  @MockBean
  private Sender mockSender;
  @MockBean
  private TwitterClient mockClient;

  @Test
  public void testScrapeTwitterStats_shouldScrapeAndSendOnSchedule() {

    ArrayList<TwitterStats> twitterStats = new ArrayList<>();
    when(mockClient.fetchStats()).thenReturn(twitterStats);

    verify(mockSender, timeout(2000)).sendMessage(eq(twitterStats), eq("twitter-topic"));
    verify(mockClient, timeout(2000)).fetchStats();
  }

}