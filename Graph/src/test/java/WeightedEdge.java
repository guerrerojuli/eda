public class WeightedEdge {
    private int weight;
    private String label;
    
    public WeightedEdge(int weight) {
        this.weight = weight;
        this.label = String.valueOf(weight);
    }
    
    public WeightedEdge(int weight, String label) {
        this.weight = weight;
        this.label = label;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public String getLabel() {
        return label;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WeightedEdge that = (WeightedEdge) obj;
        return weight == that.weight && 
               (label != null ? label.equals(that.label) : that.label == null);
    }
    
    @Override
    public int hashCode() {
        return weight * 31 + (label != null ? label.hashCode() : 0);
    }
    
    @Override
    public String toString() {
        return label;
    }
} 