package com.flockinger.kryptovoyant.scraper.resources.dto;

public class CoinInfo implements Resourceable {

  public final static String RESOURCE = "/links/CoinInfos.json";

  private String symbol;
  private String website;
  private String sources;
  private String documentation;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getSources() {
    return sources;
  }

  public void setSources(String sources) {
    this.sources = sources;
  }

  public String getDocumentation() {
    return documentation;
  }

  public void setDocumentation(String documentation) {
    this.documentation = documentation;
  }
}
