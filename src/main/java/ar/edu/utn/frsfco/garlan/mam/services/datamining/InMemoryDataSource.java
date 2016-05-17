package ar.edu.utn.frsfco.garlan.mam.services.datamining;

import ar.edu.utn.frsfco.garlan.mam.models.Message;
import ar.edu.utn.frsfco.garlan.mam.models.TwitterMessage;
import ar.edu.utn.frsfco.garlan.mam.services.TwitterService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * This class load the data from the database and generate a weka data source
 * for use with the learning algorithms
 * 
 * <p><a href="InMemoryDataSource.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Service
@Qualifier(value = "inMemoryDataSource")
public class InMemoryDataSource {
    public static final String ALL_MESSAGES_DATA_SET = "ALL_MESSAGES_DATA_SET";
    public static final String ATTR_ID = "id";
    public static final String ATTR_MESSAGE = "message";
    
    private Attribute idAttr;
    private Attribute messageAttr;
    
    private Instances dataSet;
    private Instances filteredDataSet;
    
    @Autowired
    private FiltersService filtersService;
    
    public void initDataSource() {
        idAttr = new Attribute(ATTR_ID, (FastVector)null);
        messageAttr = new Attribute(ATTR_MESSAGE, (FastVector)null);
        
        FastVector fastVector = new FastVector();
        fastVector.addElement(idAttr);
        fastVector.addElement(messageAttr);
        dataSet = new Instances(ALL_MESSAGES_DATA_SET, fastVector, 0);
    }
    
    /**
     * Fill the dataset with domain messages
     * @param messages all the messages for the current dataset
     */
    public void fillDataSet(List<Message> messages) {
        try {
            for (Message message : messages) {
                TwitterMessage currentMessage = (TwitterMessage) message;
                
                Instance newInstance = new Instance(2);
                newInstance.setValue(idAttr, String.valueOf(currentMessage.getId()));
                newInstance.setValue(messageAttr, currentMessage.getText());
                
                dataSet.add(newInstance);
            }
            filteredDataSet = dataSet;
            filteredDataSet = filtersService.applyStringToWordVector(filteredDataSet);
        } catch (Exception ex) {
            Logger.getLogger(InMemoryDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Instances getDataSet() {
        return dataSet;
    }
    
    public Instances getFilteredDataSet() {
        return filteredDataSet;
    }
}
