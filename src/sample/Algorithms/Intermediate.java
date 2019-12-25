package sample.Algorithms;

import sample.Constant.Point;

public class Intermediate extends ShortestPath {

    @Override
    public void algorithm(int [][] grid, Point src, Point des) {
        ShortestPath.grid = grid; this.src = src; this.des = des;
    }

    @Override
    public void run() { }
}
