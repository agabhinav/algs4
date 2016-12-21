/*------------------------------------------------------------
 * Name:    Abhinav
 * Programming Assignment 1: Percolation
 * Dependencies: WeightedQuickUnionUF
 * 
 *-----------------------------------------------------------*/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private int N; // grid size n-by-n
	private boolean[][] sites; // track open-blocked sites
	private int topVirtualSite; // top virtual site
	private int bottomVirtualSite; // bottom virtual site
	WeightedQuickUnionUF wquf; // object for UF data structure
	
	/**
	 * Model a percolation system using an n-by-n grid of sites, 
	 * with all sites initially blocked.
	 * Initialize an empty WeightedQuickUnionUF data structure with
	 * (n*n + 2) sites.
	 * +2 is for two virtual sites at the top and at the bottom.
	 * 
	 * Throws IllegalArgumentException if n {@literal <=} 0
	 * 
	 * @param n grid-size n-by-n where n {@literal >} 0
	 */
	public Percolation(int n) {
		if (n <= 0) {
		    throw new IllegalArgumentException("N must be > 0");
		}
		else {
		    N = n;
		    // +2 for top and bottom virtual sites
		    wquf = new WeightedQuickUnionUF(n*n + 2);
		    sites = new boolean[n][n];    // all sites blocked
		    topVirtualSite = 0;
		    bottomVirtualSite = N*N + 1;
		    }
    }
	
	/**
	 * Open site (row, col) if it is not open already.
	 * 
	 * Throws IndexOutOfBoundsException if row, col not in [1, n].
	 * 
	 * @param row site's row-index [1, n]
	 * @param col site's column-index [1, n]
	 */
	public void open(int row, int col) {
		validateIndices(row, col);
		int currentSiteIndex = xyTo1D(row, col);
		if(!isOpen(row, col)) { // open the site and connect to open neighbors
			sites[row - 1][col - 1] = true;
			
			if (row == 1) { // connect to top virtual site
				wquf.union(currentSiteIndex, topVirtualSite);
			}
			
			if (row == N) { // connect to bottom virtual site
				wquf.union(currentSiteIndex, bottomVirtualSite);
			}
			
			if (col < N && isOpen(row, col + 1)) { // right neighbor
				wquf.union(currentSiteIndex, xyTo1D(row, col + 1));
			}
			
			if (col > 1 && isOpen(row, col - 1)) { // left neighbor
				wquf.union(currentSiteIndex, xyTo1D(row, col - 1));
			}

			if (row > 1 && isOpen(row - 1, col)) { // top neighbor
				wquf.union(currentSiteIndex, xyTo1D(row - 1, col));
			}
			
			if (row < N && isOpen(row + 1, col)) { // bottom neighbor
				wquf.union(currentSiteIndex, xyTo1D(row + 1, col));
			}				
		}			
	}
	
	/**
	 * Check whether a site (row, col) is open.
	 * 
	 * Throws IndexOutOfBoundsException if row, col not in [1, n].
	 * 
	 * @param row site's row-index [1, n]
	 * @param col site's column-index [1, n]
	 * @return true or false
	 */
	public boolean isOpen(int row, int col) {
		validateIndices(row, col);
		return sites[row - 1][col - 1];		
	}
	
	/**
	 * Check whether a site (row, col) is full. A full site is an open site 
	 * that can be connected to an open site in the top row via a chain of 
	 * neighboring (left, right, up, down) open sites.
	 * 
	 * Throws IndexOutOfBoundsException if row, col not in [1, n].
	 * 
	 * @param row site's row-index [1, n]
	 * @param col site's column-index [1, n]
	 * @return true or false
	 */
	public boolean isFull (int row, int col) {
		validateIndices(row, col);
		if(isOpen(row, col) && 
		        wquf.connected(xyTo1D(row, col), topVirtualSite)) {
			return true;
		}
		else
			return false;		
	}
	
	/**
	 * Check whether the system percolates. The system percolates if there 
	 * is a full site in the bottom row
	 * @return true or false
	 */
	public boolean percolates() {
		return wquf.connected(topVirtualSite, bottomVirtualSite);
	}
	
	/**
	 * Map from a 2-dimensional (row, column) pair to a 1-dimensional 
	 * union find object index
	 * @param x site's row-index [1, n]
	 * @param y site's column-index [1, n]
	 * @return an integer indicating 1-d UF object index
	 */
	private int xyTo1D(int x, int y) {		
		return (x - 1) * N + y;
	}
	
	/**
	 * Validate row and column indices. Throw IndexOutOfBoundsException
	 * exception if row and column indices are not between [1, n].
	 * 
	 * Throws IndexOutOfBoundsException if row, col not in [1, n].
	 * 
	 * @param row site's row-index [1, n]
	 * @param col site's column-index [1, n]
	 */
	private void validateIndices(int row, int col) {
		if (row < 1 || col < 1 || row > N || col > N) {
			throw new IndexOutOfBoundsException("Invalid indices: "
			        + "row and column indices must be between 1 and " + N);
		}
	}
	
	// test client
	public static void main(String[] args) {
		Percolation p = new Percolation(4);		
		p.open(1, 1);
		p.open(2, 2);
		System.out.println(p.percolates());
		p.open(2, 3);
		p.open(3, 3);
		p.open(4, 3);
		System.out.println(p.percolates());
		p.open(2, 1);
		System.out.println(p.percolates());
		System.out.println(p.isFull(4, 4));
	}
}
