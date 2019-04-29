package com.example.msc.mpis_compiler.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eaz on 19/04/24.
 */

public class Utiliy {
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
}
