package sample.Algorithms;

import sample.Constant.CellState;
import sample.Constant.Constants;
import sample.Constant.Cell;
import sample.MazeController;

import java.util.ArrayList;
import java.util.LinkedList;

public class Depth_First extends ShortestPath {

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
        if(!samePoint(curr, src)) {
            if(curr.state != CellState.SHORTEST && curr.state != CellState.SOURCE && curr.state != CellState.TARGET) {
                MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.VISITED);
                curr.state = CellState.VISITED;
            }
        }

        for (int k = 0; k < Constants.TRAVERSAL_LEN && runThread && curr.distance < shortestPath; k++) {
            if (inRange(curr.i + Y[k], curr.j + X[k]))
            {
                Cell temp = MazeController.Grid[curr.i+Y[k]][curr.j+X[k]];

                if(temp.state == CellState.UNVISITED || temp.state == CellState.TARGET || temp.state == CellState.SHORTEST)
                {
                    try { Thread.sleep(Constants.SLEEP_TIME); } catch (Exception ignored){}

                    if(curr.state != CellState.SHORTEST && curr.state != CellState.SOURCE && curr.state != CellState.TARGET) {
                        MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.VISITED);
                        curr.state = CellState.VISITED;
                    }

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
                        }
                        return;
                    }
                }
            }
        }
    }

    public void tracePath(Cell temp) {

        LinkedList<Cell> shortestPath = new LinkedList<>();
        while (temp.state != CellState.SOURCE) {

            shortestPath.addFirst(temp);
            temp = MazeController.Grid[temp.p_i][temp.p_j];
        }

        shortestPath.removeLast();
        prevPath = shortestPath;

        System.out.println("Color Path");
        colorPath(shortestPath, Constants.SHORTEST, true);
    }

    @Override
    public void run() {

        src.distance = 0;
        DFS(src);

        Constants.currentThread = null;

        if(pathFound) System.out.println("Shortest DFS Path Found");

        System.out.println("Return Depth-First Search Algorithm Thread");
    }
}
