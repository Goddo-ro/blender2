package com.cgvsu.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class TriPolyModel extends Model{
    public ArrayList<Triangle> polygons = new ArrayList<>();

    @Override
    public void setColor(Color color) {
        this.color = color;
    }
}
