import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Status {
    private double suspicion;//-1 -- 1
    private String name;
    private List<String> nominations = new ArrayList<>();
    private List<HashMap<String, Double>> accusations = new ArrayList<>();

    Status(double suspicion, String name) {
        this.name = name;
        this.suspicion = suspicion;
    }

    public double getSuspicion() {
        return suspicion;
    }

    public String getName() {
        return name;
    }

    public void setSuspicion(double suspicion) {
        this.suspicion = suspicion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNominations() {
        return nominations;
    }

    public void addNomination(String nomination) {
        nominations.add(nomination);
    }

    public List<HashMap<String, Double>> getAccusations() {
        return accusations;
    }

    public void addAccusation(HashMap<String, Double> accusation) {
        accusations.add(accusation);
    }
}