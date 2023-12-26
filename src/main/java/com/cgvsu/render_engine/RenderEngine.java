package com.cgvsu.render_engine;

import com.cgvsu.math.matrix.Matrix4f;
import com.cgvsu.model.Model;
import com.cgvsu.utils.ZBuffer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static com.cgvsu.render_engine.GraphicConveyor.rotateScaleTranslate;
import static com.cgvsu.utils.models_utils.ModelMeshDrawer.drawMesh;
import static com.cgvsu.utils.models_utils.ModelRasterizer.rasterizeModel;


public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
            final boolean isActive,
            final RenderType renderType
    ) {
        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = Matrix4f.multiply(projectionMatrix, Matrix4f.multiply(viewMatrix, modelMatrix));

        ArrayList<ArrayList<Float>> buffer = ZBuffer.getDefaultPixelDepthMatrix(width,height);

        if (isActive) {
            graphicsContext.setStroke(new Color(0, 0.67, 0.71, 1));
        } else {
            graphicsContext.setStroke(mesh.color);
        }

        switch (renderType) {
            case RASTERIZATION -> rasterizeModel(graphicsContext, mesh.triangulatedCopy, modelViewProjectionMatrix, width, height, buffer);
            default -> drawMesh(graphicsContext, mesh, modelViewProjectionMatrix, width, height);
        }
    }
}