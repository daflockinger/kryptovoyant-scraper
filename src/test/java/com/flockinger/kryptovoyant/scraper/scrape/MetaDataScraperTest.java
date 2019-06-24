package com.flockinger.kryptovoyant.scraper.scrape;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flockinger.kryptovoyant.scraper.client.metadata.CryptoMetaDataClient;
import com.flockinger.kryptovoyant.scraper.client.metadata.dto.Coin;
import com.flockinger.kryptovoyant.scraper.client.metadata.dto.CryptoMetaData;
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
@ContextConfiguration(classes = {MetaDataScraper.class})
@Import(ScraperTestConfig.class)
@TestPropertySource(properties = {"update-schedules.metadata=0/1 * * * * ?",
    "topics.metadata=meta-topic",
    "update-schedules.coins=0/1 * * * * ?", "topics.coins=coins-topic"})
public class MetaDataScraperTest {

  @MockBean
  private Sender mockSender;
  @MockBean
  private CryptoMetaDataClient mockClient;

  @Test
  public void testScrapeMetaData_shouldScrapeAndSendOnSchedule() {

    ArrayList<CryptoMetaData> meta = new ArrayList<>();
    when(mockClient.fetchMetaData()).thenReturn(meta);

    verify(mockSender, timeout(2000)).sendMessage(eq(meta), eq("meta-topic"));
    verify(mockClient, timeout(2000)).fetchMetaData();
  }

  @Test
  public void testScrapeCoins_shouldScrapeAndSendOnSchedule() {

    ArrayList<Coin> coins = new ArrayList<>();
    when(mockClient.getCoins()).thenReturn(coins);

    verify(mockSender, timeout(2000)).sendMessage(eq(coins), eq("coins-topic"));
    verify(mockClient, timeout(2000)).getCoins();
  }

}