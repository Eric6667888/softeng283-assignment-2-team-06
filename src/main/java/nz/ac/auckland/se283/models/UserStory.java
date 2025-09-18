package nz.ac.auckland.se283.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a user story from the input JSON file
 */
public class UserStory {
    @JsonProperty("US_ID")
    private String US_ID;
    
    @JsonProperty("US_text")
    private String US_text;
    
    public UserStory() {}
    
    public UserStory(String US_ID, String US_text) {
        this.US_ID = US_ID;
        this.US_text = US_text;
    }
    
    public String getUS_ID() {
        return US_ID;
    }
    
    public void setUS_ID(String US_ID) {
        this.US_ID = US_ID;
    }
    
    public String getUS_text() {
        return US_text;
    }
    
    public void setUS_text(String US_text) {
        this.US_text = US_text;
    }
}
