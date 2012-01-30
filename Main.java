/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

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

        String filename = "ex4";
        double saving = 0.5;

        // Rendering PDF Document
        Renderpdf pdf = new Renderpdf();
        PDFPages pdfp = pdf.colorpdf(filename);

        // Manual sorting of the colorfulness
        Vector<Integer> metric1 = pdfp.sortMetric1();
        Vector<Integer> cpages = pdfp.newColorSet(metric1,saving);

        System.out.println("Metric array sorted: ");
        System.out.println(metric1);
        System.out.println("Color pages selected to be printed: "+cpages.size());
        System.out.println(cpages);

        // Clustering
        


        // Running Genetic Algorithm
        // Genetics g = new Genetics();
        // g.genetics_color(pdfp.getPercentages(),0.5);

        

        


    }

}
