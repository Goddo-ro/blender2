package com.cgvsu.controllers;


import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cgvsu.utils.ListUtils.stringToNumberList;

public class GuiController {

    final private float TRANSLATION = 1.5F;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane consoleSplit;

    @FXML
    private SplitPane mainSplit;

    @FXML
    Canvas canvas;

    @FXML
    private BorderPane modelsContainer;

    @FXML
    private BorderPane modelsManipulations;

    @FXML
    private ScrollPane consoleScroll;

    @FXML ScrollPane manipulationsScroll;

    @FXML
    private Button toggleMenu;

    @FXML
    private Button toggleManipulations;

    @FXML
    private Button triangulateBtn;

    @FXML
    private Button deleteVerticesBtn;

    @FXML
    private Button toggleConsoleBtn;

    @FXML
    private TextField indicesText;

    @FXML
    private TreeView<String> models;

    @FXML
    private AnchorPane consolePane;

    @FXML
    private VBox console;

    ModelController modelController;
    ConsoleController consoleController;
    LogController logController;

    private boolean isMenuClosed = false;
    private boolean isManipulationsClosed = false;

    private final int OPENED_MENU_WIDTH = 350;
    private final int CLOSED_MENU_WIDTH = 20;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    List<Integer> getIndicesFromRemoveInput() {
        List<String> elements = new ArrayList<>(Arrays.asList(indicesText.getText().split(" ")));
        return stringToNumberList(elements);
    }

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        modelController = new ModelController(this, anchorPane, models);
        consoleController = new ConsoleController(mainSplit, toggleConsoleBtn);
        logController = new LogController(console, consolePane, consoleScroll);

        onMouseToggleConsoleClick();
        onMouseToggleMenuClick();
        onMouseToggleManipulationsClick();

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
//            camera.setAspectRatio((float) (width / height));
            camera.setAspectRatio((float) (height / width));

            toggleConsoleBtn.setPrefWidth(width);
            console.setMinHeight(consoleSplit.getHeight() - consoleController.CLOSED_CONSOLE_POSITION - 5);
            consoleScroll.setVmax(console.getHeight());

            if (consolePane.getHeight() != console.getHeight()) {
                consolePane.setPrefHeight(console.getHeight());
            }


            if (logController.getLogs().size() != console.getChildren().size()) {
                logController.updateLogs();
            }

            for (Model model : modelController.getModelsList()) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, model,
                        (int) width, (int) height, modelController.isModelActive(model));
            }

            anchorPane.getScene().addPreLayoutPulseListener(() -> {
                consoleController.windowResizing = true;
            });
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        modelController.loadModel();
    }

    @FXML
    private void onMouseToggleMenuClick() {
        if (isMenuClosed) {
            toggleMenu.setText("-");
            toggleMenu.setPrefWidth(OPENED_MENU_WIDTH);
            modelsContainer.setPrefWidth(OPENED_MENU_WIDTH);
            models.setOpacity(1);
        } else {
            toggleMenu.setText("+");
            modelsContainer.setPrefWidth(CLOSED_MENU_WIDTH);
            models.setOpacity(0);
        }

        isMenuClosed = !isMenuClosed;
    }

    @FXML
    private void onMouseToggleManipulationsClick() {
        if (isManipulationsClosed) {
            toggleManipulations.setText("-");
            toggleManipulations.setPrefWidth(OPENED_MENU_WIDTH);
            modelsManipulations.setPrefWidth(OPENED_MENU_WIDTH);
            manipulationsScroll.setOpacity(1);
        } else {
            toggleManipulations.setText("+");
            modelsManipulations.setPrefWidth(CLOSED_MENU_WIDTH);
            manipulationsScroll.setOpacity(0);
        }

        isManipulationsClosed = !isManipulationsClosed;
    }

    @FXML
    private void onMouseToggleConsoleClick() {
        consoleController.toggleConsole();
    }

    @FXML
    private void onMouseTriangulateClick() {
        modelController.triangulateModels();
    }

    @FXML
    private void onMouseDelVerticesClick() {
        modelController.deleteVertices();
    }

    @FXML
    private void onMouseDelPolygonsClick() {
        modelController.deletePolygons();
    }

    @FXML
    private void onDelKeyClick(KeyEvent key) {
        if (key.getCode() == KeyCode.DELETE) {
            modelController.removeSelectedModels();
        }
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }
}