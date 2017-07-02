
import ar.edu.utn.frsfco.garlan.mam.configuration.AppConfig;
import ar.edu.utn.frsfco.garlan.mam.models.Message;
import ar.edu.utn.frsfco.garlan.mam.models.TwitterMessage;
import ar.edu.utn.frsfco.garlan.mam.models.redis.ClusterAssignment;
import ar.edu.utn.frsfco.garlan.mam.services.TwitterService;
import ar.edu.utn.frsfco.garlan.mam.services.datamining.InMemoryDataSource;
import ar.edu.utn.frsfco.garlan.mam.services.datamining.KMeansService;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import weka.core.Instance;

/**
 * TODO Comment of component here!
 * 
 * <p><a href="ClusteringAlgorithmsTests.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class ClusteringAlgorithmsTests {
    @Mock
    TwitterService twitterService;

    @InjectMocks
    @Autowired
    InMemoryDataSource inMemoryDataSource;
    @Autowired
    KMeansService kmeansService;

    // TODO we are using this for test before run the application, convert into a really test
    @Test
    public void showKMeansAssignments() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(twitterService.getAllTweetsThatAreNotRetweet()).thenReturn(getTestMessages());

        inMemoryDataSource.initDataSource();
        
        Collection<ClusterAssignment> assignments = kmeansService.classify(2, 10, 10);
    }
    
    public List<TwitterMessage> getTestMessages() {
        ArrayList<TwitterMessage> messages = new ArrayList<>();

        for(String textMessage: this.getTextMessages()) {
            TwitterMessage newMessage = new TwitterMessage();
            newMessage.setId(getSimulatedObjectId());
            newMessage.setText(textMessage);
            messages.add(newMessage);
        }
        
        return messages;
    }
    
    public String[] getTextMessages() {
        return new String[] {
          "Los libros no se reemplazan por e-books.",
          "Existen autos que son eléctricos",
          "Ya he leído todos los libros que me han regalado. Hay muchos de ellos que son costosos.",
          "Los autos deportivos son los mas lindos que existen."
        };
    }

    private String getSimulatedObjectId() {
        SecureRandom random = new SecureRandom();

        return new BigInteger(130, random).toString(32);
    }
}
