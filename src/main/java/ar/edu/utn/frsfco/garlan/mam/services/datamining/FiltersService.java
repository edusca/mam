package ar.edu.utn.frsfco.garlan.mam.services.datamining;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * Add the filters algorithms that are applied to the dataset.
 * 
 * <p><a href="FiltersService.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Service
@Qualifier(value = "filtersService")
public class FiltersService {
    
    /**
     * Convert string values to word vector. Most of the classifiers doesn't 
     * accept string attributes, because of that we must pass the text instance
     * to the respective word vector.
     * @param dataset current dataset
     * @return converted word vector dataset
     */
    public Instances applyStringToWordVector(Instances dataset) {
        Instances filteredDataset = dataset;
        
        try {
            StringToWordVector filter = new StringToWordVector();
            filter.setInputFormat(filteredDataset);
            filteredDataset = Filter.useFilter(filteredDataset, filter);
        } catch (Exception ex) {
            filteredDataset = null;
            Logger.getLogger(FiltersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return filteredDataset;
    }
    
    /**
     * Apply attribute selection for better performance of the results when
     * the dataset is built.
     * @param dataset current dataset
     * @return filtered dataset with the most relevant attributes
     */
    public Instances applyAttributeSelection(Instances dataset) {
        Instances filteredAttrs = dataset;
        filteredAttrs = applyStringToWordVector(filteredAttrs);
        try {
            AttributeSelection filter = new AttributeSelection();
            CfsSubsetEval evaluator = new CfsSubsetEval();
            BestFirst search = new BestFirst();
            
            filter.setEvaluator(evaluator);
            filter.setSearch(search);
            filter.setInputFormat(filteredAttrs);
            filteredAttrs = Filter.useFilter(filteredAttrs, filter);
        } catch (Exception ex) {
            filteredAttrs = null;
            Logger.getLogger(FiltersService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return filteredAttrs;
    }
}
