package com.cgvsu.controllers;

import com.cgvsu.model.Model;
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

        initializeDoubleSpinner(rotateX, -100, 100, 0, 1);
        initializeDoubleSpinner(rotateY, -100, 100, 0, 1);
        initializeDoubleSpinner(rotateZ, -100, 100, 0, 1);

        initializeDoubleSpinner(translateX, -1000, 1000, 0, 1);
        initializeDoubleSpinner(translateY, -1000, 1000, 0, 1);
        initializeDoubleSpinner(translateZ, -1000, 1000, 0, 1);
    }

    void apply(List<Model> models) {
        double sX = scaleX.getValue();
        double sY = scaleY.getValue();
        double sZ = scaleZ.getValue();
        double rX = rotateX.getValue();
        double rY = rotateY.getValue();
        double rZ = rotateZ.getValue();
        double tX = translateX.getValue();
        double tY = translateY.getValue();
        double tZ = translateZ.getValue();

        for (Model model : models) {
            // Apply modifies
        }
    }
}
