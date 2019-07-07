package com.flockinger.kryptovoyant.scraper.client.twitter;

import java.util.List;

public interface TwitterClient {

  List<TwitterStats> fetchStats();
}
