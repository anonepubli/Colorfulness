/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author jgarrido
 */
public class ColorEngine {

    public void run(String filename, String path_to_pdf, String method){


        int [] fpages = new int[0];


        // Rendering PDF Document using java pixelxpixel
        Render r1 = new Render();
        PDFInfo pdf = r1.render(filename,path_to_pdf);


        // Rendering PDF Document using python script
        //Renderpdf_python r2 = new Renderpdf_python();
        //r2.colorpdf(filename,path_to_pdf,pdf);


        if (pdf.pagenumbers.size()==0){
            System.out.println("");
            System.out.println(" > There are no color pages in this book. Bye.");
            System.out.println("");
            System.exit(0);
        }
        if (pdf.pagenumbers.size()==1 || pdf.pagenumbers.size()==2){
            System.out.println("");
            System.out.println(" > There are few color pages in this book.");
            System.out.println(" > I recommend to print them all or none of them.");
            System.out.print(" > Color pages: ");
            System.out.print(pdf.pagenumbers.toString()+'\n');
            System.out.println("");
            System.exit(0);
        }


        if (method.equals("manual")){

            /*
             * Manual sorting of the colorfulnes using as sorting parameter
             * the metric found between the percentage of color in the pages
             * and the amount of color on each page
             */
            pdf.sortMetric1();

            /*
             * Grouping the pages into manual clusters (sets) aiming to have
             * similar pages grouped into different sets
             */
            pdf.findSets();

            /*
             * Merging the sets found in the previous step aiming to have
             * 2 or 3 sets to in order to play with the options for
             * the user
             */
            pdf.mergeSets();

            // Reducing the set according to the input saving
            //ArrayList<Integer> cpages = pdf.newColorSet(pdf,saving);

            // Printing the manual clusters
            for (int i=0; i<pdf.spagenumbers.size(); i++){
                System.out.println("Size of this set: "+pdf.spagenumbers.get(i).size());
                //System.out.println(pdfsorted.spagenumbers.get(i));
                for (int j=0 ; j<pdf.spagenumbers.get(i).size(); j++){
                    System.out.print((pdf.spagenumbers.get(i).get(j)+1)+" ");
                }
                System.out.println("");
                //System.out.println("Average: "+pdfsorted.averageColorInSet(pdfsorted.spagenumbers.get(i),pdf.rgbs));
                //System.out.println("");

            }

            // Filling the final array
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for (int i=0; i<pdf.spagenumbers.size(); i++){
                for (int j = 0; j<pdf.spagenumbers.get(i).size(); j++){
                    temp.add(pdf.spagenumbers.get(i).get(j));
                }
            }
            Object [] temp2 = temp.toArray();
            fpages = new int[temp2.length];
            for (int i=0; i<temp2.length; i++)
                fpages[i] = ((Integer)temp2[i])+1; // Very important!!! Pages starts with 1 not 0!

        }
        else if (method.equals("clustering")) {
            // Clustering
            Clustering c = new Clustering(pdf);
            ArrayList<Integer> cpages = c.cluster();

            // Filling the final array
            fpages = new int[cpages.size()];
            for (int i = 0; i<cpages.size(); i++){
                fpages[i] = cpages.get(i);
            }
        }

        // Showing all the pages
        int npages = r1.numberOfPages(filename);

        ImageLoader im = new ImageLoader();
        im.draw(pdf.spagenumbers, filename, path_to_pdf,npages);

    }

}
