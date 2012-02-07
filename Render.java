/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import com.itextpdf.text.pdf.PdfReader;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author jgarrido
 */
public class Render {

     /*
     * Void Constructor
     *
     */
    public Render() {
    }

    /*
     * Given an input int pixel, this function returns the RGB data as a
     * string: Red, Green, Blue.
     *
     *  Params:
     * - IN  int i :  pixel
     *
     */
    public String getRGB(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return red + ", " + green + ", " + blue;
    }

    /*
     * Compares if a pixel has the same RGB values (to determine if the
     * pixel is greyscale or color)
     *
     *  Params:
     * - IN  int i :  pixel
     * - OUT boolean : true if the R, G and B values are the same
     *
     */
    public boolean sameARGB(int pixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        if ((red == green) & (green == blue))
            return true;
        return false;
    }

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
     * Given a PDF document, this function returns the number of
     * pages found in that document.
     *
     *  Params:
     * - IN  string filename : Filename of the PDF document
     * - OUT int : Number of pages in the document
     *
     */
    public int numberOfPages(String filename, String path){

        int n = 0;

        try {
            PdfReader reader = new PdfReader(path+filename+".pdf");
            n = reader.getNumberOfPages();
        } catch(Exception e) { e.printStackTrace(); }

        return n;
    }

    /*
     * Main function of the class. First it renders the pdf document
     * in one image per page (if the document wasn't rendered already).
     * After this, it check for colors on every page and outputs a
     * PDFInfo object containing this information
     *
     *  Params:
     * - IN  string filename : Filename of the PDF document
     * - IN  string path_to_pdf : Path to the folder where all the pdfs are
     * - OUT PDFInfo : Object containing all the PDF information
     *
     */
    public PDFInfo render(String filename, String path_to_pdf){

        int npages = numberOfPages(filename, path_to_pdf);

        // First we check if the document is already rendered
        String check = path_to_pdf+filename+"-"+digits(1,npages)+".jpg";
        System.out.println(check);
        
        File fcheck = new File(check);
        if (fcheck.exists())
            System.out.println(filename+".pdf was already rendered...");

        // If the document is not rendered then we proced with
        // the rendering process
        else {

            // Rendering the whole document at once using pdftoppm

            String cmd = "pdftoppm "; // command
            cmd += "-r 50 -jpeg ";          // flags
            cmd += path_to_pdf+filename+".pdf ";  // source file
            cmd += path_to_pdf+filename;  // source file

            //System.out.println(cmd);

            try {
                System.out.println("Rendering PDF "+filename+".pdf...");
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
                System.out.println("PDF rendered...");
            } catch(Exception e) { e.printStackTrace(); }
            
        }

        // Checking for colors in every page

        System.out.println("Checking for color in pages...");

        // Setting all the variables
        ArrayList<Double> percent_color = new ArrayList<Double>();
        ArrayList<Integer> color_variety = new ArrayList<Integer>();
        ArrayList<Integer> pagenumbers = new ArrayList<Integer>();

        PDFInfo pdf = new PDFInfo();

        // Initializing the histograms
        for (int i = 0; i<npages;i++)
            pdf.histograms.add(new HashMap<Integer, Integer>());

        // Initializing the colors
        for (int i = 0; i<npages;i++)
            pdf.rgbs.add(new RGB());

        long startTime = System.nanoTime();

        try {

            for (int x=0;x<npages;x++) {

                File f = new File(path_to_pdf+filename+"-"+digits(x+1,npages)+".jpg");
                BufferedImage im = ImageIO.read(f);

                int tot = 0;
                int w = im.getWidth();
                int h = im.getHeight();

                for (int i=0;i<w;i++){
                    for (int j=0;j<h;j++){
                        int pix = im.getRGB(i, j);
                        if (!sameARGB(pix)) {
                            tot+=1;
                            pdf.hist(pix,x); // Add it to the histogram
                        }
                    }
                }

                double per = (double)tot/(h*w);
                if (per!=0) {
                    pagenumbers.add(x);
                    percent_color.add(per);
                    color_variety.add(tot);
                }

                
            }

            // Setting all the rgbs
            for (int i = 0; i<npages;i++){
                //System.out.println("Page "+i+": ");
                pdf.rgbs.set(i, pdf.colorAve(pdf.histograms.get(i)));
                //System.out.println(pdf.rgbs.get(i).toString());
            }

            long estimatedTime = System.nanoTime() - startTime;
            System.out.println((double)estimatedTime / 1000000000.0+ " secs. analyzing the rendered images");

            System.out.println("Color Pages / Total Pages = "+pagenumbers.size()+"/"+npages);
//            System.out.println(percent_color);
//            System.out.println(color_variety);
//            System.out.println(pagenumbers);

            // Updating the PDF Object
            pdf.color_variety = color_variety;
            pdf.pagenumbers = pagenumbers;
            pdf.percent_color = percent_color;

        } catch (Exception e) { e.printStackTrace(); }

        System.out.println("Pages checked...");

        return pdf;

    }

}
