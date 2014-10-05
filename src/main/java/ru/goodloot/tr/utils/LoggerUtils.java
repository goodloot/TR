/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package ru.goodloot.tr.utils;

import java.io.*;

public class LoggerUtils {

    static boolean isWin = Utils.isWin();

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
            FileOutputStream f = new FileOutputStream(file, true);
            // Only for Windows
            f.write(("\r\n" + str).getBytes());
        } catch (IOException e) {
            System.out.println("An I/O Exception Occured" + e.toString());
        }
    }

    static public void writeAndOut(String fileName, String str) {

        LoggerUtils.write(fileName, str);
        System.out.println(str);
    }

    static public void writeAndWrite(String file1Name, String file2Name, String str) {

        LoggerUtils.write(file1Name, str);
        LoggerUtils.write(file2Name, str);
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

        if (isWin) {
            return "\r\n";
        } else {
            return "\n";
        }
    }
}
