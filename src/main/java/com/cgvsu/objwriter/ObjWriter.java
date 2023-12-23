package com.cgvsu.objwriter;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector2f;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ObjWriter {
    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public ObjWriter() {}

    public static void write(Model model, String pathLine) throws IOException {
        // File cleaning
        Files.write(Paths.get(pathLine), "".getBytes());

        try {
            // Открываем файл
            FileWriter writer = new FileWriter(pathLine, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            // Записываем каждый массив модели (вершины, текстурные вершины, нормали и полигоны)
            writeVertices(bufferedWriter, model.vertices);
            writeTextureVertices(bufferedWriter, model.textureVertices);
            writeNormals(bufferedWriter, model.normals);
            writeFaces(bufferedWriter, model.polygons);

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected static void writeVertices(BufferedWriter bufferedWriter,
                                        ArrayList<Vector3f> vertices) throws Exception {
        if (vertices.size() == 0) {
            throw new Exception("OBJ-File is incorrect. Array of vertices is empty!");
        }

        // Проходимся по каждой вершине и записываем ее координаты с токетом
        for (Vector3f vertex : vertices) {
            bufferedWriter.write(OBJ_VERTEX_TOKEN + "  " + vertex.getX() + " " + vertex.getY() + " " + vertex.getZ() + "\n");
        }
    }
    protected static void writeTextureVertices(BufferedWriter bufferedWriter,
                                               ArrayList<Vector2f> textureVertices) throws Exception {
        if (textureVertices.size() != 0) {
            // Проходимся по каждой текстурной вершине и записываем ее координаты с токеном
            for (Vector2f vertex : textureVertices) {
                bufferedWriter.write(OBJ_TEXTURE_TOKEN + " " + vertex.getX() + " " + vertex.getY() + "\n");
            }
        }
    }

    protected static void writeNormals(BufferedWriter bufferedWriter,
                                       ArrayList<Vector3f> normals) throws Exception {
        if (normals.size() != 0) {
            // Проходимся по всем нормалям и записываем их координаты
            for (Vector3f normal : normals) {
                bufferedWriter.write(OBJ_NORMAL_TOKEN + " " + normal.getX() + " " + normal.getY() + " " + normal.getZ() + "\n");
            }
        }
    }

    protected static void writeFaces(BufferedWriter bufferedWriter,
                                     ArrayList<Polygon> polygons) throws Exception {
        if (polygons.size() == 0) {
            throw new Exception("OBJ-File is incorrect. Array of polygons is empty!");
        }
        //Идем по всем полигонам и записываем построенный полигон в файл
        for (Polygon polygon : polygons) {
            bufferedWriter.write(OBJ_FACE_TOKEN + constructFace(polygon) + "\n");
        }
    }
    private static String constructFace(Polygon polygon) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        // Если у полигона нет ни текстурных вершин, ни нормалей, то записываем в строку только индексы вершин
        if (polygon.getTextureVertexIndices().size() == 0 && polygon.getNormalIndices().size() == 0) {
            for (Integer vertexIndex : polygon.getVertexIndices()) {
                stringBuilder.append(" ").append(vertexIndex + 1);
            }
        // Если у нас нет только нормалей, то записываем строку вида индекс/текстурный_индекс
        } else if (polygon.getNormalIndices().size() == 0) {
            if (polygon.getVertexIndices().size() == polygon.getTextureVertexIndices().size()) {
                for (int i = 0; i < polygon.getVertexIndices().size(); i++) {
                    stringBuilder.append(" ").append(polygon.getVertexIndices().get(i) + 1)
                            .append("/").append(polygon.getTextureVertexIndices().get(i) + 1);
                }
            } else {
                throw new Exception("Incorrect parameters for face!");
            }
        // Если у нас нет только текстурных вершин, то записываем строку вида индекс//индекс_нормали
        } else if (polygon.getTextureVertexIndices().size() == 0) {
            if (polygon.getVertexIndices().size() == polygon.getNormalIndices().size()) {
                for (int i = 0; i < polygon.getVertexIndices().size(); i++) {
                    stringBuilder.append(" ").append(polygon.getVertexIndices().get(i) + 1)
                            .append("//").append(polygon.getNormalIndices().get(i) + 1);
                }
            } else {
                throw new Exception("Incorrect parameters for face!");
            }
        // Иначе у нас есть все, записываем строку вида индекс/текстурный_индекс/индекс_нормали
        } else {
            if (polygon.getVertexIndices().size() == polygon.getTextureVertexIndices().size() &&
                    polygon.getTextureVertexIndices().size() == polygon.getNormalIndices().size()) {
                for (int i = 0; i < polygon.getVertexIndices().size(); i++) {
                    stringBuilder.append(" ").append(polygon.getVertexIndices().get(i) + 1)
                            .append("/").append(polygon.getTextureVertexIndices().get(i) + 1)
                            .append("/").append(polygon.getNormalIndices().get(i) + 1);
                }
            } else {
                throw new Exception("Incorrect parameters for face!");
            }
        }
        return stringBuilder.toString();
    }
}
