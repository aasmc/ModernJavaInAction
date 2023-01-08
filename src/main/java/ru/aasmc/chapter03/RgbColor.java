package ru.aasmc.chapter03;

public class RgbColor {
    private final int red;
    private final int green;
    private final int blue;

    public RgbColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public String toString() {
        return "Color: [" + red + ", " + green + ", " + blue + "]";
    }
}
