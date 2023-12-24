package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.math.Vector2f;
import com.cgvsu.model.Polygon;
import com.cgvsu.model.Triangle;
import com.cgvsu.utils.models_utils.ModelRasterizer;
import com.cgvsu.utils.models_utils.Triangulation;
import com.cgvsu.utils.triangles_utils.TriangleRasterization;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import com.cgvsu.model.Model;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.matrix.Matrix4f;

//import javax.vecmath.Matrix4f;
//import javax.vecmath.Vector3f;

import javax.vecmath.Point2f;

import static com.cgvsu.render_engine.GraphicConveyor.*;
import static com.cgvsu.utils.models_utils.ModelRasterizer.vertex3fToVector2f;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
            final boolean isActive
    ) {
//        ModelRasterizer.rasterizeModel(graphicsContext,Triangulation.getTriangulatedModel(mesh), width, height, Color.RED);
        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewMatrix = modelMatrix.multiply(viewMatrix);
        Matrix4f modelViewProjectionMatrix = modelViewMatrix.multiply(projectionMatrix);


        for (Polygon polygon: mesh.polygons
        ) {
            ArrayList<Vector3f> vertices = new ArrayList<>();
            ArrayList<Vector2f> points = new ArrayList<>();
            for (Integer ind: polygon.getVertexIndices()
            ) {
                vertices.add(mesh.vertices.get(ind));
            }
//            float[][] buffer = ZBuffer.getDefaultPixelDepthMatrix(width, height);
//            ArrayList<Vector3f> polygonPoints = ZBuffer.getInterpolatedPolygonVectors(vertices.get(0), vertices.get(1),vertices.get(2));
            for (Vector3f vertex: vertices
            ) {
//                if (ZBuffer.testBuffer((int)vertex.getX(), (int) vertex.getY(), vertex.getZ(), buffer  )) {
                Vector3f vertexVecmath = new Vector3f(vertex.getX(), vertex.getY(), vertex.getZ());
                Vector2f point = vertex3fToVector2f(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath), width, height);
                points.add(point);
//                }
            }

            TriangleRasterization.drawTriangle(graphicsContext,points, Color.RED);

        }

        if (isActive) {
            graphicsContext.setStroke(new Color(0, 0.67, 0.71, 1));
        } else {
            graphicsContext.setStroke(Color.BLACK);
        }

        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.polygons.get(polygonInd).getVertexIndices().size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                Vector3f vertexVecmath = new Vector3f(vertex.getX(), vertex.getY(), vertex.getZ());

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath), width, height);
                resultPoints.add(resultPoint);
            }

            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).x,
                        resultPoints.get(vertexInPolygonInd - 1).y,
                        resultPoints.get(vertexInPolygonInd).x,
                        resultPoints.get(vertexInPolygonInd).y);
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).x,
                        resultPoints.get(nVerticesInPolygon - 1).y,
                        resultPoints.get(0).x,
                        resultPoints.get(0).y);


        }


    }
}