import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CProgramRunner {

    public static boolean run(String programPath, String inputFilePath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    programPath,
                    inputFilePath
            );

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Program C zakonczyl dzialanie poprawnie.");
                return true;
            } else {
                System.out.println("Program C zakonczyl dzialanie z bledem. Kod bledu: " + exitCode);
                return false;
            }

        } catch (IOException e) {
            System.out.println("Blad podczas uruchamiania programu C:");
            System.out.println(e.getMessage());
            return false;

        } catch (InterruptedException e) {
            System.out.println("Oczekiwanie na zakonczenie programu C zostalo przerwane.");
            return false;
        }
    }
}