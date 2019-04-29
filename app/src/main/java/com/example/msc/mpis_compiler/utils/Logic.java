package com.example.msc.mpis_compiler.utils;

import com.example.msc.mpis_compiler.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.msc.mpis_compiler.utils.Utiliy.checkLine;
import static com.example.msc.mpis_compiler.utils.Utiliy.getBinaryWithDigits;
import static com.example.msc.mpis_compiler.utils.Utiliy.getDecimal;
import static com.example.msc.mpis_compiler.utils.Utiliy.getSplitedList;
import static com.example.msc.mpis_compiler.utils.Utiliy.isNumeric;

/**
 * Created by msc on 29/04/2019.
 */

public class Logic {

//    public static HashMap<String, Integer> registers = MainActivity.registers;
    public static ArrayList<String> lines = MainActivity.lines;
    public static HashMap<String, Integer> labels = MainActivity.labels;
    public static HashMap<String, String> oppCodes = MainActivity.oppCodes;
    public static ArrayList<String> directives = MainActivity.directives;
    public static ArrayList<String> used = MainActivity.used;
    public static HashMap<String, Integer> kwColorMap = MainActivity.kwColorMap;

    public static ArrayList<String> formatR = MainActivity.formatR;
    public static ArrayList<String> formatI = MainActivity.formatI;
    public static ArrayList<String> formatJ = MainActivity.formatJ;


    public static String getMachineCode() {
        String binary = " ";
        String answer = "";
        String line = lines.get(Utiliy.CounterProperties.pc).toLowerCase();
        ArrayList<String> splitedList = getSplitedList(line);
        System.out.println(line + Utiliy.CounterProperties.pc);

        if (labels.containsKey(splitedList.get(0))) { //remove first index for labeled instructions
            splitedList.remove(0);
        }

        //generate oppcode for kinds of formats
        if (formatR.contains(splitedList.get(0))) {
            binary = "0000" + oppCodes.get(splitedList.get(0)) +
                    getBinaryWithDigits(Integer.parseInt(splitedList.get(2)), 4) +
                    getBinaryWithDigits(Integer.parseInt(splitedList.get(3)), 4) +
                    getBinaryWithDigits(Integer.parseInt(splitedList.get(1)), 4) +
                    "000000000000";

        } else if (formatI.contains(splitedList.get(0))) {

            if (splitedList.get(0).equals("lui")) {//lui instruction is exception in rs field
                splitedList.add(2, "0");
            }
            String offset = "";
            int offsetindex = 3;

            if (isNumeric(splitedList.get(offsetindex))) {// check offset is label or a number
                if (splitedList.get(0).equals("beq")) {
                    offset = String.valueOf(Integer.valueOf(splitedList.get(offsetindex)) - Utiliy.CounterProperties.pc - 1);
                } else {
                    offset = (splitedList.get(offsetindex));
                }
            } else {
                if (splitedList.get(0).equals("beq")) {
                    offset = Integer.valueOf(labels.get(splitedList.get(offsetindex)) - Utiliy.CounterProperties.pc - 1).toString();
                } else {
                    offset = labels.get(splitedList.get(offsetindex)).toString();

                }
            }

            if (splitedList.get(0).equals("jalr")) {//jalr instruction is exception in rs offset field
                offset = "00000000000000000000";
            }
            binary = "0000" + oppCodes.get(splitedList.get(0)) +
                    getBinaryWithDigits(Integer.parseInt(splitedList.get(2)), 4) +
                    getBinaryWithDigits(Integer.parseInt(splitedList.get(1)), 4) +
                    getBinaryWithDigits(Integer.parseInt(offset), 16);
        } else if (formatJ.contains(splitedList.get(0))) {
            // halt instruction end program and machine code is always constant
            if (splitedList.get(0).equals("halt")) {
                binary = "1110000000000000000000000000";
            } else {
                binary = "0000" + oppCodes.get(splitedList.get(0)) + "00000000" +
                        getBinaryWithDigits(Integer.parseInt(labels.get(splitedList.get(1)).toString()), 16);
            }
        } else {
            // print directives
            if (isNumeric(splitedList.get(splitedList.size() - 1))) {// check offset is label or a number
                answer = splitedList.get(splitedList.size() - 1);
            } else {
                answer = labels.get(splitedList.get(splitedList.size() - 1)).toString();
            }

            System.out.println(answer);
            return answer;
        }

        binary = binary.replaceFirst("^0+(?!$)", ""); //remove zero from fist of binary
        answer = String.valueOf(getDecimal(binary));
        System.out.println(answer);

        return answer;
    }

    public static String scanner(String asCode) {
        BufferedReader bufReader = new BufferedReader(new StringReader(asCode));
        String line=null;
        try {
            while( (line=bufReader.readLine()) != null )
            {
                if (line.length() > 0) {
                    checkLine(line);
                    Utiliy.CounterProperties.pc++;
                    Utiliy.CounterProperties.length++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (used.size() > 0) {
            //System.exit(1);
        }
        //Second scan:
        StringBuilder sb = new StringBuilder();
        Utiliy.CounterProperties.pc = 0;
        while (Utiliy.CounterProperties.pc < Utiliy.CounterProperties.length) {
            sb.append(getMachineCode() + '\n');
            Utiliy.CounterProperties.pc++;
        }
        return sb.toString();
    }

}
