/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author jgarrido
 */
public class PDFInfo {

    public ArrayList<Double> percent_color;
    public ArrayList<Integer> color_variety;
    public ArrayList<Integer> pagenumbers;
    public ArrayList<Double> metric1;
    public ArrayList<RGB> rgbs;

    public ArrayList<HashMap<Integer,Integer>> histograms;

    public ArrayList<ArrayList<Integer>> spagenumbers;
    public ArrayList<ArrayList<Double>> smetric1;

    // Empty Constructor
    public PDFInfo() {
        this.color_variety = new ArrayList<Integer>();
        this.percent_color = new ArrayList<Double>();
        this.pagenumbers = new ArrayList<Integer>();
        this.metric1 = new ArrayList<Double>();
        this.spagenumbers = new ArrayList<ArrayList<Integer>>();
        this.smetric1 = new ArrayList<ArrayList<Double>>();
        this.histograms = new ArrayList<HashMap<Integer, Integer>>();
        this.rgbs = new ArrayList<RGB>();
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

    public void hist(int pix, int n){

        // Look for the pixel in the histogram
        Object found = this.histograms.get(n).get(pix);

        if (found == null){ // The pixel wasn't in the histogram. We add it now
            this.histograms.get(n).put(pix, 1);
        }
        else{
            this.histograms.get(n).put(pix, this.histograms.get(n).get(pix) + 1);
        }

    }

    public void sortMetric1(){

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

        this.metric1 = metric2;
        this.pagenumbers = metric1;

    }

    public ArrayList<Integer> newColorSet(PDFInfo v, double saving) {

        ArrayList<Integer> newset = new ArrayList<Integer>();
        int cut = (int) ((double) v.pagenumbers.size() * saving);

        for (int i = 0; i < cut; i++) {
            newset.add(v.pagenumbers.get(i));
        }

        return newset;

    }

    /* Average color in a set of pages */
    public RGB averageColorInSet(ArrayList<Integer> pages, ArrayList<RGB> rgbs){

        int red = 0;
        int blue = 0;
        int green = 0;

        // Totalizing all the RGBs
        for (int i=0; i<pages.size(); i++ ){
            //System.out.println("Page " + pages.get(i) + " - " + rgbs.get(pages.get(i)));
            RGB color = rgbs.get(pages.get(i));
            red += color.getRed();
            blue += color.getBlue();
            green += color.getGreen();
        }

        red = red / pages.size();
        blue = blue / pages.size();
        green = green / pages.size();

        return new RGB(red,green,blue);

    }

    /* Average color in a page */
    public RGB colorAve(HashMap<Integer, Integer> h) {

        int red = 0;
        int blue = 0;
        int green = 0;
        int tot = 0;

        if (h.size()>0) {

            Iterator it = h.entrySet().iterator();
            while (it.hasNext()) {

                Map.Entry pairs = (Map.Entry)it.next();
                int count = (Integer)pairs.getValue();
                Color co = new Color((Integer)pairs.getKey());

                red += (co.getRed()*count);
                blue += (co.getBlue()*count);
                green += (co.getGreen()*count);
                tot += count;

                it.remove(); // avoids a ConcurrentModificationException

            }

            red = red / tot;
            blue = blue / tot;
            green = green / tot;

            return new RGB(red,green,blue);

        }

        return new RGB();

    }

    public double distanceRGB(RGB a, RGB b){
        return Math.sqrt(
                Math.pow(a.getRed()-b.getRed(),2)+
                Math.pow(a.getGreen()-b.getGreen(),2)+
                Math.pow(a.getBlue()-b.getBlue(),2)
                );
    }

    public void mergeSets(){

        System.out.println("");

        // We repeat this process until we have at least 3 clusters
        while (spagenumbers.size()>3){

            ArrayList<RGB> averages = new ArrayList<RGB>();

            // Obtaining the average color for each cluster
            for (int i = 0; i<this.spagenumbers.size(); i++){
                ArrayList<Integer> pages = this.spagenumbers.get(i);
                RGB ave = this.averageColorInSet(pages, this.rgbs);
                averages.add(ave);
            }

            //System.out.println(averages);

            //System.out.println("");

            ArrayList<Double> distances = new ArrayList<Double>();

            // Obtaining the distances between each set of pages
            for (int i = 0; i<averages.size()-1; i++){
                double distance = distanceRGB(averages.get(i),averages.get(i+1));
                distances.add(distance);
            }

            System.out.println("Distances: "+distances);

            // Search for the minimum distance between a pair of sets
            Double min = Double.MAX_VALUE;
            for (int i = 0; i<distances.size(); i++){
                if (distances.get(i)<min)
                    min = distances.get(i);
            }
            int index = distances.indexOf(min);

            // Now that we know what sets we have to join, lets just join them
            ArrayList<Integer> newset = new ArrayList<Integer>();
            for (int i=0; i<spagenumbers.get(index).size(); i++)
                newset.add(spagenumbers.get(index).get(i));
            for (int i=0; i<spagenumbers.get(index+1).size(); i++)
                newset.add(spagenumbers.get(index+1).get(i));

            spagenumbers.remove(index+1);
            spagenumbers.set(index, newset);

            System.out.println(spagenumbers);
            
        }

        System.out.println("");

    }

    public void findSets(){

        double threshold = 0.1;

        this.smetric1 = new ArrayList<ArrayList<Double>>();
        this.spagenumbers = new ArrayList<ArrayList<Integer>>();

        ArrayList<Integer> set = new ArrayList<Integer>();
        ArrayList<Double> setmetric = new ArrayList<Double>();
        double cur, nex = 0.0;

        set.add(this.pagenumbers.get(0));
        setmetric.add(this.metric1.get(0));

        for (int i = 0; i<this.pagenumbers.size()-1; i++){

            cur = this.metric1.get(i);
            nex = this.metric1.get(i+1);

//            System.out.println(nex);
//            System.out.println(cur);
//            System.out.println(1-(nex/cur));

            if ((1-(nex/cur)) < threshold) {
                set.add(this.pagenumbers.get(i+1));
                setmetric.add(this.metric1.get(i+1));
            }
            else {
//                System.out.println("Change");
                this.spagenumbers.add((ArrayList<Integer>)set.clone());
                this.smetric1.add((ArrayList<Double>)setmetric.clone());
                set.clear();
                setmetric.clear();
                set.add(this.pagenumbers.get(i+1));
            }

        }

        this.spagenumbers.add((ArrayList<Integer>)set.clone());

    }
    



}
