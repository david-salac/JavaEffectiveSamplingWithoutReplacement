/*
 * Java utility for sampling without replacement
 * Using algorithm described in: Jeffrey Scott Vitter. 1987. An efficient algorithm for sequential random sampling. ACM Trans. Math. Softw. 13, 1 (March 1987), 58-67. DOI: https://doi.org/10.1145/23002.23003
 */
package samplingwithoutreplacement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Demo of Sampling Without Replacement in Java
 * @author David Salac
 */
public class SamplingWithoutReplacement {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Random rand = new Random();
        int n = 40;
        int N = 700;
        Collection<Integer> collection = new ArrayList<>();
        for(int i = 0; i < N; i++) {
            collection.add(rand.nextInt());
        } 
        
        SamplingOutput<Integer> writeIt = (Integer entity, int currentIndex, int distanceFromLast) -> {
            System.out.println("Selected: "  + entity + ", current index: " + currentIndex + ", index offset: " + distanceFromLast);
        };
        System.out.println("Size of collection: " + collection.size() + ", number of samples: " + n);
        Sampling test = new Sampling(collection, writeIt, n);
        test.sample();
    }
    
}
