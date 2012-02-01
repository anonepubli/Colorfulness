/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import java.util.Arrays;
import java.util.Vector;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

/**
 *
 * @author jgarrido
 */
public class Clustering {

    public Instance[] instances;
    PDFInfo p;
    double saving;

    public Clustering(PDFInfo p, double saving){

        // Creating the instances to start clustering the data
        this.instances = new Instance[p.getSize()];
        for(int i = 0; i < p.getSize(); i++) {
            // Creating array for clustering
            double [] insta = {(double)p.getColor_variety().get(i),p.getPercent_color().get(i)};
            //double [] insta = {(double)p.getColor_variety().get(i)*p.getPercent_color().get(i)};
            // Adding the array to the instances
            this.instances[i] = new DenseInstance(insta,
                    p.getPagenumbers().get(i));
        }
        this.p = p;
        this.saving = saving;
        
    }

    public Vector<Integer> cluster(){

        int number_of_clusters = (int)Math.ceil(this.p.len/4);
        int its = 100;

        Dataset data = new DefaultDataset();
        data.addAll(Arrays.asList(instances));
        Dataset[] best_clusters = null;
        double best_score = 999999999999.0;

        for (int niter=0; niter<5; niter++) {

            Clusterer km = new KMeans(number_of_clusters, its);
                  //  new CosineSimilarity());
            System.out.println("Clustering...");
            Dataset[] clusters = km.cluster(data);
            System.out.println("End Clustering");

            ClusterEvaluation sse = new SumOfSquaredErrors();
            double score = sse.score(clusters);
            System.out.println("Score = "+score);
            System.out.println("");

            if (score<best_score){
                best_score = score;
                best_clusters = clusters;
            }

        }

        Vector<Integer> fpages = new Vector<Integer>();
        Vector<Double> averages = new Vector<Double>();

        System.out.println("Running Clustering with:");
        System.out.println("Number of clusters = "+number_of_clusters);
        System.out.println("Iterations = "+its);
        System.out.println("Distance Measure = Cosine Similarity");
        System.out.println("Best Score = "+best_score);
        System.out.println("");

        System.out.println("Clusters found: " + best_clusters.length);

        for (int i=0;i<number_of_clusters;i++){

            System.out.println("");
            System.out.println("");
            System.out.println(" ========== Cluster #"+(i+1)+" size: "
                    +best_clusters[i].size()+" ========== ");
            System.out.println("");
            double tot = 0;
            for (int j=0;j<best_clusters[i].size();j++) {
                Integer page = (Integer)best_clusters[i].get(j).classValue();
                int key = p.pagenumbers.indexOf(page);
                tot += p.percent_color.get(key);
                System.out.println(page+1);
            }
            double aver = tot/best_clusters[i].size();
            System.out.println("Average = "+aver);
            averages.add(aver);
        }

        // Selecting the most suitabble pages from the clusters found
        int target = (int)(p.getSize()*saving);
        int tot = 0;

        while (tot < target){
            int select = max_douvector(averages);
            for (int i=0; i<best_clusters[select].size(); i++){
                fpages.add((Integer)best_clusters[select].get(i).classValue());
                tot += 1;
            }
            averages.set(select, -1.0);
        }

        return fpages;

    }

    public int max_douvector(Vector<Double> averages){

        double top = -9999999.0;
        int index = 0;

        for (int i=0;i<averages.size();i++){
            if (averages.get(i)>top){
                top = averages.get(i);
                index = i;
            }
        }

        return index;

    }


}
