package ar.edu.utn.frsfco.garlan.mam.services.datamining;

import ar.edu.utn.frsfco.garlan.mam.services.TwitterService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

/**
 * TODO Comment of component here!
 * 
 * <p><a href="KMeansService.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Service
@Qualifier(value = "kmeansService")
public class KMeansService {
    private SimpleKMeans kmeansClassifier;
    
    public KMeansService() {
    }
    
    public void classify(Instances dataSource) {
        try {
            kmeansClassifier = new SimpleKMeans();
            
            kmeansClassifier.setPreserveInstancesOrder(true);
            kmeansClassifier.setNumClusters(5);
            kmeansClassifier.buildClusterer(dataSource);
        } catch (Exception ex) {
            Logger.getLogger(KMeansService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printAssignments() {
        try {
            //cluster number for each instance
            int i = 0;
            for (int clusterNum : kmeansClassifier.getAssignments()) {
                System.out.printf("Instance %d -> Cluster %d \n", i, clusterNum);
                i++;
                
            }
        } catch (Exception ex) {
            Logger.getLogger(KMeansService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printClustersSizes() {
        try {
            //cluster number for each instance
            int i = 0;
            for (int clusterSize : kmeansClassifier.getClusterSizes()) {
                System.out.printf("Cluster %d size: %d\n", i, clusterSize);
                i++;
                
            }
        } catch (Exception ex) {
            Logger.getLogger(KMeansService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public SimpleKMeans getClassifier() {
        return kmeansClassifier;
    }
}
