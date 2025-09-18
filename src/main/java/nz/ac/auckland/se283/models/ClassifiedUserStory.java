package nz.ac.auckland.se283.models;

/**
 * Represents the output format for each classified user story
 */
public class ClassifiedUserStory {
    private String us_id;
    private int estimated_problem_id;
    private String corrected_us;
    private String optional_explanation;
    
    public ClassifiedUserStory() {}
    
    public ClassifiedUserStory(String us_id, int estimated_problem_id, String corrected_us, String optional_explanation) {
        this.us_id = us_id;
        this.estimated_problem_id = estimated_problem_id;
        this.corrected_us = corrected_us;
        this.optional_explanation = optional_explanation;
    }
    
    public String getUs_id() {
        return us_id;
    }
    
    public void setUs_id(String us_id) {
        this.us_id = us_id;
    }
    
    public int getEstimated_problem_id() {
        return estimated_problem_id;
    }
    
    public void setEstimated_problem_id(int estimated_problem_id) {
        this.estimated_problem_id = estimated_problem_id;
    }
    
    public String getCorrected_us() {
        return corrected_us;
    }
    
    public void setCorrected_us(String corrected_us) {
        this.corrected_us = corrected_us;
    }
    
    public String getOptional_explanation() {
        return optional_explanation;
    }
    
    public void setOptional_explanation(String optional_explanation) {
        this.optional_explanation = optional_explanation;
    }
}
