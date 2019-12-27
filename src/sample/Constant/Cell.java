package sample.Constant;

public class Cell {

    public int i, j, distance; // distance is 'g'
    public int p_i, p_j;

    public double f, g, h;
    public boolean visit;

    public CellState state;

    // Default Constructor for the MazeController Initialization of the grid cells
    public Cell(int i, int j)
    {
        this.i = i; this.j = j;
        state = CellState.UNVISITED;
    }

    public void AStarStateInitilization() {
        this.f = this.g =
                this.h = Float.MAX_VALUE;
    }

    public void setParent(int i, int j) {
        p_i = i; p_j = j;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "\ni=" + i +
                ",\nj=" + j +
                ",\ndistance=" + distance +
                ",\np_i=" + p_i +
                ",\np_j=" + p_j +
                ",\nf=" + f +
                ",\ng=" + g +
                ",\nh=" + h +
                ",\nstate=" + state +
                '}';
    }
}
