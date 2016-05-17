
import ar.edu.utn.frsfco.garlan.mam.configuration.AppConfig;
import ar.edu.utn.frsfco.garlan.mam.configuration.MyWebAppInitializer;
import ar.edu.utn.frsfco.garlan.mam.models.Message;
import ar.edu.utn.frsfco.garlan.mam.services.TwitterService;
import java.util.List;
import javax.inject.Qualifier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * TODO Comment of component here!
 * 
 * <p><a href="TwitterServiceTest.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class TwitterServiceTest {
    @Autowired
    TwitterService twitterService;

    @Test
    public void findAllMessagesTest() {
        List<Message> allMessages = twitterService.findAllMessages();
        if(allMessages.size() > 0) {
            System.out.println("Hay mensaje en la base de datos");
        } else {
            Assert.fail(""+allMessages.size());
        }
    }
}
