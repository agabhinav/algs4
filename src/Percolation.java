/*------------------------------------------------------------
 * Name:    Abhinav
 * Programming Assignment 1: Percolation
 * Dependencies: WeightedQuickUnionUF
 * 
 *-----------------------------------------------------------*/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int gridSize; // grid size n-by-n
    private boolean[][] sites; // track open-blocked sites
    private int topVirtualSite; // top virtual site
    private int bottomVirtualSite; // bottom virtual site
    private WeightedQuickUnionUF uf; // object for UF data structure with both top and bottom virtual sites
    private WeightedQuickUnionUF ufNoBottom; // object for UF data structure with only top virtual site
    
    /**
     * Model a percolation system using an n-by-n grid of sites, with all sites initially blocked.
     * Initialize an empty WeightedQuickUnionUF data structure with (n*n + 2) sites.
     * +2 is for two virtual sites at the top and at the bottom. This data structure is used to determine whether the
     * system percolates.
     * Initialize a second WQUF data structure with no bottom site. This is used to check if a site is full,
     * so as to avoid backwash problem that occurs with uf data structure with both top and bottom virtual sites.
     * Throws IllegalArgumentException if n {@literal <=} 0
     * @param n grid-size n-by-n where n {@literal >} 0
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        }
        else {
            gridSize = n;
            uf = new WeightedQuickUnionUF(n*n + 2); // +2 for top and bottom virtual sites
            ufNoBottom = new WeightedQuickUnionUF(n*n + 1); // no bottom virtual site
            sites = new boolean[n][n];    // all sites blocked
            topVirtualSite = 0;
            bottomVirtualSite = n*n + 1;
            }
    }
    
    /**
     * Open site (row, col) if it is not open already.
     * Throws IndexOutOfBoundsException if row, col not in [1, n].
     * @param row site's row-index [1, n]
     * @param col site's column-index [1, n]
     */
    public void open(int row, int col) {
        validateIndices(row, col);
        int currentSiteIndex = xyTo1D(row, col);

        if (!isOpen(row, col)) { // open the site and connect to open neighbors
            sites[row - 1][col - 1] = true;
            
            if (row == 1) { // connect to top virtual site
                uf.union(currentSiteIndex, topVirtualSite);
                ufNoBottom.union(currentSiteIndex, topVirtualSite);
            }
            
            if (row == gridSize) { // connect to bottom virtual site
                uf.union(currentSiteIndex, bottomVirtualSite);
            }
            
            if (col < gridSize && isOpen(row, col + 1)) { // right neighbor
                uf.union(currentSiteIndex, xyTo1D(row, col + 1));
                ufNoBottom.union(currentSiteIndex, xyTo1D(row, col + 1));
            }
            
            if (col > 1 && isOpen(row, col - 1)) { // left neighbor
                uf.union(currentSiteIndex, xyTo1D(row, col - 1));
                ufNoBottom.union(currentSiteIndex, xyTo1D(row, col - 1));
            }

            if (row > 1 && isOpen(row - 1, col)) { // top neighbor
                uf.union(currentSiteIndex, xyTo1D(row - 1, col));
                ufNoBottom.union(currentSiteIndex, xyTo1D(row - 1, col));
            }
            
            if (row < gridSize && isOpen(row + 1, col)) { // bottom neighbor
                uf.union(currentSiteIndex, xyTo1D(row + 1, col));
                ufNoBottom.union(currentSiteIndex, xyTo1D(row + 1, col));
            }                
        }
    }
    
    /**
     * Check whether a site (row, col) is open.
     * Throws IndexOutOfBoundsException if row, col not in [1, n].
     * @param row site's row-index [1, n]
     * @param col site's column-index [1, n]
     * @return true or false
     */
    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return sites[row - 1][col - 1];        
    }
    
    /**
     * Check whether a site (row, col) is full. A full site is an open site that can be 
     * connected to an open site in the top row via a chain of neighboring (left, right, up, down) open sites.
     * Throws IndexOutOfBoundsException if row, col not in [1, n].
     * @param row site's row-index [1, n]
     * @param col site's column-index [1, n]
     * @return true or false
     */
    public boolean isFull(int row, int col) { // use uf object with no bottom site
        validateIndices(row, col);
        return (isOpen(row, col) && ufNoBottom.connected(xyTo1D(row, col), topVirtualSite));
    }
    
    /**
     * Check whether the system percolates. The system percolates if there is a full site in the bottom row
     * @return true or false
     */
    public boolean percolates() {
        return uf.connected(topVirtualSite, bottomVirtualSite);
    }
    
    /**
     * Map from a 2-dimensional (row, column) pair to a 1-dimensional union find object index.
     * @param x site's row-index [1, n]
     * @param y site's column-index [1, n]
     * @return an integer indicating 1-d UF object index
     */
    private int xyTo1D(int x, int y) {        
        return (x - 1) * gridSize + y;
    }
    
    /**
     * Validate row and column indices.
     * Throws IndexOutOfBoundsException if row, col not in [1, n].
     * @param row site's row-index [1, n]
     * @param col site's column-index [1, n]
     */
    private void validateIndices(int row, int col) {
        if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
            throw new IndexOutOfBoundsException("Invalid indices: "
                    + "row and column indices must be between 1 and " + gridSize);
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
