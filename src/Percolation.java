import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private int N;
	private boolean[][] sites;
	private int topVirtualSite;
	private int bottomVirtualSite;
	WeightedQuickUnionUF wquf;
	
	public Percolation(int n) { // create n-by-n grid, with all sites blocked
		if (n <= 0) {
			throw new IllegalArgumentException("N must be > 0");
		}
		else {
			N = n;
			wquf = new WeightedQuickUnionUF(n*n + 2); // +2 for top and bottom virtual sites
			sites = new boolean[n][n]; // all sites blocked
			topVirtualSite = 0;
			bottomVirtualSite = N*N + 1;
		}		 
	}
	
	public void open(int row, int col) { // open site (row, col) if it is not open already
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
	
	public boolean isOpen(int row, int col) {
		validateIndices(row, col);
		return sites[row - 1][col - 1];		
	}
	
	public boolean isFull (int row, int col) { // is site (row, col) full?
		validateIndices(row, col);
		if(isOpen(row, col) && wquf.connected(xyTo1D(row, col), topVirtualSite)) {
			return true;
		}
		else
			return false;		
	}
	
	public boolean percolates() { // does the system percolate?
		return wquf.connected(topVirtualSite, bottomVirtualSite);
		
	}
	
	private int xyTo1D(int x, int y) {		
		return (x - 1) * N + y;
		
	}
	
	private void validateIndices(int row, int col) {
		if (row < 1 || col < 1 || row > N || col > N) {
			throw new IndexOutOfBoundsException("Invalid indices: row and column indices must be between 1 and " + N);
		}
	}
	
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
