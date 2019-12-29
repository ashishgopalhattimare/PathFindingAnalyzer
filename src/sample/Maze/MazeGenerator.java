package sample.Maze;

import sample.Constant.CellState;
import sample.Constant.Constants;
import sample.Constant.StateNumeric;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

class Point {
    int i, j;
    public Point(int i, int j) {
        this.i = i; this.j = j;
    }
}

public class MazeGenerator
{
    private static Random random = new Random();

    private static int[] Y = {-1,0,1, 0};
    private static int[] X = { 0,1,0,-1};

    private static char[] color = {StateNumeric.GetCharacter(CellState.WALL),
            StateNumeric.GetCharacter(CellState.UNVISITED)};

    private MazeGenerator() { }

    private static boolean inRange(int r, int c) {
        return (r >= 0 && r < Constants.ROW) && (c >= 0 && c < Constants.COL);
    }

    private static int randint(int limit) {
        return random.nextInt(limit);
    }

    public static String constructMaze() {

        int[][] maze = new int[Constants.ROW][Constants.COL];

        Stack<Point> st = new Stack<>();
        Point curr;

        int ni, nj;
        int ei, ej;

        Point p = new Point(randint(Constants.ROW), randint(Constants.COL));
        st.push(p);

        while(!st.isEmpty())
        {
            curr = st.peek(); // get the top Point

            maze[curr.i][curr.j] = 1;
            ArrayList<Integer> nlst = new ArrayList<>();

            for(int i = 0; i < 4; i++) {
                ni = curr.i+Y[i]; nj = curr.j+X[i];
                if(inRange(ni, nj)) {
                    if(maze[ni][nj] == 0) {
                        // # of occupied neighbours must be 1
                        int ctr = 0;
                        for(int j = 0; j < 4; j++) {
                            ei = ni+Y[j]; ej = nj+X[j];
                            if(inRange(ei,ej)) {
                                if(maze[ei][ej] == 1) ctr += 1;
                            }
                        }
                        if(ctr == 1) nlst.add(i);
                    }
                }
            }

            if(nlst.size() > 0) {
                int ranDir = nlst.get(randint(nlst.size()));
                st.add(new Point(curr.i+Y[ranDir], curr.j+X[ranDir]));
            }
            else { st.pop(); }
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < Constants.ROW; i++) {
            for(int j = 0; j < Constants.COL; j++) {
                sb.append(color[maze[i][j]]);
            }
        }

        return sb.toString();
    }
}
