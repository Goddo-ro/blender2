package com.cgvsu;

import com.cgvsu.log.Log;
import com.cgvsu.log.Statuses;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.vecmath.Vector3f;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.cgvsu.utils.ListUtils.stringToNumberList;
import static com.cgvsu.utils.LogsUtils.generateLabelFromLog;
import static com.cgvsu.utils.ModelUtils.deleteVertexes;
import static com.cgvsu.utils.StringUtils.generateUniqueName;

public class GuiController {

    final private float TRANSLATION = 1.5F;

    @FXML
    AnchorPane anchorPane;


    @FXML
    private Canvas canvas;

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

    private Model mesh = null;

    private final List<Model> modelsList = new ArrayList<>();

    private boolean isMenuClosed = false;
    private boolean isConsoleClosed = false;
    private boolean isManipulationsClosed = false;

    private MultipleSelectionModel<TreeItem<String>> selectionModel;

    private final List<Log> logs = new ArrayList<>();

    private final int OPENED_MENU_WIDTH = 350;
    private final int CLOSED_MENU_WIDTH = 20;

    private final int OPENED_CONSOLE_HEIGHT = 350;
    private final int CLOSED_CONSOLE_HEIGHT = 20;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    EventHandler<MouseEvent> modelClickHandler = (MouseEvent event) -> {
        if (!event.getPickResult().getIntersectedNode().getParent().toString().contains("'")) {
            return;
        }

        String name = event.getPickResult().getIntersectedNode().getParent().toString().split("'")[1];
        if (!name.endsWith(".obj")) {
            return;
        }

        if (Objects.equals(event.getButton().toString(), "SECONDARY")) {
            double sceneX = event.getSceneX();
            double sceneY = event.getSceneY();

            Button saveBtn = new Button("Save");
            saveBtn.setAlignment(Pos.BASELINE_LEFT);
            saveBtn.setPrefWidth(100);

            TilePane pane = new TilePane(saveBtn);
            pane.setLayoutX(sceneX);
            pane.setLayoutY(sceneY);

            anchorPane.getChildren().add(pane);

            pane.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent exitedEvent) -> {
                if (anchorPane.getChildren().contains(pane)) {
                    anchorPane.getChildren().remove(pane);
                }
            });

            saveBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent clickEvent) -> {
                pane.setOpacity(0);
                saveModel(Objects.requireNonNull(getModelByName(name)));
            });
        }
    };

    private void updateModels() {
        TreeItem<String> modelsNode = new TreeItem<>("Models");
        for (Model model : modelsList) {
            TreeItem<String> modelItem = new TreeItem<>(model.getName());
            modelsNode.getChildren().add(modelItem);
        }

        models.getRoot().getChildren().remove(0);
        models.getRoot().getChildren().add(modelsNode);
        expandTreeView(models.getRoot());

        models.addEventHandler(MouseEvent.MOUSE_CLICKED, modelClickHandler);
    }

    private void expandTreeView(TreeItem<?> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(true);
            for (TreeItem<?> child : item.getChildren()) {
                expandTreeView(child);
            }
        }
    }

    private List<String> getModelsName() {
        List<String> names = new ArrayList<>();
        for (Model model : modelsList) {
            names.add(model.getName());
        }

        return names;
    }

    private Model getModelByName(String name) {
        for (Model model : modelsList) {
            if (model.getName().equals(name))
                return model;
        }

        return null;
    }

    private void saveModel(Model model) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Model");

        fileChooser.setInitialFileName(model.getName());

        File selectedFile = fileChooser.showSaveDialog(canvas.getScene().getWindow());

        if (selectedFile != null) {
            try {
                ObjWriter.write(model, selectedFile.getAbsolutePath());
                addLog("Model " + model.getName() + " was successfully saved", Statuses.MESSAGE);
            } catch (Exception exception) {
                addLog(exception.getMessage(), Statuses.ERROR);
            }
        }
    }

    private void removeModel(Model model) {
        modelsList.remove(model);
    }

    public void addLog(String body, Statuses status) {
        logs.add(0, new Log(body, status));
        updateLogs();
    }

    public List<TreeItem<String>> getSelectedModels() {
        return selectionModel.getSelectedItems();
    }

    public boolean isModelActive(Model model) {
        for (TreeItem<String> item : getSelectedModels()) {
            if (item.getValue().equals(model.getName()))
                return true;
        }

        return false;
    }

    private void removeActiveModels() {
        for (int i = 0; i < modelsList.size(); i++) {
            if (isModelActive(modelsList.get(i))) {
                modelsList.remove(i);
                i--;
            }
        }

        updateModels();
    }

    private void initializeModels() {
        TreeItem<String> rootTreeNode = new TreeItem<>("Objects");
        TreeItem<String> modelsNode = new TreeItem<>("Models");
        rootTreeNode.getChildren().add(modelsNode);
        models.setRoot(rootTreeNode);

        selectionModel = models.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void updateLogs() {
        console.getChildren().clear();
        for (Log log : logs) {
            Label label = generateLabelFromLog(log);
            console.getChildren().add(label);
        }

        console.setMinHeight(console.getChildren().size() * 30 - 12);
        consolePane.setPrefHeight(console.getChildren().size() * 30 - 12);
        consoleScroll.setVmax(console.getChildren().size() * 30 - 12);
        consoleScroll.setVvalue(console.getChildren().size() * 30 - 12);
    }

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        onMouseToggleConsoleClick();
        onMouseToggleMenuClick();
        onMouseToggleManipulationsClick();

        initializeModels();

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            toggleConsoleBtn.setPrefWidth(width);
            modelsContainer.setPrefHeight(canvas.getHeight() - CLOSED_CONSOLE_HEIGHT + 1);

            if (consolePane.getHeight() != console.getHeight()) {
                consolePane.setPrefHeight(console.getHeight());
            }


            if (logs.size() != console.getChildren().size()) {
                updateLogs();
            }

            for (Model model : modelsList) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, model, (int) width, (int) height, isModelActive(model));
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
            mesh = ObjReader.read(fileContent);
            mesh.setName(generateUniqueName(file.getName(), getModelsName()));
            modelsList.add(mesh);
            updateModels();
            addLog("Model " + mesh.getName() + " was successfully loaded", Statuses.MESSAGE);
        } catch (Exception exception) {
            addLog(exception.getMessage(), Statuses.ERROR);
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
        // Вызов функции трингуляции (для активных моделей!)
        System.out.println("Triangulated!");
    }

    @FXML
    private void onMouseDelVerticesClick() {
        // TODO: not change vertices if some of them are greater than max index
        try {
            List<TreeItem<String>> selectedModels = getSelectedModels();
            if (selectedModels.size() == 0) {
                addLog("Indices haven't been deleted as there is no selected models", Statuses.WARNING);
                return;
            }

            Model model = getModelByName(selectedModels.get(0).getValue());
            List<String> elements = new ArrayList<>(Arrays.asList(indicesText.getText().split(" ")));
            List<Integer> indices = stringToNumberList(elements);
            assert model != null;

            deleteVertexes(model, indices);

            addLog("Indices successfully deleted", Statuses.MESSAGE);

            if (model.polygons.size() == 0) {
                removeModel(model);
                addLog("Model was removed as it hadn't any polygons", Statuses.WARNING);
            }

            updateModels();
        } catch (Exception exception) {
            addLog(exception.getMessage(), Statuses.ERROR);
        }
    }


    @FXML
    private void onDelKeyClick(KeyEvent key) {
        if (key.getCode() == KeyCode.DELETE) {
            removeActiveModels();
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