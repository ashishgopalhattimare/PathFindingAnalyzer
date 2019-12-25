package sample.Constant;

import sample.Algorithms.ShortestPath;

public class Constants
{
    public static int ROW = 40, COL = 80;

    public static final int unvisit = 0;
    public static final int visit = 1;
    public static final int wall = 2;
    public static final int shortest = 4;
    public static final int intermediate = 3;
    public static final int target = 5;
    public static final int source = 6;

    public static final String BORDER       = "black";
    public static final String WALL         = "black";
    public static final String UNVISITED    = "white";
    public static final String SOURCE       = "#ff8d00"; // orange
    public static final String TARGET       = "#ff0000"; // red
    public static final String SHORTEST     = "yellow";
    public static final String NEXT_VISIT   = "cyan";
    public static final String VISITED      = "blue";
    public static final String INTERMEDIATE = "#f000ff"; // pink

    public static final String mazeDirect   = "template/";

    public static final int SLEEP_TIME      = 40;
    public static final int SHORT_TIME      = 30;
    public static final int MAZE_TIME       = 02;

    public static ShortestPath currentThread;

    private Constants() { }
}
