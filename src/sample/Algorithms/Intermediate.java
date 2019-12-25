package sample.Algorithms;

import sample.Constant.Point;

public class Intermediate extends ShortestPath {

    @Override
    public void algorithm(int [][] grid, Point src, Point des) {
        this.src = src; this.des = des;
        ShortestPath.grid = grid;
    }

    @Override
    public void run() { }
}
