package sample.Algorithms;

import sample.Constant.Constants;
import sample.Constant.Point;
import sample.MazeController;

import java.util.ArrayList;
import java.util.LinkedList;

public class Depth_First extends ShortestPath {

    public static boolean COMPLETE_DEPTHFIRST = false;

    private static int [][] pathLen = new int[Constants.ROW][Constants.COL];

    private ArrayList<Point> prevpath = null;
    int shortestPath;

    @Override
    public void algorithm(int[][] grid, Point src, Point des)
    {
        ShortestPath.grid = grid; this.src = src; this.des = des;
        shortestPath = Integer.MAX_VALUE;

        for(int i = 0; i < Constants.ROW; i++) {
            for(int j = 0; j < Constants.COL; j++) {
                pathLen[i][j] = Integer.MAX_VALUE;
            }
        }

        runThread = true;
    }

    private void DFS(Point curr, Point des, LinkedList<Point> pathList, int len) {

        if(!samePoint(curr, src)) {
            if(grid[curr.i][curr.j] != Constants.shortest) {
                MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.VISITED);
                grid[curr.i][curr.j] = Constants.visit;
            }
        }

        pathLen[curr.i][curr.j] = len; // update shortest len path

        for (int k = 0; k < 4 && runThread && pathList.size() < shortestPath; k++) {
            if (inRange(curr.i + Y[k], Constants.ROW) && inRange(curr.j + X[k], Constants.COL) && grid[curr.i + Y[k]][curr.j + X[k]] != Constants.wall)
            {
                if((pathLen[curr.i + Y[k]][curr.j + X[k]] > len && COMPLETE_DEPTHFIRST) || (grid[curr.i + Y[k]][curr.j + X[k]] == Constants.unvisit))
                {
                    try { Thread.sleep(Constants.SLEEP_TIME); } catch (Exception ignored){}

                    Point temp = new Point(curr.i + Y[k], curr.j + X[k]);

                    if (!samePoint(temp, des)) // next possible visit for BSF
                    {
                        pathList.add(temp); DFS(temp, des, pathList, len+1);
                        pathList.removeLast();
                    }

                    // Don't make target as visited
                    else { // destination reached, check if path is shortest
                        if(pathList.size() < shortestPath)
                        {
                            if(prevpath != null) {
                                colorPath(prevpath, Constants.VISITED, false);
                            }

                            prevpath = new ArrayList<>(pathList);
                            colorPath(prevpath, Constants.SHORTEST, true);

                            shortestPath = pathList.size();
                        }
                    }
                }
            }
        }

        // If the index is not from the shortest path
        if(grid[curr.i][curr.j] != Constants.shortest) {
            MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.UNVISITED);
        }
    }

    @Override
    public void run() {
        LinkedList<Point> pathList = new LinkedList<>();
        grid[src.i][src.j] = Constants.shortest;

        pathList.add(src);

        DFS(src, des, pathList, 0);
        Constants.currentThread = null;
    }
}
