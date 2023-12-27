package com.cgvsu.controllers;

import com.cgvsu.log.Statuses;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cgvsu.utils.StringUtils.generateUniqueName;
import static com.cgvsu.utils.models_utils.ModelConverter.triPolyModelToModel;
import static com.cgvsu.utils.models_utils.PolygonRemover.removePolygons;
import static com.cgvsu.utils.models_utils.Triangulation.getTriangulatedModel;
import static com.cgvsu.utils.models_utils.VerticesRemover.deleteVertexes;

public class ModelController {
    private final List<Model> modelsList = new ArrayList<>();
    private final TreeView<String> models;
    private final GuiController root;
    private GridPane modelGrid;
    private AnchorPane anchorPane;
    private MultipleSelectionModel<TreeItem<String>> selectionModel;
    private Model activeModel = null;

    public ModelController(GuiController root, AnchorPane anchorPane, GridPane modelGrid, TreeView<String> models) {
        this.root = root;
        this.anchorPane = anchorPane;
        this.modelGrid = modelGrid;
        this.models = models;

        TreeItem<String> rootTreeNode = new TreeItem<>("Objects");
        TreeItem<String> modelsNode = new TreeItem<>("Models");
        rootTreeNode.getChildren().add(modelsNode);
        models.setRoot(rootTreeNode);

        selectionModel = models.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);

