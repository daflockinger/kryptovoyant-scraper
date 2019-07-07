package com.flockinger.kryptovoyant.scraper.resources.dto;

public class TwitterId {

  public final static String RESOURCE = "/links/TwitterIds.json";

  private String id;
  private String name;
  private String twitterUrl;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTwitterUrl() {
    return twitterUrl;
  }

  public void setTwitterUrl(String twitterUrl) {
    this.twitterUrl = twitterUrl;
  }
}
