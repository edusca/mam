package ar.edu.utn.frsfco.garlan.mam.services;

import ar.edu.utn.frsfco.garlan.mam.models.Message;
import ar.edu.utn.frsfco.garlan.mam.models.TwitterMessage;
import ar.edu.utn.frsfco.garlan.mam.repositories.MessageRepository;
import ar.edu.utn.frsfco.garlan.mam.websocket.WebSocketLiterals;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.connect.Connection;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.FriendOperations;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import weka.core.Debug;

/**
 * Service for manage the logic of Twitter
 *
 * <p>
 * <a href="TwitterService.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Service
@Qualifier("twitterService")
public class TwitterService {
    private static final Logger logger = Logger.getLogger(TwitterService.class); 
    
    private static final int TWEETS_QUANTITY_FOR_ONE_REQUEST = 200;
    /*
     * By reading the documentation, the api returns a maximum of 100 for page, 
     * so by muliply this number for the 450 request per application, we can
     * query for 4500 with a loop tweets per 15 minutes windows
     */
    private static final int TWEETS_QUANTITY_FOR_SEARCH_QUERY = 100;
    private static final int ITERATION_FOR_SEARCH_QUERY = 100;
    
    private Twitter twitter;
    private final TwitterConnectionFactory twitterConnectionFactory;
    private final OAuth1Operations oauthOperations;
    
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public TwitterService(
            @Qualifier("twitter") Twitter twitter,
            @Qualifier("twitterConnectionFactory") TwitterConnectionFactory twitterConnectionFactory,
            @Qualifier("oauthOperations") OAuth1Operations oauthOperations
    ) {
        this.twitter = twitter;
        this.twitterConnectionFactory = twitterConnectionFactory;
        this.oauthOperations = oauthOperations;
    }

    /**
     * @param screenName screen name of the selected user
     * @param pageSize return the quantity of tweets. Max: 200 (I think?)
     * @return the tweets from the selected user
     */
    public List<Tweet> getTweetsFromUser(String screenName, int pageSize) {
        TimelineOperations timelineOperations = twitter.timelineOperations();
        return timelineOperations.getUserTimeline(screenName, pageSize);
    }

    /**
     * @param screenName screen name of the selected user
     * @return the followers of the selected user. Returns only the first twenty
     */
    public List<TwitterProfile> getFollowersFromUser(String screenName) {
        FriendOperations friendOperations = twitter.friendOperations();
        return friendOperations.getFollowers(screenName);
    }

    /**
     * @param screenName screen name of the selected user
     * @param cursor
     * @return the friends of the selected user. Can be up to 5000 followers (using a pager)
     */
    public List<TwitterProfile> getAllFollowersFromUser(String screenName) {
        CursoredList<Long> cursoredList;
        cursoredList = twitter.friendOperations().getFollowerIds(screenName);
        List<TwitterProfile> followers = getFollowers(cursoredList);
        return followers;
    }

    /**
     * Authorize the url for fetch the oauth_access token from twitter
     *
     * @param baseURL base url of this app
     * @return the authorized url
     */
    public String authorizeURL(String baseURL) {
        OAuthToken requestToken = oauthOperations.fetchRequestToken(baseURL + "/twitter/twitter-auth-callback", null);
        String authorizeUrl = oauthOperations.buildAuthorizeUrl(requestToken.getValue(), OAuth1Parameters.NONE);
        return authorizeUrl;
    }

    /**
     * Create a connection once twitter has validate the url and return the
     * oauth access token
     *
     * @param oauthToken returned from twitter
     * @param oauthVerifier returned from twitter
     * @return true or false
     */
    public boolean twitterAuthURLCallback(String oauthToken, String oauthVerifier) {
        OAuthToken accessToken = oauthOperations.exchangeForAccessToken(
                new AuthorizedRequestToken(new OAuthToken(oauthToken, ""), oauthVerifier), null);
        Connection<Twitter> connection = twitterConnectionFactory.createConnection(accessToken);
        this.twitter = connection.getApi();

        return true;
    }

    /**
     * @param tweetId
     * @return the tweet of the current tweet id
     */
    public Tweet getTweet(long tweetId) {
        return twitter.timelineOperations().getStatus(tweetId);
    }
    
    /**
     * TODO we can use the @Async annotation for run this command in background
     * Save a list of tweets
     * @param tweets 
     */
    public void saveTweetsList(List<Tweet> tweets) {
        for (Tweet tweet : tweets) {
            saveTweet(tweet);
        }
    }

    /**
     * Save a tweet in the database like a document
     *
     * @param tweet
     */
    public void saveTweet(Tweet tweet) {
        TwitterMessage message = new TwitterMessage();
        //add basic message data
        message.setId(null);
        message.setUserLocation(tweet.getUser().getLocation());
        message.setText(tweet.getText());
        //add twitter message data
        message.setUserScreenName(tweet.getUser().getScreenName());
        message.setUserId(tweet.getUser().getId());
        message.setMessageId(tweet.getId());
        message.setTextLanguageCode(tweet.getLanguageCode());
        message.setToUserId(tweet.getToUserId());
        message.setFromUserId(tweet.getFromUserId());
        messageRepository.save(message);
    }

    /**
     * This method is executed when the client send a request for fill the
     * database with their desired users, then with a web socket opened messages
     * are sent to the client for update the status of the process
     *
     * @param screenNames desired users list
     */
    public void wsSaveUsersTweetsByScreenName(List<String> screenNames) {
        List<TwitterProfile> profiles;
        int usersCounter = 1;
        
        try {
            //For each screen name
            for (String screenName : screenNames) {
                messagingTemplate.convertAndSend(WebSocketLiterals.WS_SAVE_TWEETS_BY_SCREEN_NAME_TOPIC,
                        new TextMessage("Actual usuario: " + screenName)
                );
                //Fetch the current followers
                profiles = getAllFollowersFromUser(
                        screenName
                );
                //Add to the list the current profile: fetch from current screen name
                profiles.add(getProfile(screenName));

                messagingTemplate.convertAndSend(WebSocketLiterals.WS_SAVE_TWEETS_BY_SCREEN_NAME_TOPIC,
                        new TextMessage("Cantidad de seguidores: " + profiles.size())
                );

                //For each profile fetch the last tweets
                for (TwitterProfile profile : profiles) {
                    messagingTemplate.convertAndSend(WebSocketLiterals.WS_SAVE_TWEETS_BY_SCREEN_NAME_TOPIC,
                            new TextMessage("Actual follower: " + profile.getScreenName() + " de " + screenName)
                    );
                    //Save the current fetched tweets
                    List<Tweet> tweets = getTweetsFromUser(
                            profile.getScreenName(),
                            TwitterService.TWEETS_QUANTITY_FOR_ONE_REQUEST
                    );
                    
                    saveTweetsList(tweets);
                    
                    messagingTemplate.convertAndSend(WebSocketLiterals.WS_SAVE_TWEETS_BY_SCREEN_NAME_TOPIC,
                            new TextMessage("Guardando tweets de: " + profile.getScreenName())
                    );
                }

                usersCounter++;
                
                if(!isTheLastUser(screenNames.size(), usersCounter)) {
                    messagingTemplate.convertAndSend(WebSocketLiterals.WS_SAVE_TWEETS_BY_SCREEN_NAME_TOPIC,
                            new TextMessage("Esperando 15 minutos....(Para no exceder los límtes de la API de Twitter)")
                    );
                    
                    //Wait 15 minutes for acomplish the twitter api rate limits per window
                    TimeUnit.MINUTES.sleep(15);
                }
            }

            messagingTemplate.convertAndSend(WebSocketLiterals.WS_SAVE_TWEETS_BY_SCREEN_NAME_TOPIC,
                    new TextMessage("Fin del proceso.")
            );
        } catch (InterruptedException | RateLimitExceededException ex) {
            messagingTemplate.convertAndSend(WebSocketLiterals.WS_SAVE_TWEETS_BY_SCREEN_NAME_TOPIC,
                    new TextMessage(ex.getMessage())
            );
        }
    }
    
    /**
     * @param totalUsers 
     * @param currentCountOfUsers
     * @return true if is the last user in the collection else otherwise
     */
    private boolean isTheLastUser(int totalUsers, int currentCountOfUsers) {
        if(currentCountOfUsers <= totalUsers) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param screenName for fetch a twitter profile
     * @return the twitter profile
     */
    public TwitterProfile getProfile(String screenName) {
        UserOperations op = twitter.userOperations();
        return op.getUserProfile(screenName);
    }

    /**
     * Through a cursored list, fetch all the followers of a screen name
     */
    private List<TwitterProfile> getFollowers(CursoredList<Long> cursoredList) {
        List<TwitterProfile> followers = new ArrayList<>();
        for (int i = 0; i < cursoredList.size(); i += 100) {
            followers.addAll(getProfiles(cursoredList, i));
        }
        return followers;
    }

    private List<TwitterProfile> getProfiles(CursoredList<Long> cursoredList, int start) {
        int end = Math.min(start + 100, cursoredList.size());
        Long[] currentList = cursoredList.subList(start, end).toArray(new Long[0]);
        long[] ids = ArrayUtils.toPrimitive(currentList);
        List<TwitterProfile> profiles = twitter.userOperations().getUsers(ids);
        return profiles;
    }
    
    /**
     * @return a list of all messages in the database. This can overflow the
     * memory, TODO Replace this with map-reduce
     */
    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }
    
    /**
     * Important: The tweets are of the past seven days, the documentation here
     * @param query 
     * @see https://dev.twitter.com/rest/public/search
     */
    public void saveTweetsByQuery(String query) {
        SearchParameters sp = new SearchParameters(query);
        sp.count(TWEETS_QUANTITY_FOR_SEARCH_QUERY);
       
        for(int i = 0; i < ITERATION_FOR_SEARCH_QUERY; i++) {
            SearchResults results = 
                twitter
                .searchOperations()
                .search(sp);
            saveTweetsList(results.getTweets());
        }
    }
    
    /**
     * Using the text search of mongo we can retrieve specific data
     * about the data that the user wants to find
     * @param text this can be a word, a phrase or a list of words (separated by coma)
     * @return twitter messages by text search
     */
    public List<TwitterMessage> getTweetsByTextSearch(String text) {
        //Set the index in the field text of the twitter message
        TextIndexDefinition index = new TextIndexDefinition.TextIndexDefinitionBuilder()
                .onField("text")
                .build();
        mongoTemplate.indexOps(TwitterMessage.class).ensureIndex(index);
        
        //Build the criteria with the text
        TextCriteria criteria = TextCriteria.forDefaultLanguage();
        criteria.matchingAny(text);
        
        //TODO create a pager with the PageRequest class
        Query query = TextQuery.query(criteria);
        
        return mongoTemplate.find(query, TwitterMessage.class);
    }
}
