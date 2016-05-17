package ar.edu.utn.frsfco.garlan.mam.utils;

/**
 * TODO Comment of component here!
 * 
 * <p><a href="JSONResponse.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:eduardo.scarello@gmail.com">Eduardo Scarello</a>
 */
public class JSONResponse {
    
    private String key;
    private String value;

    public JSONResponse() {
    }
    
    public JSONResponse(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
