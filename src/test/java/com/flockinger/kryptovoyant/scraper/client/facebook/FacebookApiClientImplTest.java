package com.flockinger.kryptovoyant.scraper.client.facebook;

import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flockinger.kryptovoyant.scraper.config.RetryConfig;
import com.flockinger.kryptovoyant.scraper.resources.JsonFileReader;
import com.flockinger.kryptovoyant.scraper.resources.ResourceReader;
import java.io.FileWriter;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@Import(RetryConfig.class)
public class FacebookApiClientImplTest {


  @Autowired
  @Qualifier("FacebookRetryTemplate")
  private RetryTemplate retryTemplate;

  private ResourceReader reader;

  private FacebookApiClient client;

  @Before
  public void setup() {
    reader = mock(ResourceReader.class);
    client = new FacebookApiApiClientImpl(retryTemplate, reader);
  }

  @Test
  public void testFetchStats_withAllRealStats_shouldFetchCorrectly() throws Exception {
    client = new FacebookApiApiClientImpl(retryTemplate, new JsonFileReader(new ObjectMapper()));

    List<FacebookStats> stats = client.fetchStats();

    ObjectMapper mapper = new ObjectMapper();

    IOUtils.write(mapper.writeValueAsString(stats), new FileWriter("/root/FacebookStats.json"));

    System.out.println();
  }


}