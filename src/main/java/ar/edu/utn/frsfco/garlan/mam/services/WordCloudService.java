package ar.edu.utn.frsfco.garlan.mam.services;

import ar.edu.utn.frsfco.garlan.mam.configuration.MyWebAppInitializer;
import ar.edu.utn.frsfco.garlan.mam.models.TwitterMessage;
import ar.edu.utn.frsfco.garlan.mam.models.redis.ClusterAssignment;
import ar.edu.utn.frsfco.garlan.mam.models.redis.ClusterAssignmentIds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.Background;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletContext;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.*;
import java.util.List;

/**
 * TODO add description for class.
 * <p>
 * <p>
 * <a href="WordCloudService.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Service
@Qualifier(value = "wordCloudService")
public class WordCloudService {
    private Integer clusterNumbers;
    private Integer iterations;
    private Integer seed;
    private Integer currentCluster;

    @Autowired
    ServletContext servletContext;

    @Autowired
    Jedis redisClient;

    @Autowired
    MongoTemplate mongoTemplate;

    public String getWordCloudImage() {

        String cachedWordCloudImage = getWordCloudFilePathInCache();
        if (cachedWordCloudImage != null) {
            return cachedWordCloudImage;
        }

        String json = redisClient.get(
            ClusterAssignmentIds.getRedisKey(clusterNumbers, iterations, seed)
        );

        Type listType = new TypeToken<Collection<ClusterAssignmentIds>>(){}.getType();
        Collection<ClusterAssignmentIds> clusterAssignments = new Gson().fromJson(json, listType);
        List<String> texts = new ArrayList<>();

        for (ClusterAssignmentIds clusterAssignment: clusterAssignments) {
            if (clusterAssignment.getClusterNumber() == currentCluster) {
                for (String id : clusterAssignment.getIds()) {
                    TwitterMessage message = mongoTemplate.findById(id, TwitterMessage.class);
                    texts.add(message.getText());
                }
                break;
            }
        }

        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(1000);
        List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(texts);
        final Dimension dimension = new Dimension(600, 300);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 30, 30));
        wordCloud.setFontScalar( new SqrtFontScalar(5, 20));
        wordCloud.setBackgroundColor(new Color(169, 169, 169));
        wordCloud.build(wordFrequencies);

        File file = null;
        String imagesFolder = "/mam/output/";
        String fileName = Calendar.getInstance().getTimeInMillis()+".png";
        try {
            //String imagesFolder = servletContext.getContext("/WEB-INF/resoruces/images/").getContextPath();
            file = new File(imagesFolder + fileName);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wordCloud.writeToFile(file.getPath());

        saveFilePathInCache(fileName);

        return fileName;
    }

    /**
     * @return path
     */
    public String getWordCloudFilePathInCache() {
        return redisClient.get(getWordCloudFilePathCacheKey());
    }

    /**
     * @param fileName
     */
    public void saveFilePathInCache(String fileName) {
        redisClient.set(getWordCloudFilePathCacheKey(), fileName);
        redisClient.expire(getWordCloudFilePathCacheKey(), 84600);
    }

    public String getWordCloudFilePathCacheKey() {
        return "word_cloud_image_path:" + clusterNumbers + ":" + iterations + ":" + seed + ":" + currentCluster;
    }

    public Integer getClusterNumbers() {
        return clusterNumbers;
    }

    public void setClusterNumbers(Integer clusterNumbers) {
        this.clusterNumbers = clusterNumbers;
    }

    public Integer getIterations() {
        return iterations;
    }

    public void setIterations(Integer iterations) {
        this.iterations = iterations;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public Integer getCurrentCluster() {
        return currentCluster;
    }

    public void setCurrentCluster(Integer currentCluster) {
        this.currentCluster = currentCluster;
    }
}
