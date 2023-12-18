package com.cgvsu;

import com.cgvsu.render_engine.RenderEngine;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.vecmath.Vector3f;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

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
    private Button toggleMenu;

    @FXML
    private TreeView<String> models;

    private Model mesh = null;

    private final List<Model> modelsList = new ArrayList<>();

    private boolean isMenuClosed = false;

    private final int OPENED_MENU_WIDTH = 300;
    private final int CLOSED_MENU_WIDTH = 20;

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
                anchorPane.getChildren().remove(pane);
            });

            saveBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent clickEvent) -> {
                System.out.println("Saving!");
                anchorPane.getChildren().remove(pane);
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

    private void initializeModels() {
        TreeItem<String> rootTreeNode = new TreeItem<>("Objects");
        TreeItem<String> modelsNode = new TreeItem<>("Models");
        rootTreeNode.getChildren().add(modelsNode);
        models.setRoot(rootTreeNode);

        MultipleSelectionModel<TreeItem<String>> selectionModel = models.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
    }

    private List<String> getModelsName() {
        List<String> names = new ArrayList<>();
        for (Model model : modelsList) {
            names.add(model.getName());
        }

        return names;
    }

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        modelsContainer.setLayoutX(canvas.getWidth() - modelsContainer.getWidth());
        modelsContainer.setPrefWidth(OPENED_MENU_WIDTH);

        toggleMenu.setPrefWidth(OPENED_MENU_WIDTH);

        initializeModels();

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            for (Model model : modelsList) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, model, (int) width, (int) height);
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

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
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
            // todo: обработка ошибок
        } catch (IOException exception) {

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