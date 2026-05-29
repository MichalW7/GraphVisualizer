public class Edge {
    private String name;
    private int from;
    private int to;
    private double weight;

    public Edge(String name, int from, int to, double weight) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }
}