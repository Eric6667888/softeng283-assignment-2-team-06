package nz.ac.auckland.se283.models;

/**
 * Represents a ground truth label from the ground truth JSON file
 */
public class GroundTruthLabel {
    private String us_id;
    private int ground_truth_problem_id;
    
    public GroundTruthLabel() {}
    
    public GroundTruthLabel(String us_id, int ground_truth_problem_id) {
        this.us_id = us_id;
        this.ground_truth_problem_id = ground_truth_problem_id;
    }
    
    public String getUs_id() {
        return us_id;
    }
    
    public void setUs_id(String us_id) {
        this.us_id = us_id;
    }
    
    public int getGround_truth_problem_id() {
        return ground_truth_problem_id;
    }
    
    public void setGround_truth_problem_id(int ground_truth_problem_id) {
        this.ground_truth_problem_id = ground_truth_problem_id;
    }
}
