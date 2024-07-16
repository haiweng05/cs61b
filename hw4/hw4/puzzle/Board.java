package hw4.puzzle;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.swap;

public class Board implements WorldState{

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    private int[][] tiles;
    private int size;
    public Board(int[][] tiles) {
        size = tiles.length;
        this.tiles = new int[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }
    public int tileAt(int i, int j) {
        if (i < 0 || j < 0 || i >= size || j >= size) {
            throw(new java.lang.IndexOutOfBoundsException());
        }
        return tiles[i][j];
    }
    public int size() {
        return size;
    }
    public Iterable<WorldState> neighbors() {
        List<WorldState> list = new ArrayList<>();
        int x = 0;
        int y = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                }
            }
        }
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int i = 0; i < 4; ++i) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (inRange(nx, ny)) {
                int temp = tiles[x][y];
                tiles[x][y] = tiles[nx][ny];
                tiles[nx][ny] = temp;
                Board b = new Board(tiles);
                list.add(b);

                temp = tiles[x][y];
                tiles[x][y] = tiles[nx][ny];
                tiles[nx][ny] = temp;
            }
        }
        return list;
    }

    private boolean inRange(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            return true;
        }
        return false;
    }
    public int hamming() {
        int cnt = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (tiles[i][j] != 0 && tiles[i][j] != i * size + j + 1) {
                    cnt += 1;
                }
            }
        }
        return cnt;
    }
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (tiles[i][j] != 0) {
                    int x = (tiles[i][j] - 1) / size;
                    int y = (tiles[i][j] - 1) % size;
                    sum += Math.abs(x - i) + Math.abs(y - j);
                }
            }
        }
        return sum;
    }
    public int estimatedDistanceToGoal() {
        return manhattan();
    }
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        if (this == y) {
            return true;
        }
        Board b = (Board) y;
        int N = this.size();
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tileAt(i, j) != b.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
    public int hashCode() {
        int res = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                res = res * (size * size + 1) + tiles[i][j];
            }
        }
        return res;
    }
}
