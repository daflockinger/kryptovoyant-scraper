package com.flockinger.kryptovoyant.scraper.client.metadata;

import static org.apache.commons.lang3.BooleanUtils.toBoolean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flockinger.kryptovoyant.scraper.client.metadata.dto.Coin;
import com.flockinger.kryptovoyant.scraper.client.metadata.dto.CryptoMetaData;
import com.flockinger.kryptovoyant.scraper.client.metadata.dto.ExchangeRate;
import com.flockinger.kryptovoyant.scraper.config.CryptoMetaClientSettings;
import com.flockinger.kryptovoyant.scraper.exception.ApiClientException;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class CoinApiClient implements CryptoMetaDataClient {

  private final RestTemplate restTemplate;
  private final CryptoMetaClientSettings metaClientSettings;
  private final static String ASSET_METADATA_PATH = "/v1/assets";
  private final static String EXCHANGE_RATE_PATH = "/v1/exchangerate/";
  private final static String DOLLAR_ASSET_ID = "USD";
  private final static String EURO_ASSET_ID = "EUR";
  private final static String AUTH_HEADER = "X-CoinAPI-Key";
  private final static ParameterizedTypeReference<List<Asset>> ASSET_TYPE
      = new ParameterizedTypeReference<>() {
  };
  private final static ParameterizedTypeReference<ExchangeRates> EXCHANGE_RATE_TYPE
      = new ParameterizedTypeReference<>() {
  };

  public CoinApiClient(RestTemplate restTemplate,
      CryptoMetaClientSettings metaClientSettings) {
    this.restTemplate = restTemplate;
    this.metaClientSettings = metaClientSettings;
  }

  @Override
  public List<Coin> getCoins() {
    List<Asset> assets = getFromCoinApi(ASSET_METADATA_PATH, ASSET_TYPE);
    List<Coin> coins = assets.stream()
        .filter(it -> toBoolean(it.getIsCrypto()))
        .map(it -> new Coin(it.getName(), it.getAssetId(), it.getLaunchDate(), it.tradeCount))
        .collect(Collectors.toList());

    if (coins.isEmpty()) {
      throw new ApiClientException("Coin API cannot return empty!");
    }
    return coins;
  }

  private <T> T getFromCoinApi(String path, ParameterizedTypeReference<T> type) {
    MultiValueMap<String, String> authHeader = new LinkedMultiValueMap<>();
    authHeader.set(AUTH_HEADER, metaClientSettings.getApiKey());
    RequestEntity<Void> request = new RequestEntity<>(authHeader, HttpMethod.GET,
        buildUri(path));
    ResponseEntity<T> assets = restTemplate.exchange(request, type);
    return assets.getBody();
  }

  private URI buildUri(String path) {
    try {
      URIBuilder builder = new URIBuilder(metaClientSettings.getBaseUrl());
      return builder
          .setPath(path)
          .build();
    } catch (URISyntaxException e) {
      throw new ApiClientException("Incorrect base URL for coin-API was set, please fix!", e);
    }
  }

  @Override
  public List<CryptoMetaData> fetchMetaData() {
    ExchangeRates exchangeRatesEuro = getFromCoinApi(EXCHANGE_RATE_PATH + EURO_ASSET_ID,
        EXCHANGE_RATE_TYPE);
    ExchangeRates exchangeRatesUSDollar = getFromCoinApi(EXCHANGE_RATE_PATH + DOLLAR_ASSET_ID,
        EXCHANGE_RATE_TYPE);
    return Stream.of(
        toRates(EURO_ASSET_ID, exchangeRatesEuro),
        toRates(DOLLAR_ASSET_ID, exchangeRatesUSDollar))
        .reduce(Stream::concat).stream()
        .flatMap(it -> it)
        .collect(Collectors.groupingBy(it -> it._2.getAssetId()))
        .entrySet().stream()
        .map(this::mapToMetaData)
        .collect(Collectors.toList());
  }

  private Stream<Tuple2<String, Rate>> toRates(String baseAsset, ExchangeRates exchangeRates) {
    return exchangeRates.getRates().stream()
        .map(it -> Tuple.of(baseAsset, it));
  }

  private CryptoMetaData mapToMetaData(Entry<String, List<Tuple2<String, Rate>>> it) {
    CryptoMetaData metaData = new CryptoMetaData();
    metaData.setSymbol(it.getKey());
    metaData.setExchangeRate(it.getValue().stream()
        .map(this::mapToExchangeRate)
        .collect(Collectors.toList()));
    return metaData;
  }

  private ExchangeRate mapToExchangeRate(Tuple2<String, Rate> rate) {
    ExchangeRate exchangeRate = new ExchangeRate();
    exchangeRate.setCurrency(rate._1);
    // inversion is needed ause the opposite rate is received
    exchangeRate.setRate(1 / rate._2.getRate());
    return exchangeRate;
  }


  private static class Asset {

    @JsonProperty("asset_id")
    private String assetId;
    private String name;
    @JsonProperty("type_is_crypto")
    private Integer isCrypto;
    @JsonProperty("data_start")
    private Date launchDate;
    @JsonProperty("data_trade_count")
    private Long tradeCount;

    public Date getLaunchDate() {
      return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
      this.launchDate = launchDate;
    }

    public Long getTradeCount() {
      return tradeCount;
    }

    public void setTradeCount(Long tradeCount) {
      this.tradeCount = tradeCount;
    }

    public String getAssetId() {
      return assetId;
    }

    public void setAssetId(String assetId) {
      this.assetId = assetId;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getIsCrypto() {
      return isCrypto;
    }

    public void setIsCrypto(Integer isCrypto) {
      this.isCrypto = isCrypto;
    }
  }

  private static class ExchangeRates {

    private List<Rate> rates;

    public List<Rate> getRates() {
      return rates;
    }

    public void setRates(
        List<Rate> rates) {
      this.rates = rates;
    }
  }

  private static class Rate {

    @JsonProperty("asset_id_quote")
    private String assetId;
    private Double rate;

    public String getAssetId() {
      return assetId;
    }

    public void setAssetId(String assetId) {
      this.assetId = assetId;
    }

    public Double getRate() {
      return rate;
    }

    public void setRate(Double rate) {
      this.rate = rate;
    }
  }
}
