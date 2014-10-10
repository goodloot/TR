/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.utils;

import java.util.Date;

/**
 * 
 * @author lol
 */
public class Utils {

    public static double round(double number, int scale) {

        int pow = 10;
        for (int i = 1; i < scale; i++) {
            pow *= 10;
        }
        double tmp = number * pow;
        return (double) (int) tmp / pow;
    }

    public static long getNonce() {

        return System.currentTimeMillis();
    }

    public static String getDateString() {

        String d = new Date().toString();
        return d.substring(0, d.length() - 9);
    }

    public static boolean isWin() {

        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    public static byte[] concat(byte[] A, byte[] B) {

        int aLen = A.length;
        int bLen = B.length;
        byte[] C = new byte[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

    public static void sleep(long millisec) {

        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            LoggerUtils.writeAndOut("errs/errs.txt",
                            "Thread Interrunted! " + e.toString());
            e.printStackTrace();
        }
    }

    public static Date now() {
        return new Date();
    }
}
