package ar.edu.utn.frsfco.garlan.mam.models.redis;

import java.util.ArrayList;

/**
 * This holds the cluster assignments with some models ids into each cluster.
 * <p>
 * <p>
 * <a href="ClusterAssignmentIds.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
public class ClusterAssignmentIds {
    public static final int TTL = 86400;

    private static String clusterAssignmentsIdsKey = "cluster_assignments_ids:{clusterNumber}:{iterations}:{seed}";

    private int clusterNumber;
    private ArrayList<String> ids;

    public int getClusterNumber() {
        return clusterNumber;
    }

    public void setClusterNumber(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public static String getRedisKey(int clusterNumber, int iterations, int seed) {
        return clusterAssignmentsIdsKey
                .replace("{clusterNumber}", String.valueOf(clusterNumber))
                .replace("{iterations}", String.valueOf(iterations))
                .replace("{seed}", String.valueOf(seed));
    }
}
