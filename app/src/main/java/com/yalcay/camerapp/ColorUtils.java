package com.yalcay.camerapp;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ColorUtils {

    public static int[] getAverageRGB(Bitmap bitmap, int x, int y, int width, int height) {
        int r = 0, g = 0, b = 0, count = 0;

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                if (i >= bitmap.getWidth() || j >= bitmap.getHeight()) continue;

                int pixel = bitmap.getPixel(i, j);
                r += Color.red(pixel);
                g += Color.green(pixel);
                b += Color.blue(pixel);
                count++;
            }
        }

        return new int[]{r / count, g / count, b / count};
    }

    public static float[] getHSVFromRGB(int[] rgb) {
        float[] hsv = new float[3];
        Color.RGBToHSV(rgb[0], rgb[1], rgb[2], hsv);
        return hsv;
    }

    public static float[] calculateRGBMetrics(int[] rgb) {
        float r = rgb[0], g = rgb[1], b = rgb[2];
        return new float[]{
                r + g, r + b, b + g,
                r / g, r / b, g / b,
                r / (g + b), g / (r + b), b / (r + g),
                r + g + b,
                r - g, r - b, g - b,
                r / (g - b), g / (r - b), b / (r - g),
                r - g - b, g - r - b, b - g - r,
                r - g + b, g - r + b, b - g + r, g - b + r
        };
    }

    public static float[] calculateHSVMetrics(float[] hsv) {
        float h = hsv[0], s = hsv[1], v = hsv[2];
        return new float[]{
                h + s, h + v, v + s,
                h / s, h / v, s / v,
                h / (s + v), s / (h + v), v / (h + s),
                h + s + v,
                h - s, h - v, s - v,
                h / (s - v), s / (h - v), v / (h - s),
                h - s - v, s - h - v, v - s - h,
                h - s + v, s - h + v, v - s + h, s - v + h
        };
    }
}