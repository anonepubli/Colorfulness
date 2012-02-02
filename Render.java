/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import com.itextpdf.text.pdf.PdfReader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;
import javax.imageio.ImageIO;

/**
 *
 * @author jgarrido
 */
public class Render {

    public Render() {
    }

    public void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
    }

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
    public int numberOfPages(String filename){

        int n = 0;

        try {
            PdfReader reader = new PdfReader("pdfs/"+filename+".pdf");
            n = reader.getNumberOfPages();
        } catch(Exception e) { System.out.println(e.toString()); }

        return n;
    }

    public void render(String filename, String path_to_pdf, PDFInfo pdf){

        int npages = numberOfPages(filename);
        boolean rendered = false;

        // First we check if the document is already rendered
        String check = path_to_pdf+filename+"-"+digits(1,npages)+".pdf";
        try{
            File fcheck = new File(check);
            rendered = true;
            System.out.println(filename+".pdf was already rendered...");
        } catch(Exception e) { 
            System.out.println(e.toString());
        }

        // If the document is not rendered then we proced with
        // the rendering process
        if (!rendered) {

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
            } catch(Exception e) { System.out.println(e.toString()); }
            
        }

        // Checking for colors in every page

        System.out.println("Checking for color in pages...");

        Vector<Double> percent_color = new Vector<Double>();
        Vector<Integer> color_variety = new Vector<Integer>();
        Vector<Integer> pagenumbers = new Vector<Integer>();

        long startTime = System.nanoTime();

        try {

            for (int x=1;x<npages;x++) {

                File f = new File(path_to_pdf+filename+"-"+digits(x,npages)+".jpg");
                BufferedImage im = ImageIO.read(f);

                int tot = 0;
                int w = im.getWidth();
                int h = im.getHeight();

                for (int i=0;i<w;i++){
                    for (int j=0;j<h;j++){
                        if (!sameARGB(im.getRGB(i, j)))
                            tot+=1;
                    }
                }

                double per = (double)tot/(h*w);
                if (per!=0) {
                    pagenumbers.add(x);
                    percent_color.add(per);
                    color_variety.add(tot);
                }
                
            }

            long estimatedTime = System.nanoTime() - startTime;
            System.out.println((double)estimatedTime / 1000000000.0);

            System.out.println("Color Pages / Total Pages = "+pagenumbers.size()+"/"+npages);
            System.out.println(percent_color);
            System.out.println(color_variety);
            System.out.println(pagenumbers);

            // Updating the PDF Object
            pdf.color_variety = color_variety;
            pdf.pagenumbers = pagenumbers;
            pdf.percent_color = percent_color;
            pdf.len = pagenumbers.size();

            

        } catch (Exception e) { e.printStackTrace(); }

        System.out.println("Pages checked...");
        
    }

}
