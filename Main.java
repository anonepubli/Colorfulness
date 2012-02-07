/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author jgarrido
 */
public class Main {

    public static void main(String[] args) {

        /*
         * ex7 mountains
         * ex10 art pictures - Nice result
         * ex11 horse pictures?
         * ex15 good short example
         * ex16 iguanas good example too
         * ex17 art good example
         */

        String filename = "ex1";
        if (args.length>0)
            filename = args[0];
        String path_to_pdf = "/home/jgarrido/NetBeansProjects/Colorfulnes/pdfs/";
        String method = "manual";

        // Starting the Color Engine
        ColorEngine engine = new ColorEngine();
        String url = engine.run(filename, path_to_pdf, method);

        // Writing the serial into a file so php can ack that
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("url.txt"));
            out.write(url);
            out.close();
        } catch (IOException e) { }

    }

}
