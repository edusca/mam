package ar.edu.utn.frsfco.garlan.mam.controllers;

import ar.edu.utn.frsfco.garlan.mam.services.TwitterService;
import ar.edu.utn.frsfco.garlan.mam.websocket.WebSocketLiterals;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;

/**
 * Controllers for manage the web sockets requests
 * 
 * <p><a href="TwitterControllerWS.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Controller
public class TwitterControllerWS {
    private static final Logger logger = LogManager.getLogger(TwitterControllerWS.class);
    
    @Autowired
    @Qualifier("twitterService") 
    TwitterService twitterService;

    public TwitterControllerWS() {
    }
    
    @MessageMapping(WebSocketLiterals.WS_SAVE_TWEETS_BY_SCREEN_NAME_ENDPOINT+"/{screenNames}")
    public void wsSaveTweets(@DestinationVariable String screenNames) {
        List<String> screenNamesList = Arrays.asList(screenNames.split("-"));
        
        //call the process in the service
        twitterService.wsSaveUsersTweetsByScreenName(screenNamesList);
    }
    
    /**
     * @return a message for the initialization process of the web socket
     * connection
     */
    @SubscribeMapping(WebSocketLiterals.WS_SAVE_TWEETS_BY_SCREEN_NAME_TOPIC)
    public TextMessage suscribed() {
        return new TextMessage("Proceso inicializado, verá actualizaciones del proceso"
                + " en la pantalla");
    }
}
