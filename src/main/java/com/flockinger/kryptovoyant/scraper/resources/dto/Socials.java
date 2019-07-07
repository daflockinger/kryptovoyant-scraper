package com.flockinger.kryptovoyant.scraper.resources.dto;

public class Socials implements Resourceable {

  public final static String RESOURCE = "/links/SocialLinks.json";

  private String symbol;
  private String twitter;
  private String reddit;
  private String facebook;
  private String github;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getTwitter() {
    return twitter;
  }

  public void setTwitter(String twitter) {
    this.twitter = twitter;
  }

  public String getReddit() {
    return reddit;
  }

  public void setReddit(String reddit) {
    this.reddit = reddit;
  }

  public String getFacebook() {
    return facebook;
  }

  public void setFacebook(String facebook) {
    this.facebook = facebook;
  }

  public String getGithub() {
    return github;
  }

  public void setGithub(String github) {
    this.github = github;
  }
}
