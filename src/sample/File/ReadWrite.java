package sample.File;

import sample.Constant.Constants;
import sample.Constant.Point;
import sample.MazeController;

import java.io.*;
import java.util.ArrayList;

public class ReadWrite {

    private ReadWrite() {}

    public static boolean save(String path) {
        try {
            FileWriter fw = new FileWriter(path);

            StringBuilder sb = new StringBuilder();
            boolean possibleSave = false;

            for(int i = 0; i < Constants.ROW; i++) {
                for(int j = 0; j < Constants.COL; j++) {
                    if(MazeController.Grid[i][j] == Constants.wall)
                        possibleSave = true;
                    sb.append(MazeController.Grid[i][j]);
                }
                if(i != Constants.ROW-1) sb.append("\n");
            }

            if(!possibleSave) {
                fw.close();
                File file = new File(path);
                file.delete();

                return false;
            }

            fw.write(sb.toString());
            fw.close();
        }
        catch (Exception ignored) { }
        return false;
    }

    public static ArrayList<Point> read(String path)
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            ArrayList<Point> wallAr = new ArrayList<>();

            for(int i = 0; i < Constants.ROW; i++) {
                String str = br.readLine();
                for(int j = 0; j < Constants.COL; j++) {

                    if(MazeController.Grid[i][j] != Constants.source && MazeController.Grid[i][j] != Constants.target) {
                        MazeController.PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                        MazeController.Grid[i][j] = Character.getNumericValue(str.charAt(j));
                    }
                    if(MazeController.Grid[i][j] == Constants.wall) {
                        wallAr.add(new Point(i, j));
                    }
                }
            }
            return wallAr;
        }
        catch (Exception ignored) { }
        return null;
    }
}
