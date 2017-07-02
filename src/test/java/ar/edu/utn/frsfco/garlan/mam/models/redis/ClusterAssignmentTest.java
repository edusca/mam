package ar.edu.utn.frsfco.garlan.mam.models.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * <p>
 * <a href="ClusterAssignmentTest.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ClusterAssignmentTest {

    @Test
    public void testGetCacheKey() {
        String key = ClusterAssignment.getRedisKey(5, 2, 1);
        Assert.assertEquals("cluster_assignments:5:2:1", key);
    }
}
