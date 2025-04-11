package com.example.cameraapp;

import android.graphics.Bitmap;
import android.graphics.Color;

public class CameraUtils {

    // RGB değerlerini alır
    public static int[] getRGBValues(Bitmap bitmap, int x, int y) {
        int pixel = bitmap.getPixel(x, y);
        int red = Color.red(pixel);
        int green = Color.green(pixel);
        int blue = Color.blue(pixel);

        return new int[]{red, green, blue};
    }

    // HSV değerlerini alır
    public static float[] getHSVValues(Bitmap bitmap, int x, int y) {
        int pixel = bitmap.getPixel(x, y);
        float[] hsv = new float[3];
        Color.colorToHSV(pixel, hsv);

        return hsv;
    }
}