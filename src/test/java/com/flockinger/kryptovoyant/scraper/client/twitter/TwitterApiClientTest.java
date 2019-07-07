package com.flockinger.kryptovoyant.scraper.client.twitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flockinger.kryptovoyant.scraper.resources.JsonFileReader;
import com.flockinger.kryptovoyant.scraper.resources.ResourceReader;
import com.flockinger.kryptovoyant.scraper.resources.dto.TwitterId;
import com.google.common.collect.ImmutableList;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.api.UsersResources;


public class TwitterApiClientTest {


  private TwitterFactory factory;

  private Twitter twitter;
  private UsersResources usersResources;
  private ResourceReader reader = mock(ResourceReader.class);
  private TwitterClient client;

  @Before
  public void setup() {
    factory = mock(TwitterFactory.class);
    twitter = mock(Twitter.class);
    when(factory.getInstance()).thenReturn(twitter);
    usersResources = mock(UsersResources.class);
    when(twitter.users()).thenReturn(usersResources);

  }

  @Test
  public void testFetchStats_withValidIds_shouldFetchAll() throws Exception {
    TwitterId id1 = new TwitterId();
    id1.setId("877155737713209344");
    id1.setName("BTC");
    id1.setTwitterUrl("https://twitter.com/btc");
    when(reader.read(anyString(), any())).thenReturn(ImmutableList.of(id1));
    User user = mock(User.class);
    when(user.getFavouritesCount()).thenReturn(1);
    when(user.getFollowersCount()).thenReturn(100);
    when(user.getFriendsCount()).thenReturn(2);
    when(user.getListedCount()).thenReturn(3);
    when(user.getStatusesCount()).thenReturn(1000);
    when(user.getId()).thenReturn(877155737713209344L);
    ArrayList<User> users = new ArrayList<>();
    users.add(user);
    ResponseList<User> response = mock(ResponseList.class);
    when(response.stream()).thenReturn(users.stream());
    when(usersResources.lookupUsers(any(Long.class))).thenReturn(response);

    client = new TwitterApiClient(factory, reader);
    List<TwitterStats> stats = client.fetchStats();

    assertThat(stats).isNotNull().hasSize(1)
        .extracting("symbol", "followers", "favourites", "friends", "listed", "tweetCount")
        .contains(tuple("BTC", 100, 1, 2, 3, 1000));
    verify(reader).read(eq(TwitterId.RESOURCE), any());
    verify(usersResources).lookupUsers(eq(877155737713209344L));
  }

  @Test
  public void testFetchStats_withTwoEqualIds_shouldFetchAll() throws Exception {
    TwitterId id1 = new TwitterId();
    id1.setId("877155737713209344");
    id1.setName("BTC");
    id1.setTwitterUrl("https://twitter.com/btc");
    TwitterId id2 = new TwitterId();
    id2.setId("877155737713209344");
    id2.setName("BTC2");
    id2.setTwitterUrl("https://twitter.com/btc2");
    when(reader.read(anyString(), any())).thenReturn(ImmutableList.of(id1, id2));
    User user = mock(User.class);
    when(user.getFavouritesCount()).thenReturn(1);
    when(user.getFollowersCount()).thenReturn(100);
    when(user.getFriendsCount()).thenReturn(2);
    when(user.getListedCount()).thenReturn(3);
    when(user.getStatusesCount()).thenReturn(1000);
    when(user.getId()).thenReturn(877155737713209344L);
    ArrayList<User> users = new ArrayList<>();
    users.add(user);
    ResponseList<User> response = mock(ResponseList.class);
    when(response.stream()).thenReturn(users.stream());
    when(usersResources.lookupUsers(any(Long.class))).thenReturn(response);

    client = new TwitterApiClient(factory, reader);
    List<TwitterStats> stats = client.fetchStats();

    assertThat(stats).isNotNull().hasSize(2)
        .extracting("symbol", "followers", "favourites", "friends", "listed", "tweetCount")
        .contains(tuple("BTC", 100, 1, 2, 3, 1000),
            tuple("BTC2", 100, 1, 2, 3, 1000));
    verify(reader).read(eq(TwitterId.RESOURCE), any());
    verify(usersResources).lookupUsers(eq(877155737713209344L));
  }

  //Only Integration test to verify functioning
  //@Test
  public void testFetchStats_withAllStats_shouldFetchAllEasily() throws Exception {
    reader = new JsonFileReader(new ObjectMapper());

    client = new TwitterApiClient(factory, reader);

    List<TwitterStats> twitterStats = client.fetchStats();

    ObjectMapper mapper = new ObjectMapper();

    mapper.writerWithDefaultPrettyPrinter()
        .writeValue(new FileWriter("/root/twitterstats.json"), twitterStats);
  }

}