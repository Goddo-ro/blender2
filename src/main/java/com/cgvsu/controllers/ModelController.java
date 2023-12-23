package com.cgvsu.controllers;

import com.cgvsu.log.Statuses;
import com.cgvsu.model.Model;
import com.cgvsu.objwriter.ObjWriter;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelController {
    private final List<Model> modelsList = new ArrayList<>();
    private final TreeView<String> models;
    private final GuiController root;
    private AnchorPane anchorPane;
    private MultipleSelectionModel<TreeItem<String>> selectionModel;

    public ModelController(GuiController root, AnchorPane anchorPane, TreeView<String> models) {
        this.root = root;
        this.anchorPane = anchorPane;
        this.models = models;

        TreeItem<String> rootTreeNode = new TreeItem<>("Objects");
        TreeItem<String> modelsNode = new TreeItem<>("Models");
        rootTreeNode.getChildren().add(modelsNode);
        models.setRoot(rootTreeNode);

        selectionModel = models.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
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
}
