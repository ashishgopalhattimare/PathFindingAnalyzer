package sample.Algorithms;

import sample.Constant.CellState;
import sample.Constant.Constants;
import sample.Constant.Cell;
import sample.MazeController;

import java.util.ArrayList;
import java.util.LinkedList;

public class DepthFirst extends ShortestPath {

    public static boolean COMPLETE_DEPTHFIRST = false;

    private LinkedList<Cell> prevPath = null;

    @Override
    public void algorithm(Cell src, Cell des)
    {
        this.src = src;
        this.des = des;

        shortestPath = Integer.MAX_VALUE;
        pathFound = false; runThread = true;
    }

    private void DFS(Cell curr) {

        // The current is not the SOURCE node
        if(curr.state != CellState.SHORTEST && curr.state != CellState.SOURCE && curr.state != CellState.TARGET) {
            MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.VISITED);
            curr.state = CellState.VISITED;
        }

        for (int k = 0; k < Constants.TRAVERSAL_LEN && runThread && curr.distance < shortestPath && !pathFound; k++) {
            if (inRange(curr.i + Y[k], curr.j + X[k]))
            {
                Cell temp = MazeController.Grid[curr.i+Y[k]][curr.j+X[k]];

                if(curr.distance + 1 < temp.distance)
                {
                    try { Thread.sleep(Constants.SLEEP_TIME); } catch (Exception ignored){}

                    temp.distance = curr.distance + 1;
                    temp.setParent(curr.i, curr.j);

                    if(temp.state != CellState.TARGET) {
                        DFS(temp);
                    }
                    else { // IF THE TARGET IS REACHED
                        if(temp.distance < shortestPath) {
                            if(prevPath != null) {
                                colorPath(prevPath, Constants.VISITED, false);
                            }

                            shortestPath = temp.distance;
                            tracePath(temp);

                            if(!Constants.DFX_EXHAUSTIVE) runThread = false;
                        }
                        return;
                    }
                }
            }
        }

        if(curr.state != CellState.SHORTEST && curr.state != CellState.SOURCE && curr.state != CellState.TARGET) {
            MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.UNVISITED);
            curr.state = CellState.UNVISITED;
        }
    }

    public void tracePath(Cell temp) {

        LinkedList<Cell> shortestPath = new LinkedList<>();
        while (temp.state != CellState.SOURCE) {

            shortestPath.addFirst(temp);
            temp = MazeController.Grid[temp.p_i][temp.p_j];
        }
        prevPath = shortestPath;
        colorPath(shortestPath, Constants.SHORTEST, true);
    }

    @Override
    public void run() {

        src.distance = 0;
        DFS(src);

        if(!pathFound) MazeController.UpdateBorder(Constants.TARGET);
        Constants.currentThread = null;
        System.out.println("Return Depth-First Search Algorithm Thread");
    }
}
