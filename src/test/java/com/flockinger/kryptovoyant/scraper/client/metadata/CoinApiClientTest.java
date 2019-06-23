package com.flockinger.kryptovoyant.scraper.client.metadata;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.flockinger.kryptovoyant.scraper.client.metadata.dto.Coin;
import com.flockinger.kryptovoyant.scraper.client.metadata.dto.CryptoMetaData;
import com.flockinger.kryptovoyant.scraper.config.CryptoMetaClientSettings;
import com.flockinger.kryptovoyant.scraper.exception.ApiClientException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;


public class CoinApiClientTest {

  private CryptoMetaClientSettings settings = new CryptoMetaClientSettings();
  private RestTemplate restTemplate = new RestTemplate();

  private CoinApiClient client;

  @Rule
  public WireMockRule coinApiMock = new WireMockRule(8081);


  @Before
  public void setup() {
    settings.setApiKey("super-secret-key");
    settings.setBaseUrl("http://localhost:8081");
    client = new CoinApiClient(restTemplate, settings);
  }

  @Test
  public void testgGetCoins_withValidResponse_shouldReturnCorrect() throws Exception {
    coinApiMock.stubFor(get(urlPathEqualTo("/v1/assets"))
        .withHeader("X-CoinAPI-Key", equalTo(settings.getApiKey()))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(assetResponse())));

    List<Coin> coins = client.getCoins();

