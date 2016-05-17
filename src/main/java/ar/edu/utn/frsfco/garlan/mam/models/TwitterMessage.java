package ar.edu.utn.frsfco.garlan.mam.models;

/**
 * Specific twitter message content
 * 
 * <p><a href="TwitterMessage.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
public class TwitterMessage extends Message{
    
    //User data
    private String userScreenName;
    private long userId;
    
    //Message data
    private long messageId;
    private String textLanguageCode;
    
    //Use this extra attributes from a tweet for create the graph db
    private long fromUserId;
    private long toUserId;

    public String getUserScreenName() {
        return userScreenName;
    }

    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getTextLanguageCode() {
        return textLanguageCode;
    }

    public void setTextLanguageCode(String textLanguageCode) {
        this.textLanguageCode = textLanguageCode;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }
    
}
