package sample.Algorithms;

import sample.Constant.CellState;
import sample.Constant.Constants;
import sample.Constant.Cell;
import sample.MazeController;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class A_Search extends ShortestPath {

    private LinkedList<Cell> prevPath = null;
    public int shortestPath;

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

        src.distance = 0;
        pq.add(src);

        try {
            while (!pq.isEmpty() && !pathFound && runThread) {

                curr = pq.poll();

                // The distance of the last element in the array should be
                // used to check that array's validity
                if(curr.distance + 1 < shortestPath) // if the current front node can be better than the previous one
                {
                    if (!samePoint(src, curr)) // Ignore the source node
                        MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.VISITED);

                    for (int k = 0; k < Constants.TRAVERSAL_LEN; k++) {
                        if (inRange(curr.i + Y[k], curr.j + X[k])) {

                            temp = MazeController.Grid[curr.i + Y[k]][curr.j + X[k]];

                            if(curr.distance + 1 < temp.distance)
                            {
                                temp.distance = curr.distance + 1;
                                temp.setParent(curr.i, curr.j);

                                if (!samePoint(temp, des)) { // next possible visit for BSF
                                    MazeController.PaintBlock(temp.i, temp.j, Constants.BORDER, Constants.NEXT_VISIT);
                                    pq.add(temp);
                                }
                                else { // IF THE TARGET IS REACHED
                                    if(prevPath != null) {
                                        colorPath(prevPath, Constants.VISITED, false);
                                    }

                                    shortestPath = temp.distance;
                                    tracePath(temp);
                                }
                            }
                        }
                    }
                    Thread.sleep(Constants.SLEEP_TIME);
                }
            }
        }
        catch (Exception ignored) {}
        finally {
            while(!pq.isEmpty()) pq.poll();
        }

        if(!pathFound) MazeController.UpdateBorder(Constants.TARGET);
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

        long dest1 = calculateHValue(cell1);
        long dest2 = calculateHValue(cell2);

        if(dest1 == dest2) return 0;
        else if(dest1 < dest2)
            return -1;
        return 1;
    }

    // No need to do the square root as both are getting square root
    // if A > B => sqrt(A) > sqrt(B)
    public long calculateHValue(Cell curr) {
        return (long) (Math.pow(curr.i-dest.i,2) + Math.pow(curr.j-dest.j,2));
    }
}
