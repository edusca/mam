package ar.edu.utn.frsfco.garlan.mam.models.redis;

/**
 * This class is for store in redis the assignments
 * cluster with the count of instances in each cluster
 * for each classification execution.
 * <p>
 * <p>
 * <a href="ClusterAssignment.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
public class ClusterAssignment {
    public static final int TTL = 86400;

    private static String clusterAssignmentsKey = "cluster_assignments:{clusterNumber}:{iterations}:{seed}";

    private int clusterNumber;

    private int assignmentsQuantity;

    public int getClusterNumber() {
        return clusterNumber;
    }

    public void setClusterNumber(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    public int getAssignmentsQuantity() {
        return assignmentsQuantity;
    }

    public void setAssignmentsQuantity(int assignmentsQuantity) {
        this.assignmentsQuantity = assignmentsQuantity;
    }

    public static String getRedisKey(int clusterNumber, int iterations, int seed) {
        return clusterAssignmentsKey
            .replace("{clusterNumber}", String.valueOf(clusterNumber))
            .replace("{iterations}", String.valueOf(iterations))
            .replace("{seed}", String.valueOf(seed));
    }
}
