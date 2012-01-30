/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import com.itextpdf.text.pdf.PdfReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

/**
 *
 * @author jgarrido
 */
public class Renderpdf {

    /*
     * Given an int i and an int n, this function adds 0's to the
     * left of i until it reaches the number of digits in n.
     * This is used because pdftoppm adds 0s to the left of the
     * number pages.
     *
     *  Params:
     * - IN  int i :  integer to be modified
     * - IN  int n :  max integer to be reached
     * - OUT string : string representing i with all the 0s it needs
     * 
     */
    public String digits(int i, int n){
        String istr = Integer.toString(i);
        String nstr = Integer.toString(n);
        int y = istr.length();
        int x = nstr.length();
        for (int j=0; j<x-y; j++)
            istr = "0"+istr;
        return istr;
    }

    /*
     * Given a string (pdf filename) this function open the file,
     * renders it (with an auxiliary function) and extracts all the
     * parameters and information from the whole document.
     * I.e. percentage of color per page, total number of colors per page
     *
     *  Params:
     * - IN  string filename : pdf filename to be examinated
     * - OUT PDFPages : Object containing all the information needed
     *                  about the pdf document
     *
     */
    public void colorpdf(String filename, PDFPages pdfp){

        int n_pages = 0;

        n_pages = numberOfPages(filename);

        int color, bw = 0;

        // Rendering the whole document at once using pdftoppm

        String cmd = "pdftoppm "; // command
        cmd += "-r 50 -jpeg ";          // flags
        cmd += "pdfs/"+filename+".pdf ";  // source file
        cmd += "pdfs/"+filename;  // source file

        //System.out.println(cmd);

        try {
            System.out.println("Rendering PDF "+filename+".pdf...");
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            System.out.println("PDF rendered...");
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        // Analyzing each page looking for colors

        System.out.println("Checking for color in pages...");

        // Helper function in python...
        // Maybe its better if I find another way in Java
        try {
            cmd = "python pdfs/imaging.py "+filename;
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            System.out.println("Pages checked...");

            FileReader fr = new FileReader("pdfs/results.txt");
            BufferedReader br = new BufferedReader(fr);
            String s;
            s = br.readLine();
            fr.close();

            // Building the vectors with the info of the color
            // on every page
            Vector<Double> percent_color = new Vector<Double>();
            Vector<Integer> color_variety = new Vector<Integer>();
            Vector<Integer> pagenumbers = new Vector<Integer>();
            String [] colors = s.split(",");
            for (int i = 0; i<colors.length; i++){
                String [] temp = colors[i].split(" ");
                if (!temp[0].equals("0")) {
                    percent_color.add(Double.parseDouble(temp[0]));
                    color_variety.add(Integer.parseInt(temp[1]));
                    pagenumbers.add(i);
                }
            }

            System.out.println("Color Pages / Total Pages = "+pagenumbers.size()+"/"+n_pages);
//            System.out.println(percent_color);
//            System.out.println(color_variety);
//            System.out.println(pagenumbers);

            // Updating the PDF Object
            pdfp.color_variety = color_variety;
            pdfp.pagenumbers = pagenumbers;
            pdfp.percent_color = percent_color;
            pdfp.len = pagenumbers.size();
            

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public int numberOfPages(String filename){

        int n = 0;
        
        try {
            PdfReader reader = new PdfReader("pdfs/"+filename+".pdf");
            n = reader.getNumberOfPages();
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        return n;
    }

}
