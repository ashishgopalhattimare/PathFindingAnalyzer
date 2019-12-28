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

    public AStar() {}

    @Override
    public void run() {

        PriorityQueue<Cell> pq = new PriorityQueue<Cell>(new ComparePoint(des));
        Cell curr, temp;
        long gNew, hNew, fNew;

        src.f = src.g = src.h = 0;

        src.inPQ = true; pq.add(src);

        try {
            while (!pq.isEmpty() && !pathFound && runThread)
            {
                curr = pq.poll();
//                System.out.println("Current Cell : " + curr);

                if (!samePoint(src, curr))
                {
                    MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.VISITED);
                    curr.state = CellState.VISITED;
                }

                for (int k = 0; k < Constants.TRAVERSAL_LEN; k++)
                {
                    if (inRange(curr.i + Y[k], curr.j + X[k]))
                    {
                        temp = MazeController.Grid[curr.i + Y[k]][curr.j + X[k]];

                        if(samePoint(temp, des)) { // TARGET IS REACHED
                            temp.setParent(curr.i, curr.j);
                            if(prevPath != null) {
                                colorPath(prevPath, Constants.VISITED, false);
                            }
                            tracePath(temp);

                            shortestPath = (int) temp.g;
                            pathFound = true;
                            break;
                        }
                        else {
                            // Neither VISITED, WALL OR SOURCE
                            if(temp.state != CellState.VISITED && temp.state != CellState.WALL &&
                                    temp.state != CellState.SOURCE)
                            {
                                if(k < 4) gNew = curr.g + Constants.MOVE_STRAIGHT; // E-W-N-S     + 10
                                else gNew = curr.g + Constants.MOVE_DIAGONAL;      // NW-NW-SW-SE + 14

                                hNew = calculateDistance(temp);
                                fNew = gNew + hNew;

                                if(temp.f == Float.MAX_VALUE || temp.f > fNew)
                                {
                                    temp.f = fNew; temp.g = gNew; temp.h = hNew;
                                    temp.setParent(curr.i, curr.j);

                                    MazeController.PaintBlock(temp.i, temp.j, Constants.BORDER, Constants.NEXT_VISIT);

                                    if(!temp.inPQ) {
//                                        System.out.println("Into PQ : " + temp);
                                        temp.inPQ = true; pq.add(temp);
                                    }
//                                    else {
//                                        System.out.println("Updated : "  + temp);
//                                    }
                                }
                            }
                        }
                    }
                }
                Thread.sleep(Constants.SLEEP_TIME);

                if(!pq.isEmpty()) pq.add(pq.poll());
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

    public void tracePath(Cell temp)
    {
        LinkedList<Cell> shortestPath = new LinkedList<>();
        while (temp.state != CellState.SOURCE) {
            shortestPath.addFirst(temp);
            temp = MazeController.Grid[temp.p_i][temp.p_j];
        }
        prevPath = shortestPath;
        colorPath(shortestPath, Constants.SHORTEST, true);
    }

    private int calculateDistance(Cell curr)
    {
        return Math.min(diagonal(curr, Math.abs(des.i-curr.i)), diagonal(curr, Math.abs(des.j-curr.j)));
    }

    private int diagonal(Cell curr, int diagonal)
    {
        int fi = 1, fj = 1;

        if(curr.j < des.j) fj=-1;
        if(curr.i < des.i) fi=-1;

        int newI = des.i + fi*diagonal;
        int newJ = des.j + fj*diagonal;

        int straight = Math.abs(curr.j-newJ) + Math.abs(curr.i-newI);

        return diagonal * Constants.MOVE_DIAGONAL + straight * Constants.MOVE_STRAIGHT;
    }
}

class ComparePoint implements Comparator<Cell>
{
    public static Cell dest;

    public ComparePoint(Cell dest) {
        ComparePoint.dest = dest;
    }

    @Override
    public int compare(Cell cell1, Cell cell2) {
        if(cell1.f == cell2.f) {// same f
            if(cell1.h == cell2.h) { // same h
                if(calculateHValue(cell1) < calculateHValue(cell2)) {
                    return -1;
                }
                return 1;
            }
            else if(cell1.h < cell2.h) {
                return -1;
            }
            return 0;
        }
        if(cell1.f < cell2.f) return -1;
        return 1;
    }

    // No need to do the square root as both are getting square root
    // if A > B => sqrt(A) > sqrt(B)
    public static long calculateHValue(Cell curr) {
        return (long) (Math.pow(curr.i-dest.i,2) + Math.pow(curr.j-dest.j,2));
    }
}
