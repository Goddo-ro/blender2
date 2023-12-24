package com.cgvsu.math;

import com.cgvsu.render_engine.GraphicConveyor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.cgvsu.math.matrix.*;

import java.util.Arrays;

public class MathTest {

    @Test
    public void testNormalization01() {
        Vector3f vector3fB = new Vector3f(1,2,2);
        Vector3f vector3f = vector3fB.normalize();
        Vector3f result = new Vector3f(0.33333f, 0.66666f, 0.66666f);

        Assertions.assertTrue(vector3f.equals(result));
    }

    @Test
    public void testNormalization02() {
        Vector3f vector3fB = new Vector3f(0,0,0);
        Vector3f vector3f = vector3fB.normalize();
        Vector3f result = new Vector3f(0,0,0);

        Assertions.assertTrue(vector3f.equals(result));
    }

    @Test
    public void testScaleMatrix4f01() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f scaleMatrix = GraphicConveyor.scaleMatrix4f(1,1,1);

        Matrix4f actual = Matrix4f.multiply(scaleMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted), Arrays.deepToString(actual.getMatrix()));
    }

    @Test
    public void testScaleMatrix4f02() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {0,0,0,0},
                        {0,0,0,0},
                        {0,0,0,0},
                        {4,3,2,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f scaleMatrix = GraphicConveyor.scaleMatrix4f(0,0,0);

        Matrix4f actual = Matrix4f.multiply(scaleMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted), Arrays.deepToString(actual.getMatrix()));
    }

    @Test
    public void testScaleMatrix4f03() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {2,4,6,8},
                        {5,6,7,8},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f scaleMatrix = GraphicConveyor.scaleMatrix4f(2,1,1);

        Matrix4f actual = Matrix4f.multiply(scaleMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted), Arrays.deepToString(actual.getMatrix()));
    }

    @Test
    public void testScaleMatrix4f04() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {10,12,14,16},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f scaleMatrix = GraphicConveyor.scaleMatrix4f(1,2,1);

        Matrix4f actual = Matrix4f.multiply(scaleMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted), Arrays.deepToString(actual.getMatrix()));
    }

    @Test
    public void testScaleMatrix4f05() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {5,6,7,8},
                        {18,20,22,24},
                        {4,3,2,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f scaleMatrix = GraphicConveyor.scaleMatrix4f(1,1,2);

        Matrix4f actual = Matrix4f.multiply(scaleMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted), Arrays.deepToString(actual.getMatrix()));
    }

    @Test
    public void testScaleMatrix4f06() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {-1,-2,-3,-4},
                        {-5,-6,-7,-8},
                        {-9,-10,-11,-12},
                        {4,3,2,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f scaleMatrix = GraphicConveyor.scaleMatrix4f(-1,-1,-1);

        Matrix4f actual = Matrix4f.multiply(scaleMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted), Arrays.deepToString(actual.getMatrix()));
    }

    @Test
    public void testScaleMatrix4f07() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {-1.5f,-3,-4.5f,-6},
                        {-5,-6,-7,-8},
                        {-4.5f,-5,-5.5f,-6},
                        {4,3,2,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f scaleMatrix = GraphicConveyor.scaleMatrix4f(-1.5f,-1,-0.5f);

        Matrix4f actual = Matrix4f.multiply(scaleMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted), Arrays.deepToString(actual.getMatrix()));
    }

    @Test
    public void testScaleMatrix4f08() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {4,3,2,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {-1.5f,-3,-4.5f,-6},
                        {50,60,70,80},
                        {-4.5f,-5,-5.5f,-6},
                        {4,3,2,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f scaleMatrix = GraphicConveyor.scaleMatrix4f(-1.5f,10,-0.5f);

        Matrix4f actual = Matrix4f.multiply(scaleMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted), Arrays.deepToString(actual.getMatrix()));
    }

    @Test
    public void testRotateX01() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateX(0);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateX02() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {3,4,5,6},
                        {-2,-3,-4,-5},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateX(90);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateX03() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateX(360);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateX04() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {-3,-4,-5,-6},
                        {2,3,4,5},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateX(-90);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateY01() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateY(0);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateY02() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {3,4,5,6},
                        {2,3,4,5},
                        {-1,-2,-3,-4},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateY(90);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateY03() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateY(360);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateY04() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {-3,-4,-5,-6},
                        {2,3,4,5},
                        {1,2,3,4},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateY(-90);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateZ01() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateZ(0);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateZ02() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {2,3,4,5},
                        {-1,-2,-3,-4},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateZ(90);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateZ03() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateZ(360);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateZ04() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {-2,-3,-4,-5},
                        {1,2,3,4},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateZ(-90);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateMatrix4f01() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateMatrix4f(0,0,0);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateMatrix4f02() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {3,4,5,6},
                        {2,3,4,5},
                        {-1,-2,-3,-4},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateMatrix4f(90,90,90);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateMatrix4f03() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {-2,-3,-4,-5},
                        {3,4,5,6},
                        {-1,-2,-3,-4},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateMatrix4f(90,90,0);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateMatrix4f04() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {3,4,5,6},
                        {-1,-2,-3,-4},
                        {-2,-3,-4,-5},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateMatrix4f(90,0,90);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateMatrix4f05() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {2,3,4,5},
                        {-3,-4,-5,-6},
                        {-1,-2,-3,-4},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateMatrix4f(0,90,90);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testRotateMatrix4f06() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {-3,-4,-5,-6},
                        {-1,-2,-3,-4},
                        {2,3,4,5},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f rotateXMatrix = GraphicConveyor.rotateMatrix4f(90,180,270);

        Matrix4f actual = Matrix4f.multiply(rotateXMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testTranslationMatrix4f01() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {4,5,6,7}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f translationMatrix4f = GraphicConveyor.translationMatrix4f(0,0,0);

        Matrix4f actual = Matrix4f.multiply(translationMatrix4f, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testTranslationMatrix4f02() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {1,1,1,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {11,12,13,14},
                        {12,13,14,15},
                        {13,14,15,16},
                        {1,1,1,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f translationMatrix4f = GraphicConveyor.translationMatrix4f(10,10,10);

        Matrix4f actual = Matrix4f.multiply(translationMatrix4f, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testTranslationMatrix4f03() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {1,1,1,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {-10,-9,-8,-7},
                        {-9,-8,-7,-6},
                        {-8,-7,-6,-5},
                        {1,1,1,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f translationMatrix4f = GraphicConveyor.translationMatrix4f(-11,-11,-11);

        Matrix4f actual = Matrix4f.multiply(translationMatrix4f, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testSRTMatrix4f01() {
        float[][] matrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {1,1,1,1}
                };
        float[][] exceptedMatrix = new float[][]
                {
                        {1,2,3,4},
                        {2,3,4,5},
                        {3,4,5,6},
                        {1,1,1,1}
                };
        Matrix4f matrix4f = new Matrix4f(matrix);
        Matrix4f srtMatrix = GraphicConveyor.rotateScaleTranslate(1,1,1,0,0,0,0,0,0);

        Matrix4f actual = Matrix4f.multiply(srtMatrix, matrix4f);
        Matrix4f excepted = new Matrix4f(exceptedMatrix);

        Assertions.assertTrue(actual.equals(excepted));
    }

    @Test
    public void testSRTMatrix4f02() {
        Vector4f matrix4f = new Vector4f(3,4,5,1);
        Matrix4f srtMatrix = GraphicConveyor.rotateScaleTranslate(-1,-1,-1,90,90,90,0,0,0);

        Vector4f actual = Matrix4f.multiply(srtMatrix, matrix4f);
        Vector4f excepted = new Vector4f(-5,-4,3,1);

        Assertions.assertTrue(actual.equals(excepted), actual.toString());
    }

    @Test
    public void testSRTMatrix4f03() {
        Vector4f matrix4f = new Vector4f(3,4,5,1);
        Matrix4f srtMatrix = GraphicConveyor.rotateScaleTranslate(-1,-1,-1,90,90,90,10,8,-5);

        Vector4f actual = Matrix4f.multiply(srtMatrix, matrix4f);
        Vector4f excepted = new Vector4f(5,4,-2,1);

        Assertions.assertTrue(actual.equals(excepted), actual.toString());
    }

    @Test
    public void testSRTMatrixToZero() {
        Matrix4f actual = GraphicConveyor.rotateScaleTranslate();
        Matrix4f excepted = GraphicConveyor.rotateScaleTranslate(1,1,1,0,0,0,0,0,0);
        Assertions.assertTrue(actual.equals(excepted));
    }
}