    assertThat(coins).hasSize(3)
        .extracting("currencyName", "symbol", "launchDate", "tradeCount")
        .containsExactly(tuple("Bitcoin", "BTC", new Date(1279324800000L), 4196037957L),
            tuple("Ethereum", "ETH", new Date(1438905600000L), 521247100L),
            tuple("Ripple", "XRP", new Date(1385337600000L), 139862863L));
  }

  private String assetResponse() {
    return "[\n"
        + "  {\n"
        + "    \"asset_id\": \"BTC\",\n"
        + "    \"name\": \"Bitcoin\",\n"
        + "    \"type_is_crypto\": 1,\n"
        + "    \"data_start\": \"2010-07-17\",\n"
        + "    \"data_end\": \"2019-06-23\",\n"
        + "    \"data_quote_start\": \"2014-02-24T17:43:05.0000000Z\",\n"
        + "    \"data_quote_end\": \"2019-06-23T17:01:02.7897180Z\",\n"
        + "    \"data_orderbook_start\": \"2014-02-24T17:43:05.0000000Z\",\n"
        + "    \"data_orderbook_end\": \"2019-06-23T17:01:12.7427910Z\",\n"
        + "    \"data_trade_start\": \"2010-07-17T23:09:17.0000000Z\",\n"
        + "    \"data_trade_end\": \"2019-06-23T23:58:16.0000000Z\",\n"
        + "    \"data_trade_count\": 4196037957,\n"
        + "    \"data_symbols_count\": 19102\n"
        + "  },\n"
        + "{\n"
        + "    \"asset_id\": \"ETH\",\n"
        + "    \"name\": \"Ethereum\",\n"
        + "    \"type_is_crypto\": 1,\n"
        + "    \"data_start\": \"2015-08-07\",\n"
        + "    \"data_end\": \"2019-06-23\",\n"
        + "    \"data_quote_start\": \"2015-08-07T14:50:38.1774950Z\",\n"
        + "    \"data_quote_end\": \"2019-06-23T17:01:05.8630747Z\",\n"
        + "    \"data_orderbook_start\": \"2015-08-07T14:50:38.1774950Z\",\n"
        + "    \"data_orderbook_end\": \"2019-06-23T17:01:12.3693150Z\",\n"
        + "    \"data_trade_start\": \"2015-08-07T15:21:48.1062520Z\",\n"
        + "    \"data_trade_end\": \"2019-06-23T23:56:15.0000000Z\",\n"
        + "    \"data_trade_count\": 521247100,\n"
        + "    \"data_symbols_count\": 10819\n"
        + "  },\n"
        + "  {\n"
        + "    \"asset_id\": \"USD\",\n"
        + "    \"name\": \"US Dollar\",\n"
        + "    \"type_is_crypto\": 0,\n"
        + "    \"data_start\": \"2010-07-17\",\n"
        + "    \"data_end\": \"2019-06-23\",\n"
        + "    \"data_quote_start\": \"2014-02-24T17:43:05.0000000Z\",\n"
        + "    \"data_quote_end\": \"2019-06-23T17:01:02.8716265Z\",\n"
        + "    \"data_orderbook_start\": \"2014-02-24T17:43:05.0000000Z\",\n"
        + "    \"data_orderbook_end\": \"2019-06-23T17:01:12.9119685Z\",\n"
        + "    \"data_trade_start\": \"2010-07-17T23:09:17.0000000Z\",\n"
        + "    \"data_trade_end\": \"2019-06-23T17:01:06.2062325Z\",\n"
        + "    \"data_trade_count\": 476477248,\n"
        + "    \"data_symbols_count\": 8672\n"
        + "  },\n"
        + "  {\n"
        + "    \"asset_id\": \"XRP\",\n"
        + "    \"name\": \"Ripple\",\n"
        + "    \"type_is_crypto\": 1,\n"
        + "    \"data_start\": \"2013-11-25\",\n"
        + "    \"data_end\": \"2019-06-23\",\n"
        + "    \"data_quote_start\": \"2014-07-31T13:05:46.0000000Z\",\n"
        + "    \"data_quote_end\": \"2019-06-23T17:00:53.5865536Z\",\n"
        + "    \"data_orderbook_start\": \"2014-07-31T13:05:46.0000000Z\",\n"
        + "    \"data_orderbook_end\": \"2019-06-23T17:01:01.7269410Z\",\n"
        + "    \"data_trade_start\": \"2013-11-25T11:54:57.9270000Z\",\n"
        + "    \"data_trade_end\": \"2019-06-23T23:43:38.0000000Z\",\n"
        + "    \"data_trade_count\": 139862863,\n"
        + "    \"data_symbols_count\": 819\n"
        + "  }]";
  }

  @Test(expected = ApiClientException.class)
  public void testgGetCoins_withEmptyResponse_shouldThrowException() throws Exception {
    coinApiMock.stubFor(get(urlPathEqualTo("/v1/assets"))
        .withHeader("X-CoinAPI-Key", equalTo(settings.getApiKey()))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("[]")));
    List<Coin> coins = client.getCoins();
  }

  @Test
  public void testFetchMetaData_withValidResponse_shouldReturnCorrect() {
    coinApiMock.stubFor(get(urlPathEqualTo("/v1/exchangerate/USD"))
        .withHeader("X-CoinAPI-Key", equalTo(settings.getApiKey()))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(exchangeRateResponse())));
    coinApiMock.stubFor(get(urlPathEqualTo("/v1/exchangerate/EUR"))
        .withHeader("X-CoinAPI-Key", equalTo(settings.getApiKey()))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(exchangeRateResponse())));

    List<CryptoMetaData> metaData = client.fetchMetaData();

    assertThat(metaData).hasSize(5)
        .extracting("symbol")
        .contains("IOTA", "NEO", "BTC", "LTC", "ETH");
    assertThat(metaData.stream().filter(it -> it.getSymbol().equals("IOTA"))
        .findFirst().get().getExchangeRate()).hasSize(2)
        .extracting("currency", "rate")
        .containsExactly(tuple("EUR", 0.40992262641348665d),
            tuple("USD", 0.40992262641348665d));
  }

  private String exchangeRateResponse() {
    return "{\n"
        + "  \"asset_id_base\": \"EUR\",\n"
        + "  \"rates\": [\n"
        + "    {\n"
        + "      \"time\": \"2019-06-23T17:30:47.1190672Z\",\n"
        + "      \"asset_id_quote\": \"IOTA\",\n"
        + "      \"rate\": 2.4394847602076634513094884980\n"
        + "    },\n"
        + "    {\n"
        + "      \"time\": \"2019-06-23T17:31:26.8256249Z\",\n"
        + "      \"asset_id_quote\": \"NEO\",\n"
        + "      \"rate\": 0.0662514906585398171458857824\n"
        + "    },\n"
        + "    {\n"
        + "      \"time\": \"2019-06-23T17:31:26.9154152Z\",\n"
        + "      \"asset_id_quote\": \"BTC\",\n"
        + "      \"rate\": 0.0001062540887007728910218021\n"
        + "    },\n"
        + "    {\n"
        + "      \"time\": \"2019-06-23T17:31:26.9046730Z\",\n"
        + "      \"asset_id_quote\": \"LTC\",\n"
        + "      \"rate\": 0.0081645968643554359601750512\n"
        + "    },\n"
        + "    {\n"
        + "      \"time\": \"2019-06-23T17:31:26.9515547Z\",\n"
        + "      \"asset_id_quote\": \"ETH\",\n"
        + "      \"rate\": 0.0036773229961515239807322167\n"
        + "    }]\n"
        + "}";
  }
}