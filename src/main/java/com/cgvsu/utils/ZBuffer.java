package com.cgvsu.utils;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.TriPolyModel;
import com.cgvsu.model.Triangle;

import java.util.ArrayList;
import java.util.Arrays;

public class ZBuffer {

    public static float[][] getDefaultPixelDepthMatrix(int width, int height) {
        float[][] buffer = new float[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                buffer[i][j] = Float.MAX_VALUE ;
            }
        }
        return buffer;
    }

    public static boolean testBuffer(int x, int y, float depth, float[][] buffer) {
        if (x >= 0 && x < buffer.length && y >= 0 && y < buffer[0].length) {

            if (depth < buffer[x][y]) {
                buffer[x][y] = depth;
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Vector3f> getInterpolatedPolygonVectors(Vector3f v1, Vector3f v2, Vector3f v3) {
        float step = 1f;
        ArrayList<Vector3f> interpolatedPolygonVectors = new ArrayList<>();
        for (float u = 0; u <= 1; u += step) {
            for (float v = 0; v <= 1 - u; v += step) {
                float w = 1 - u - v;
                if (w >= 0) {
                    Vector3f interpolatedVector = interpolateVector3f(v1, v2, v3, u, v);
                    interpolatedPolygonVectors.add(interpolatedVector);
                }
            }
        }
        return interpolatedPolygonVectors;
    }

    public static Vector3f interpolateVector3f(Vector3f v1, Vector3f v2, Vector3f v3, float u, float v) {
        float w = 1 - u - v;

        float x = (u * v1.getX()) + (v * v2.getX()) + (w * v3.getX());
        float y = (u * v1.getY()) + (v * v2.getY()) + (w * v3.getY());
        float z = (u * v1.getZ()) + (v * v2.getZ()) + (w * v3.getZ());

        return new Vector3f(x, y, z);
    }



}
