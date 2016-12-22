/*------------------------------------------------------------
 * Name:    Abhinav
 * Programming Assignment 1: PercolationStats
 * Dependencies: Percolation.java, StdRandom, StdStats
 * 
 * 
 *-----------------------------------------------------------*/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private int n; // grid-size n-by-n
    private int trials; // number of experiments
    private int openSites; // No. of open sites in each experiment (percolates)
    private int totalSites; // n*n
    private Percolation perc; // object of Percolation data type
    private double[] fractionOfOpenSites = null; // array to store fraction 
    // of open sites in each experiment, when the system percolates
    
    /**
     * Perform trials independent experiments on an n-by-n grid
     * @param n grid-size n-by-n where n {@literal >} 0
     * @param trials number of computation experiments {@literal >} 0
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("grid size n and number of "
                    + "experiments must be > 0");
        }
        
        this.n = n;
        this.trials = trials;
        totalSites = n * n;     
        fractionOfOpenSites = new double[trials];
        
        for (int t = 0; t < trials; t++) {
            openSites = 0; // reset open sites for each experiment
            perc = new Percolation(this.n); // create new Percolation object
            // n*n grid of blocked sites - initially does not percolate
            
            while (!perc.percolates()) {
                // Pick a site at random and open if it is blocked
                int i = StdRandom.uniform(1, this.n + 1);
                int j = StdRandom.uniform(1, this.n + 1);
                if (!perc.isOpen(i,  j)) {
                    perc.open(i,  j);
                    openSites++; // increment open sites counter
                }
            }
            
            // When system percolates, add fraction of open sites to the array
            fractionOfOpenSites[t] = openSites*1.0 / totalSites;
            /* System.out.println("Trial# " + t + " Open sites " + openSites + 
            ", Fraction open = " + fractionOfOpenSites[t]); */
        }
    }
    
    /**
     * Calculate sample mean of percolation threshold across all experiments
     * @return sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(fractionOfOpenSites);
    }
    
    /**
     * Calculate sample standard deviation of percolation threshold
     * across all experiments.
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(fractionOfOpenSites);
    }
    
    /**
     * Calculate low endpoint of 95% confidence interval across all experiments
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return (mean() - (1.96 * stddev() / Math.sqrt(this.trials)));
    }

    /**
     * Calculate high endpoint of 95% confidence interval across all experiments
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return (mean() + (1.96 * stddev() / Math.sqrt(this.trials)));
    }

    /**
     * Test client to perform T independent computational experiments on an 
     * n-by-n grid, and prints the mean, standard deviation, and the 
     * 95% confidence interval for the percolation threshold
     * @param args n (for n-by-n grid), T (number of experiments)
     */
    public static void main(String[] args) {
 
        int gridSize = Integer.parseInt(args[0]); // n-by-n grid
        int numberOfExperiments = Integer.parseInt(args[1]); // number of experiments
        PercolationStats ps = new PercolationStats(gridSize, numberOfExperiments);
        

        System.out.println("mean                       = " + ps.mean());
        System.out.println("stddev                     = " + ps.stddev());
        System.out.println("95% confidence interval    = " + ps.confidenceLo() + ", " + ps.confidenceHi());
    }
}
