package sample.Algorithms;

import sample.Constant.CellState;
import sample.Constant.Constants;
import sample.Constant.Cell;
import sample.MazeController;

import java.util.LinkedList;
import java.util.ListIterator;

public abstract class ShortestPath extends Thread {

    public static int[] Y = {-1,0,1, 0,-1,-1,1, 1};
    public static int[] X = { 0,1,0,-1,-1, 1,1,-1};

    protected Cell src;
    protected Cell des;
    protected boolean runThread;
    protected boolean pathFound;
    protected int shortestPath;

    public abstract void algorithm(Cell src, Cell des);

    public boolean inRange(int r, int c) {
        return (r >= 0 && r < Constants.ROW) && (c >= 0 && c < Constants.COL);
    }

    public boolean samePoint(Cell x, Cell y) {
        return x.i == y.i && x.j == y.j;
    }

    public void colorPath(LinkedList<Cell> shortestPath, String Color, boolean shortPath)
    {
        Cell prev = null, curr = null;

        ListIterator<Cell> iterator = shortestPath.listIterator();
        while(iterator.hasNext() && runThread) {
            curr = iterator.next();

            if(!shortPath) { // Change its state to DEFAULT_VISITED state
                MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Color);
                MazeController.Grid[curr.i][curr.j].state = CellState.VISITED;

                prev = curr;
            }
            else {
                if(prev != null) {
                    MazeController.PaintBlock(prev.i, prev.j, Constants.BORDER, Color);
                }
                MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.TARGET);
                curr.state = CellState.SHORTEST;

                prev = curr;
                try {
                    MazeController.Grid[curr.i][curr.j].state = CellState.SHORTEST;
                    Thread.sleep(Constants.SHORT_TIME);
                }
                catch (Exception ignored) {}
            }
        }

        if(prev != null) MazeController.PaintBlock(prev.i, prev.j, Constants.BORDER, Constants.TARGET);
    }

    protected void tracePath(Cell temp) {

        LinkedList<Cell> shortestPath = new LinkedList<>();
        while (temp.state != CellState.SOURCE) {

            shortestPath.addFirst(temp);
            temp = MazeController.Grid[temp.p_i][temp.p_j];
        }
        colorPath(shortestPath, Constants.SHORTEST, true);
    }

    public void killThread() {
        System.out.println("Kill thread");

        runThread=false;pathFound=true;
        this.interrupt();
    }
}