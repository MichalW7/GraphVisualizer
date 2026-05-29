import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class MainFrame extends JFrame{
    public MainFrame(){
        setTitle("Wizualizacja Grafu");
        setSize(1200,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        GraphPanel gp=new GraphPanel();

        JPanel buttonPanel=new JPanel();
        JButton showFullGraphButton = new JButton("Pokaz caly graf");
        JButton showAnimationButton = new JButton("Animuj");
        JButton resetButton = new JButton("Reset");
        showFullGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.showFullGraph();
            }
        });
        showAnimationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.startAnimation();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.resetAnimation();
            }
        });

        JPanel speedSlowAnimationPanel=new JPanel();
        JButton speedUpAnimation=new JButton("Przyspiesz");
        JButton slowDownAnimation=new JButton("Zwolnij");
        speedUpAnimation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.speedUpAnimation();
            }
        });
        slowDownAnimation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.slowDownAnimation();
            }
        });
        JLabel fileLabel=new JLabel("Plik:");
        JTextField fileNameField = new JTextField("dane_testowe_1.txt", 20);
        JButton loadGraphButton = new JButton("Wczytaj graf");
        loadGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = fileNameField.getText();

                String cProgramPath = ".\\c_program\\graf_program.exe";
                String inputFilePath = ".\\c_program\\" + fileName;
                String resultFilePath = "wynik.txt";

                boolean success = CProgramRunner.run(cProgramPath, inputFilePath);

                if (!success) {
                    System.out.println("Program C nie wykonal sie poprawnie.");
                    return;
                }

                try {
                    Graph graph = new Graph();

                    GraphLoader.loadVertices(graph, resultFilePath);
                    GraphLoader.loadEdges(graph, inputFilePath);

                    System.out.println("Wczytano graf.");
                    System.out.println("Liczba wierzcholkow: " + graph.getVertices().size());
                    System.out.println("Liczba krawedzi: " + graph.getEdges().size());

                    gp.setGraph(graph);

                } catch (Exception ex) {
                    System.out.println("Wystapil blad podczas wczytywania grafu:");
                    System.out.println(ex.getMessage());
                }
            }
        });

        speedSlowAnimationPanel.add(speedUpAnimation);
        speedSlowAnimationPanel.add(slowDownAnimation);
        add(speedSlowAnimationPanel,BorderLayout.PAGE_END);

        buttonPanel.add(fileLabel);
        buttonPanel.add(fileNameField);
        buttonPanel.add(loadGraphButton);
        buttonPanel.add(showFullGraphButton);
        buttonPanel.add(showAnimationButton);
        buttonPanel.add(resetButton);
        add(buttonPanel,BorderLayout.NORTH);

        add(gp,BorderLayout.CENTER);
    }
}
