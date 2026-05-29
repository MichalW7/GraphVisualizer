import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphPanel extends JPanel {
    private Graph graph;

    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private final double margin=100;
    private int vertexRadius;

    private HashMap<Integer,Boolean> visibleVertices;
    private HashMap<String,Boolean>visibleEdges;

    int mode=1;
    private int animationStep = 0;
    private int edgePosition = 0;
    private int delay=500;
    private Timer animationTimer;

    private ArrayList<HashMap<Integer, Vertex>> frames;

    private Timer algorithmTimer;
    private int frameIndex = 0;
    private int algorithmDelay = 50;

    public GraphPanel() {
        setBackground(Color.WHITE);
        visibleVertices = new HashMap<>();
        visibleEdges = new HashMap<>();
    }
    public void setFrames(ArrayList<HashMap<Integer, Vertex>> frames) {
        this.frames = frames;
        calculateBoundsForFrames();
        //repaint();
    }
    public void setGraph(Graph graph) {
        if (animationTimer != null) {
            animationTimer.stop();
        }

        this.graph = graph;


        vertexRadius = getVertexRadius(graph);

        visibleVertices = new HashMap<>();
        visibleEdges = new HashMap<>();
        setFalseVisibility();

        mode = 1;

        repaint();
    }
    private void setFalseVisibility(){
        for(Vertex vertex:graph.getVertices().values()){
            visibleVertices.put(vertex.getId(),false);
        }
        for(Edge edge:graph.getEdges()){
            visibleEdges.put(edge.getName(),false);
        }
    }
    private int getVertexRadius(Graph graph){
        int verticesCount = graph.getVertices().size();
        if (verticesCount <= 15) return 30;
        if (verticesCount <= 30) return 24;
        if (verticesCount <= 60) return 18;
        if (verticesCount <= 100) return 12;
        return 8;
    }
    private void calculateBoundsForFrames() {
        boolean first = true;

        for (HashMap<Integer, Vertex> frame : frames) {
            for (Vertex vertex : frame.values()) {
                double x = vertex.getX();
                double y = vertex.getY();

                if (first) {
                    minX = x;
                    maxX = x;
                    minY = y;
                    maxY = y;
                    first = false;
                } else {
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
    }

    private double getScale(){
        double graphWidth=maxX-minX;
        double graphHeight=maxY-minY;
        double panelWidth=(getWidth()-margin*2);
        double panelHeight=(getHeight()-margin*2);
        if(graphWidth==0)graphWidth=1;
        if(graphHeight==0)graphHeight=1;
        double scaleX=panelWidth/graphWidth;
        double scaleY=panelHeight/graphHeight;
        double scale=Math.min(scaleX,scaleY);
        return scale;

    }
    private double offSetX(double scale){
        double graphWidth = maxX - minX;
        double panelWidth = getWidth() - 2 * margin;
        double drawnGraphWidth = graphWidth * scale;
        return (panelWidth - drawnGraphWidth) / 2;
    }
    private double offSetY(double scale){
        double graphHeight = maxY - minY;
        double panelHeight = getHeight() - 2 * margin;
        double drawnGraphHeight = graphHeight * scale;
        return (panelHeight - drawnGraphHeight) / 2;
    }
    private int toScreenX(double x, double scale){
        return (int)Math.round(margin+offSetX(scale)+(x-minX)*scale);
    }
    private int toScreenY(double y, double scale){
        return (int)Math.round(margin+offSetY(scale)+(y-minY)*scale);
    }
    private void drawEdges(Graphics2D g,double scale){
        for(Edge edge:graph.getEdges()){
            if(mode==1 && !visibleEdges.get(edge.getName()))continue;
            Vertex from=graph.getVertex(edge.getFrom());
            Vertex to=graph.getVertex(edge.getTo());
            int xFrom= toScreenX(from.getX(),scale);
            int yFrom= toScreenY(from.getY(),scale);
            int xTo= toScreenX(to.getX(),scale);
            int yTo= toScreenY(to.getY(),scale);
            g.drawLine(xFrom,yFrom,xTo,yTo);
        }
    }
    private void drawVertices(Graphics2D g, double scale){
        g.setFont(new Font("Arial", Font.BOLD, 14));
        for(Vertex v: graph.getVertices().values()){
            if(mode==1 && !visibleVertices.get(v.getId()))continue;
            int x= toScreenX(v.getX(),scale);
            int y= toScreenY(v.getY(),scale);
            g.setColor(Color.WHITE);
            g.fillOval(x-vertexRadius,y-vertexRadius,2*vertexRadius,2*vertexRadius);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - vertexRadius, y - vertexRadius, 2 * vertexRadius, 2 * vertexRadius);
            String text = String.valueOf(v.getId());
            int textWidth = g.getFontMetrics().stringWidth(text);
            int textHeight = g.getFontMetrics().getAscent();

            g.drawString(text, x - textWidth / 2, y + textHeight / 2 - 2);
        }
    }
    public void speedUpAnimation(){
        delay -= 200;

        if (delay < 100) {
            delay = 100;
        }

        if (animationTimer != null) {
            animationTimer.setDelay(delay);
        }
    }
    public void slowDownAnimation(){
        delay += 200;
        if (delay > 3000) {
            delay = 3000;
        }

        if (animationTimer != null) {
            animationTimer.setDelay(delay);
        }
    }
    public void startAnimation() {
        if (graph == null) {
            return;
        }

        stopAllTimers();

        if (frames != null && !frames.isEmpty()) {
            graph.setVertices(frames.get(frames.size() - 1));
        }

        mode = 1;

        setFalseVisibility();

        animationStep = 0;
        edgePosition = 0;

        ArrayList<Edge> edges = graph.getEdges();

        animationTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (edgePosition >= edges.size()) {
                    animationTimer.stop();
                    return;
                }

                Edge edge = edges.get(edgePosition);

                if (animationStep == 0) {
                    visibleVertices.put(edge.getFrom(), true);
                    animationStep++;
                } else if (animationStep == 1) {
                    visibleVertices.put(edge.getTo(), true);
                    animationStep++;
                } else if (animationStep == 2) {
                    visibleEdges.put(edge.getName(), true);
                    animationStep = 0;
                    edgePosition++;
                }

                repaint();
            }
        });

        animationTimer.start();
    }
    public void startAlgorithmAnimation(){
        if (graph == null || frames == null || frames.isEmpty()) {
            return;
        }
        stopAllTimers();
        mode=0;
        frameIndex=0;
        graph.setVertices(frames.get(frameIndex));
        repaint();
        algorithmTimer =new Timer(algorithmDelay,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                frameIndex++;
                if(frameIndex>=frames.size()){
                    algorithmTimer.stop();
                    graph.setVertices(frames.get(frames.size()-1));
                    repaint();
                    return;
                }
                graph.setVertices(frames.get(frameIndex));
                repaint();
            }
        });
        algorithmTimer.start();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph == null) {
            g.drawString("Wpisz nazwe pliku i kliknij Wczytaj graf.", 50, 50);
            return;
        }
        double scale=getScale();
        Graphics2D g2d=(Graphics2D)g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );


        drawEdges(g2d,scale);

        drawVertices(g2d,scale);

    }
    public void showFullGraph() {
        if (graph == null) {
            return;
        }

        stopAllTimers();

        if (frames != null && !frames.isEmpty()) {
            graph.setVertices(frames.get(frames.size() - 1));
        }

        mode = 0;
        repaint();
    }
    public void resetAnimation() {
        if (graph == null) {
            return;
        }

        stopAllTimers();

        mode = 1;
        setFalseVisibility();

        animationStep = 0;
        edgePosition = 0;
        frameIndex = 0;

        repaint();
    }
    private void stopAllTimers() {
        if (animationTimer != null) {
            animationTimer.stop();
        }

        if (algorithmTimer != null) {
            algorithmTimer.stop();
        }
    }
}