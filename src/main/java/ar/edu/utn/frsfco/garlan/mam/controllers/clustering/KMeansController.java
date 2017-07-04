package ar.edu.utn.frsfco.garlan.mam.controllers.clustering;

import javax.servlet.http.HttpServletRequest;

import ar.edu.utn.frsfco.garlan.mam.models.redis.ClusterAssignment;
import ar.edu.utn.frsfco.garlan.mam.services.WordCloudService;
import ar.edu.utn.frsfco.garlan.mam.services.datamining.KMeansService;
import ar.edu.utn.frsfco.garlan.mam.utils.JSONResponse;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private WordCloudService wordCloudService;

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
        model.addAttribute("current_cluster_numbers", clusterNumbers);
        model.addAttribute("current_cluster_iterations", iterations);
        model.addAttribute("current_cluster_seed", seed);

        return "/clustering/k-means";
    }

    @RequestMapping(
        value = "/clustering/kmeans/word_cloud",
        method = RequestMethod.GET,
        params = {"cluster_numbers", "iterations", "seed", "current_cluster"}
    )
    public @ResponseBody JSONResponse wordCloudFilePathImage(
        @RequestParam("cluster_numbers") Integer clusterNumbers,
        @RequestParam("iterations") Integer iterations,
        @RequestParam("seed") Integer seed,
        @RequestParam("current_cluster") Integer currentCluster
    ) {
        wordCloudService.setClusterNumbers(clusterNumbers);
        wordCloudService.setIterations(iterations);
        wordCloudService.setSeed(seed);
        wordCloudService.setCurrentCluster(currentCluster);

        String filePath = wordCloudService.getWordCloudImage();

        return new JSONResponse("wordCloudPath", filePath);
    }
}
