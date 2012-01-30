/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import java.awt.image.BufferedImage;
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

}
