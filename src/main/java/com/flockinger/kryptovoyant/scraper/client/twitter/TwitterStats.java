package com.flockinger.kryptovoyant.scraper.client.twitter;

public class TwitterStats {

  private String symbol;
  /**
   * Number of Followers.
   */
  private Integer followers;
  /**
   * The number of Tweets this user has liked in the account’s lifetime
   */
  private Integer favourites;
  /**
   * Number of friends aka followings.
   */
  private Integer friends;
  /**
   * Number of lists the User is on.
   */
  private Integer listed;
  /**
   * The number of Tweets (including retweets) issued by the user.
   */
  private Integer tweetCount;


  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Integer getFollowers() {
    return followers;
  }

  public void setFollowers(Integer followers) {
    this.followers = followers;
  }

  public Integer getFavourites() {
    return favourites;
  }

  public void setFavourites(Integer favourites) {
    this.favourites = favourites;
  }

  public Integer getFriends() {
    return friends;
  }

  public void setFriends(Integer friends) {
    this.friends = friends;
  }

  public Integer getListed() {
    return listed;
  }

  public void setListed(Integer listed) {
    this.listed = listed;
  }

  public Integer getTweetCount() {
    return tweetCount;
  }

  public void setTweetCount(Integer tweetCount) {
    this.tweetCount = tweetCount;
  }
}
