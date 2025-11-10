package ru.panyukovnn.httpintegration.util;

import com.google.common.io.Resources;

import java.nio.charset.StandardCharsets;

public class TestFileUtil {

    public static String readFileFromResources(String fileName) {
        try {
            return Resources.toString(Resources.getResource(fileName), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
