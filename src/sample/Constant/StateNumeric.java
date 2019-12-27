package sample.Constant;

public class StateNumeric {
    private StateNumeric() {}

    public static CellState GetNumeric(char chr) {
        switch (chr)
        {
            case '0': return CellState.UNVISITED;
            case '1': return CellState.VISITED;
            case '2': return CellState.WALL;
            case '3': return CellState.SHORTEST;
            case '4': return CellState.INTERMEDIATE;
            case '5': return CellState.TARGET;
            case '6': return CellState.SOURCE;
        }
        return null;
    }

    public static char GetCharacter(CellState state) {
        if(state == CellState.WALL) return '2';
        return '0';
    }
}