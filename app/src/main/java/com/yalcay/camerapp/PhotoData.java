package com.yalcay.camerapp;

public class PhotoData {
    private String fileName;
    private int r1, r2, r3, g1, g2, g3, b1, b2, b3;

    public PhotoData(String fileName, int r1, int g1, int b1, int r2, int g2, int b2, int r3, int g3, int b3) {
        this.fileName = fileName;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
    }

    public String getFileName() {
        return fileName;
    }

    public int getR1() {
        return r1;
    }

    public int getR2() {
        return r2;
    }

    public int getR3() {
        return r3;
    }

    public int getG1() {
        return g1;
    }

    public int getG2() {
        return g2;
    }

    public int getG3() {
        return g3;
    }

    public int getB1() {
        return b1;
    }

    public int getB2() {
        return b2;
    }

    public int getB3() {
        return b3;
    }
}