package com.cgvsu.utils.models_utils;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.matrix.Matrix4f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.model.TriPolyModel;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.utils.ZBuffer;
import com.cgvsu.utils.triangles_utils.TriangleRasterization;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

import static com.cgvsu.render_engine.GraphicConveyor.multiplyMatrix4ByVector3;
import static com.cgvsu.render_engine.GraphicConveyor.rotateScaleTranslate;


public class ModelRasterizer {
    public static void rasterizeModel(GraphicsContext gc, TriPolyModel model, int width, int height, Color color){

//        if (!model.isTriangulated) model =ModelConverter.triPolyModelToModel(Triangulation.getTriangulatedModel(model));
        for (Polygon polygon: model.polygons
             ) {
            ArrayList<Vector3f> vertices = new ArrayList<>();
            ArrayList<Vector2f> points = new ArrayList<>();
            for (Integer ind: polygon.getVertexIndices()
                 ) {
                vertices.add(model.vertices.get(ind));
            }
//            float[][] buffer = ZBuffer.getDefaultPixelDepthMatrix(width, height);
//            ArrayList<Vector3f> polygonPoints = ZBuffer.getInterpolatedPolygonVectors(vertices.get(0), vertices.get(1),vertices.get(2));
//            for (Vector3f vertex: vertices
//                 ) {
////                if (ZBuffer.testBuffer((int)vertex.getX(), (int) vertex.getY(), vertex.getZ(), buffer  )) {
//                    Vector2f point = vertex3fToVector2f(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath), width, height);
//                    points.add(point);
////                }
//            }

            TriangleRasterization.drawTriangle(gc,points, color);

        }
    }

    public static Vector2f vertex3fToVector2f(final Vector3f vertex, final int width, final int height) {
        return new Vector2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
