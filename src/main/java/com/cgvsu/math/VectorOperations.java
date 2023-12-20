package com.cgvsu.math;

public class VectorOperations {
    /*
    v2 - координата конца
    v1 - координата начала
     */
    public static Vector3f vector(Vector3f v1, Vector3f v2) {
        return new Vector3f(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }

    /*
    u - вектор 1-2 вершины
    v - вектор 1-3 вершины
    для вершин указанных против часовой стрелки в полигонах
     */
    public static Vector3f vectorProduct(Vector3f u, Vector3f v) {
        return new Vector3f(u.y * v.z - u.z * v.y, u.z * v.x - u.x * v.z, u.x * v.y - u.y * v.x);
    }

    public static Vector3f summaVector(Vector3f v1, Vector3f v2) {
        return new Vector3f(v2.x + v1.x, v2.y + v1.y, v2.z + v1.z);
    }

    public static Vector3f quotient(Vector3f v1, Integer count) {
        return new Vector3f(v1.x / count, v1.y / count, v1.z / count);
    }

    public static Vector3f normalize(Vector3f v) {
        float s = (float) Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        return new Vector3f(v.x / s, v.y / s, v.z / s);
    }
}