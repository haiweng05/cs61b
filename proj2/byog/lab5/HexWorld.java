package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.WATER;
            default: return Tileset.TREE;
        }
    }

    private static void drawHex(TETile[][] tiles, TETile pattern, int x, int y, int size) {
        for (int i = 0; i < size; ++i) {
            drawHexHelper(tiles, pattern, x - i, y + i, size + 2 * i);
            drawHexHelper(tiles, pattern, x - i, 2 * size + y - i - 1, size + 2 * i);
        }
    }

    private static void drawHexHelper(TETile[][] tiles, TETile pattern, int x, int y, int width) {
        for (int i = 0; i < width; ++i) {
            tiles[x + i][y] = pattern;
        }
    }


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] Tiles = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                Tiles[i][j] = Tileset.NOTHING;
            }
        }
        drawHex(Tiles, Tileset.FLOWER, 10, 10, 4);

        ter.renderFrame(Tiles);
    }
}
