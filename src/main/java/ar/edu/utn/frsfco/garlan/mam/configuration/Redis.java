package ar.edu.utn.frsfco.garlan.mam.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import redis.clients.jedis.Jedis;

/**
 * Redis instance configuration.
 * <p>
 * <p>
 * <a href="Redis.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Configuration
public class Redis {

    @Bean
    @Qualifier(value = "redis")
    @Scope(value = "singleton")
    public Jedis getRedisClient() {
        // TODO refactor to properties file
        return new Jedis("redis");
    }
}
