package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Algorithms.A_Search;
import sample.Algorithms.Breadth_First;
import sample.Algorithms.Depth_First;
import sample.Constant.Point;
import sample.Algorithms.ShortestPath;
import sample.Constant.Constants;
import sample.File.ReadWrite;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class MazeController implements Initializable {

    @FXML
    private GridPane platform;
    @FXML
    private ComboBox<String> algoOptions;

    @FXML
    private JFXButton   clearButton, intermediateButton, pathButton, mazeButton, sourceButton,
                        targetButton, drawButton, saveButton, loadButton, cancelButton;
    @FXML
    private JFXButton   visualButton;
    @FXML
    private JFXCheckBox diagonalCheckBox;

    public enum State { UNVISITED, WALL, SOURCE, TARGET, INTERMEDIATE }
    private State curState;
    private int algoIndex;
    private boolean applyColor;

    int currSD[][] = new int[3][2];

    public static BorderPane[][] borderGrid = new BorderPane[Constants.ROW][Constants.COL];

    public static int[][] Grid = new int[Constants.ROW][Constants.COL];

    void addBoxProperty(int i, int j) {
        BorderPane pane = new BorderPane();
        Font f = new Font(5);
        Label l = new Label("1");
        l.setFont(f);

        pane.getChildren().add(l);

        pane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.UNVISITED + ";");
        platform.add(pane, j, i);
        borderGrid[i][j] = pane;

        pane.setOnMouseEntered(event -> {
            if(curState == State.WALL) {
                // NEED TO WORK ON THIS FUNCTION
                if(MazeController.Grid[i][j] != Constants.source && MazeController.Grid[i][j] != Constants.target) {
                    if(applyColor) {
                        pane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.WALL + ";");
                        Grid[i][j] = Constants.wall;
                    }
                }
            }
            else if(curState == State.UNVISITED) {
                if(MazeController.Grid[i][j] != Constants.source && MazeController.Grid[i][j] != Constants.target) {
                    if(applyColor) {
                        pane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.UNVISITED + ";");
                        Grid[i][j] = Constants.unvisit;
                    }
                }
            }
        });

        pane.setOnMouseClicked(event -> {

            if(curState == State.SOURCE) {
                if(Grid[i][j] == Constants.unvisit) {
                    unvisitPreviousBlock(0);
                    PaintBlock(i, j, Constants.BORDER, Constants.SOURCE);
                    currSD[0][0] = i; currSD[0][1] = j;
                }
            }
            else if(curState == State.TARGET) {
                if(Grid[i][j] == Constants.unvisit) {
                    unvisitPreviousBlock(1);
                    PaintBlock(i, j, Constants.BORDER, Constants.TARGET);
                    currSD[1][0] = i; currSD[1][1] = j;
                }
            }
            else if(curState == State.INTERMEDIATE) {

                if(Grid[i][j] == Constants.intermediate) {
                    unvisitPreviousBlock(2);
                    currSD[2][0] = -1; currSD[2][1] = -1;
                }
                else { // first time or change somewhere else
                    unvisitPreviousBlock(2);

                    PaintBlock(i, j, Constants.BORDER, Constants.INTERMEDIATE);
                    Grid[i][j] = Constants.intermediate;
                    currSD[2][0] = i; currSD[2][1] = j;
                }
            }

            if(curState == State.WALL || curState == State.UNVISITED) {
                applyColor = !(applyColor);

                if(applyColor) {
                    // Apply Wall on the current Grid box
                    if (curState == State.WALL) {
                        PaintBlock(i, j, Constants.BORDER, Constants.WALL);
                        Grid[i][j] = Constants.wall;
                    }
                    // Apply Unvisited on the current Grid box
                    else {
                        PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                        Grid[i][j] = Constants.unvisit;
                    }
                }
            }
        });

        Grid[i][j] = Constants.unvisit;
    }

    private void unvisitPreviousBlock(int row) {
        if(currSD[row][0] != -1) {
            PaintBlock(currSD[row][0], currSD[row][1], Constants.BORDER, Constants.UNVISITED);
            Grid[currSD[row][0]][currSD[row][1]] = Constants.unvisit;
        }
    }

    public synchronized static void PaintBlock(int i, int j, String border, String fill)
    {
        borderGrid[i][j].setStyle("-fx-border-color: " + border + "; -fx-background-color: " + fill + ";");
    }

    private void buttonStateChange(boolean show)
    {
        pathButton.setDisable(show); mazeButton.setDisable(show);
        intermediateButton.setDisable(show);
        clearButton.setDisable(show);
    }

    private void resetGrid() {

        System.out.println("resetGrid");
        for (int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {
                PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                Grid[i][j] = Constants.unvisit;
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for(int i = 0; i < Constants.ROW; i++) {
            for(int j = 0; j < Constants.COL; j++) {
                addBoxProperty(i, j);
            }
        }
        for(int i = 0; i < 3; i++) currSD[i][0] = -1;

        algoOptions.getItems().addAll("Breadth First Search", "Depth First Search",
                "Intermediate Shortest Path", "A* Algorithm");

        algoOptions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                algoIndex = algoOptions.getSelectionModel().getSelectedIndex();
            }
        });
        algoIndex = -1;

        applyColor = false;
        curState = null;

        cancelButton.setDisable(true);
    }

    private void updateGrid() {

        for(int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {
                if(Grid[i][j] != Constants.wall) {
                    PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                    Grid[i][j] = Constants.unvisit;
                }
            }
        }

        PaintBlock(currSD[0][0], currSD[0][1], Constants.BORDER, Constants.SOURCE);
        PaintBlock(currSD[1][0], currSD[1][1], Constants.BORDER, Constants.TARGET);
    }

    @FXML void visualActionEvent(ActionEvent event) {

        // If both the source and destination points are given
        applyColor = false;
        if(Constants.currentThread == null) {
            if(currSD[0][0] != -1 && currSD[1][0] != -1 && algoIndex != -1) {

                ShortestPath algo = null;
                updateGrid();

                if(diagonalCheckBox.isSelected()) Constants.TRAVERSAL_LEN = Constants.DIAGONAL;
                else Constants.TRAVERSAL_LEN = Constants.NON_DIAGONAL;

                switch(algoIndex) {
                    case 0: algo = new Breadth_First();
                            break;
                    case 1: algo = new Depth_First();
                            break;
                    case 3: algo = new A_Search();
                            break;
                }
                if(algo != null)
                {
                    Constants.currentThread = algo;

                    algo.algorithm(Grid,new Point(currSD[0][0], currSD[0][1]), new Point(currSD[1][0], currSD[1][1]));
                    algo.start();
                }

                saveButton.setDisable(true); drawButton.setDisable(true); loadButton.setDisable(true);
                sourceButton.setDisable(true); targetButton.setDisable(true);
                buttonStateChange(true);

                cancelButton.setDisable(false);
            }
        }
    }

    @FXML void clearBoardActionEvent(ActionEvent event) {

        for(int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {
                PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                Grid[i][j] = Constants.unvisit;
            }
        }

        if(currSD[0][0] != -1) PaintBlock(currSD[0][0], currSD[0][1], Constants.BORDER, Constants.SOURCE);
        if(currSD[1][0] != -1) PaintBlock(currSD[1][0], currSD[1][1], Constants.BORDER, Constants.TARGET);
    }

    @FXML void clearPathActionEvent(ActionEvent event) {

        System.out.println("clearPath");
        for(int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {

                if(Grid[i][j] == Constants.wall) continue;

                PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                Grid[i][j] = Constants.unvisit;
            }
        }

        if(currSD[0][0] != -1) PaintBlock(currSD[0][0], currSD[0][1], Constants.BORDER, Constants.SOURCE);
        if(currSD[1][0] != -1) PaintBlock(currSD[1][0], currSD[1][1], Constants.BORDER, Constants.TARGET);
    }

    @FXML void mazeActionEvent(ActionEvent event) {

        System.out.println("Load Random Maze");

    }

    @FXML void drawMazeActionEvent(ActionEvent event) {

        buttonStateChange(true); // enable the other buttons
        applyColor = false;
        resetGrid();

        cancelButton.setDisable(false); saveButton.setDisable(false);
        drawButton.setDisable(true);
    }

    @FXML void saveMazeActionEvent(ActionEvent event) {

        String filePath = Constants.mazeDirect + "fun.txt";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("template"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Save Maze");

        Stage fileStage = new Stage();
        File file = fileChooser.showSaveDialog(fileStage);

        if(file != null) filePath = file.getPath();
        System.out.println(filePath);

        ReadWrite.save(filePath);
        cancelButton.setDisable(true); saveButton.setDisable(true);
        drawButton.setDisable(false);
    }

    @FXML void cancelMazeActionEvent(ActionEvent event) {

        if(Constants.currentThread != null) Constants.currentThread.killThread();
        Constants.currentThread = null;

        applyColor = false;

        saveButton.setDisable(false); loadButton.setDisable(false); drawButton.setDisable(false);
        sourceButton.setDisable(false); targetButton.setDisable(false);
        buttonStateChange(false);

        cancelButton.setDisable(true);
    }

    @FXML void loadMazeActionEvent(ActionEvent event) {

        ArrayList<Point> wallAr = ReadWrite.read(Constants.mazeDirect + "template.txt");

        if(wallAr != null) { // There are wall
            Collections.shuffle(wallAr);

            Thread thread = new Thread(() -> {
                int wallPainted = 0;
                for(Point wall : wallAr) {
                    try {
                        PaintBlock(wall.i, wall.j, Constants.BORDER, Constants.WALL);
                        Thread.sleep(Constants.MAZE_TIME);
                        wallPainted++;
                    } catch (Exception ignored) { }
                }
                if(wallPainted < wallAr.size()) { // If any wall is left
                    for(Point x : wallAr) {
                        PaintBlock(x.i, x.j, Constants.BORDER, Constants.WALL);
                    }
                }
            });
            thread.start();
        }

        saveButton.setDisable(false);
    }

    @FXML void wallActionEvent(ActionEvent event) {
        curState = State.WALL;
        applyColor = false;
    }
    @FXML void unvisitedActionEvent(ActionEvent event){
        curState = State.UNVISITED;
        applyColor = false;
    }

    @FXML void intermediateActionEvent(ActionEvent event)   { curState = State.INTERMEDIATE; }
    @FXML void sourceActionEvent(ActionEvent event)         { curState = State.SOURCE; }
    @FXML void targetActionEvent(ActionEvent event)         { curState = State.TARGET; }
}
