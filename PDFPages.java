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

    public Vector<Double> percent_color;
    public Vector<Integer> color_variety;
    public Vector<Integer> pagenumbers;
    public Vector<Double> metric1;
    public int len;

    public PDFPages(Vector<Double> percent_color, Vector<Integer> color_variety, Vector<Integer> pagenumbers) {

        this.percent_color = percent_color;
        this.color_variety = color_variety;
        this.pagenumbers = pagenumbers;
        this.len = pagenumbers.size();

        // Initializing the Metric1 array
        for (int i = 0; i<this.len; i++)
            this.metric1.add((double)this.getColor_variety().get(i)*this.getPercent_color().get(i));
        
    }

    public PDFPages() {
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

    public Vector<Double> getMetric1() {
        return this.metric1;
    }

    public int getSize(){
        return this.len;
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
