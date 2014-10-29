/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.utils;

import java.io.*;

public class LoggerUtils {

    static public String newLine = getNewline();

    static public void write(String fileName, String str) {

        File file = new File(fileName);
        File dir = file.getParentFile();
        try {
            if (file.exists() == false) {
                if (dir != null) {
                    dir.mkdir();
                }
                file.createNewFile();
            }
            
            try (FileOutputStream f = new FileOutputStream(file, true);) {
            	f.write((newLine + str).getBytes());
            }
        } catch (IOException e) {
            System.out.println("An I/O Exception Occured" + e.toString());
        }
    }

    static public void writeAndOut(String fileName, String str) {

        write(fileName, str);
        System.out.println(str);
    }

    static public void writeAndWrite(String file1Name, String file2Name, String str) {

        write(file1Name, str);
        write(file2Name, str);
    }

    static public String readLast(String fileName) {

        String last = null;
        String line;
        try (BufferedReader f = new BufferedReader(new FileReader(fileName));) {
            while ((line = f.readLine()) != null) {
                last = line;
            }
        } catch (IOException e) {
            System.out.println("An I/O Exception Occured" + e.toString());
        }
        return last;
    }

    static public String find(String fileName, String str) {

        String search = null;
        String line;
        try (BufferedReader f = new BufferedReader(new FileReader(fileName));) {
            while ((line = f.readLine()) != null) {
                if (line.equals(str)) {
                    search = line;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("An I/O Exception Occured" + e.toString());
        }
        return search;
    }

    static public void findAndDoit(String logName, String fileName) {

        String last = LoggerUtils.readLast(fileName);
        String line;
        try (BufferedReader f = new BufferedReader(new FileReader(logName));) {
            while ((line = f.readLine()) != null) {
                if (line.equals(last)) {
                    break;
                }
            }
            while ((line = f.readLine()) != null) {
                LoggerUtils.write(fileName, line);
            }
        } catch (IOException e) {
            System.out.println("An I/O Exception Occured" + e.toString());
        }
    }

    static public String getNewline() {

        if (Utils.isWin()) {
            return "\r\n";
        } else {
            return "\n";
        }
    }

    static private String getFullStr(String date, Object... objs) {

        StringBuilder sb = new StringBuilder(date);
        for (Object o : objs) {
            if (o instanceof Double) {
                sb.append(String.format("%7.5f", o));
            } else {
                sb.append(o);
            }
            if (o != objs[objs.length - 1]) {
                sb.append("\t");
            }
        }
        return sb.toString();
    }

    static public String getFullStr(Object... objs) {
        return getFullStr(Utils.getDateString() + "\t", objs);
    }

    static public String getFullStrWithoutDate(Object... objs) {
        return getFullStr("", objs);
    }

    static public void out(Object... objs) {
        System.out.println(getFullStr(objs));
    }
}
