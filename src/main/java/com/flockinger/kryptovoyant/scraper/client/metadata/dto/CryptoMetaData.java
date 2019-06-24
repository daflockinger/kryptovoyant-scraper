package com.flockinger.kryptovoyant.scraper.client.metadata.dto;

import java.io.Serializable;
import java.util.List;

public class CryptoMetaData implements Serializable {

  private String symbol;
  private List<ExchangeRate> exchangeRate;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public List<ExchangeRate> getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(
      List<ExchangeRate> exchangeRate) {
    this.exchangeRate = exchangeRate;
  }
}
