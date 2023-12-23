package com.cgvsu.utils;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.math.Vector3f;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cgvsu.utils.models_utils.VerticesRemover.deleteVertexes;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteTest {
    @Test
    public void testDeleteVertexes() throws IOException {
        Model model = new Model();
        Vector3f v1 = new Vector3f(0, 0, 0);
        Vector3f v2 = new Vector3f(1, 1, 1);
        Vector3f v3 = new Vector3f(2, 2, 2);
        Vector3f v4 = new Vector3f(3, 3, 3);
        Vector3f v5 = new Vector3f(4, 4, 4);
        Vector3f v6 = new Vector3f(5, 5, 5);
        model.vertices = new ArrayList<>(Arrays.asList(v1, v2, v3, v4, v5, v6));

        deleteVertexes(model, new Integer[] {1});

        assertEquals(5, model.vertices.size());
    }

    @Test
    public void testDeletePolygon() {
        Model model = new Model();
        Vector3f v1 = new Vector3f(0, 0, 0);
        Vector3f v2 = new Vector3f(1, 1, 1);
        Vector3f v3 = new Vector3f(2, 2, 2);
        Vector3f v4 = new Vector3f(3, 3, 3);
        Vector3f v5 = new Vector3f(4, 4, 4);
        Vector3f v6 = new Vector3f(5, 5, 5);
        model.vertices = new ArrayList<>(Arrays.asList(v1, v2, v3, v4, v5, v6));
        Polygon polygon1 = new Polygon();
        polygon1.setVertexIndices(new ArrayList<>(Arrays.asList(1, 2, 3)));
        Polygon polygon2 = new Polygon();
        polygon2.setVertexIndices(new ArrayList<>(Arrays.asList(4, 5, 6)));
        model.polygons = new ArrayList<>(List.of(polygon1, polygon2));

        deleteVertexes(model, new Integer[] {4});

        assertEquals(1, model.polygons.size());

        Polygon polygon = model.polygons.get(0);
        assertEquals(polygon.getVertexIndices(), new ArrayList<>(List.of(1, 2, 3)));
    }

    @Test
    public void testOffset() {
        Model model = new Model();
        Vector3f v1 = new Vector3f(0, 0, 0);
        Vector3f v2 = new Vector3f(1, 1, 1);
        Vector3f v3 = new Vector3f(2, 2, 2);
        Vector3f v4 = new Vector3f(3, 3, 3);
        Vector3f v5 = new Vector3f(4, 4, 4);
        Vector3f v6 = new Vector3f(5, 5, 5);
        model.vertices = new ArrayList<>(Arrays.asList(v1, v2, v3, v4, v5, v6));
        Polygon polygon1 = new Polygon();
        polygon1.setVertexIndices(new ArrayList<>(Arrays.asList(1, 2, 3)));
        Polygon polygon2 = new Polygon();
        polygon2.setVertexIndices(new ArrayList<>(Arrays.asList(4, 5, 6)));
        model.polygons = new ArrayList<>(List.of(polygon1, polygon2));

        deleteVertexes(model, new Integer[] {1});

        Polygon polygon = model.polygons.get(0);
        assertEquals(polygon.getVertexIndices(), new ArrayList<>(List.of(3, 4, 5)));
    }
}