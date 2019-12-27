package sample.Algorithms;

import sample.Constant.CellState;
import sample.Constant.Constants;
import sample.Constant.Cell;
import sample.MazeController;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AStar extends ShortestPath {

    private LinkedList<Cell> prevPath = null;
    public int shortestPath;

    private final int CELL_WEIGHT = 1;

    public AStar() {}

    @Override
    public void algorithm(Cell src, Cell des) {
        this.src = src; this.des = des;

        this.shortestPath = Integer.MAX_VALUE;
        pathFound = false; runThread = true;
    }

    @Override
    public void run() {

        PriorityQueue<Cell> pq = new PriorityQueue<Cell>(new ComparePoint(des));
        Cell curr, temp;
        double gNew, hNew, fNew;

        src.f = src.g = src.h = 0.0;
        src.distance = 0;

        pq.add(src);

        try {
            while (!pq.isEmpty() && !pathFound && runThread) {

                curr = pq.poll();
                curr.visit = true;

                if (!samePoint(src, curr)) // Ignore the source node
                    MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.VISITED);

                for (int k = 0; k < Constants.TRAVERSAL_LEN; k++) {

                    if (inRange(curr.i + Y[k], curr.j + X[k])) {

                        temp = MazeController.Grid[curr.i + Y[k]][curr.j + X[k]];

                        if(curr.distance < temp.distance) {

                            if(samePoint(temp, des)){ // IF THE TARGET IS REACHED

                                if(curr.distance < shortestPath) {
                                    temp.setParent(curr.i, curr.j);
                                    if(prevPath != null) {
                                        colorPath(prevPath, Constants.VISITED, false);
                                    }
                                    shortestPath = curr.distance;
                                    tracePath(temp);

                                    pathFound = true;
                                    break;
                                }
                            }
                            else {
                                if(!temp.visit && temp.state != CellState.WALL && temp.state != CellState.SOURCE && temp.state != CellState.TARGET)
                                {
                                    gNew = curr.g + CELL_WEIGHT;
                                    hNew = Math.sqrt(ComparePoint.calculateHValue(temp));
                                    fNew = gNew + hNew;

                                    if(temp.f == Float.MAX_VALUE || temp.f > fNew) {

                                        temp.f = fNew; temp.g = gNew; temp.h = hNew;
                                        temp.setParent(curr.i, curr.j);

                                        temp.distance = curr.distance + 1;

                                        MazeController.PaintBlock(temp.i, temp.j, Constants.BORDER, Constants.NEXT_VISIT);
                                        pq.add(temp);
                                    }
                                }
                            }
                        }
                    }
                }
                Thread.sleep(Constants.SLEEP_TIME);
            }
        }
        catch (Exception ignored) {}
        finally {
            while(!pq.isEmpty()) pq.poll();
        }

        if(pathFound) MazeController.UpdateBorder(Constants.TARGET);
        Constants.currentThread = null;
        System.out.println("Return A* Search Algorithm Thread");
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
}

class ComparePoint implements Comparator<Cell> {

    public static Cell dest;

    public ComparePoint(Cell dest) {
        ComparePoint.dest = dest;
    }

    @Override
    public int compare(Cell cell1, Cell cell2) {
        if(cell1.f < cell2.f) return -1;
        return 1;
    }

    // No need to do the square root as both are getting square root
    // if A > B => sqrt(A) > sqrt(B)
    public static long calculateHValue(Cell curr) {
        return (long) (Math.pow(curr.i-dest.i,2) + Math.pow(curr.j-dest.j,2));
    }
}
