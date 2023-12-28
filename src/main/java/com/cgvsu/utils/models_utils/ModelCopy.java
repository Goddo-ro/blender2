package com.cgvsu.utils.models_utils;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.model.TriPolyModel;
import com.cgvsu.model.Triangle;

import java.util.ArrayList;
import java.util.Collections;

public class ModelCopy {
    public static Model copyModel(Model model) {
        Model result = new Model();

        ArrayList<Vector3f> vertices = new ArrayList<>();
        ArrayList<Vector2f> textureVertices = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();
        ArrayList<Polygon> polygons = new ArrayList<>();

        Collections.copy(model.vertices, vertices);
        Collections.copy(model.textureVertices, textureVertices);
        Collections.copy(model.normals, normals);
        Collections.copy(model.polygons, polygons);
        TriPolyModel triPolyModel = Triangulation.getTriangulatedModel(model);

        result.vertices = vertices;
        result.textureVertices = textureVertices;
        result.normals = normals;
        result.polygons = polygons;
        result.triangulatedCopy = triPolyModel;
        result.setName(model.getName());

        return result;
    }
}
