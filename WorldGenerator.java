package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.lab12.Position;
import byow.lab12.Room;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator {
    private int WIDTH;
    private int HEIGHT;
    private long SEED;
    private Random random;
    TETile[][] world;
    TERenderer ter = new TERenderer();
    WeightedQuickUnionUF connection;
    private int numOfRooms = 0;
    private ArrayList<Room> rooms;

    public WorldGenerator(int width, int height, long seed) {
        WIDTH = width;
        HEIGHT = height;
        SEED = seed;
        random = new Random(SEED);
        rooms = new ArrayList<>();
    }

    public TETile[][] getRandomWorldFrame() {
        ter.initialize(WIDTH, HEIGHT);
        world = new TETile[WIDTH][HEIGHT];
        initialize(world, Tileset.NOTHING);
        int upperLimit = Math.max(WIDTH, HEIGHT);
        for (int i = 0; i < upperLimit; i++) {
            Position start = new Position(RandomUtils.uniform(random, WIDTH), RandomUtils.uniform(random, HEIGHT));
            createRoom(world, start, random);
        }
        connection = new WeightedQuickUnionUF(numOfRooms + 1);
        for (int i = 0; i < rooms.size(); i++) {
            for (int j = i; j < rooms.size(); j++) {
                if (!connection.connected(i, j)) {
                    connectRoom(world, rooms.get(i), rooms.get(j));
                }
            }
        }
        checkConnection();
        repairAfterGeneration();
        return world;
    }

    private void initialize(TETile[][] world, TETile t) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = t;
            }
        }
    }

    private void createRoom(TETile[][] world, Position start, Random random) {
        int width = RandomUtils.uniform(random, 5, WIDTH / 5);
        int height = RandomUtils.uniform(random, 5, HEIGHT / 5);
        if (CanPlaceRoom(world, start, width, height)) {
            for (int i = start.getX(); i < start.getX() + width; i++) {
                world[i][start.getY()] = Tileset.WALL;
                world[i][start.getY() + height - 1] = Tileset.WALL;
            }
            for (int i = start.getY(); i < start.getY() + height; i++) {
                world[start.getX()][i] = Tileset.WALL;
                world[start.getX() + width - 1][i] = Tileset.WALL;
            }
            for (int i = start.getX() + 1; i < start.getX() + width - 1; i++) {
                for (int j = start.getY() + 1; j < start.getY() + height - 1; j++) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    private boolean CanPlaceRoom(TETile[][] world, Position start, int width, int height) {
        if (start.getX() + width >= WIDTH || start.getY() + height >= HEIGHT) {
            return false;
        }
        for (int i = start.getX(); i < start.getX() + width; i++) {
            for (int j = start.getY(); j < start.getY() + height; j++) {
                if (!world[i][j].equals(Tileset.NOTHING))
                    return false;
            }
        }
        Room room = new Room(start.getX(), start.getY(), width, height);
        rooms.add(room);
        numOfRooms += 1;
        return true;
    }

    private void connectRoom(TETile[][] world, Room start1, Room start2) {
        boolean xConnect = false;
        boolean yConnect = false;
        int connectRangelo = 0;
        int connectRangehi = 0;
        if (Math.min(start1.x + start1.width, start2.x + start2.width) > Math.max(start1.x, start2.x) + 3) {
            xConnect = true;
            connectRangelo = Math.max(start1.x, start2.x);
            connectRangehi = Math.min(start1.x + start1.width, start2.x + start2.width);
        }
        if (Math.min(start1.y + start1.height, start2.y + start2.height) > Math.max(start1.y, start2.y) + 3 && !xConnect) {
            yConnect = true;
            connectRangelo = Math.max(start1.y, start2.y);
            connectRangehi = Math.min(start1.y + start1.height, start2.y + start2.height);
        }
        if (xConnect) {
            int loc = RandomUtils.uniform(random, connectRangelo, connectRangehi - 3);
            if (start1.y < start2.y) {
                for (int i = start1.y + start1.height - 1; i <= start2.y; i++) {
                    if ((world[loc][i].equals(Tileset.NOTHING)))
                        world[loc][i] = Tileset.WALL;
                    if (world[loc + 2][i].equals(Tileset.NOTHING))
                        world[loc + 2][i] = Tileset.WALL;
                    //if (i != start1.x || i != start2.x - start2.width - 1)
                    world[loc + 1][i] = Tileset.FLOOR;
                }
                connection.union(rooms.indexOf(start1), rooms.indexOf(start2));
            } else {
                for (int i = start2.y + start2.height - 1; i <= start1.y; i++) {
                    if ((world[loc][i].equals(Tileset.NOTHING)))
                        world[loc][i] = Tileset.WALL;
                    if (world[loc + 2][i].equals(Tileset.NOTHING))
                        world[loc + 2][i] = Tileset.WALL;
                    //if (i != start2.x || i != start1.x - start1.width - 1)
                    world[loc + 1][i] = Tileset.FLOOR;
                }
                connection.union(rooms.indexOf(start1), rooms.indexOf(start2));
            }
        } else if (yConnect) {
            int loc = RandomUtils.uniform(random, connectRangelo, connectRangehi - 3);
            random.ints(connectRangelo, connectRangehi - 3);
            if (start1.x < start2.x) {
                for (int i = start1.x + start1.width - 1; i <= start2.x; i++) {
                    if (world[i][loc].equals(Tileset.NOTHING))
                        world[i][loc] = Tileset.WALL;
                    if (world[i][loc + 2].equals(Tileset.NOTHING))
                        world[i][loc + 2] = Tileset.WALL;
                    //if (i != start1.x || i != start2.x - start2.width - 1)
                    world[i][loc + 1] = Tileset.FLOOR;
                }
                connection.union(rooms.indexOf(start1), rooms.indexOf(start2));
            } else {
                for (int i = start2.x + start2.width - 1; i <= start1.x; i++) {
                    if (world[i][loc].equals(Tileset.NOTHING))
                        world[i][loc] = Tileset.WALL;
                    if (world[i][loc + 2].equals(Tileset.NOTHING))
                        world[i][loc + 2] = Tileset.WALL;
                    //if (i != start2.x || i != start1.x - start1.width - 1)
                    world[i][loc + 1] = Tileset.FLOOR;
                }
                connection.union(rooms.indexOf(start1), rooms.indexOf(start2));
            }
        }
    }


    public Position addRandomExit(TETile[][] world, Random random) {
        Position p = new Position(RandomUtils.uniform(random, 3, WIDTH - 3),
                RandomUtils.uniform(random, 3, HEIGHT - 3));
        while (!world[p.x][p.y].equals(Tileset.WALL)) {
            p = new Position(RandomUtils.uniform(random, 3, WIDTH - 3),
                    RandomUtils.uniform(random, 3, HEIGHT - 3));
        }
        world[p.x][p.y] = Tileset.UNLOCKED_DOOR;
        return p;
    }

    public void checkConnection() {
        for (int i = 0; i < numOfRooms; i++) {
            for (int j = 0; j < numOfRooms; j++) {
                if (!connection.connected(i, j)) {
                    connectRoomcheck(world, rooms.get(i), rooms.get(j));
                }
            }
        }
    }

    public void connectRoomcheck(TETile[][] world, Room start1, Room start2) {
        if (start1.x < start2.x) {
            if (start1.y < start2.y) {
                int loc1 = RandomUtils.uniform(random, start1.y, start1.y + start1.height - 3);
                int loc2 = RandomUtils.uniform(random, start2.x, start2.x + start2.width - 3);
                for (int i = start1.x + start1.width - 1; i <= loc2 + 2; i++) {
                    if (world[i][loc1].equals(Tileset.NOTHING))
                        world[i][loc1] = Tileset.WALL;
                    if (world[i][loc1 + 2].equals(Tileset.NOTHING))
                        world[i][loc1 + 2] = Tileset.WALL;
                    world[i][loc1 + 1] = Tileset.FLOOR;
                }
                for (int i = start2.y - 1; i >= loc1 + 1; i--) {
                    if (world[loc2][i].equals(Tileset.NOTHING))
                        world[loc2][i] = Tileset.WALL;
                    if (world[loc2 + 2][i].equals(Tileset.NOTHING))
                        world[loc2 + 2][i] = Tileset.WALL;
                    world[loc2 + 1][i] = Tileset.FLOOR;
                }
            } else {
                int loc1 = RandomUtils.uniform(random, start1.x, start1.x + start1.width - 3);
                int loc2 = RandomUtils.uniform(random, start2.y, start2.y + start2.height - 3);
                for (int i = start2.x - 1; i >= loc1; i--) {
                    if (world[i][loc2].equals(Tileset.NOTHING))
                        world[i][loc2] = Tileset.WALL;
                    if (world[i][loc2 + 2].equals(Tileset.NOTHING))
                        world[i][loc2 + 2] = Tileset.WALL;
                    world[i][loc2 + 1] = Tileset.FLOOR;
                }
                for (int i = start1.y - 1; i >= loc2 + 1; i--) {
                    if (world[loc1][i].equals(Tileset.NOTHING))
                        world[loc1][i] = Tileset.WALL;
                    if (world[loc1 + 2][i].equals(Tileset.NOTHING))
                        world[loc1 + 2][i] = Tileset.WALL;
                    world[loc1 + 1][i] = Tileset.FLOOR;
                }
            }
        } else {
            if (start1.y < start2.y) {
                int loc1 = RandomUtils.uniform(random, start1.y, start1.y + start1.height - 3);
                int loc2 = RandomUtils.uniform(random, start2.x, start2.x + start2.width - 3);
                for (int i = start1.x - 1; i >= loc2; i--) {
                    if (world[i][loc1].equals(Tileset.NOTHING))
                        world[i][loc1] = Tileset.WALL;
                    if (world[i][loc1 + 2].equals(Tileset.NOTHING))
                        world[i][loc1 + 2] = Tileset.WALL;
                    world[i][loc1 + 1] = Tileset.FLOOR;
                }
                for (int i = start2.y - 1; i >= loc1 + 1; i--) {
                    if (world[loc2][i].equals(Tileset.NOTHING))
                        world[loc2][i] = Tileset.WALL;
                    if (world[loc2 + 2][i].equals(Tileset.NOTHING))
                        world[loc2 + 2][i] = Tileset.WALL;
                    world[loc2 + 1][i] = Tileset.FLOOR;
                }
            } else {
                int loc1 = RandomUtils.uniform(random, start1.x, start1.x + start1.width - 3);
                int loc2 = RandomUtils.uniform(random, start2.y, start2.y + start2.height - 3);
                for (int i = start2.x + start2.width - 1; i <= loc1 + 2; i++) {
                    if (world[i][loc2].equals(Tileset.NOTHING))
                        world[i][loc2] = Tileset.WALL;
                    if (world[i][loc2 + 2].equals(Tileset.NOTHING))
                        world[i][loc2 + 2] = Tileset.WALL;
                    world[i][loc2 + 1] = Tileset.FLOOR;
                }
                for (int i = start1.y - 1; i >= loc2 + 1; i--) {
                    if (world[loc1][i].equals(Tileset.NOTHING))
                        world[loc1][i] = Tileset.WALL;
                    if (world[loc1 + 2][i].equals(Tileset.NOTHING))
                        world[loc1 + 2][i] = Tileset.WALL;
                    world[loc1 + 1][i] = Tileset.FLOOR;
                }
            }
        }
    }

    private void repairAfterGeneration() {
        for (int i = 0; i < WIDTH - 1; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) {
                    if (i == 0 || i == WIDTH - 1 || j == 0 || j == HEIGHT - 1) {
                        world[i][j] = Tileset.WALL;
                        continue;
                    }
                    if (world[i - 1][j].equals(Tileset.NOTHING) ||
                            world[i + 1][j].equals(Tileset.NOTHING) ||
                            world[i][j - 1].equals(Tileset.NOTHING) ||
                            world[i][j + 1].equals(Tileset.NOTHING)) {
                        world[i][j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public Position addRandomAvatar(TETile[][] world, Random random) {
        Position p = new Position(RandomUtils.uniform(random, 3, WIDTH - 3),
                RandomUtils.uniform(random, 3, HEIGHT - 3));
        while (!world[p.x][p.y].equals(Tileset.FLOOR)) {
            p = new Position(RandomUtils.uniform(random, 3, WIDTH - 3),
                    RandomUtils.uniform(random, 3, HEIGHT - 3));
        }
        world[p.x][p.y] = Tileset.AVATAR;
        return p;
    }

    public Position addRandomBoss(TETile[][] world, Position cur, Random random) {
        Position p = new Position(RandomUtils.uniform(random, 3, WIDTH - 3),
                RandomUtils.uniform(random, 3, HEIGHT - 3));
        while (!world[p.x][p.y].equals(Tileset.FLOOR) /*&& Distance(p, cur) <= 10*/) {
            p = new Position(RandomUtils.uniform(random, 3, WIDTH - 3),
                    RandomUtils.uniform(random, 3, HEIGHT - 3));
        }
        world[p.x][p.y] = Tileset.TREE;
        return p;
    }

    private int Distance(Position x, Position y) {
        int xDis = (int) (Math.pow(x.x, 2) + Math.pow(y.x, 2));
        int yDis = (int) (Math.pow(x.y, 2) + Math.pow(y.y, 2));
        return (int) Math.sqrt(xDis + yDis);
    }
}
