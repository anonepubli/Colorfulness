/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import java.util.ArrayList;

/**
 *
 * @author jgarrido
 */
public class PDFInfo {

    public ArrayList<Double> percent_color;
    public ArrayList<Integer> color_variety;
    public ArrayList<Integer> pagenumbers;
    public ArrayList<Double> metric1;
    public int len;

    public PDFInfo(ArrayList<Double> percent_color, ArrayList<Integer> color_variety, ArrayList<Integer> pagenumbers) {

        this.percent_color = percent_color;
        this.color_variety = color_variety;
        this.pagenumbers = pagenumbers;
        this.len = pagenumbers.size();

        // Initializing the Metric1 array
        for (int i = 0; i<this.len; i++)
            this.metric1.add((double)this.getColor_variety().get(i)*this.getPercent_color().get(i));
        
    }

    public PDFInfo() {
    }

    public ArrayList<Integer> getColor_variety() {
        return color_variety;
    }

    public ArrayList<Integer> getPagenumbers() {
        return pagenumbers;
    }

    public ArrayList<Double> getPercent_color() {
        return percent_color;
    }

    public ArrayList<Double> getMetric1() {
        return this.metric1;
    }

    public int getSize(){
        return this.len;
    }

    public PDFInfo sortMetric1(){

        ArrayList<Integer> pn = (ArrayList<Integer>)this.pagenumbers.clone();
        ArrayList<Integer> metric1 = new ArrayList<Integer>();
        ArrayList<Double> metric2 = new ArrayList<Double>();

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

        PDFInfo pdf = new PDFInfo();
        pdf.metric1 = metric2;
        pdf.pagenumbers = metric1;

        return pdf;

    }

    public ArrayList<Integer> newColorSet(PDFInfo v, double saving){

        ArrayList<Integer> newset = new ArrayList<Integer>();
        int cut = (int)((double)v.pagenumbers.size()*saving);

        for (int i=0; i<cut; i++){
            newset.add(v.pagenumbers.get(i));
        }

        return newset;

    }

    public ArrayList<ArrayList<Integer>> findSets(){

        double threshold = 0.5;

        ArrayList<Integer> set = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> bigset = new ArrayList<ArrayList<Integer>>();
        double cur, nex = 0.0;

        set.add(this.pagenumbers.get(0));

        for (int i = 0; i<this.pagenumbers.size()-1; i++){

            cur = this.metric1.get(i);
            nex = this.metric1.get(i+1);

//            System.out.println(nex);
//            System.out.println(cur);
//            System.out.println(1-(nex/cur));

            if ((1-(nex/cur)) < threshold)
                set.add(this.pagenumbers.get(i+1));
            else {
//                System.out.println("Change");
                bigset.add((ArrayList<Integer>)set.clone());
                set.clear();
                set.add(this.pagenumbers.get(i+1));
            }

        }

        bigset.add((ArrayList<Integer>)set.clone());
        
        return bigset;

    }
    



}
