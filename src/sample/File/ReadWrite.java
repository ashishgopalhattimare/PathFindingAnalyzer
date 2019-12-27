package sample.File;

import sample.Constant.CellState;
import sample.Constant.Constants;
import sample.Constant.Cell;
import sample.Constant.StateNumeric;
import sample.MazeController;

import java.io.*;
import java.util.ArrayList;

public class ReadWrite {

    private ReadWrite() {}

    public static void save(String path) {
        try {
            FileWriter fw = new FileWriter(path);

            StringBuilder sb = new StringBuilder();
            boolean possibleSave = false;

            for(int i = 0; i < Constants.ROW; i++) {
                for(int j = 0; j < Constants.COL; j++) {
                    if(MazeController.Grid[i][j].state == CellState.WALL)
                        possibleSave = true;

                    sb.append(StateNumeric.GetCharacter(MazeController.Grid[i][j].state) + " ");
                }
                if(i != Constants.ROW-1) sb.append("\n");
            }

            if(!possibleSave) { // the maze is not saved, delete the file, and close the FileWriter
                fw.close(); File file = new File(path);
                file.delete(); // delete the file if the file is empty

                return;
            }

            fw.write(sb.toString());
            fw.close();
        }
        catch (Exception ignored) { }
    }

    public static ArrayList<Cell> read(String path)
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            ArrayList<Cell> wallAr = new ArrayList<>();
            Cell curr;

            for(int i = 0; i < Constants.ROW; i++) {
                String str = br.readLine();
                for(int j = 0; j < Constants.COL; j++) {

                    curr = MazeController.Grid[i][j];

                    if(curr.state != CellState.SOURCE && curr.state != CellState.TARGET) {
                        MazeController.PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);

                        curr.state = StateNumeric.GetNumeric(str.charAt(j));
                    }
                    if(curr.state == CellState.WALL) {
                        wallAr.add(new Cell(i, j));
                    }
                }
            }
            return wallAr;
        }
        catch (Exception ignored) { }
        return null;
    }
}
