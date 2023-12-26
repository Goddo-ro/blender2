package com.cgvsu.controllers;

import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector4f;
import com.cgvsu.math.matrix.Matrix4f;
import com.cgvsu.model.Model;
import com.cgvsu.render_engine.GraphicConveyor;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.util.ArrayList;
import java.util.List;

public class MoveController {
    private Spinner<Double> scaleX;
    private Spinner<Double> scaleY;
    private Spinner<Double> scaleZ;
    private Spinner<Double> rotateX;
    private Spinner<Double> rotateY;
    private Spinner<Double> rotateZ;
    private Spinner<Double> translateX;
    private Spinner<Double> translateY;
    private Spinner<Double> translateZ;

    public MoveController(Spinner<Double> scaleX, Spinner<Double> scaleY, Spinner<Double> scaleZ,
                          Spinner<Double> rotateX, Spinner<Double> rotateY, Spinner<Double> rotateZ,
                          Spinner<Double> translateX, Spinner<Double> translateY, Spinner<Double> translateZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.rotateX = rotateX;
        this.rotateY = rotateY;
        this.rotateZ = rotateZ;
        this.translateX = translateX;
        this.translateY = translateY;
        this.translateZ = translateZ;

        initializeMoveSpinners();
    }

    private void initializeDoubleSpinner(Spinner<Double> spinner, double min, double max, double cur, double step) {
        spinner.setEditable(true);
        SpinnerValueFactory<Double> valueFactory = //
                new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, cur, step);
        spinner.setValueFactory(valueFactory);
    }

    private void initializeMoveSpinners() {
        initializeDoubleSpinner(scaleX, -100, 100, 1, 0.1);
        initializeDoubleSpinner(scaleY, -100, 100, 1, 0.1);
        initializeDoubleSpinner(scaleZ, -100, 100, 1, 0.1);

        initializeDoubleSpinner(rotateX, -360, 360, 0, 1);
        initializeDoubleSpinner(rotateY, -360, 360, 0, 1);
        initializeDoubleSpinner(rotateZ, -360, 360, 0, 1);

        initializeDoubleSpinner(translateX, -1000, 1000, 0, 1);
        initializeDoubleSpinner(translateY, -1000, 1000, 0, 1);
        initializeDoubleSpinner(translateZ, -1000, 1000, 0, 1);
    }

    void apply(List<Model> models) {
        // TODO: 25.12.2023 Костыль 
        float sX = scaleX.getValue().floatValue();
        float sY = scaleY.getValue().floatValue();
        float sZ = scaleZ.getValue().floatValue();
        float rX = rotateX.getValue().floatValue();
        float rY = rotateY.getValue().floatValue();
        float rZ = rotateZ.getValue().floatValue();
        float tX = translateX.getValue().floatValue();
        float tY = translateY.getValue().floatValue();
        float tZ = translateZ.getValue().floatValue();

        for (Model model : models) {
            
            ArrayList<Vector3f> vertices = model.vertices;
            for (Vector3f vector : vertices) {
                // TODO: 25.12.2023 Костыль 
                Vector3f result = Matrix4f.multiply(GraphicConveyor.rotateScaleTranslate(sX, sY, sZ, rX, rY, rZ, tX, tY, tZ),
                        new Vector4f(vector.getX(), vector.getY(), vector.getZ(), 1))
                        .normalizeTo3f();
                vector.setX(result.getX());
                vector.setY(result.getY());
                vector.setZ(result.getZ());
            }
        }
    }

    public float getScaleX() {
        return this.scaleX.getValue().floatValue();
    };
    public float getScaleY() {
        return this.scaleY.getValue().floatValue();
    };
    public float getScaleZ() {
        return this.scaleZ.getValue().floatValue();
    };
    public float getRotationX() {
        return this.rotateX.getValue().floatValue();
    };
    public float getRotationY() {
        return this.rotateY.getValue().floatValue();
    };
    public float getRotationZ() {
        return this.rotateZ.getValue().floatValue();
    };
    public float getTranslationX() {
        return this.translateX.getValue().floatValue();
    };
    public float getTranslationY() {
        return this.translateY.getValue().floatValue();
    };
    public float getTranslationZ() {
        return this.translateZ.getValue().floatValue();
    };
}
