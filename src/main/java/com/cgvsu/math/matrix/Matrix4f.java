package com.cgvsu.math.matrix;

import com.cgvsu.math.Vector4f;

import java.util.Arrays;

public class Matrix4f{
    private float[][] matrix = new float[4][4];

    public Matrix4f (float[][] data) {
        if (data.length != 4 || data[0].length != 4) {
            throw new IllegalArgumentException("Matrix should be 3x3");
        }
        this.matrix = data;
    }

    public Matrix4f () {
        this.matrix = new float[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
    }

    public float[][] getMatrix() {
        return matrix;
    }

    public void setValue(int i, int j, float value) {
        matrix[i][j] = value;
    }

    public float getValue(int i, int j) {
        return matrix[i][j];
    }

    //Сложение матриц
    public Matrix4f add(Matrix4f other){
        float [][] result = new float[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                result[i][j] = this.matrix[i][j] + other.matrix[i][j];
            }
        }
        return new Matrix4f(result);
    }

    //Вычитание матриц
    public Matrix4f deduct(Matrix4f other){
        float [][] result = new float[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                result[i][j] = this.matrix[i][j] - other.matrix[i][j];
            }
        }
        return new Matrix4f(result);
    }

    //Умножение на вектор
    public Vector4f multiply(Vector4f vector){
        if (vector == null) {
            throw new NullPointerException("Vector can't be null");
        }
        float [] result = new float[4];
        for (int i = 0; i < 4; i++){
            result[i] = 0;
            for (int j = 0; j < 4; j++){
                result[i] += this.matrix[i][j] * vector.get(j);
            }
        }
        return new Vector4f(result[0], result[1], result[2], result[3]);
    }
    //Умножение на матрицу
    public Matrix4f multiply(Matrix4f other) {
        float [][] result = new float[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                result[i][j] = 0;
                for (int k = 0; k < 4; k++){
                    result[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        return new Matrix4f(result);
    }

    // Транспонирование
    public Matrix4f transpose(){
        float [][] result = new float[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                result[i][j] = this.matrix[j][i];
            }
        }
        return new Matrix4f(result);
    }

    // Быстрое задание единичной матрицы
    public static Matrix4f unit(){
        float[][] unitMatrix = new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        return new Matrix4f(unitMatrix);
    }
    //Вычисление детерминанта
    public float determinate(){
        float[][] data1 = new float[3][3];
        float[][] data2 = new float[3][3];
        float[][] data3 = new float[3][3];
        float[][] data4 = new float[3][3];

        for(int i = 1; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if (j != 0){
                    data1[i-1][j-1] = matrix[i][j];
                }
                if (j != 1){
                    if (j==0) {
                        data2[i-1][j] = matrix[i][j];
                    }else{
                        data2[i-1][j-1] = matrix[i][j];
                    }
                }
                if (j != 2){
                    if (j==0 || j==1) {
                        data3[i-1][j] = matrix[i][j];
                    }else{
                        data3[i-1][j-1] = matrix[i][j];
                    }
                }
                if (j != 3){
                    data4[i-1][j] = matrix[i][j];
                }
            }
        }

        Matrix3f m1 = new Matrix3f(data1);
        Matrix3f m2 = new Matrix3f(data2);
        Matrix3f m3 = new Matrix3f(data3);
        Matrix3f m4 = new Matrix3f(data4);

        return (matrix[0][0]*m1.determinate()-matrix[0][1]*m2.determinate()+matrix[0][2]*m3.determinate()-matrix[0][3]*m4.determinate());
    }

    public Matrix4f inverse(){
        float det = this.determinate();
        if (det == 0){
            throw new NullPointerException("There is no inverse matrix, since the determinant is zero");
        }
        float[][] matrix = this.minor().getMatrix();

        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = matrix[i][j]/det;
                if ((i == 0 && j == 1) || (i == 1 && j == 0) || (i == 1 && j == 2) || (i == 2 && j == 1) || (i == 0 && j == 3) || (i == 3 && j == 0) || (i == 3 && j == 2) || (i == 2 && j == 3)){
                    matrix[i][j] *= -1;
                }
            }
        }
        System.out.println(Arrays.deepToString(matrix));
        Matrix4f result = new Matrix4f(matrix);
        result = result.transpose();

        return result;

    }

    public Matrix4f minor(){
        float [][] minor = new float[4][4];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                float [][] temp = new float[3][3];

                int kI = 0; // индексы k: объявлены отдельно, чтобы исключить строку и столбец текущего элемента
                for (int k = 0; k < 3; k++){
                    if (kI == i){
                        kI++;
                    }
                    int qJ = 0; // индексы q: объявлены отдельно, чтобы исключить строку и столбец текущего элемента

                    for(int q = 0; q < 3; q++){
                        if (qJ == j){
                            qJ++;
                        }
                        temp[k][q]=this.matrix[kI][qJ];
                        qJ++;
                    }
                    kI++;
                }
                Matrix3f tempM = new Matrix3f(temp);
                minor[i][j] = tempM.determinate();

            }
        }
        return new Matrix4f(minor);
    }
}
