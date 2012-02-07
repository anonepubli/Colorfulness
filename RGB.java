/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

/**
 *
 * @author jgarrido
 */
public class RGB {

    private int red;
    private int blue;
    private int green;

    public RGB(int red, int green, int blue) {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public RGB() {
        this.red = -1;
        this.blue = -1;
        this.green = -1;
    }

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }

    public int getRed() {
        return red;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setRed(int red) {
        this.red = red;
    }

    @Override
    public String toString(){
        return red + " " + green + " " + blue;
    }



}
