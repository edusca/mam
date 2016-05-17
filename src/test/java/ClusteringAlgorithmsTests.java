
import ar.edu.utn.frsfco.garlan.mam.configuration.AppConfig;
import ar.edu.utn.frsfco.garlan.mam.services.TwitterService;
import ar.edu.utn.frsfco.garlan.mam.services.datamining.InMemoryDataSource;
import ar.edu.utn.frsfco.garlan.mam.services.datamining.KMeansService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
    @Autowired
    TwitterService twitterService;
    @Autowired
    InMemoryDataSource inMemoryDataSource;
    @Autowired
    KMeansService kmeansService;
    
    @Test
    public void showKMeansAssignments() {
        inMemoryDataSource.initDataSource();
        inMemoryDataSource.fillDataSet(twitterService.findAllMessages());
        
        kmeansService.classify(inMemoryDataSource.getFilteredDataSet());
        //kmeansService.printAssignments();
        kmeansService.printClustersSizes();
    }
}
