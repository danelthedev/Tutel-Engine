package org.tuiasi.engine.misc;


public class MathMisc {

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double wrapAngle(double angle, double min, double max) {
        double range = max - min;
        return angle - (range * Math.floor((angle - min) / range));
    }
}
