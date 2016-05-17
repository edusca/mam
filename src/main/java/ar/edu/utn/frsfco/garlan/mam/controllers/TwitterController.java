package ar.edu.utn.frsfco.garlan.mam.controllers;

import ar.edu.utn.frsfco.garlan.mam.models.TwitterMessage;
import ar.edu.utn.frsfco.garlan.mam.services.TwitterService;
import ar.edu.utn.frsfco.garlan.mam.utils.JSONResponse;
import ar.edu.utn.frsfco.garlan.mam.websocket.WebSocketLiterals;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

/**
 * Twitter controller. All the REST controllers for twitter views here
 * 
 * <p><a href="TwitterController.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Controller
public class TwitterController {
    private static final Logger logger = Logger.getLogger(TwitterController.class);  
    
    @Autowired
    @Qualifier("twitterService") 
    TwitterService twitterService;
    
    public TwitterController() {
    }
    
    @RequestMapping(value = "/twitter/authorize", method = RequestMethod.GET)
    public String authorizeApp(HttpServletRequest request) {
        String baseUrl = String.format(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
        return "redirect:" + twitterService.authorizeURL(baseUrl);
    }
    
    @RequestMapping(value = "/twitter/twitter-auth-callback", method = RequestMethod.GET)
    public String twitterAuthorizeCallback(@RequestParam("oauth_token") String oauthToken, @RequestParam("oauth_verifier") String oauthVerifier) {
        boolean isTheProvideAuthorizeTheService = twitterService.twitterAuthURLCallback(oauthToken, oauthVerifier);
        
        if (isTheProvideAuthorizeTheService) {
            return "redirect:/";
        } else {
            //return some message telling that the oauth process fail
            return "";
        }
    }
    
    @RequestMapping(value = "/twitter/tweets", method = RequestMethod.GET)
    public String searchTweets() {
        return "twitter/tweets";
    }
	
    @RequestMapping(value = "/twitter/tweets", params = "screenName", method = RequestMethod.GET)
    public String searchTweetsByUser(@RequestParam("screenName") String screenName, Model model) {
        List<Tweet> tweets = twitterService.getTweetsFromUser(screenName, 20);
        
        model.addAttribute("tweets", tweets);
        model.addAttribute("screenName", screenName);
        
        return "twitter/tweets";
    }
    
    @RequestMapping(value = "/twitter/followers", method = RequestMethod.GET)
    public String searchFollowers() {      
        return "twitter/followers";
    }
    
    @RequestMapping(value = "/followers", params = "screenName", method = RequestMethod.GET)
    public String searchFollowersByUser(@RequestParam("screenName") String screenName, Model model) {
        List<TwitterProfile> followers = twitterService.getFollowersFromUser(screenName);
        
        model.addAttribute("followers", followers);
        model.addAttribute("screenName", screenName);
        
        return "twitter/followers";
    }
    
    @RequestMapping(value = "/twitter/save-tweet", method = RequestMethod.GET)
    public String saveTweet(@RequestParam("tweetId")long tweetId) {
        twitterService.saveTweet(twitterService.getTweet(tweetId));
        return "redirect:/";
    }
    
    @RequestMapping(value = "/twitter/save-tweets-by-screen-name", method = RequestMethod.GET)
    public String searchTweetsByScreenName() {
        return "twitter/save-tweets-by-screen-name";
    }
    
    @RequestMapping(value = "/twitter/save-tweets-by-words", method = RequestMethod.GET)
    public String searchTweetsByWords() {
        return "twitter/save-tweets-by-words";
    }
    
    @RequestMapping(value = "/twitter/save-tweets-by-words", method = RequestMethod.POST)
    public @ResponseBody JSONResponse saveTweetsBySearch(@RequestParam("query") String query) {
        twitterService.saveTweetsByQuery(query);
        
        return new JSONResponse();
    }
    
    @RequestMapping(value = "/twitter/saved-tweets", method = RequestMethod.GET)
    public String searchSavedTweets() {
        return "twitter/saved-tweets";
    }
	
    @RequestMapping(value = "/twitter/saved-tweets", params = "textValues", method = RequestMethod.GET)
    public String searchSavedTweetsByText(@RequestParam("textValues") String text, Model model) {
        List<TwitterMessage> tweets = twitterService.getTweetsByTextSearch(text);
        
        model.addAttribute("tweets", tweets);
        
        return "twitter/saved-tweets";
    }
}