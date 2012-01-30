/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 *
 * @author jgarrido
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        // Initial parameters
        String filename = "ex4";
        double saving = 0.5;
        int method = 1;
        int [] fpages = new int[0];


        
        // Rendering PDF Document
        PDFPages pdf = new PDFPages();
        Renderpdf render = new Renderpdf();
        render.colorpdf(filename,pdf);

        if (method == 0){
            // Manual sorting of the colorfulness
            Vector<Integer> metric1 = pdf.sortMetric1();
            Vector<Integer> cpages = pdf.newColorSet(metric1,saving);

            // Filling the final array
            fpages = new int[cpages.size()];
            for (int i = 0; i<cpages.size(); i++){
                fpages[i] = cpages.get(i);
            }

        }
        else if (method == 1) {
            // Clustering
            Clustering c = new Clustering(pdf,saving);
            Vector<Integer> cpages = c.cluster();

            // Filling the final array
            fpages = new int[cpages.size()];
            for (int i = 0; i<cpages.size(); i++){
                fpages[i] = cpages.get(i);
            }
        }

        // Showing all the pages
        int npages = render.numberOfPages(filename);
        boolean go = true;
        if (fpages.length>20){
            System.out.println(" ------ Warning: There are "+fpages.length+" pages to show. Do you want to show them? ([y]/[n])");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String answer = null;
            try {
                answer = br.readLine();
                if (answer.equals("n")) go = false;
            } catch (IOException ioe) {
                System.out.println("IO error trying to read user answer...");
                go = false;
            }
        }
        if (go) {
            System.out.print("Pages found: ");
            for (int i = fpages.length-1; i>=0; i--) {
                System.out.print(fpages[i]+1 + " ");
                ImageLoader im = new ImageLoader();
                im.run("/home/jgarrido/NetBeansProjects/Colorfulnes/pdfs/"+filename
                        +"-"+
                        render.digits(fpages[i]+1,npages)+
                        ".jpg","Page "+(fpages[i]+1));

            }
        }
        System.out.println("");

    }

}
