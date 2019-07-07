package com.flockinger.kryptovoyant.scraper.client.twitter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flockinger.kryptovoyant.scraper.exception.ApiClientException;
import com.flockinger.kryptovoyant.scraper.resources.ResourceReader;
import com.flockinger.kryptovoyant.scraper.resources.dto.TwitterId;
import com.google.common.collect.Lists;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

@Component
public class TwitterApiClient implements TwitterClient {

  private final Twitter twitter;
  private List<TwitterId> twitterIds;

  private final static Integer PER_REQUEST_BATCH_SIZE = 100;

  public TwitterApiClient(TwitterFactory factory, ResourceReader reader) {
    this.twitter = factory.getInstance();
    this.twitterIds = reader.read(TwitterId.RESOURCE, new TypeReference<>() {
    });
  }

  @Override
  public List<TwitterStats> fetchStats() {
    Map<String, List<TwitterId>> idsToSymboles = twitterIds.stream()
        .collect(Collectors.groupingBy(TwitterId::getId, Collectors.toList()));

    List<List<Long>> idBatches = Lists.partition(twitterIds.stream()
        .map(TwitterId::getId)
        .map(NumberUtils::createLong)
        .collect(Collectors.toList()), PER_REQUEST_BATCH_SIZE);

    return idBatches.stream()
        .map(it -> it.stream().collect(Collectors.toSet())
            .stream().mapToLong(id -> id).toArray())
        .map(this::lookupUsers)
        .flatMap(Collection::stream)
        .map(it -> extractSymbolesFromId(it, idsToSymboles))
        .flatMap(Collection::stream)
        .map(this::mapToStats)
        .collect(Collectors.toList());
  }

  private List<User> lookupUsers(long[] userIds) {
    List<User> users;
    try {
      users = twitter.users().lookupUsers(userIds);
    } catch (TwitterException e) {
      throw new ApiClientException("Could not look up Twitter Users!", e);
    }
    return users;
  }

  private List<Tuple2<User, String>> extractSymbolesFromId(User user,
      Map<String, List<TwitterId>> idsToSymboles) {
    return idsToSymboles.get(Long.toString(user.getId())).stream()
        .map(it -> Tuple.of(user, it.getName()))
        .collect(Collectors.toList());
  }

  private TwitterStats mapToStats(Tuple2<User, String> userAndSymbol) {
    TwitterStats stats = new TwitterStats();
    stats.setSymbol(userAndSymbol._2);
    stats.setFavourites(userAndSymbol._1.getFavouritesCount());
    stats.setFollowers(userAndSymbol._1.getFollowersCount());
    stats.setFriends(userAndSymbol._1.getFriendsCount());
    stats.setListed(userAndSymbol._1.getListedCount());
    stats.setTweetCount(userAndSymbol._1.getStatusesCount());
    return stats;
  }

}
