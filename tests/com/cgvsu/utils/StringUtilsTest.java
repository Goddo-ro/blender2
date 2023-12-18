package com.cgvsu.utils;

import org.junit.jupiter.api.Test;

import static com.cgvsu.utils.StringUtils.generateUniqueName;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

class StringUtilsTest {

    @Test
    void testGenerateUniqueName_UniqueName() {
        List<String> existingNames = new ArrayList<>();
        existingNames.add("Название.obj");
        existingNames.add("Другое Название.obj");

        String inputName = "Уникальное Название.obj";
        String result = generateUniqueName(inputName, existingNames);
        assertEquals(inputName, result);
    }

    @Test
    void testGenerateUniqueName_DuplicateName() {
        List<String> existingNames = new ArrayList<>();
        existingNames.add("Название.obj");
        existingNames.add("Название (1).obj");
        existingNames.add("Другое Название.obj");

        String inputName = "Название.obj";
        String expected = "Название (2).obj";
        String result = generateUniqueName(inputName, existingNames);
        assertEquals(expected, result);
    }

    @Test
    void testGenerateUniqueName_NameWithCount() {
        List<String> existingNames = new ArrayList<>();
        existingNames.add("Название.obj");
        existingNames.add("Название (1).obj");
        existingNames.add("Название (3).obj");
        existingNames.add("Другое Название.obj");

        String inputName = "Название (2).obj";
        String expected = "Название (4).obj";
        String result = generateUniqueName(inputName, existingNames);
        assertEquals(expected, result);
    }

    @Test
    void testGenerateUniqueName_NameWithCount_NoExistingCount() {
        List<String> existingNames = new ArrayList<>();
        existingNames.add("Название.obj");
        existingNames.add("Название (1).obj");
        existingNames.add("Название (3).obj");
        existingNames.add("Другое Название.obj");

        String inputName = "Название (5).obj";
        String expected = "Название (5).obj";
        String result = generateUniqueName(inputName, existingNames);
        assertEquals(expected, result);
    }
}
