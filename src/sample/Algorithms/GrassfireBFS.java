package sample.Algorithms;

import sample.Constant.CellState;
import sample.Constant.Constants;
import sample.Constant.Cell;
import sample.MazeController;

import java.util.*;

public class GrassfireBFS extends ShortestPath {

    @Override
    public void algorithm(Cell src, Cell des) {
        this.src = src;
        this.des = des;

        shortestPath = Integer.MAX_VALUE;
        pathFound = false; runThread = true;
    }

    @Override
    public void run() {

        Queue<Cell> queue = new LinkedList<>();
        Cell curr, temp;

        src.distance = 0;
        queue.add(src);

        try {
            while(!queue.isEmpty() && !pathFound && runThread) {

                curr = queue.poll();

                if(curr.state != CellState.SOURCE)
                    MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.VISITED);

                for (int k = 0; k < Constants.TRAVERSAL_LEN && !pathFound; k++) {

                    if (inRange(curr.i+Y[k], curr.j+X[k])) {

                        temp = MazeController.Grid[curr.i+Y[k]][curr.j+X[k]];

                        if(temp.state == CellState.UNVISITED || temp.state == CellState.TARGET)
                        {
                            temp.distance = curr.distance + 1;
                            temp.setParent(curr.i, curr.j);

                            if(temp.state != CellState.TARGET)
                            {
                                MazeController.PaintBlock(temp.i, temp.j, Constants.BORDER, Constants.NEXT_VISIT);
                            }
                            else { // destination reached
                                tracePath(temp); // TracePath of the Destination Node and print its path, Target node is not saved

                                shortestPath = temp.distance;
                                pathFound = true;
                                break;
                            }

                            MazeController.Grid[temp.i][temp.j].state = CellState.VISITED;
                            queue.add(temp);
                        }
                    }
                }
                Thread.sleep(Constants.SLEEP_TIME);
            }
        }
        catch (Exception ignored) {}
        finally {
            while(!queue.isEmpty()) queue.poll();
        }

        if(!pathFound) MazeController.UpdateBorder(Constants.TARGET);
        Constants.currentThread = null;
        System.out.println("Return Breadth-First Search Algorithm Thread");
    }
}