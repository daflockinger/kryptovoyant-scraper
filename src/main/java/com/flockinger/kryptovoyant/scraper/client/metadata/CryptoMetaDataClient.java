package com.flockinger.kryptovoyant.scraper.client.metadata;

import com.flockinger.kryptovoyant.scraper.client.metadata.dto.Coin;
import com.flockinger.kryptovoyant.scraper.client.metadata.dto.CryptoMetaData;
import java.util.List;

public interface CryptoMetaDataClient {

  List<Coin> getCoins();

  List<CryptoMetaData> fetchMetaData();

}
