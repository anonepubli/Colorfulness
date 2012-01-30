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

    public void load(PDFPages p){
        
        // Creating the instances to start clustering the data
        Instance[] instances = new Instance[p.size()];
        for(int i = 0; i < instances.length; i++) {
            instances[i] = new DenseInstance(col[i].getVsm(),
                    col[i].getTweet().getText());
        }
        
    }

    public String cluster(){

        
        Dataset data = new DefaultDataset();
        data.addAll(Arrays.asList(instances));
        Dataset[] best_clusters = null;
        double best_score = 999999999999.0;

        for (int niter=0; niter<5; niter++) {

            Clusterer km = new KMeans(number_of_clusters, its,
                    new CosineSimilarity());
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

        return "";

    }


}
