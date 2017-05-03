
import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Need input file.");
            return;
        }
        WebProfiler wp = new WebProfiler();
        wp.setUp();
        try {
            BufferedReader br = new BufferedReader(new FileReader(args[1]));
            br.lines()
                    .forEach(wp::RunCommand);
            wp.showLogs();
        } finally {
            wp.cleanUp();
        }
    }
}
