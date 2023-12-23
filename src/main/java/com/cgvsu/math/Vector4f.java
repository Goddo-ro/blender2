package com.cgvsu.math;

public class Vector4f {
    private float x;
    private float y;
    private float z;
    private float w;

    public Vector4f (float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }
    public float getX() {
        return this.x;
    }

    public float getY () {
        return y;
    }

    public float getZ () {
        return z;
    }

    public float getW () {
        return w;
    }
    public float get(int index) {
        switch (index){
            case 0: return x;
            case 1: return y;
            case 2: return z;
            case 3: return w;
        }
        throw new IllegalArgumentException("Индекс выходит за границы");
    }
    // Сложение векторов
    public Vector4f add(Vector4f other) {
        return new Vector4f(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w);
    }

    public static Vector4f add(Vector4f first, Vector4f second) {
        return new Vector4f(
                first.x + second.x,
                first.y + second.y,
                first.z + second.z,
                first.w + second.z
        );
    }

    // Вычитание векторов
    public Vector4f deduct(Vector4f other) {
        return new Vector4f(this.x - other.x, this.y - other.y, this.z - other.z, this.w - other.w);
    }

    public static Vector4f deduct(Vector4f first, Vector4f second) {
        return new Vector4f(
                first.x - second.x,
                first.y - second.y,
                first.z - second.z,
                first.w - second.z
        );
    }

    // Умножение на скаляр
    public Vector4f multiply(float scalar) {
        return new Vector4f(this.x * scalar, this.y * scalar, this.z * scalar, this.w * scalar);
    }

    // Деление на скаляр
    public Vector4f divide(float scalar){
        if (scalar == 0){
            throw new ArithmeticException("Dividing by zero is undefined and not allowed");
        }
        return (new Vector4f(this.x/scalar, this.y/scalar, this.z/scalar, this.w/scalar));
    }
    //Вычисление длины вектора
    public float length(){
        return (float) Math.sqrt(x*x + y*y + z*z + w*w);
    }
    //Нормализация вектора
    public Vector4f normalize(){
        float normalization = length() != 0 ? 1 / length() : 0;
        return new Vector4f(
                this.x * normalization,
                this.y * normalization,
                this.z * normalization,
                this.w * normalization
        );
    }
    //Скалярное произведение векторов
    public float dot(Vector4f other){
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    }

}
