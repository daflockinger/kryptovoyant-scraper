package com.flockinger.kryptovoyant.scraper.client.metadata.dto;

import java.util.Date;

public class Coin {

  private String currencyName;
  private String symbol;
  private Date launchDate;
  private Long tradeCount;

  public Coin() {
  }


  public Coin(String currencyName, String symbol, Date launchDate, Long tradeCount) {
    this.currencyName = currencyName;
    this.symbol = symbol;
    this.launchDate = launchDate;
    this.tradeCount = tradeCount;
  }

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

  public String getCurrencyName() {
    return currencyName;
  }

  public void setCurrencyName(String currencyName) {
    this.currencyName = currencyName;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
}
