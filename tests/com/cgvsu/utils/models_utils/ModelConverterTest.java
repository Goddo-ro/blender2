package com.cgvsu.utils.models_utils;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.TriPolyModel;
import com.cgvsu.model.Triangle;
import com.cgvsu.utils.NormalUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelConverterTest {
    private void assertVectorListsEqual(List<Vector3f> expected, List<Vector3f> actual) {
        Assertions.assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            Vector3f expectedVector = expected.get(i);
            Vector3f actualVector = actual.get(i);

            Assertions.assertEquals(expectedVector.getX(), actualVector.getX());
            Assertions.assertEquals(expectedVector.getY(), actualVector.getY());
            Assertions.assertEquals(expectedVector.getZ(), actualVector.getZ());
        }
    }
    @Test
    @DisplayName("Simple model")
    void simpleTriModelToModel(){
        ArrayList<Vector3f> vertList = new ArrayList<>(Arrays.asList(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0, 1, 0)
        ));
        ArrayList<Vector2f> vertTextList = new ArrayList<>(Arrays.asList(
                new Vector2f(0, 0),
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1)
        ));
        TriPolyModel triPolyModel = new TriPolyModel();
        Triangle triangle1 = new Triangle();
        ArrayList<Integer> vertInd1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        ArrayList<Integer> textInd1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        triangle1.setVertexIndices(vertInd1);
        Triangle triangle2 = new Triangle();
        triangle1.setNormalIndices(vertInd1);
        triangle1.setTextureVertexIndices(textInd1);
        ArrayList<Integer> vertInd2 = new ArrayList<>(Arrays.asList(2, 3, 4));
        triangle2.setVertexIndices(vertInd2);
        triangle2.setNormalIndices(vertInd2);
        triangle2.setTextureVertexIndices(textInd1);
        ArrayList<Triangle> triangles = new ArrayList<>(Arrays.asList(triangle1, triangle2));

        triPolyModel.vertices = vertList;
        triPolyModel.polygons = triangles;
        triPolyModel.normals = vertList;
        triPolyModel.textureVertices = vertTextList;
        Model model = ModelConverter.triPolyModelToModel(triPolyModel);
        assertVectorListsEqual(model.vertices,triPolyModel.vertices );
        for (int i = 0; i<model.polygons.size();i++){
            assertIntegerListsEqual(model.polygons.get(i).getVertexIndices(),triPolyModel.polygons.get(i).getVertexIndices());
        }
    }

    private void assertIntegerListsEqual(ArrayList<Integer> vertexIndices, ArrayList<Integer> vertexIndices1) {
        assertEquals(vertexIndices.size(), vertexIndices1.size());

        for (int i = 0; i < vertexIndices.size(); i++) {
            Integer expectedVector = vertexIndices.get(i);
            Integer actualVector = vertexIndices1.get(i);
            assertEquals(expectedVector, actualVector);
            assertEquals(expectedVector, actualVector);
            assertEquals(expectedVector, actualVector);
        }
    }
}