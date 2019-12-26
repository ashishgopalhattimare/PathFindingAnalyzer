package sample.Constant;

public class Cell {

    public int i, j, distance;
    public int p_i, p_j;

    public CellState state;

    // Default Constructor for the MazeController Initialization of the grid cells
    public Cell(int i, int j)
    {
        this.i = i; this.j = j;
        distance = 0;
        state = CellState.UNVISITED;
    }

    public Cell(int i, int j, int dist) {
        this.i = i; this.j = j;
        this.distance = dist;
    }

    public void setParent(int i, int j) {
        p_i = i; p_j = j;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "i=" + i +
                ", j=" + j +
                ", distance=" + distance +
                ", p_i=" + p_i +
                ", p_j=" + p_j +
                ", state=" + state +
                '}';
    }
}
