/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

/**
 *
 * @author jgarrido
 */
public class Main {

    public static void main(String[] args) {

        /*
         * ex7 mountains
         * ex10 art pictures
         * ex11 horse pictures?
         * ex15 good short example
         * ex16 iguanas good example too
         * ex17 art good example
         */
        
        String filename = "ex15";
        String path_to_pdf = "pdfs/";
        String method = "manual";

        // Starting the Color Engine
        ColorEngine engine = new ColorEngine();
        engine.run(filename, path_to_pdf, method);

    }

}
