/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package colorfulnes;

import java.util.Arrays;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.CosineSimilarity;

/**
 *
 * @author jgarrido
 */
public class Clustering {

    public Instance[] instances;

    public Clustering(PDFPages p){

        // Creating the instances to start clustering the data
        this.instances = new Instance[p.getSize()];
        for(int i = 0; i < p.getSize(); i++) {
            // Creating array for clustering
            double [] insta = {(double)p.getColor_variety().get(i),p.getPercent_color().get(i)};
            // Adding the array to the instances
            this.instances[i] = new DenseInstance(insta,
                    p.getPagenumbers().get(i));
        }
        
    }

    public void cluster(){

        int number_of_clusters = 4;
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
            for (int j=0;j<best_clusters[i].size();j++) {
                Integer page = (Integer)best_clusters[i].get(j).classValue();
                System.out.println(page+1);
            }
        }

    }


}
