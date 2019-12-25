package sample.Algorithms;

import sample.Constant.Constants;
import sample.Constant.Point;
import sample.MazeController;

import java.util.ArrayList;

public abstract class ShortestPath extends Thread {

    public static int[] Y = {-1,0,1, 0,-1,-1,1, 1};
    public static int[] X = { 0,1,0,-1,-1, 1,1,-1};

    protected static int [][] grid;
    protected Point src;
    protected Point des;
    protected boolean runThread;
    protected boolean pathFound;

    public boolean inRange(int r, int c) {
        return (r >= 0 && r < Constants.ROW) && (c >= 0 && c < Constants.COL);
    }

    public abstract void algorithm(int [][] grid, Point src, Point des);

    public boolean samePoint(Point x, Point y) {
        return x.i == y.i && x.j == y.j;
    }

    public void colorPath(ArrayList<Point> shortestPath, String Color, boolean shortPath)
    {
        Point prev = null, curr = null;
        for(int i = 1; i < shortestPath.size(); i++) {
            curr = shortestPath.get(i);

            if(!shortPath) {
                MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Color);
            }
            else { // shortest path so far
                if(prev != null) MazeController.PaintBlock(prev.i, prev.j, Constants.BORDER, Color);
                MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.TARGET);

                prev = curr;
                try {
                    grid[curr.i][curr.j] = Constants.shortest;
                    Thread.sleep(Constants.SHORT_TIME);
                } catch (Exception ignored){}
            }
        }
        if(curr != null) MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Color);
    }

    public void killThread() {
        System.out.println("Kill thread");

        runThread=false;pathFound=true;
        this.interrupt();
    }
}