package sample.testing;

import sample.Constant.Cell;
import sample.Constant.Constants;

/**
 * Testing the distance between two points in a grid (shortest distance involving only diagonal and (vertical,horizontal)
 * distance total cost
 */
public class Test {

    static Cell des;
    public static void main(String[] args) {

        Cell curr = new Cell(8,28);
        des = new Cell(6,25);

        System.out.println(calculateDistance(curr));
    }

    private static int calculateDistance(Cell curr) {
        return Math.min(diagonal(curr, Math.abs(des.i-curr.i)), diagonal(curr, Math.abs(des.j-curr.j)));
    }

    private static int diagonal(Cell curr, int diagonal) {

        int fi = 1, fj = 1;
        if(curr.j < des.j) fj=-1;
        if(curr.i < des.i) fi=-1;

        int newI = des.i + fi*diagonal;
        int newJ = des.j + fj*diagonal;

        int hv = Math.abs(curr.j-newJ) + Math.abs(curr.i-newI);

        System.out.println("Diagonal : " + diagonal);
        System.out.println("Straight : " + hv);

        return diagonal * Constants.MOVE_DIAGONAL + hv * Constants.MOVE_STRAIGHT;
    }
}
