/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.utils;

/**
 * 
 * @author lol
 */
public class Logger {

    private final String className;

    public Logger(String className) {
        this.className = className;
    }

    public Logger(Class<?> clazz) {
        this(clazz.getSimpleName());
    }

    public void write(String fileName, Object... objs) {
        LoggerUtils.write(appendFolder(fileName), LoggerUtils.getFullStr(objs));
    }

    public void writeWithoutDate(String fileName, Object... objs) {
        LoggerUtils.write(appendFolder(fileName), LoggerUtils.getFullStrWithoutDate(objs));
    }

    public void writeAndOut(String fileName, Object... objs) {
        LoggerUtils.writeAndOut(appendFolder(fileName), LoggerUtils.getFullStr(objs));
    }

    private String appendFolder(String fileName) {
        return new StringBuilder(className).append("/").append(fileName).toString();
    }
}
