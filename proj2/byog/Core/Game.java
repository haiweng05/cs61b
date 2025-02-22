package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.util.Random;

public class Game {
    TERenderer ter;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 64;
    public static final int HEIGHT = 36;
    private static final int MAXWIDTH = 6;
    private static final int MINWIDTH = 2;
    private static final int MAXHEIGHT = 6;
    private static final int MINHEIGHT = 2;
    private static final int[][] BEARINGS = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};
    private Random random;
    private TETile[][] world;
    private int roomCount = 0;
    private int curX;
    private int curY;

    public Game() {
        // initialize tiles
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void start(long seed, boolean mode) {
        random = new Random(seed);
        gererate();
        addWall();
        // draws the world to the screen
        if (mode) {
            ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(world);
        }
    }

    private void gererate() {
        int startX = RandomUtils.uniform(random, WIDTH / 2 - 5, WIDTH / 2 + 5);
        int startY = RandomUtils.uniform(random, HEIGHT / 2 - 5,  HEIGHT / 2 + 5);
        curX = startX;
        curY = startY;

        search(startX, startY);
    }

    private void search(int x, int y) {
        double[] possibilities = getPossibility(x, y);
        for (int i = 0; i < 4; ++i) {
            int width = RandomUtils.uniform(random, MINWIDTH, MAXWIDTH);
            int height = RandomUtils.uniform(random,  MINHEIGHT, MAXHEIGHT);
            int length = RandomUtils.uniform(random, 9, 14);
            double r = RandomUtils.uniform(random);
            int nx = x + length * BEARINGS[i][0];
            int ny = y + length * BEARINGS[i][1];
            if (r + possibilities[i] < 1) {
                continue;
            }
            if (nx + width / 2 < WIDTH - 1 && ny + height / 2 < HEIGHT - 1) {
                if (nx - width / 2 > 0 && ny - height / 2 > 0) {
                    if (world[nx][ny].equals(Tileset.NOTHING)) {
                        roomCount += 1;

                        addPath(x, y, i, length);

                        addRoom(nx, ny, width, height);

                        search(nx, ny);
                    }
                }


            }

        }
    }


    private void addRoom(int x, int y, int width, int height) {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                world[x - width / 2 + i][y - height / 2 + j] = Tileset.FLOOR;
            }
        }
    }

    private void addPath(int x, int y, int bearing, int length) {
        for (int i = 0; i < length; ++i) {
            world[x + i * BEARINGS[bearing][0]][y + i * BEARINGS[bearing][1]] = Tileset.FLOOR;
        }
    }

    private void addWall() {
        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                if (!world[i][j].equals(Tileset.FLOOR)) {
                    boolean flag = false;
                    flag = flag || i - 1 >= 0 && world[i - 1][j].equals(Tileset.FLOOR);
                    flag = flag || i + 1 < WIDTH && world[i + 1][j].equals(Tileset.FLOOR);
                    flag = flag || j - 1 >= 0 && world[i][j - 1].equals(Tileset.FLOOR);
                    flag = flag || j + 1 < HEIGHT && world[i][j + 1].equals(Tileset.FLOOR);
                    if (i - 1 >= 0 && j - 1 >= 0) {
                        flag = flag || world[i - 1][j - 1].equals(Tileset.FLOOR);
                    }
                    if (i - 1 >= 0 && j + 1 < HEIGHT) {
                        flag = flag || world[i - 1][j + 1].equals(Tileset.FLOOR);
                    }
                    if (i + 1 < WIDTH && j - 1 >= 0) {
                        flag = flag || world[i + 1][j - 1].equals(Tileset.FLOOR);
                    }
                    if (i + 1 < WIDTH && j + 1 < HEIGHT) {
                        flag = flag || world[i + 1][j + 1].equals(Tileset.FLOOR);
                    }
                    if (flag) {
                        world[i][j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private double[] getPossibility(int x, int y) {
        double[] result = new double[4];
        double coefficient = 3 * Math.exp(-1.0d * roomCount / 10);
        result[0] = coefficient * (WIDTH - x) / WIDTH;
        result[1] = coefficient * (HEIGHT - y) / HEIGHT;
        result[2] = coefficient * x / WIDTH;
        result[3] = coefficient * y / HEIGHT;
        return result;
    }
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        menu();

        String input = "";
        boolean quit = false;
        boolean start = false;

        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input += String.valueOf(key);

            if (!start) {
                input += getSeed(key);
                start = true;
            }

            StdDraw.clear();
            ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            world[curX][curY] = Tileset.PLAYER;
            ter.renderFrame(world);
            StdDraw.enableDoubleBuffering();

            if (key == 'W' || key == 'w') {
                if (world[curX + BEARINGS[0][0]][curY + BEARINGS[0][1]].equals(Tileset.FLOOR)) {
                    world[curX][curY] = Tileset.FLOOR;
                    curX = curX + BEARINGS[0][0];
                    curY = curY + BEARINGS[0][1];
                    world[curX][curY] = Tileset.PLAYER;
                    ter.renderFrame(world);
                }
                quit = false;
            } else if (key == 'A' || key == 'a') {
                if (world[curX + BEARINGS[1][0]][curY + BEARINGS[1][1]].equals(Tileset.FLOOR)) {
                    world[curX][curY] = Tileset.FLOOR;
                    curX = curX + BEARINGS[1][0];
                    curY = curY + BEARINGS[1][1];
                    world[curX][curY] = Tileset.PLAYER;
                    ter.renderFrame(world);
                }
                quit = false;
            } else if (key == 'S' || key == 's') {
                if (world[curX + BEARINGS[2][0]][curY + BEARINGS[2][1]].equals(Tileset.FLOOR)) {
                    world[curX][curY] = Tileset.FLOOR;
                    curX = curX + BEARINGS[2][0];
                    curY = curY + BEARINGS[2][1];
                    world[curX][curY] = Tileset.PLAYER;
                    ter.renderFrame(world);
                }
                quit = false;
            } else if (key == 'D' || key == 'd') {
                if (world[curX + BEARINGS[3][0]][curY + BEARINGS[3][1]].equals(Tileset.FLOOR)) {
                    world[curX][curY] = Tileset.FLOOR;
                    curX = curX + BEARINGS[3][0];
                    curY = curY + BEARINGS[3][1];
                    world[curX][curY] = Tileset.PLAYER;
                    ter.renderFrame(world);
                }
                quit = false;
            } else if (key == ':') {
                quit = true;
            } else if ((key == 'Q' || key == 'q') && quit) {
                Out file = new Out("memory.txt");
                file.print(input);
                ter = null;
                System.exit(0);
                break;
            } else {
                quit = false;
            }
        }
    }

    private String getSeed(char c) {
        String input = "";
        while (true) {
            if (c == 'N' || c == 'n') {
                long seed = 0;
                while (true) {
                    if (!StdDraw.hasNextKeyTyped()) {
                        continue;
                    }
                    char key = StdDraw.nextKeyTyped();
                    input += String.valueOf(key);

                    if (key == 'S' || key == 's') {
                        start(seed, false);
                        break;
                    } else {
                        seed = seed * 10L + (key - '0');
                    }
                }
                break;
            } else if (c == 'L' || c == 'l') {
                In file = new In("memory.txt");
                input = file.readString();
                input = input.substring(0, input.length() - 2);
                world = playWithInputString(input);
                break;
            }
        }
        return input;
    }

    public void drawFrame(String s) {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        // Draw the actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, s);
        StdDraw.show();
    }

    private void menu() {
        StdDraw.setCanvasSize(40 * 16, 40 * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, 40);
        StdDraw.setYscale(0, 40);

        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        StdDraw.clear();
        StdDraw.clear(Color.black);

        // Draw the Menu
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(font);
        StdDraw.text(20, 20 + 4, "New Game(N)");
        StdDraw.text(20, 20, "Load Game(L)");
        StdDraw.text(20, 20 - 4, "Quit(Q)");
        StdDraw.show();
        StdDraw.pause(2000);
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        boolean quit = false;
        int cur = 1;
        while (input.charAt(0) == 'l' || input.charAt(0) == 'L') {
            In file = new In("memory.txt");
            input = file.readString() + input.substring(cur);
        }
        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            long num = 0L;
            for (cur = 1; cur < input.length(); ++cur) {
                if (input.charAt(cur) != 'S' && input.charAt(cur) != 's') {
                    num = num * 10L + (input.charAt(cur) - '0');
                } else {
                    break;
                }
            }
            start(num, false);
            cur++;
        }
        for (; cur < input.length(); ++cur) {
            if (input.charAt(cur) == 'W' || input.charAt(cur) == 'w') {
                if (world[curX + BEARINGS[0][0]][curY + BEARINGS[0][1]].equals(Tileset.FLOOR)) {
                    curX = curX + BEARINGS[0][0];
                    curY = curY + BEARINGS[0][1];

                }
            }
            if (input.charAt(cur) == 'A' || input.charAt(cur) == 'a') {
                if (world[curX + BEARINGS[1][0]][curY + BEARINGS[1][1]].equals(Tileset.FLOOR)) {
                    curX = curX + BEARINGS[1][0];
                    curY = curY + BEARINGS[1][1];
                }
            }
            if (input.charAt(cur) == 'S' || input.charAt(cur) == 's') {
                if (world[curX + BEARINGS[2][0]][curY + BEARINGS[2][1]].equals(Tileset.FLOOR)) {
                    curX = curX + BEARINGS[2][0];
                    curY = curY + BEARINGS[2][1];

                }
            }
            if (input.charAt(cur) == 'D' || input.charAt(cur) == 'd') {
                if (world[curX + BEARINGS[3][0]][curY + BEARINGS[3][1]].equals(Tileset.FLOOR)) {
                    curX = curX + BEARINGS[3][0];
                    curY = curY + BEARINGS[3][1];

                }
            }
            if (input.charAt(cur) == ':') {
                quit = true;
            }
            if ((input.charAt(cur) == 'Q' || input.charAt(cur) == 'q') && quit) {
                cur++;
                Out file = new Out("memory.txt");
                file.print(input.substring(0, input.length() - 2));
            }
        }
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; ++i) {
            for (int j = 0; j < HEIGHT; ++j) {
                finalWorldFrame[i][j] = world[i][j];
            }
        }
        finalWorldFrame[curX][curY] = Tileset.PLAYER;
        return finalWorldFrame;
    }

    private void save() {
        return;
    }

    private void load() {
        return;
    }
}

