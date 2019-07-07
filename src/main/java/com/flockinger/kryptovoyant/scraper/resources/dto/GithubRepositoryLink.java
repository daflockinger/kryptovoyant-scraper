package com.flockinger.kryptovoyant.scraper.resources.dto;

public class GithubRepositoryLink implements Resourceable {

  public final static String RESOURCE = "/links/GoodGithubRepoLinks.json";

  private String symbol;
  private String sources;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getSources() {
    return sources;
  }

  public void setSources(String sources) {
    this.sources = sources;
  }
}
