package com.cgvsu.render_engine;

import com.cgvsu.model.Model;
import com.cgvsu.model.TriPolyModel;
import com.cgvsu.model.Polygon;
import com.cgvsu.utils.models_utils.Triangulation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TriangulationTest {
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
    @DisplayName("Simple polygon triangulation")
    void simplePolyTriangulation() {

        List<Vector3f> vertList = new ArrayList<>(Arrays.asList(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0, 1, 0)
        ));

        List<Vector3f> resultingSublist1 = new ArrayList<>(Arrays.asList(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0)

        ));
        List<Vector3f> resultingSublist2 = new ArrayList<>(Arrays.asList(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0, 1, 0)
        ));

        List<List<Vector3f>> resultingList = new ArrayList<>(Arrays.asList(
                resultingSublist1, resultingSublist2
        ));
        List<List<Vector3f>> actualList = Triangulation.triangulate(vertList);
        Assertions.assertEquals(resultingList.size(), actualList.size());

        for (int i = 0; i < resultingList.size(); i++) {
            assertVectorListsEqual(resultingList.get(i), actualList.get(i));
        }

    }

    @Test
    @DisplayName("Empty list passed as an argument")
    void emptyListTriangulation() {
        List<Vector3f> vertList = new ArrayList<>();

        List<Vector3f> resultingSublist1 = new ArrayList<>();


        List<List<Vector3f>> resultingList = new ArrayList<>(List.of(
                resultingSublist1
        ));
        List<List<Vector3f>> actualList = Triangulation.triangulate(vertList);
        Assertions.assertEquals(resultingList.size(), actualList.size());
        Assertions.assertEquals(1, actualList.size());
        Assertions.assertEquals(0, actualList.get(0).size());

    }

    @Test
    @DisplayName("Incorrect polygon vertices amount triangulation")
    void incVertTriangulation() {

        List<Vector3f> vertList = new ArrayList<>(Arrays.asList(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0)
        ));

        List<Vector3f> resultingSublist1 = new ArrayList<>(Arrays.asList(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0)

        ));

        List<List<Vector3f>> resultingList = new ArrayList<>(List.of(
                resultingSublist1
        ));
        List<List<Vector3f>> actualList = Triangulation.triangulate(vertList);
        Assertions.assertEquals(resultingList.size(), actualList.size());

        for (int i = 0; i < resultingList.size(); i++) {
            assertVectorListsEqual(resultingList.get(i), actualList.get(i));
        }

    }

    @Test
    @DisplayName("Correct Model passed for getTriangulatedModel")
    void correctModelGetTriangulatedModel() {
        ArrayList<Vector3f> vertices = new ArrayList<>(List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0, 1, 0)
        ));
        ArrayList<Vector2f> textureVertices = new ArrayList<>(List.of(
                new Vector2f(0, 0),
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1)
        ));
        ArrayList<Vector3f> normals = new ArrayList<>(List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0, 1, 0)
        ));
        Polygon polygon1 = new Polygon();
        Polygon polygon2 = new Polygon();
        Polygon polygon3 = new Polygon();
        polygon1.setNormalIndices(new ArrayList<>(List.of(
                1, 2, 3, 4
        )));
        polygon1.setTextureVertexIndices(new ArrayList<>(List.of(
                1, 2, 3, 4
        )));
        polygon1.setVertexIndices(new ArrayList<>(List.of(
                1, 2, 3, 4
        )));
        polygon2.setNormalIndices(new ArrayList<>(List.of(
                1, 2, 3
        )));
        polygon2.setTextureVertexIndices(new ArrayList<>(List.of(
                1, 2, 3
        )));
        polygon2.setVertexIndices(new ArrayList<>(List.of(
                1, 2, 3
        )));
        polygon3.setNormalIndices(new ArrayList<>(List.of(
                1, 3, 4
        )));
        polygon3.setTextureVertexIndices(new ArrayList<>(List.of(
                1, 3, 4
        )));
        polygon3.setVertexIndices(new ArrayList<>(List.of(
                1, 3, 4
        )));
        ArrayList<Polygon> polygons = new ArrayList<>(List.of(
                polygon1
        ));
        Model model = new Model();
        model.vertices = vertices;
        model.normals = normals;
        model.textureVertices = textureVertices;
        model.polygons = polygons;
        TriPolyModel expectedModel = new TriPolyModel();
        expectedModel.vertices = vertices;
        expectedModel.textureVertices = textureVertices;
        expectedModel.normals = normals;
        expectedModel.polygons.add(polygon2);
        expectedModel.polygons.add(polygon3);


        TriPolyModel actualModel = Triangulation.getNewTriangulatedModel(model);
        Assertions.assertEquals(expectedModel.polygons.size(), actualModel.polygons.size());
        assertVectorListsEqual(expectedModel.vertices, actualModel.vertices);
        assertVectorListsEqual(expectedModel.normals, actualModel.normals);
        for (int i = 0; i < actualModel.polygons.size(); i++) {
            Assertions.assertEquals(expectedModel.polygons.get(i).getVertexIndices(), actualModel.polygons.get(i).getVertexIndices());
        }
    }

    @Test
    @DisplayName("Empty polygon model passed for getTriangulatedModel")
    void incorrectModelGetTriangulatedModel() {
        ArrayList<Vector3f> vertices = new ArrayList<>(List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0, 1, 0)
        ));
        ArrayList<Vector2f> textureVertices = new ArrayList<>(List.of(
                new Vector2f(0, 0),
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1)
        ));
        ArrayList<Vector3f> normals = new ArrayList<>(List.of(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0, 1, 0)
        ));
        Polygon polygon1 = new Polygon();
        Polygon polygon2 = new Polygon();
        Polygon polygon3 = new Polygon();


        ArrayList<Polygon> polygons = new ArrayList<>(List.of(
                polygon1
        ));
        Model model = new Model();
        model.vertices = vertices;
        model.normals = normals;
        model.textureVertices = textureVertices;
        model.polygons = polygons;
        TriPolyModel expectedModel = new TriPolyModel();
        expectedModel.vertices = vertices;
        expectedModel.textureVertices = textureVertices;
        expectedModel.normals = normals;
        expectedModel.polygons.add(polygon2);
        Throwable exception = Assertions.assertThrows(AssertionError.class, () -> {
            TriPolyModel actualModel = Triangulation.getNewTriangulatedModel(model);
        });


        Assertions.assertEquals("Empty polygon", exception.getMessage());


    }
}