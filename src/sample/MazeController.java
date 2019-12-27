package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import sample.Algorithms.*;
import sample.Constant.CellState;
import sample.Constant.Cell;
import sample.Constant.Constants;
import sample.File.ReadWrite;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class MazeController implements Initializable {

    @FXML private GridPane platform;

    @FXML private ComboBox<String> algoOptions;
    @FXML private ToggleSwitch updateBorder;

    @FXML private JFXButton   clearButton, intermediateButton, pathButton, mazeButton, sourceButton,
                        targetButton, drawButton, saveButton, loadButton, cancelButton;

    @FXML private JFXButton   visualButton;
    @FXML private JFXCheckBox diagonalCheckBox;
    @FXML private JFXCheckBox dfsCheckBox;
    @FXML private FlowPane labelPanel;

    public static GridPane gp;

    private CellState curState;
    private int algoIndex;
    private boolean applyColor;

    private double xOffset = 0;
    private double yOffset = 0;

    private int[][] currSD = new int[3][2];

    public static BorderPane[][] borderGrid = new BorderPane[Constants.ROW][Constants.COL];
    public static Cell[][] Grid = new Cell[Constants.ROW][Constants.COL];


    void addBoxProperty(int i, int j) {

        BorderPane pane = new BorderPane();

        pane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.UNVISITED + ";");
        platform.add(pane, j, i);
        borderGrid[i][j] = pane;

        pane.setOnMouseEntered(event -> {
            if(curState == CellState.WALL) {
                // NEED TO WORK ON THIS FUNCTION
                if(Grid[i][j].state != CellState.SOURCE && Grid[i][j].state != CellState.TARGET) {
                    if(applyColor) {
                        pane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.WALL + ";");
                        Grid[i][j].state = CellState.WALL;
                    }
                }
            }
            else if(curState == CellState.UNVISITED) {
                if(Grid[i][j].state != CellState.SOURCE && Grid[i][j].state != CellState.TARGET) {
                    if(applyColor) {
                        pane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.UNVISITED + ";");
                        Grid[i][j].state = CellState.UNVISITED;
                    }
                }
            }
        });

        pane.setOnMouseClicked(event -> {

            if(curState == CellState.SOURCE) {
                if(Grid[i][j].state == CellState.SOURCE) { // Remove it from the board
                    Grid[i][j].state = CellState.UNVISITED;
                    unvisitPreviousBlock(0);
                }
                else if(Grid[i][j].state == CellState.UNVISITED) {
                    unvisitPreviousBlock(0);
                    PaintBlock(i, j, Constants.BORDER, Constants.SOURCE);
                    currSD[0][0] = i; currSD[0][1] = j;

                    Grid[i][j].state = CellState.SOURCE;
                }
            }
            else if(curState == CellState.TARGET) {
                if(Grid[i][j].state == CellState.TARGET) { // Remove it from the board
                    Grid[i][j].state = CellState.UNVISITED;
                    unvisitPreviousBlock(1);
                }
                else if(Grid[i][j].state == CellState.UNVISITED) {
                    unvisitPreviousBlock(1);
                    PaintBlock(i, j, Constants.BORDER, Constants.TARGET);
                    currSD[1][0] = i; currSD[1][1] = j;

                    Grid[i][j].state = CellState.TARGET;
                }
            }
            else if(curState == CellState.INTERMEDIATE) {
                if(Grid[i][j].state == CellState.INTERMEDIATE) {
                    unvisitPreviousBlock(2);
                    currSD[2][0] = -1; currSD[2][1] = -1;

                    Grid[i][j].state = CellState.UNVISITED;
                }
                else { // first time or change somewhere else
                    unvisitPreviousBlock(2);

                    PaintBlock(i, j, Constants.BORDER, Constants.INTERMEDIATE);
                    Grid[i][j].state = CellState.INTERMEDIATE;
                    currSD[2][0] = i; currSD[2][1] = j;
                }
            }

            if(curState == CellState.WALL || curState == CellState.UNVISITED) {
                applyColor = !(applyColor);

                if(applyColor && Grid[i][j].state != CellState.SOURCE && Grid[i][j].state != CellState.TARGET) {
                    // Apply Wall on the current Grid box
                    if (curState == CellState.WALL) {
                        PaintBlock(i, j, Constants.BORDER, Constants.WALL);
                        Grid[i][j].state = CellState.WALL;
                    }
                    // Apply Unvisited on the current Grid box
                    else {
                        PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                        Grid[i][j].state = CellState.UNVISITED;
                    }
                }
            }
        });

        Grid[i][j].state = CellState.UNVISITED; // is it default
    }

    private void unvisitPreviousBlock(int row) {
        if(currSD[row][0] != -1) {
            PaintBlock(currSD[row][0], currSD[row][1], Constants.BORDER, Constants.UNVISITED);
            Grid[currSD[row][0]][currSD[row][1]].state = CellState.UNVISITED;
        }
    }

    public synchronized static void PaintBlock(int i, int j, String border, String fill) {
        borderGrid[i][j].setStyle("-fx-border-color: " + border + "; -fx-background-color: " + fill + ";");
    }

    private void buttonStateChange(boolean show) {
        pathButton.setDisable(show); mazeButton.setDisable(show);
        intermediateButton.setDisable(show);
        clearButton.setDisable(show);
    }

    private void resetGrid()
    {
        for (int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {
                PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                Grid[i][j].state = CellState.UNVISITED;
            }
        }
    }

    public static void UpdateBorder(String color) {
        if(Constants.UPDATE_BORDER) {
            gp.setStyle("-fx-border-color: " + color);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

         /**
        movablePane.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        movablePane.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        platform.setStyle("-fx-border-color: " + Constants.WALL);
        gp = platform;

        minimizeButton.setOnAction(event -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
         **/

        for(int i = 0; i < Constants.ROW; i++) {
            for(int j = 0; j < Constants.COL; j++) {
                Grid[i][j] = new Cell(i, j);
                addBoxProperty(i, j);
            }
        }
        for(int i = 0; i < 3; i++) currSD[i][0] = -1;

        algoOptions.getItems().addAll("Breadth First Search", "Depth First Search",
                "A* Algorithm", "Greedy Best-First");

        algoOptions.setOnAction(event -> algoIndex = algoOptions.getSelectionModel().getSelectedIndex());

        applyColor = false;
        curState = null;
        algoIndex = -1;
    }

    private void updateGrid() {

        for(int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {

                Grid[i][j].setParent(-1,-1); // Set parent to null
                Grid[i][j].visit = false;

                if(algoIndex == 2) { // A-start Algorithm Implementation
                    Grid[i][j].AStarStateInitilization();
                }

                // Remove Everything except the walls
                if(Grid[i][j].state != CellState.WALL) {
                    PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                    Grid[i][j].state = CellState.UNVISITED;

                    Grid[i][j].distance = Constants.UNVISITED_WEIGHT;
                }
                else {
                    Grid[i][j].distance = Constants.WALL_WEIGHT;
                }
            }
        }

        // Regain the Source and Target point
        PaintBlock(currSD[0][0], currSD[0][1], Constants.BORDER, Constants.SOURCE);
        PaintBlock(currSD[1][0], currSD[1][1], Constants.BORDER, Constants.TARGET);

        Grid[currSD[0][0]][currSD[0][1]].state = CellState.SOURCE;
        Grid[currSD[1][0]][currSD[1][1]].state = CellState.TARGET;
    }

    @FXML void visualActionEvent(ActionEvent event) {

        // If both the source and destination points are given
        applyColor = false;
        if(Constants.currentThread == null) {
            if(currSD[0][0] != -1 && currSD[1][0] != -1 && algoIndex != -1) {

                ShortestPath algo = null;
                updateGrid();

                Constants.UPDATE_BORDER = updateBorder.isSelected();
                UpdateBorder(Constants.WALL);

                if(diagonalCheckBox.isSelected()) Constants.TRAVERSAL_LEN = Constants.DIAGONAL;
                else Constants.TRAVERSAL_LEN = Constants.NON_DIAGONAL;

                Constants.DFX_EXHAUSTIVE = dfsCheckBox.isSelected();

                switch(algoIndex) {
                    case 0: algo = new GrassfireBFS();
                            break;
                    case 1: algo = new DepthFirst();
                            break;
                    case 2: algo = new AStar();
                            break;
                    case 3: algo = new GreedyBestFirst();
                            break;
                }
                if(algo != null)
                {
                    Constants.currentThread = algo;

                    algo.algorithm(Grid[currSD[0][0]][currSD[0][1]], Grid[currSD[1][0]][currSD[1][1]]);
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
                Grid[i][j].state = CellState.UNVISITED;
            }
        }

        if(currSD[0][0] != -1) PaintBlock(currSD[0][0], currSD[0][1], Constants.BORDER, Constants.SOURCE);
        if(currSD[1][0] != -1) PaintBlock(currSD[1][0], currSD[1][1], Constants.BORDER, Constants.TARGET);
    }

    @FXML void clearPathActionEvent(ActionEvent event) {

        System.out.println("clearPath");
        for(int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {

                if(Grid[i][j].state == CellState.WALL) continue;

                PaintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                Grid[i][j].state = CellState.UNVISITED;
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Save Maze");

        Stage fileStage = new Stage();
        File file = fileChooser.showSaveDialog(fileStage);

        if(file != null) filePath = file.getPath();
        else {
            File f = new File(filePath);
            if(!f.exists()) return;
        }

        ReadWrite.save(filePath);
        cancelButton.setDisable(true); saveButton.setDisable(true);
        drawButton.setDisable(false);
    }

    @FXML void cancelMazeActionEvent(ActionEvent event) {

        if(Constants.currentThread != null) Constants.currentThread.killThread();
        Constants.currentThread = null;

        UpdateBorder(Constants.WALL);

        applyColor = false;

        saveButton.setDisable(false); loadButton.setDisable(false); drawButton.setDisable(false);
        sourceButton.setDisable(false); targetButton.setDisable(false);
        buttonStateChange(false);

        cancelButton.setDisable(true);
    }

    @FXML void loadMazeActionEvent(ActionEvent event) {

        String filePath = Constants.mazeDirect + "template.txt";

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Load Maze");

        Stage fileStage = new Stage();
        File file = fileChooser.showOpenDialog(fileStage);

        if(file != null) filePath = file.getPath();
        else {
            File f = new File(filePath);
            if(!f.exists()) return;
        }

        ArrayList<Cell> wallAr = ReadWrite.read(filePath);

        if(wallAr != null) { // There are wall
            Collections.shuffle(wallAr);

            Thread thread = new Thread(() -> {
                int wallPainted = 0;
                for(Cell wall : wallAr) {
                    try {
                        PaintBlock(wall.i, wall.j, Constants.BORDER, Constants.WALL);
                        Thread.sleep(Constants.MAZE_TIME);
                        wallPainted++;
                    } catch (Exception ignored) { }
                }

                if(wallPainted < wallAr.size()) { // If any wall is left
                    for(Cell x : wallAr) {
                        PaintBlock(x.i, x.j, Constants.BORDER, Constants.WALL);
                    }
                }
            });
            thread.start();
        }

        saveButton.setDisable(false);
    }

    @FXML void wallActionEvent(ActionEvent event) {
        curState = CellState.WALL;
        applyColor = false;
    }
    @FXML void unvisitedActionEvent(ActionEvent event){
        curState = CellState.UNVISITED;
        applyColor = false;
    }

    @FXML void intermediateActionEvent(ActionEvent event)   { curState = CellState.INTERMEDIATE; }
    @FXML void sourceActionEvent(ActionEvent event)         { curState = CellState.SOURCE; }
    @FXML void targetActionEvent(ActionEvent event)         { curState = CellState.TARGET; }
}
