package ar.edu.utn.frsfco.garlan.mam.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

/**
 * Confguration for twitter social, add other social networks config here
 * 
 * <p><a href="SocialConfig.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Configuration
public class SocialConfig {

    @Autowired
    Environment env;

    private Twitter twitter;

    //Default Twitter object without authorization
    public SocialConfig() {
        if (twitter == null) {
            System.out.println(env);
            this.twitter = new TwitterTemplate(
                    "4oCawOSFo1EAuhdzBQG0Quwu3",
                    "sZ6zJbSVuclXUyv8y8KN3EASVverjUIvejNdCqOLDLtl1uXzH0"
            );
        }
    }

    /**
     * The simple twitter api representation, this is used without a login
     *
     * @return Twitter
     */
    @Bean
    @Qualifier("twitter")
    @Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
    public Twitter twitter() {
        return twitter;
    }

    @Bean
    @Qualifier("twitterConnectionFactory")
    public TwitterConnectionFactory connectionFactory() {
        return new TwitterConnectionFactory(
                "4oCawOSFo1EAuhdzBQG0Quwu3",
                 "sZ6zJbSVuclXUyv8y8KN3EASVverjUIvejNdCqOLDLtl1uXzH0"
        );
    }

    @Bean
    @Qualifier("oauthOperations")
    public OAuth1Operations oauth1Operations() {
        OAuth1Operations oauthOperations = connectionFactory().getOAuthOperations();

        return oauthOperations;
    }
}
