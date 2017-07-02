package ar.edu.utn.frsfco.garlan.mam.controllers.clustering;

import javax.servlet.http.HttpServletRequest;

import ar.edu.utn.frsfco.garlan.mam.models.redis.ClusterAssignment;
import ar.edu.utn.frsfco.garlan.mam.services.datamining.KMeansService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manage the views and the ui calls of the k-means clustering algorithm
 * 
 * <p><a href="KMeansController.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Controller
public class KMeansController {
    static final Logger logger = LogManager.getLogger(KMeansController.class);

    @Autowired
    private KMeansService kMeansService;

    @RequestMapping(value = "/clustering/k-means", method = RequestMethod.GET)
    public String kmeansClusteringPage(Model model) {

        return "/clustering/k-means";
    }

    @RequestMapping(
        value = "/clustering/k-means/classify",
        method = RequestMethod.GET,
        params = {"cluster_numbers", "iterations", "seed"}
    )
    public String kmeansClusteringClassify(
        @RequestParam("cluster_numbers") Integer clusterNumbers,
        @RequestParam("iterations") Integer iterations,
        @RequestParam("seed") Integer seed,
        Model model
    ) {
        Collection<ClusterAssignment> assignments = kMeansService.classify(clusterNumbers, iterations, seed);

        model.addAttribute("assignments", assignments);

        return "/clustering/k-means";
    }
}
