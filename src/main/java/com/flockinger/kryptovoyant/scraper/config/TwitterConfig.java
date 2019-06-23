package com.flockinger.kryptovoyant.scraper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfig {

  @Bean
  public TwitterFactory twitterFactory(TwitterSettings settings) {
    ConfigurationBuilder configBuilder = new ConfigurationBuilder();
    configBuilder.setOAuthConsumerKey(settings.getConsumerKey());
    configBuilder.setOAuthConsumerSecret(settings.getConsumerSecret());
    configBuilder.setOAuthAccessToken(settings.getAccessToken());
    configBuilder.setOAuthAccessTokenSecret(settings.getAccessTokenSecret());

    return new TwitterFactory(configBuilder.build());
  }

}
