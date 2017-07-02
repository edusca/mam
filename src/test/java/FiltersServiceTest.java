
import ar.edu.utn.frsfco.garlan.mam.configuration.AppConfig;
import ar.edu.utn.frsfco.garlan.mam.services.TwitterService;
import ar.edu.utn.frsfco.garlan.mam.services.datamining.FiltersService;
import ar.edu.utn.frsfco.garlan.mam.services.datamining.InMemoryDataSource;
import ar.edu.utn.frsfco.garlan.mam.services.datamining.KMeansService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

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
public class FiltersServiceTest {
    @Autowired
    TwitterService twitterService;
    @Autowired
    InMemoryDataSource inMemoryDataSource;
    @Autowired
    FiltersService filtersService;
    
    @Test
    public void showKMeansAssignmentsUsingAttributeSelection() {
        inMemoryDataSource.initDataSource();

        Instances featuresSelected =  filtersService.applyAttributeSelection(
                inMemoryDataSource.getFilteredDataSet()
        );
        
        System.out.println(featuresSelected);
        Assert.assertNotNull("There are not instances in the dataset", featuresSelected);
    }
    
    @Test
    public void showKMeansAssignmentsUsingStringToWordVector() {
        inMemoryDataSource.initDataSource();

        Instances featuresSelected =  filtersService.applyStringToWordVector(
                inMemoryDataSource.getFilteredDataSet()
        );
        
        for (int j = 0; j < featuresSelected.numInstances(); j++) {
		Instance currentInstance = featuresSelected.instance(j);
                System.out.println(currentInstance);
	}
        
        Assert.assertNotNull("There are not instances in the dataset", featuresSelected);
    }
}
