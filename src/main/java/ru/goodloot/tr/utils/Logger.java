/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * @author lol
 */
public class Logger {

    String className;

    public Logger(String className) {
        this.className = className;
    }

    public Logger(Class clazz) {
        this.className = clazz.getSimpleName();
    }

    private void write(String fileName, String str) {

        File file = new File(className + "/" + fileName);
        File dir = file.getParentFile();
        try {
            if (file.exists() == false) {
                if (dir != null) {
                    dir.mkdir();
                }
                file.createNewFile();
            }
            FileOutputStream f = new FileOutputStream(file, true);
            // Only for Windows
            f.write(("\r\n" + str).getBytes());
        } catch (IOException e) {
            System.out.println("An I/O Exception Occured" + e.toString());
        }
    }

    private String getFullStr(String date, Object... objs) {

        StringBuilder sb = new StringBuilder(date);
        for (Object o : objs) {
            if (o instanceof Double) {
                sb.append((o + "       ").substring(0, 7));
            } else {
                sb.append(o);
            }
            if (o != objs[objs.length - 1]) {
                sb.append("\t");
            }
        }
        return sb.toString();
    }

    public String getFullStr(Object... objs) {
        return getFullStr(Utils.getDateString() + "\t", objs);
    }

    public String getFullStrWithoutDate(Object... objs) {
        return getFullStr("", objs);
    }

    public void writeAndOut(String fileName, Object... objs) {

        String str = getFullStr(objs);
        write(fileName, str);
        System.out.println(str);
    }

    public void write(String fileName, Object... objs) {
        write(fileName, getFullStr(objs));
    }

    public void writeWithoutDate(String fileName, Object... objs) {
        write(fileName, getFullStrWithoutDate(objs));
    }

    public void out(Object... objs) {
        System.out.println(getFullStr(objs));
    }
}
