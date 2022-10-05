package br.com.luque.svgchart.common;

import java.util.Random;

public class Color {

    private final int value;

    public static final Color RED = new Color(255, 255, 0, 0);
    public static final Color GREEN = new Color(255, 0, 255, 0);
    public static final Color BLUE = new Color(255, 0, 0, 255);
    public static final Color BLACK = new Color(255, 0, 0, 0);
    public static final Color WHITE = new Color(255, 255, 255, 255);

    public Color(int r, int g, int b) {
        this(255, r, g, b);
    }
    public Color(int a, int r, int g, int b) {
        int colorAsInt = a;
        colorAsInt = (colorAsInt << 8) | r;
        colorAsInt = (colorAsInt << 8) | g;
        colorAsInt = (colorAsInt << 8) | b;
        value = colorAsInt;
    }

    public int getR() {
        return value >> 16 & 0xFF;
    }

    public int getG() {
        return value >> 8 & 0xFF;
    }

    public int getB() {
        return value & 0xFF;
    }

    public int getIntValue() {
        return value;
    }

    public static Color random() {
        Random random = new Random(System.currentTimeMillis());
        return new Color(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

}
