import java.util.ArrayList;
import java.util.HashMap;
public class Graph {
    private HashMap<Integer,Vertex>vertices;
    private ArrayList<Edge>edges;
    public Graph(){
        vertices=new HashMap<>();
        edges = new ArrayList<>();
    }
    public void addVertex(Vertex vertex){
        vertices.put(vertex.getId(),vertex);
    }
    public void addEdge(Edge edge){
        edges.add(edge);
    }
    public Vertex getVertex(int id){
        return vertices.get(id);
    }
    public HashMap<Integer,Vertex> getVertices(){
        return vertices;
    }
    public ArrayList<Edge>getEdges(){
        return edges;
    }
    public void setVertices(HashMap<Integer, Vertex> vertices) {
        this.vertices = vertices;
    }
}
