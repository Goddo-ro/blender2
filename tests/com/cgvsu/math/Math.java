package com.cgvsu.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Math {

    @Test
    public void testNormalization() {
        Vector3f vector3f = new Vector3f(1,2,2);
        vector3f.normalize();
        Vector3f result = new Vector3f(1/3, 2/3, 2/3);
        Assertions.assertEquals(result, vector3f);
    }
}
