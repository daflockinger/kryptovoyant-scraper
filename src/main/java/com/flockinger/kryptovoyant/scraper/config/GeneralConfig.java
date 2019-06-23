package com.flockinger.kryptovoyant.scraper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class GeneralConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
