package sample.Constant;

import sample.Algorithms.ShortestPath;

public class Constants
{
    public static int ROW = 40, COL = 80;

    public static final int WALL_WEIGHT = -1;
    public static final int UNVISITED_WEIGHT = Integer.MAX_VALUE;

    public static final String BORDER       = "black";
    public static final String WALL         = "black";
    public static final String UNVISITED    = "white";
    public static final String SOURCE       = "#ff8d00"; // orange
    public static final String TARGET       = "#ff0000"; // red
    public static final String SHORTEST     = "yellow";

    public static final String NEXT_VISIT   = "#8BE42D";//"cyan";
    public static final String VISITED      = "blue";

    public static final String NEXT_VISIT2  = "#8BE42D"; // light green
    public static final String VISITED2     = "#3F7B00"; // dark green

    public static final String INTERMEDIATE = "#f000ff"; // pink

    public static final String MAZE_DIRECT   = "template/";

    public static final int SLEEP_TIME      = 40;
    public static final int SHORT_TIME      = 30;
    public static final int MAZE_TIME       = 01;

    public static final int MOVE_STRAIGHT = 10;
    public static final int MOVE_DIAGONAL = 14;

    public static final int NON_DIAGONAL = 4;
    public static final int DIAGONAL = 8;
    public static int TRAVERSAL_LEN;

    public static boolean DFX_EXHAUSTIVE;
    public static boolean UPDATE_BORDER;

    public static ShortestPath currentThread;

    public static final String DEFAULT_LOAD = "maze1.txt";
    public static final String DEFAULT_SAVE = "untitled.txt";

    public static final String GITHUB_LINK = "https://github.com/ashishgopalhattimare";

    private Constants() { }
}
