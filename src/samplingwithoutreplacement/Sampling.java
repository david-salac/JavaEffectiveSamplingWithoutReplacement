/*
 * Java utility for sampling without replacement
 * Using algorithm described in: Jeffrey Scott Vitter. 1987. An efficient algorithm for sequential random sampling. ACM Trans. Math. Softw. 13, 1 (March 1987), 58-67. DOI: https://doi.org/10.1145/23002.23003
 */
package samplingwithoutreplacement;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * Interface for working with selected sample
 * @author David Salac
 * @param <E> Type of a sample
 */
public class Sampling<E> {
    Random rand;
    int N; //Size of collection
    int n; //Number of selected samples
    
    int skipStep; 
    int currentIndex;
    Collection<E> collection;
    SamplingOutput<E> outputFunction;
    Iterator<E> collectionIterator;
    
    public Sampling(Collection<E> collection, SamplingOutput<E> outputFunction, int n) {
        this.outputFunction = outputFunction; this.collectionIterator = collection.iterator();
        this.collection = collection; this.N = collection.size(); this.n = n; this.rand = new Random(); 
        this.skipStep = 0; this.currentIndex = 0;
    }
    
    private void runSampling() {
        int skip = 0;
        E nextElement = collectionIterator.next();
        while (collectionIterator.hasNext() && skip < skipStep) {
            nextElement = collectionIterator.next();
            ++skip;
        }
        outputFunction.sampleCall(nextElement, currentIndex, skipStep);
    }
    
    double UNIFORMRV() {
        return rand.nextDouble();
    }
    
    void methodA() {
        double top = N - n; double Nreal = N;
        if (n == 1) { //SPECIAL CASE n=1
            //RUN SAMPLING
            skipStep = (int) (Nreal*UNIFORMRV());
            currentIndex += skipStep;
            runSampling();
            return;
        }
        while (n >= 2) {
            double V = UNIFORMRV(); int S = 0; double quot = top / Nreal;
            while (quot > V) {
                ++S; top = -1.0 + top; Nreal = -1.0 + Nreal;
                quot = (quot * top) / Nreal;
            }
            //RUN SAMPLING
            skipStep = S;
            currentIndex += skipStep;
            runSampling();
            
            Nreal = -1.0 + Nreal; --n;
        }
        
    }
    void methodD() {
        double nreal = n; double ninv = 1.0 / nreal; double Nreal = N;
        double Vprime = Math.exp(Math.log(UNIFORMRV()) * ninv);
        int qu1 = -n + 1 + N; double qu1real = -nreal + 1.0 + Nreal;
        int negalphinv = -13; int threshold = -negalphinv * n;
        
        int S; double negSreal; 
        while ( (n > 1) && (threshold < N)) { 
            double nmin1inv = 1.0 / (-1.0 + nreal);
            while (true) {
                double X;
                //STEP D2
                while (true) { 
                    X = Nreal * (-Vprime + 1.0); S = (int)X;
                    if(S < qu1) {
                        break;
                    }
                    Vprime = Math.exp(Math.log(UNIFORMRV()) * ninv);
                }
                double U = UNIFORMRV(); negSreal = -S;
                //Step D3
                double y1 = Math.exp(Math.log(U * Nreal / qu1real) * nmin1inv);
                Vprime = y1 * (-X / Nreal + 1.0) * (qu1real / (negSreal + qu1real));
                if(Vprime <= 1.0)  { 
                    break;
                }
                //Step D4
                double y2 = 1.0;  double top = -1.0 + Nreal;
                
                double bottom, limit;
                if(-1 + n > S) {
                    bottom = -nreal + Nreal;
                    limit = -S + N;
                }
                else {
                    bottom = -1.0  + negSreal + Nreal; limit = qu1;
                }
                for (int t = -1 + N; t >= limit; --t) {
                    y2 = (y2 * top) / bottom;
                    top = -1.0 + top; bottom = -1.0 + bottom;
                }
                if (Nreal / (-X + Nreal) >= y1 * Math.exp(Math.log(y2) * nmin1inv)) {
                    //ACCEPT
                    Vprime = Math.exp(Math.log(UNIFORMRV()) * nmin1inv);
                    break;
                }
                Vprime = Math.exp(Math.log(UNIFORMRV()) * ninv);
            }
            //Step D5
            skipStep = S; //SELECT THE RECORD
            currentIndex += skipStep;
            runSampling();
            
            N = -S + (-1 + N); Nreal = negSreal + (-1.0 + Nreal);
            --n; nreal = -1.0 + nreal; ninv = nmin1inv;
            qu1 = -S + qu1; qu1real = negSreal + qu1real;
            threshold = threshold + negalphinv;
        }
        if(n >= 1) methodA();
    }
    
    public void sample() {
        this.methodD();
    }
}