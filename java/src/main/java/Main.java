
import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Need input file.");
            return;
        }
        WebProfiler wp = new WebProfiler();
        wp.setUp();
        try {
            String fileName = args[0];
            if (!fileName.equals("-")) {
                System.setIn(new FileInputStream(new File(fileName)));
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            br.lines()
                    .forEach(wp::RunCommand);
            wp.showLogs();
        } finally {
            wp.cleanUp();
        }
    }
}
