package ar.edu.utn.frsfco.garlan.mam.configuration;

import ar.edu.utn.frsfco.garlan.mam.websocket.WebSocketLiterals;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Configure the web socket for spring using stomp messages
 * 
 * <p><a href="WebSocketConfig.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(WebSocketLiterals.WS_TWITTER_GENERIC_TOPIC);
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry ser) {
        ser.addEndpoint(WebSocketLiterals.WS_SAVE_TWEETS_BY_SCREEN_NAME_ENDPOINT)
                .withSockJS();
    }
}
