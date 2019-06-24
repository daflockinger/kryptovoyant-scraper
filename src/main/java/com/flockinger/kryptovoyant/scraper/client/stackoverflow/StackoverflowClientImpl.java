package com.flockinger.kryptovoyant.scraper.client.stackoverflow;

import java.util.List;
import java.util.Map;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class StackoverflowClientImpl implements StackoverflowClient {

  private String query = "https://api.stackexchange.com/2.2/search?filter=total&site=stackoverflow&key=tXUjuscCFSI6AdV*KweFsA((&intitle=";

  /// 2.2/search?fromdate=1560902400&todate=1560988800

  // improve that, need search pairs ideally
  private Map<String, Long> blub(List<String> searchTerms) {

    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
        HttpClientBuilder.create().build());
    RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

    ResponseEntity<StackoverflowResults> result = restTemplate
        .getForEntity(query + "bitcoin", StackoverflowResults.class);

    System.out.println(result.getBody().getTotal());

    return null;
  }

}
