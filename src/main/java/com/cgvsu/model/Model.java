package com.cgvsu.model;

import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector2f;
import com.cgvsu.utils.models_utils.ModelCopy;

import java.util.ArrayList;

public class Model {

    public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<Vector2f>();
    public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();
    public TriPolyModel triangulatedCopy;
    private Model originModel;
    private String name;

    public Model(Model model) {
        Model copy = ModelCopy.copyModel(model);

        this.vertices = copy.vertices;
        this.textureVertices = copy.textureVertices;
        this.normals = copy.normals;
        this.polygons = copy.polygons;
        this.triangulatedCopy = copy.triangulatedCopy;
        this.name = copy.name;
    }

    public Model() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Model getOriginModel () {
        return originModel;
    };

    public void setOriginModel(Model originModel) {
        this.originModel = originModel;
    }
}
