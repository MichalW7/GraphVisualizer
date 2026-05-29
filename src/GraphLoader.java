import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Locale;

public class GraphLoader {
    public static void loadVertices(Graph graph, String filePath) throws FileNotFoundException{
        File file = new File(filePath);
        Scanner scanner= new Scanner(file);
        scanner.useLocale(Locale.US);
        while(scanner.hasNext()){
            int id = scanner.nextInt();
            double x = scanner.nextDouble();
            double y= scanner.nextDouble();
            Vertex vertex= new Vertex(id,x,y);
            graph.addVertex(vertex);
        }
        scanner.close();
    }
    public static void loadEdges(Graph graph,String filePath) throws FileNotFoundException{
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        scanner.useLocale(Locale.US);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.trim().isEmpty()){
                continue;
            }
            Scanner lineScanner = new Scanner(line);
            lineScanner.useLocale(Locale.US);
            String name = lineScanner.next();
            int from = lineScanner.nextInt();
            int to = lineScanner.nextInt();
            double weight=lineScanner.nextDouble();
            Edge edge = new Edge(name,from,to,weight);
            graph.addEdge(edge);
            lineScanner.close();
        }
        scanner.close();
    }
}
