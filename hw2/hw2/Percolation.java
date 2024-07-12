package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // stores whether the blocks are closed or open.
    private boolean[][] map;
    private WeightedQuickUnionUF disjointSet;
    private int n;
    private int opensites;

    private int convert(int x, int y) {
        return 1 + x * n + y;
    }

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw(new java.lang.IllegalArgumentException("N less or equal to 0"));
        }
        n = N;
        opensites = 0;

        map = new boolean[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                map[i][j] = false;
            }
        }
        disjointSet = new WeightedQuickUnionUF(n * n + 1);
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw(new java.lang.IndexOutOfBoundsException("Index out of the range [0,n)"));
        }
        if (isOpen(row, col)) {
            return;
        }
        map[row][col] = true;
        opensites += 1;
        if (row == 0) {
            disjointSet.union(0, convert(row, col));
        }
        if (row - 1 >= 0 && map[row - 1][col]) {
            disjointSet.union(convert(row - 1, col), convert(row, col));
        }
        if (col - 1 >= 0 && map[row][col - 1]) {
            disjointSet.union(convert(row, col - 1), convert(row, col));
        }
        if (row + 1 < n && map[row + 1][col]) {
            disjointSet.union(convert(row + 1, col), convert(row, col));
        }
        if (col + 1 < n && map[row][col + 1]) {
            disjointSet.union(convert(row, col + 1), convert(row, col));
        }
    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw(new java.lang.IndexOutOfBoundsException("Index out of the range [0,n)"));
        }
        return map[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) {
            throw(new java.lang.IndexOutOfBoundsException("Index out of the range [0,n)"));
        }
        return disjointSet.connected(0, convert(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return opensites;
    }

    // does the system percolate?
    public boolean percolates() {
        boolean flag = false;
        for (int i = 0; i < n; ++i) {
            flag = flag || isFull(n - 1, i);
        }
        return flag;
    }

    public static void main(String[] args) {

    }
}
