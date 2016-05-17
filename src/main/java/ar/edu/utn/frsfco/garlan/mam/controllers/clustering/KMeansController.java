package ar.edu.utn.frsfco.garlan.mam.controllers.clustering;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Manage the views and the ui calls of the k-means clustering algorithm
 * 
 * <p><a href="KMeansController.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Controller
public class KMeansController {
    
    @RequestMapping(value = "/clustering/k-means", method = RequestMethod.GET)
    public String authorizeApp(HttpServletRequest request) {
        return "data-mining/clustering/k-means";
    }
}
