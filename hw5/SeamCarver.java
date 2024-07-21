import java.awt.*;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.width = picture.width();
        this.height = picture.height();
    }
    // current picture
    public Picture picture() {
        return picture;
    }
    // width of current picture
    public int width() {
        return width;
    }
    // height of current picture
    public int height() {
        return height;
    }
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        Color left = picture.get(left(x), y);
        Color right = picture.get(right(x), y);
        int energyX = (left.getRed() - right.getRed()) * (left.getRed() - right.getRed()) +
                (left.getBlue() - right.getBlue()) * (left.getBlue() - right.getBlue()) +
                (left.getGreen() - right.getGreen()) * (left.getGreen() - right.getGreen());

        Color up = picture.get(x, up(y));
        Color down = picture.get(x, down(y));
        int energyY = (up.getRed() - down.getRed()) * (up.getRed() - down.getRed()) +
                (up.getBlue() - down.getBlue()) * (up.getBlue() - down.getBlue()) +
                (up.getGreen() - down.getGreen()) * (up.getGreen() - down.getGreen());

        return energyX + energyY;
    }
    private int left(int x) {
        if (x == 0) {
            return width() - 1;
        }
        return x - 1;
    }

    private int right(int x) {
        if (x == width() - 1) {
            return 0;
        }
        return x + 1;
    }

    private int up(int y) {
        if (y == 0) {
            return height() - 1;
        }
        return y - 1;
    }

    private int down(int y) {
        if (y == height() - 1) {
            return 0;
        }
        return y + 1;
    }


    private Picture transpose(Picture p) {
        Picture newPicture = new Picture(p.height(), p.width());
        int w = p.width();
        int h = p.height();
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                newPicture.set(j, i, p.get(i, j));
            }
        }
        return newPicture;
    }
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture newPicture = transpose(picture);
        SeamCarver s = new SeamCarver(newPicture);
        return s.findVerticalSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[][] prev = new int [width + 2][height];
        double[][] dp = new double[width + 2][height];
        double[][] energy = new double[width + 2][height];

        for (int j = 0; j < height; ++j) {
            energy[0][j] = Double.MAX_VALUE;
            dp[0][j] = Double.MAX_VALUE;
            for (int i = 0; i < width; ++i) {
                energy[i + 1][j] = energy(i, j);
            }
            energy[width + 1][j] = Double.MAX_VALUE;
            dp[width + 1][j] = Double.MAX_VALUE;
        }

        for (int i = 1; i <= width; ++i) {
            dp[i][0] = energy[i][0];
            prev[i][0] = i;
        }
        for (int j = 1; j < height; ++j) {
            for (int i = 1; i <= width; ++i) {
                double left = dp[i - 1][j - 1];
                double mid = dp[i][j - 1];
                double right = dp[i + 1][j - 1];
                prev[i][j] = i - 1;
                double min = left;
                if (mid < min) {
                    min = mid;
                    prev[i][j] = i;
                }
                if (right < min) {
                    min = right;
                    prev[i][j] = i + 1;
                }
                dp[i][j] = energy[i][j] + min;
            }
        }
        int minIdx = 1;
        double minVal = Double.MAX_VALUE;
        for (int i = 1; i <= width; ++i) {
            if (dp[i][height - 1] < minVal) {
                minVal = dp[i][height - 1];
                minIdx = i;
            }
        }

        int[] result = new int[height];
        for (int j = height - 1; j >= 0; --j) {
            result[j] = minIdx - 1;
            minIdx = prev[minIdx][j];
        }
        return result;
    }
    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; ++i) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        Picture p = new Picture(width, height - 1);
        for (int i = 0; i < width; ++i) {
            int y = 0;
            for (int j = 0; j < height; ++j) {
                if (seam[i] != j) {
                    p.set(i, y, picture.get(i, j));
                    y += 1;
                }
            }
        }
        picture = p;
        height -= 1;
    }
    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; ++i) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        Picture p = new Picture(width - 1, height);
        for (int j = 0; j < height; ++j) {
            int x = 0;
            for (int i = 0; i < width; ++i) {
                if (seam[j] != i) {
                    p.set(x, j, picture.get(i, j));
                    x += 1;
                }
            }
        }

        picture = p;
        width -= 1;
    }
}