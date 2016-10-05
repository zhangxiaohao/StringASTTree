package cn.edu.fudan;

import java.util.Date;

public class Timer {
    private static Date refTime = new Date();
    private static long refTick = System.nanoTime();

    public static long now() {
        return new Date().getTime() * 1000000 + System.nanoTime() - Timer.refTick;
    }
}