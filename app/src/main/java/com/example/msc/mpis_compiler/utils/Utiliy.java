package com.example.msc.mpis_compiler.utils;

import com.example.msc.mpis_compiler.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by eaz on 19/04/24.
 */

public class Utiliy {


//    public static Integer pc = MainActivity.pc;
//    public static Integer length = MainActivity.length;
    public static HashMap<String, Integer> registers = MainActivity.registers;
    public static ArrayList<String> lines = MainActivity.lines;
    public static HashMap<String, Integer> labels = MainActivity.labels;
    public static HashMap<String, String> oppCodes = MainActivity.oppCodes;
    public static ArrayList<String> directives = MainActivity.directives;
    public static ArrayList<String> used = MainActivity.used;
    public static HashMap<String, Integer> kwColorMap = MainActivity.kwColorMap;

    public static ArrayList<String> formatR = MainActivity.formatR;
    public static ArrayList<String> formatI = MainActivity.formatI;
    public static ArrayList<String> formatJ = MainActivity.formatJ;



    public static ArrayList<String> getSplitedList(String str) {
        if (str.contains("#")) { //delete from # to end
            str = str.substring(0, str.indexOf("#"));
        }
        ArrayList<String> splitedArray = new ArrayList<String>(Arrays.asList(str.split(","))); // Create an ArrayList object

        for (int i = 0; i < splitedArray.size(); i++) {
            if (splitedArray.get(i).equals("")) {
                splitedArray.remove(i);
                i = 0;
            }
        }
        return splitedArray;
    }


    public static String getBinaryWithDigits(int no, int digit) {
        StringBuilder result = new StringBuilder();
        int container[] = new int[digit];
        for (int i = 0; i < digit; i++) {
            container[i] = 0;
        }
        int i = 0;
        while (no > 0) {
            container[i] = no % 2;
            i++;
            no = no / 2;
        }
        for (int j = digit - 1; j >= 0; j--) {
            result.append(String.valueOf(container[j]));
        }
        return String.valueOf(result);
    }

    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static long getDecimal(String binaryNumber) {
        String reverse = new StringBuffer(binaryNumber).reverse().toString();
        long decimal = 0;
        for (int i = 0; i < reverse.length(); i++) {
            char c = reverse.charAt(i);
            int k = c - '0';
            decimal = decimal + k * (long) Math.pow(2, i);
        }
        return decimal;
    }

    public static void checkLine(String str) {
        str = str.toLowerCase().replace(" ", ",");
        String[] arr = str.split(",");
        lines.add(str);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("#")) {
                break;
            }
            if (arr[i].length() > 0) {
                if ((Pattern.matches("\\d+", arr[i]) || Pattern.matches("\\w+", arr[i]))
                        && !checkExpression(arr[i], i == 0)) {
                    System.out.println("No:" + arr[i]);
                    System.exit(1);
                }
            }
        }
    }

    private static boolean checkExpression(String exp, boolean isLabel) {
        if (oppCodes.containsKey(exp)) {
            return true;
        }
        if (Pattern.matches("\\d+", exp) && !isLabel) {
            return Integer.parseInt(exp) <= 65535;
        } else if (directives.contains(exp)) {
            return true;
        } else if (isLabel) {
            labels.put(exp, Utiliy.CounterProperties.pc);
            if (used.contains(exp)) {
                used.remove(exp);
            }
            return true;
        } else {
            if (used.contains(exp)) {
                return false;
            }
            if (!labels.containsKey(exp)) {
                used.add(exp);
                return true;
            }
        }
        return true;
    }

    public static class CounterProperties {
        public static int pc;
        public static int length;
    }
}
