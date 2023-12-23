package com.cgvsu.render_engine;
import com.cgvsu.model.Model;
import com.cgvsu.math.Vector3f;

import com.cgvsu.model.Polygon;
import com.cgvsu.utils.NormalUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class NormalTest {

    @Test
    void normalsVertex() {
        Model model = new Model();
        model.vertices = new ArrayList<>(List.of(new Vector3f[]{
                new Vector3f(1.0f, 0.0f, 1.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 0.0f)
        }));
        ArrayList<Polygon> polygons = getPolygons();
        ArrayList<Vector3f> resultNormalsVertex = NormalUtils.normalsVertex(model.vertices, polygons);
        ArrayList<Vector3f> expectedResultNormalsVertex = new ArrayList<>(List.of(new Vector3f[]{
                new Vector3f((float) (1.0f / Math.sqrt(2)), 0.0f, (float) (1.0f / Math.sqrt(2))),
                new Vector3f((float) (1.0f / Math.sqrt(2)), 0.0f, (float) (-1.0f / Math.sqrt(2))),
                new Vector3f(0.0f, (float) (1.0f / Math.sqrt(2)), (float) (-1.0f / Math.sqrt(2))),
                new Vector3f(0.0f, (float) (1.0f / Math.sqrt(2)), (float) (1.0f / Math.sqrt(2))),
                new Vector3f((float) (-1.0f / Math.sqrt(3)), (float) (-1.0f / Math.sqrt(3)), (float) (1.0f / Math.sqrt(3))),
                new Vector3f((float) (-1.0f / Math.sqrt(3)), (float) (-1.0f / Math.sqrt(3)), (float) (-1.0f / Math.sqrt(3)))
        }));
        for (int i = 0; i < resultNormalsVertex.size(); i++) {
            Assertions.assertTrue(resultNormalsVertex.get(i).equals(expectedResultNormalsVertex.get(i)));
        }
    }

    private static ArrayList<Polygon> getPolygons() {
        Polygon polygon1 = new Polygon();
        Polygon polygon2 = new Polygon();
        Polygon polygon3 = new Polygon();
        Polygon polygon4 = new Polygon();
        Polygon polygon5 = new Polygon();
        ArrayList<Integer> resultingSublist1 = new ArrayList<>(Arrays.asList(
                0, 1, 2, 3

        ));
        ArrayList<Integer> resultingSublist2 = new ArrayList<>(Arrays.asList(
                3, 2, 5, 4

        ));
        ArrayList<Integer> resultingSublist3 = new ArrayList<>(Arrays.asList(
                0, 4, 5, 1

        ));
        ArrayList<Integer> resultingSublist4 = new ArrayList<>(Arrays.asList(
                1, 5, 2

        ));
        ArrayList<Integer> resultingSublist5 = new ArrayList<>(Arrays.asList(
                0, 3, 4

        ));
        polygon1.setVertexIndices(resultingSublist1);
        polygon2.setVertexIndices(resultingSublist2);
        polygon3.setVertexIndices(resultingSublist3);
        polygon4.setVertexIndices(resultingSublist4);
        polygon5.setVertexIndices(resultingSublist5);
        return new ArrayList<>(Arrays.asList(
                polygon1, polygon2, polygon3, polygon4, polygon5
        ));
    }
}