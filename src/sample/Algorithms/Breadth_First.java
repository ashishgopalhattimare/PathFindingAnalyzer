package sample.Algorithms;

import sample.Constant.Constants;
import sample.Constant.Point;
import sample.MazeController;

import java.util.*;

public class Breadth_First extends ShortestPath {

    @Override
    public void algorithm(int [][] grid, Point src, Point des) {
        ShortestPath.grid = grid; this.src = src; this.des = des;
        pathFound = false; runThread = true;
    }

    @Override
    public void run() {

        Queue<ArrayList<Point>> queue = new LinkedList<>();
        Point curr;

        grid[src.i][src.j] = Constants.visit;

        ArrayList arrayList = new ArrayList<>(Arrays.asList(src));
        queue.add(arrayList);

        try {
            while(!queue.isEmpty() && !pathFound && runThread) {

                arrayList = queue.poll();
                curr = (Point) arrayList.get(arrayList.size() - 1);

                if (!samePoint(src, curr)) // Ignore the source node
                    MazeController.PaintBlock(curr.i, curr.j, Constants.BORDER, Constants.VISITED);

                for (int k = 0; k < Constants.TRAVERSAL_LEN && !pathFound; k++) {
                    if (inRange(curr.i + Y[k], curr.j + X[k]) && grid[curr.i + Y[k]][curr.j + X[k]] == Constants.unvisit ) {

                        Point temp = new Point(curr.i + Y[k], curr.j + X[k]);

                        if (!samePoint(temp, des)) { // next possible visit for BSF
                            MazeController.PaintBlock(temp.i, temp.j, Constants.BORDER, Constants.NEXT_VISIT);
                        }
                        else { // destination reached
                            colorPath(arrayList, Constants.SHORTEST, true);
                            pathFound = true;
                            break;
                        }

                        grid[temp.i][temp.j] = Constants.visit;

                        ArrayList<Point> xArrayList = (ArrayList<Point>) arrayList.clone();
                        xArrayList.add(temp);
                        queue.add(xArrayList);
                    }
                }
                Thread.sleep(Constants.SLEEP_TIME);
            }
        }
        catch (Exception ignored) {}

        while(!queue.isEmpty()) queue.poll();
        Constants.currentThread = null;
        System.out.println("Thrd end");
    }
}