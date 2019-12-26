package sample;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Constant.Constants;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class TutorialController implements Initializable {

    @FXML private JFXButton previousButton, nextButton, skipButton;
    @FXML private AnchorPane page0, page1, page2, page3, page4;
    @FXML private Label github;

    private int prevPage = -1, currPage = 0;
    private AnchorPane anchorPage[];

    private static final int PAGE_WIDTH = 400;
    private static final int TOTAL_PAGE = 5;

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        anchorPage = new AnchorPane[]{page0, page1, page2, page3, page4};

        nextButton.setOnAction(event -> {
            if(currPage < TOTAL_PAGE -1){
                prevPage = currPage;
                currPage++;

                updateNextButton();
                updatePage();
            }
            else {
                OpenApplication();
            }
        });

        previousButton.setOnAction(event -> {
            if(currPage > 0) {
                prevPage = currPage;
                currPage--;

                updateNextButton();
                updatePage();
            }
        });

        skipButton.setOnAction(event -> OpenApplication());

        github.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URI(Constants.GITHUB_LINK));
            } catch (Exception ignored) {}
        });
    }

    private void updatePage()
    {
        anchorPage[prevPage].setLayoutX(-PAGE_WIDTH);
        anchorPage[currPage].setLayoutX(0);
    }

    private void updateNextButton()
    {
        if(currPage == TOTAL_PAGE-1){
            nextButton.setText("Finish");
        }
        else {
            nextButton.setText("Next");
        }
    }

    private void OpenApplication()
    {
        Stage stage = (Stage) skipButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("maze.fxml"));
            Stage primaryStage = new Stage();

            root.setOnMousePressed((MouseEvent event) ->
            {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged((MouseEvent event) ->
            {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            });

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("PathFinder Analyzer");
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.getIcons().add(new Image("/images/logo.png"));
            primaryStage.show();

        }
        catch (Exception ignored) {}
    }
}
