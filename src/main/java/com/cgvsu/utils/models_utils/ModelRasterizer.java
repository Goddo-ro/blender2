package com.cgvsu.utils.models_utils;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.model.TriPolyModel;
import com.cgvsu.utils.triangles_utils.TriangleRasterization;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

import static com.cgvsu.math.Vector3f.vertex3fToVector2f;

public class ModelRasterizer {
    public static void rasterizeModel(PixelWriter pw, TriPolyModel model, int width, int height, Color color){
//        if (!model.isTriangulated) model =ModelConverter.triPolyModelToModel(Triangulation.getTriangulatedModel(model));
        for (Polygon polygon: model.polygons
             ) {
            ArrayList<Vector3f> vertices = new ArrayList<>();
            ArrayList<Vector2f> points = new ArrayList<>();
            for (Integer ind: polygon.getVertexIndices()
                 ) {
                vertices.add(model.vertices.get(ind));
            }
            for (Vector3f vertex: vertices
                 ) {
                Vector2f point = vertex3fToVector2f(vertex, width, height);
                points.add(point);
            }
            TriangleRasterization.drawTriangle(pw,points, color);
        }
    }
}
