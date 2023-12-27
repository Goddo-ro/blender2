package com.cgvsu.controllers;

import com.cgvsu.render_engine.RenderType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

public class RenderTypeController {
    RenderType renderType = RenderType.MESH;
    private SplitMenuButton renderTypeMenu;

    public RenderTypeController(SplitMenuButton renderTypeMenu) {
        this.renderTypeMenu = renderTypeMenu;

        initialize();
    }

    private void initialize() {
        MenuItem mesh = new MenuItem("Mesh");
        MenuItem raster = new MenuItem("Rasterization");
        MenuItem texture = new MenuItem("Texture");

        renderTypeMenu.getItems().clear();
        renderTypeMenu.getItems().addAll(mesh, raster, texture);

        mesh.setOnAction((e) -> renderType = RenderType.MESH);
        raster.setOnAction((e) -> renderType = RenderType.RASTERIZATION);
    }
}
