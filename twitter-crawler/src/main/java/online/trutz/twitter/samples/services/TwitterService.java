package online.trutz.twitter.samples.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import online.trutz.twitter.samples.model.Tweet;
import online.trutz.twitter.samples.model.TweetListData;
import online.trutz.twitter.samples.model.User;
import online.trutz.twitter.samples.model.UserData;
import online.trutz.twitter.samples.model.UserListData;

@Service
public class TwitterService {

    private static final String TWITTER_API = "https://api.twitter.com";

    private static final String TWITTER_API_V2 = TWITTER_API + "/2";

    private static final Logger log = LoggerFactory.getLogger(TwitterService.class);

    private final OAuth2AccessToken accessToken;

    private final RestTemplate restTemplate;

    TwitterService(OAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
        this.restTemplate = new RestTemplate();
    }

    public User usersByUsername(String username) {
        return restTemplate.exchange(TWITTER_API_V2 + "/users/by/username/" + username, HttpMethod.GET,
                createHttpEntity(), UserData.class).getBody().data();
    }

    public Stream<User> following(Long id) {
        var streamBuilder = Stream.<User>builder();
        var nextToken = (String) null;
        do {
            var paginationToken = Optional.ofNullable(nextToken).map(token -> "?pagination_token=" + token).orElse("");
            var body = restTemplate.exchange(TWITTER_API_V2 + "/users/" + id + "/following" + paginationToken,
                    HttpMethod.GET, createHttpEntity(), UserListData.class).getBody();
            body.data().forEach(streamBuilder::accept);
            nextToken = Objects.requireNonNull(body.meta()).next_token();
            log.info("Next token: {}", nextToken);
        } while (nextToken != null);
        return streamBuilder.build();
    }

    public Stream<Tweet> tweets(String authorId) {
        var streamBuilder = Stream.<Tweet>builder();
        var nextToken = (String) null;
        do {
            var paginationToken = Optional.ofNullable(nextToken).map(token -> "&pagination_token=" + token).orElse("");
            var body = restTemplate.exchange(TWITTER_API_V2 + "/users/" + authorId
                    + "/tweets?max_results=100&tweet.fields=created_at,public_metrics,source,text,author_id"
                    + paginationToken, HttpMethod.GET, createHttpEntity(), TweetListData.class).getBody();
            body.data().forEach(streamBuilder::accept);
            nextToken = Objects.requireNonNull(body.meta()).next_token();
            log.info("Next token: {}", nextToken);
        } while (nextToken != null);
        return streamBuilder.build();
    }

    private HttpEntity<Object> createHttpEntity() {
        return createHttpEntity(null);
    }

    private <B> HttpEntity<B> createHttpEntity(B body) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        return new HttpEntity<B>(body, headers);
    }

}
