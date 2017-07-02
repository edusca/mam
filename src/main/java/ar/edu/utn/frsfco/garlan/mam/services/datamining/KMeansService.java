package ar.edu.utn.frsfco.garlan.mam.services.datamining;

import java.lang.reflect.Type;
import java.util.*;

import ar.edu.utn.frsfco.garlan.mam.models.redis.ClusterAssignment;
import ar.edu.utn.frsfco.garlan.mam.models.redis.ClusterAssignmentIds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instance;
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
    static final Logger logger = LogManager.getLogger(KMeansService.class);

    private String CLUSTER_ASSIGNMENTS_INSTANCES_IDS = "cluster_assignments_messages_ids:{clusterNumber}:{iterations}:{seed}";

    private SimpleKMeans kmeansClassifier;
    private Instances currentDataSource;
    private Instances dataSource;

    private int numberClusters;
    private int iterations;
    private int seed;

    @Autowired
    InMemoryDataSource inMemoryDataSource;

    @Autowired
    Jedis redisClient;

    @Autowired
    public KMeansService(
        @Qualifier("inMemoryDataSource") InMemoryDataSource inMemoryDataSource,
        @Qualifier("redis") Jedis redisClient
    ) {
        this.inMemoryDataSource = inMemoryDataSource;
        this.redisClient = redisClient;
    }
    
    public Collection<ClusterAssignment> classify(Integer numberClusters, Integer iterations, Integer seed) {
        this.numberClusters = numberClusters;
        this.iterations = iterations;
        this.seed = seed;

        Collection<ClusterAssignment> assignments = getClusterAssginmentsFromCache();
        if (assignments != null) {
            return assignments;
        }

        inMemoryDataSource.initDataSource();
        inMemoryDataSource.fillDataSetWithTwitterMessage();
        dataSource = inMemoryDataSource.getFilteredDataSet();

        try {
            logger.error("Dataset size:" + dataSource.numInstances());
            kmeansClassifier = new SimpleKMeans();
            
            kmeansClassifier.setPreserveInstancesOrder(true);
            kmeansClassifier.setNumClusters(numberClusters);
            kmeansClassifier.setMaxIterations(iterations);
            kmeansClassifier.setSeed(seed);
            kmeansClassifier.buildClusterer(dataSource);
            EuclideanDistance distance = new EuclideanDistance();
            distance.setDontNormalize(true);
            kmeansClassifier.setDistanceFunction(distance);

            currentDataSource = dataSource;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex.getCause());
        }

        return getAssignments();
    }

    /**
     * TODO refactor this to be independent of the classify method.
     * For this to work, has to be called after {@link KMeansService#classify(Integer, Integer, Integer)}
     * @return Collection<ClusterAssignment> clusterAssignments
     */
    private Collection<ClusterAssignment> getAssignments() {
        HashMap<Integer, ArrayList<Integer>> assignments = new HashMap<>();
        int i = 0;
        try {
            for (int clusterAssignment : kmeansClassifier.getAssignments()) {
                boolean containsKey = assignments.containsKey(clusterAssignment);

                if (!containsKey) {
                    assignments.put(clusterAssignment, new ArrayList<>());
                }

                assignments.get(clusterAssignment).add(currentDataSource.instance(i).attribute(i).index());

                i++;
                logger.error("Cluster N°: " + clusterAssignment);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex.getCause());
        }

        this.saveAssignmentsIntoCache(assignments);
        this.saveAssignmentsInstancesIntoCache(assignments);
        
        return getClusterAssignmentsAsObjectsList(assignments);
    }

    /**
     * TODO this has to be removed from the online process.
     * Save the result of the assignments after classify in cache.
     * @param assignments
     */
    private void saveAssignmentsIntoCache(HashMap<Integer, ArrayList<Integer>> assignments) {
        Collection<ClusterAssignment> clusterAssignments = getClusterAssignmentsAsObjectsList(assignments);

        Gson gson = new Gson();

        String cacheKey = ClusterAssignment.getRedisKey(numberClusters, iterations, seed);
        redisClient.set(cacheKey, gson.toJson(clusterAssignments));
        redisClient.expire(cacheKey, ClusterAssignment.TTL);
    }

    /**
     * TODO this has to be removed from the online process.
     * Save the instance ids, representing the messages into cache.
     * @param assignments
     */
    private void saveAssignmentsInstancesIntoCache(HashMap<Integer, ArrayList<Integer>> assignments) {
        Collection<ClusterAssignmentIds> clusterAssignmentsIdsList = new ArrayList<>();

        for(Map.Entry<Integer, ArrayList<Integer>> assignment : assignments.entrySet()) {
            ClusterAssignmentIds clusterAssignmentIds = new ClusterAssignmentIds();
            clusterAssignmentIds.setClusterNumber(assignment.getKey());

            // Iterate over the assignments for get the model ids
            ArrayList<String> ids = new ArrayList<>();
            for (Integer index : assignment.getValue()) {
                System.out.println(
                        inMemoryDataSource.getDataSet().instance(index).stringValue(0)
                );
                ids.add(inMemoryDataSource.getDataSet().instance(index).stringValue(0));
            }

            clusterAssignmentIds.setIds(ids);
            clusterAssignmentsIdsList.add(clusterAssignmentIds);
        }

        Gson gson = new Gson();
        String cacheKey = ClusterAssignmentIds.getRedisKey(numberClusters, iterations, seed);
        redisClient.set(cacheKey, gson.toJson(clusterAssignmentsIdsList));
        redisClient.expire(cacheKey, ClusterAssignmentIds.TTL);
    }

    private Collection<ClusterAssignment> getClusterAssignmentsAsObjectsList(HashMap<Integer, ArrayList<Integer>> assignments) {
        Collection<ClusterAssignment> clusterAssignments = new ArrayList<>();

        for(Map.Entry<Integer, ArrayList<Integer>> assignment : assignments.entrySet()) {
            ClusterAssignment newAssignment = new ClusterAssignment();
            newAssignment.setClusterNumber(assignment.getKey());
            newAssignment.setAssignmentsQuantity(assignment.getValue().size());

            clusterAssignments.add(newAssignment);
        }

        return clusterAssignments;
    }

    /**
     * @return Collection<ClusterAssignment>|null
     */
    private Collection<ClusterAssignment> getClusterAssginmentsFromCache() {
        // Check cache first
        String serializedClusterAssignments = redisClient.get(
                ClusterAssignment.getRedisKey(numberClusters, iterations, seed)
        );

        if (serializedClusterAssignments != null) {
            Type listType = new TypeToken<Collection<ClusterAssignment>>(){}.getType();
            return new Gson().fromJson(serializedClusterAssignments, listType);
        }

        return null;
    }

    public SimpleKMeans getClassifier() {
        return kmeansClassifier;
    }
}
