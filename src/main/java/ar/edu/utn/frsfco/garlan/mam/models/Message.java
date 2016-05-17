package ar.edu.utn.frsfco.garlan.mam.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Default message content. New message content for other social networks
 * must extends from here
 * 
 * <p><a href="Message.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
@Document(collection = "messages")
public class Message {
    
    //Document data
    @Id
    private String id;
    
    //User data
    private String userLocation;
    
    //Message data
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
