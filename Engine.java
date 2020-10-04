package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.lab12.Position;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Random;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private long SEED;
    private TETile[][] randomWorld;
    private static final int TITLEFONT = 70;
    private static final int KEYWORDFONT = 30;
    private static final int DISCRIPTIONFONT = 20;
    private Random random;
    private Position cur;
    private Position exit;
    private Position boss;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        randomWorld = new TETile[WIDTH][HEIGHT];
        drawMainMenu();
        WorldGenerator world = new WorldGenerator(WIDTH, HEIGHT, SEED);
        randomWorld = world.getRandomWorldFrame();
        cur = world.addRandomAvatar(randomWorld, random);
        exit = world.addRandomExit(randomWorld, random);
        boss = world.addRandomBoss(randomWorld, cur, random);
        ter.renderFrame(randomWorld);
        StartGame();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.


        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }

    private void drawMainMenu() {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, TITLEFONT));
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "CS61B: The Game");
        StdDraw.setFont(new Font("Monaco", Font.BOLD, KEYWORDFONT));
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game(N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Load Game(L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, "Quit(Q)");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                switch (key) {
                    case 'N':
                    case 'n': {
                        getSeed();
                        return;
                    }
                    case 'L':
                    case 'l': {

                    }
                    default: {
                        break;
                    }
                }
            }
        }
    }

    private void getSeed() {
        StringBuilder seed = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 's' || key == 'S') {
                    SEED = Long.parseLong(seed.toString());
                    random = new Random(SEED);
                    return;
                }
                seed.append(key);
                StdDraw.clear(Color.black);
                StdDraw.setFont(new Font("Monaco", Font.BOLD, TITLEFONT));
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "CS61B: THE GAME");
                StdDraw.setFont(new Font("Monaco", Font.BOLD, KEYWORDFONT));
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "Entering seed: ");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, seed.toString());
                StdDraw.show();
            }
        }
    }

    private void loadGameStatus() {

    }

    private void StartGame() {
        ter.renderFrame(randomWorld);
        while (true) {
            showDiscription();
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                switch (key) {
                    case 'W':
                    case 'w': {
                        if (randomWorld[cur.getX()][cur.getY() + 1].equals(Tileset.UNLOCKED_DOOR)) {
                            findExit();
                        } else if (randomWorld[cur.getX()][cur.getY() + 1].equals(Tileset.FLOOR)) {
                            randomWorld[cur.getX()][cur.getY()] = Tileset.FLOOR;
                            randomWorld[cur.getX()][cur.getY() + 1] = Tileset.AVATAR;
                            cur.y += 1;
                            ter.renderFrame(randomWorld);
                        }
                        break;
                    }
                    case 'S':
                    case 's': {
                        if (randomWorld[cur.getX()][cur.getY() - 1].equals(Tileset.UNLOCKED_DOOR)) {
                            findExit();
                        } else if (randomWorld[cur.getX()][cur.getY() - 1].equals(Tileset.FLOOR)) {
                            randomWorld[cur.getX()][cur.getY()] = Tileset.FLOOR;
                            randomWorld[cur.getX()][cur.getY() - 1] = Tileset.AVATAR;
                            cur.y -= 1;
                            ter.renderFrame(randomWorld);
                        }
                        break;
                    }
                    case 'A':
                    case 'a': {
                        if (randomWorld[cur.getX() - 1][cur.getY()].equals(Tileset.UNLOCKED_DOOR)) {
                            findExit();
                        } else if (randomWorld[cur.getX() - 1][cur.getY()].equals(Tileset.FLOOR)) {
                            randomWorld[cur.getX()][cur.getY()] = Tileset.FLOOR;
                            randomWorld[cur.getX() - 1][cur.getY()] = Tileset.AVATAR;
                            cur.x -= 1;
                            ter.renderFrame(randomWorld);
                        }
                        break;
                    }
                    case 'D':
                    case 'd': {
                        if (randomWorld[cur.getX() + 1][cur.getY()].equals(Tileset.UNLOCKED_DOOR)) {
                            findExit();
                        } else if (randomWorld[cur.getX() + 1][cur.getY()].equals(Tileset.FLOOR)) {
                            randomWorld[cur.getX()][cur.getY()] = Tileset.FLOOR;
                            randomWorld[cur.getX() + 1][cur.getY()] = Tileset.AVATAR;
                            cur.x += 1;
                            ter.renderFrame(randomWorld);
                        }
                        break;
                    }
                    case ':': {
                        while (true) {
                            if (StdDraw.hasNextKeyTyped()) {
                                char c = StdDraw.nextKeyTyped();
                                if (c == 'Q' || c == 'q') {
                                    saveGameStatus();
                                    System.exit(0);
                                } else {
                                    break;
                                }
                            }
                        }
                        break;
                    }

                    case 'F':
                    case 'f': {
                        if (randomWorld[cur.getX() + 1][cur.getY()].equals(Tileset.TREE)
                                || randomWorld[cur.getX() - 1][cur.getY()].equals(Tileset.TREE)
                                || randomWorld[cur.getX()][cur.getY() + 1].equals(Tileset.TREE)
                                || randomWorld[cur.getX()][cur.getY() - 1].equals(Tileset.TREE)) {
                            actWithBoss();
                        }
                        break;
                    }

                    default: {
                        break;
                    }
                }
            }
        }
    }

    public void findExit() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, KEYWORDFONT));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "You have completed this level.");
        StdDraw.show();
        StdDraw.pause(5000);
    }

    private void actWithBoss() {
        ter.renderFrame(randomWorld);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, DISCRIPTIONFONT));
        StdDraw.text(WIDTH / 2, HEIGHT - 1, "So here is the boss.");
        StdDraw.show();
        StdDraw.pause(1000);
    }

    private void showDiscription() {
        ter.renderFrame(randomWorld);
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (!randomWorld[x][y].equals(Tileset.NOTHING)) {
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setFont(new Font("Monaco", Font.BOLD, DISCRIPTIONFONT));
            StdDraw.text(5, HEIGHT - 1, randomWorld[x][y].description());
            StdDraw.show();
        }
    }

    private void showSeed() {
        ter.renderFrame(randomWorld);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, DISCRIPTIONFONT));
        StdDraw.text(WIDTH - 10, HEIGHT - 1, String.valueOf(SEED));
        StdDraw.show();
    }

    private void saveGameStatus() {
        File f = new File("./savedGame.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(this.cur);
            os.writeObject(SEED);
            os.close();
            fs.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}
