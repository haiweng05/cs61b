package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private Percolation percolation;
    private int n;
    private int t;
    private double sum;
    private double[] res;
    private int[] order;


    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw(new java.lang.IllegalArgumentException("N or T less or equal to 0"));
        }
        n = N;
        t = T;
        res = new double[T];
        order = new int[N * N];
        for (int i = 0; i < N * N; ++i) {
            order[i] = i;
        }
        for (int i = 0; i < T; ++i) {
            percolation = pf.make(N);
            StdRandom.shuffle(order);

            for (int num = 1; num <= N * N; ++num) {
                int value = order[num - 1];
                percolation.open(value / N, value % N);
                if (percolation.percolates()) {
                    res[i] = 1.0 * num / (N * N);
                    break;
                }
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(res, 0, res.length);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(res, 0, res.length);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(t);
    }

}
