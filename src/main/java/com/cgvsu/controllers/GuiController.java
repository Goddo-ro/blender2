package com.cgvsu.controllers;


import com.cgvsu.log.Statuses;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;

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
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.vecmath.Vector3f;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.cgvsu.utils.models_utils.Triangulation.triangulateModel;
import static com.cgvsu.utils.ListUtils.stringToNumberList;
import static com.cgvsu.utils.models_utils.PolygonRemover.removePolygons;
import static com.cgvsu.utils.models_utils.VerticesRemover.deleteVertexes;
import static com.cgvsu.utils.StringUtils.generateUniqueName;

public class GuiController {

    final private float TRANSLATION = 1.5F;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    Canvas canvas;

    @FXML
    private BorderPane modelsContainer;

    @FXML
    private BorderPane modelsManipulations;

    @FXML
    private BorderPane consoleContainer;

    @FXML
    private ScrollPane consoleScroll;

    @FXML ScrollPane manipulationsScroll;

    @FXML
    private Button toggleMenu;

    @FXML
    private Button toggleConsoleBtn;

    @FXML
    private Button toggleManipulations;

    @FXML
    private Button triangulateBtn;

    @FXML
    private Button deleteVerticesBtn;

    @FXML
    private TextField indicesText;

    @FXML
    private TreeView<String> models;

    @FXML
    private AnchorPane consolePane;

    @FXML
    private VBox console;

    private ModelController modelController;
    public LogController logController;

    private boolean isMenuClosed = false;
    private boolean isConsoleClosed = false;
    private boolean isManipulationsClosed = false;

    private final int OPENED_MENU_WIDTH = 350;
    private final int CLOSED_MENU_WIDTH = 20;

    private final int OPENED_CONSOLE_HEIGHT = 350;
    private final int CLOSED_CONSOLE_HEIGHT = 20;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    private List<Integer> getIndicesFromRemoveInput() {
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
        logController = new LogController(console, consolePane, consoleScroll);

        onMouseToggleConsoleClick();
        onMouseToggleMenuClick();
        onMouseToggleManipulationsClick();

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            toggleConsoleBtn.setPrefWidth(width);
            consoleScroll.setVmax(console.getHeight());
            modelsContainer.setPrefHeight(canvas.getHeight() - CLOSED_CONSOLE_HEIGHT + 1);

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
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            Model mesh = ObjReader.read(fileContent);
            mesh.setName(generateUniqueName(file.getName(), modelController.getModelsName()));
            modelController.getModelsList().add(mesh);
            modelController.updateModels();
            logController.addLog("Model " + mesh.getName() + " was successfully loaded", Statuses.MESSAGE);
        } catch (Exception exception) {
            logController.addLog(exception.getMessage(), Statuses.ERROR);
        }
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
        if (isConsoleClosed) {
            toggleConsoleBtn.setText("-");
            consoleContainer.setMaxHeight(OPENED_CONSOLE_HEIGHT - 1);
            modelsContainer.setPrefHeight(canvas.getHeight() - OPENED_CONSOLE_HEIGHT + 1);
            modelsManipulations.setMinHeight(canvas.getHeight() - OPENED_CONSOLE_HEIGHT + 1);
            consoleScroll.setOpacity(1);
        } else {
            toggleConsoleBtn.setText("+");
            consoleContainer.setMaxHeight(CLOSED_CONSOLE_HEIGHT);
            modelsContainer.setMinHeight(canvas.getHeight() - CLOSED_CONSOLE_HEIGHT + 1);
            modelsManipulations.setMinHeight(canvas.getHeight() - OPENED_CONSOLE_HEIGHT + 1);
            consoleScroll.setOpacity(0);
        }

        isConsoleClosed = !isConsoleClosed;
    }

    @FXML
    private void onMouseTriangulateClick() {
        try {
            List<Model> selectedModels = modelController.getSelectedModels();
            if (selectedModels.size() == 0) {
                logController.addLog("Models haven't been triangulated as there is no selected models", Statuses.WARNING);
                return;
            }

            for (Model model : selectedModels) {
                triangulateModel(model);
                logController.addLog("Model " + model.getName() + " was successfully triangulated", Statuses.MESSAGE);
            }
        } catch (Exception exception) {
            logController.addLog(exception.getMessage(), Statuses.ERROR);
        }
    }

    @FXML
    private void onMouseDelVerticesClick() {
        // TODO: not change vertices if some of them are greater than max index
        try {
            List<TreeItem<String>> selectedModels = modelController.getSelectedModelsNames();
            if (selectedModels.size() == 0) {
                logController.addLog("Polygons haven't been deleted as there is no selected models", Statuses.WARNING);
                return;
            }

            Model model = modelController.getModelByName(selectedModels.get(0).getValue());
            assert model != null;

            List<Integer> indices = getIndicesFromRemoveInput();

            deleteVertexes(model, indices);

            logController.addLog("Vertices successfully deleted", Statuses.MESSAGE);

            if (model.polygons.size() == 0) {
                modelController.removeModel(model);
                logController.addLog("Model was removed as it hadn't any polygons", Statuses.WARNING);
            }

            modelController.updateModels();
        } catch (Exception exception) {
            logController.addLog(exception.getMessage(), Statuses.ERROR);
        }
    }

    @FXML
    private void onMouseDelPolygonsClick() {
        try {
            List<TreeItem<String>> selectedModels = modelController.getSelectedModelsNames();
            if (selectedModels.size() == 0) {
                logController.addLog("Indices haven't been deleted as there is no selected models", Statuses.WARNING);
                return;
            }

            Model model = modelController.getModelByName(selectedModels.get(0).getValue());
            assert model != null;

            List<Integer> indices = getIndicesFromRemoveInput();

            removePolygons(model, (ArrayList<Integer>) indices, true);

            logController.addLog("Polygons successfully deleted", Statuses.MESSAGE);

            if (model.polygons.size() == 0) {
                modelController.removeModel(model);
                logController.addLog("Model was removed as it hadn't any polygons", Statuses.WARNING);
            }

            modelController.updateModels();
        } catch (Exception exception) {
            logController.addLog(exception.getMessage(), Statuses.ERROR);
        }
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