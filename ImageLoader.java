/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author jgarrido
 */
public class ImageLoader {
    
    private BufferedImage image;

    public ImageLoader() {
    }

    // Draw the image located at filename into the screen
    public void run(String filename, String n){
     ImageIcon ii=new ImageIcon(filename);
     JLabel label=new JLabel(ii);
     JFrame frame=new JFrame(String.valueOf(n));
     frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
     frame.getContentPane().add(label);
     frame.setSize(500,700);
     frame.pack();
     frame.setVisible(true);
    }

    // Draw all the images of a set inside a Browser window
    public void draw (ArrayList<ArrayList<Integer>> pages, String filename, String path, int npages){

        String serial = "";
        
        for (int i=0; i<pages.size();i++){
            for (int j = 0; j<pages.get(i).size(); j++){
                serial += (pages.get(i).get(j)+1)+",";
            }
            serial+="-";
        }

        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://localhost/colorfulnes/colorout.php?serial="+serial+"&filename="+filename+"&npages="+npages));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(serial);
        
    }

}
