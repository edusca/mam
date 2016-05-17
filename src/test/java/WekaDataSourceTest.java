
import ar.edu.utn.frsfco.garlan.mam.configuration.AppConfig;
import ar.edu.utn.frsfco.garlan.mam.services.TwitterService;
import ar.edu.utn.frsfco.garlan.mam.services.datamining.InMemoryDataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import weka.core.Instances;

/**
 * Test methods related with the weka data source building
 * 
 * <p><a href="WekaDataSourceTest.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class WekaDataSourceTest {
    @Autowired
    TwitterService twitterService;
    @Autowired
    InMemoryDataSource inMemoryDataSource;
    
    @Test
    public void testFillingTheDataSet() {
        Instances dataSet = null;
        
        inMemoryDataSource.initDataSource();
        inMemoryDataSource.fillDataSet(twitterService.findAllMessages());
        dataSet = inMemoryDataSource.getDataSet();
        
        if(dataSet == null) {
            Assert.fail("The dataset is null");
        } else {
            System.out.println(dataSet);
        }
    }

}
