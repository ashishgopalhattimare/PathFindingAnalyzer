package sample.Algorithms;

import sample.Constant.Constants;
import sample.Constant.Point;
import sample.MazeController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class A_Search extends ShortestPath {

    private ArrayList<Point> prevPath = null;
    public int shortestPath;

    @Override
    public void algorithm(int[][] grid, Point src, Point des) {
        ShortestPath.grid = grid; this.src = src; this.des = des;
        pathFound = false; runThread = true;

        this.shortestPath = Integer.MAX_VALUE;
    }

    @Override
    public void run() {

        PriorityQueue<ArrayList<Point>> pq = new PriorityQueue<ArrayList<Point>>(new ComparePoint(des));
        Point curr;

        grid[src.i][src.j] = Constants.visit;

        ArrayList pathList = new ArrayList<>(Arrays.asList(src));
        pq.add(pathList);

        try {
            while (!pq.isEmpty() && !pathFound && runThread) {

                pathList = pq.poll();

                if(pathList.size() < shortestPath) // if the current front node can be better than the previous one
                {
                    curr = (Point) pathList.get(pathList.size() - 1);

                    if (!samePoint(src, curr)) // Ignore the source node
                        MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.VISITED);

                    for (int k = 0; k < Constants.TRAVERSAL_LEN && !pathFound; k++) {
                        if (inRange(curr.i + Y[k], curr.j + X[k]) && grid[curr.i + Y[k]][curr.j + X[k]] == Constants.unvisit ) {

                            Point temp = new Point(curr.i + Y[k], curr.j + X[k]);

                            if (!samePoint(temp, des)) { // next possible visit for BSF
                                MazeController.PaintBlock(temp.i, temp.j, Constants.BORDER, Constants.NEXT_VISIT);
                            }
                            else { // destination reached

                                if(prevPath != null) {
                                    colorPath(prevPath, Constants.VISITED, false);
                                }

                                colorPath(pathList, Constants.SHORTEST, true);
                                shortestPath = pathList.size();
                            }

                            grid[temp.i][temp.j] = Constants.visit;

                            ArrayList<Point> xArrayList = (ArrayList<Point>) pathList.clone();
                            xArrayList.add(temp);
                            pq.add(xArrayList);
                        }
                    }
                    Thread.sleep(Constants.SLEEP_TIME);
                }
            }
        }
        catch (Exception ignored) {}

        Constants.currentThread = null;
        System.out.println("Thrd end");
    }
}

class ComparePoint implements Comparator<ArrayList<Point>> {

    public static Point dest;

    public ComparePoint(Point dest) {
        ComparePoint.dest = dest;
    }

    @Override
    public int compare(ArrayList<Point> pr1, ArrayList<Point> pr2) {

        Point point1 = pr1.get(pr1.size()-1);
        Point point2 = pr2.get(pr2.size()-1);

        long dest1 = calculateHValue(point1);
        long dest2 = calculateHValue(point2);

        if(dest1 == dest2) return 0;
        else if(dest1 < dest2)
            return -1;
        return 1;
    }

    // No need to do the square root as both are getting square root
    // if A > B => sqrt(A) > sqrt(B)
    public long calculateHValue(Point curr) {
        return (long) (Math.pow(curr.i-dest.i,2) + Math.pow(curr.j-dest.j,2));
    }
}
