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
public class PDFPages {

    private Vector<Double> percent_color;
    private Vector<Integer> color_variety;
    private Vector<Integer> pagenumbers;
    private int size;

    public PDFPages(Vector<Double> percent_color, Vector<Integer> color_variety, Vector<Integer> pagenumbers) {

        this.percent_color = percent_color;
        this.color_variety = color_variety;
        this.pagenumbers = pagenumbers;
        this.size = pagenumbers.size();
        
    }

    public Vector<Integer> getColor_variety() {
        return color_variety;
    }

    public Vector<Integer> getPagenumbers() {
        return pagenumbers;
    }

    public Vector<Double> getPercent_color() {
        return percent_color;
    }

    public int size(){
        return size;
    }

    public Vector<Integer> sortMetric1(){

        Vector<Integer> pn = (Vector<Integer>)this.pagenumbers.clone();
        Vector<Integer> metric1 = new Vector<Integer>();
        Vector<Double> metric2 = new Vector<Double>();

        double accum = 0;
        int max = 0;
        for (int i=0; i<this.pagenumbers.size(); i++){
            for (int j=0; j<this.pagenumbers.size(); j++){
                if (pn.get(j)!=-1) {
                    double x = this.color_variety.get(j)*this.percent_color.get(j);
                    if (x>accum) {
                        accum = x;
                        max = j;
                    }
                }
            }
            pn.set(max, -1);
            metric1.add(pagenumbers.get(max));
            metric2.add(accum);
            max = 0;
            accum = 0;
        }

        return metric1;

    }

    public Vector<Integer> newColorSet(Vector<Integer> v, double saving){

        Vector<Integer> newset = new Vector<Integer>();
        int cut = (int)((double)v.size()*saving);

        for (int i=0; i<cut; i++){
            newset.add(v.get(i));
        }

        return newset;

    }
    



}
