package ar.edu.utn.frsfco.garlan.mam.configuration;

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Confguration for mongo access
 * 
 * <p><a href="MongoDBConfig.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Configuration
@PropertySource("classpath:mongo.properties")
@EnableMongoRepositories(basePackages = {"ar.edu.utn.frsfco.garlan.mam.repositories"})
public class MongoDBConfig extends AbstractMongoConfiguration{
    
    @Autowired
    Environment env;
    
    @Bean
    @Override
    @Qualifier(value = "mongoTemplate")
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongo(), getDatabaseName());
        
        return mongoTemplate;
    }

    @Override
    protected String getDatabaseName() {
        return env.getProperty("database.name");
    }

    @Override
    public MongoClient mongo() throws Exception {
        MongoClient mongoClient = new MongoClient(
                env.getProperty("database.url"),
                env.getProperty("database.port", Integer.class)
        );
        mongoClient.setWriteConcern(WriteConcern.SAFE);
        return mongoClient;
    }
}