        models.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue,
                                Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                if (selectedItem == null) {
                    activeModel = null;
                    clearModelGrid();
                    return;
                }
                updateActiveModel(selectedItem.getValue());
            }
        });
    }

    void updateActiveModel(String modelName) {
        clearModelGrid();
        if (modelName.endsWith(".obj")) {
            activeModel = getModelByName(modelName);
            clearModelGrid();
            Label nameLabel = new Label(activeModel.getName());
            nameLabel.setStyle("-fx-text-fill: #ffffff; -fx-label-padding: 0 0 0 12");
            modelGrid.add(nameLabel, 1, 0);

            ColorPicker modelColor = new ColorPicker(activeModel.color);
            modelGrid.add(modelColor, 1, 1);

            modelColor.setOnAction((e) -> {
                Color color = modelColor.getValue();
                activeModel.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity()));
            });
        }
    }

    void clearModelGrid() {
        if (3 > modelGrid.getChildren().size() - 1)
            return;
        for (int i = 3; i < modelGrid.getChildren().size(); i++) {
            modelGrid.getChildren().set(i, new Label(""));
        }
    }

    List<Model> getModelsList() {
        return modelsList;
    }

    List<String> getModelsName() {
        List<String> names = new ArrayList<>();
        for (Model model : modelsList) {
            names.add(model.getName());
        }

        return names;
    }

    Model getModelByName(String name) {
        for (Model model : modelsList) {
            if (model.getName().equals(name))
                return model;
        }

        return null;
    }

    List<TreeItem<String>> getSelectedModelsNames() {
        return selectionModel.getSelectedItems();
    }

    List<Model> getSelectedModels() {
        List<TreeItem<String>> selectedModelsNames = getSelectedModelsNames();
        List<Model> result = new ArrayList<>();
        for (TreeItem<String> modelName : selectedModelsNames) {
            result.add(getModelByName(modelName.getValue()));
        }

        return result;
    }

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

    void updateModels() {
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

    boolean isModelActive(Model model) {
        for (TreeItem<String> item : getSelectedModelsNames()) {
            if (item.getValue().equals(model.getName()))
                return true;
        }

        return false;
    }

    void replaceModel(String modelName, Model newModel) {
        for (int i = 0; i < modelsList.size(); i++) {
            if (modelsList.get(i).getName().equals(modelName)) {
                modelsList.set(i, newModel);
                return;
            }
        }
    }

    void removeModel(Model model) {
        modelsList.remove(model);
    }

    void removeSelectedModels() {
        for (int i = 0; i < modelsList.size(); i++) {
            if (isModelActive(modelsList.get(i))) {
                modelsList.remove(i);
                i--;
            }
        }

        updateModels();

        root.logController.addLog("Selected models successfully removed", Statuses.MESSAGE);
    }

    void expandTreeView(TreeItem<?> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(true);
            for (TreeItem<?> child : item.getChildren()) {
                expandTreeView(child);
            }
        }
    }

    void loadModel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(root.canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            Model mesh = ObjReader.read(fileContent);
            mesh.setName(generateUniqueName(file.getName(), getModelsName()));
            mesh.triangulatedCopy = getTriangulatedModel(mesh);
            getModelsList().add(mesh);
            updateModels();
            root.logController.addLog("Model " + mesh.getName() + " was successfully loaded", Statuses.MESSAGE);
        } catch (Exception exception) {
            root.logController.addLog(exception.getMessage(), Statuses.ERROR);
        }
    }

    void saveModel(Model model) {
        // TODO: fix triangulated models saving
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save Model");

        fileChooser.setInitialFileName(model.getName());

        File selectedFile = fileChooser.showSaveDialog(root.canvas.getScene().getWindow());

        if (selectedFile != null) {
            try {
                ObjWriter.write(model, selectedFile.getAbsolutePath());
                root.logController.addLog("Model " + model.getName() + " was successfully saved", Statuses.MESSAGE);
            } catch (Exception exception) {
                root.logController.addLog(exception.getMessage(), Statuses.ERROR);
            }
        }
    }

    void triangulateModels() {
        try {
            List<Model> selectedModels = getSelectedModels();
            if (selectedModels.size() == 0) {
                root.logController.addLog("Models haven't been triangulated as there is no selected models", Statuses.WARNING);
                return;
            }

            for (Model model : selectedModels) {
                Model triangulatedModel = triPolyModelToModel(getTriangulatedModel(model));
                System.out.println(triangulatedModel.getName());
                replaceModel(model.getName(), triangulatedModel);
                root.logController.addLog("Model " + model.getName() + " was successfully triangulated", Statuses.MESSAGE);
            }
        } catch (Exception exception) {
            root.logController.addLog(exception.getMessage(), Statuses.ERROR);
        }
    }

    void deleteVertices() {
        // TODO: not change vertices if some of them are greater than max index
        try {
            List<TreeItem<String>> selectedModels = getSelectedModelsNames();
            if (selectedModels.size() == 0) {
                root.logController.addLog("Polygons haven't been deleted as there is no selected models", Statuses.WARNING);
                return;
            }

            Model model = root.modelController.getModelByName(selectedModels.get(0).getValue());
            assert model != null;

            List<Integer> indices = root.getIndicesFromRemoveInput();

            deleteVertexes(model, indices);

            root.logController.addLog("Vertices successfully deleted", Statuses.MESSAGE);

            if (model.polygons.size() == 0) {
                root.modelController.removeModel(model);
                root.logController.addLog("Model was removed as it hadn't any polygons", Statuses.WARNING);
            }

            root.modelController.updateModels();
        } catch (Exception exception) {
            root.logController.addLog(exception.getMessage(), Statuses.ERROR);
        }
    }

    void deletePolygons() {
        try {
            List<TreeItem<String>> selectedModels = getSelectedModelsNames();
            if (selectedModels.size() == 0) {
                root.logController.addLog("Indices haven't been deleted as there is no selected models", Statuses.WARNING);
                return;
            }

            Model model = root.modelController.getModelByName(selectedModels.get(0).getValue());
            assert model != null;

            List<Integer> indices = root.getIndicesFromRemoveInput();

            removePolygons(model, (ArrayList<Integer>) indices, true);

            root.logController.addLog("Polygons successfully deleted", Statuses.MESSAGE);

            if (model.polygons.size() == 0) {
                root.modelController.removeModel(model);
                root.logController.addLog("Model was removed as it hadn't any polygons", Statuses.WARNING);
            }

            root.modelController.updateModels();
        } catch (Exception exception) {
            root.logController.addLog(exception.getMessage(), Statuses.ERROR);
        }
    }
}
