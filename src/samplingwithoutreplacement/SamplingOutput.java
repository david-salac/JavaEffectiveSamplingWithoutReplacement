/*
 * Java utility for sampling without replacement
 * Using algorithm described in: Jeffrey Scott Vitter. 1987. An efficient algorithm for sequential random sampling. ACM Trans. Math. Softw. 13, 1 (March 1987), 58-67. DOI: https://doi.org/10.1145/23002.23003
 */
package samplingwithoutreplacement;

/**
 * Interface for working with selected sample
 * @author David Salac
 * @param <T> Type of sample
 */
public interface SamplingOutput<T> {
    public void sampleCall(T entity, int currentIndex, int distanceFromLast);
}