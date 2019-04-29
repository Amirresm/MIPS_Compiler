package com.example.msc.mpis_compiler.utils;

import com.example.msc.mpis_compiler.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.msc.mpis_compiler.utils.Utiliy.getBinaryWithDigits;
import static com.example.msc.mpis_compiler.utils.Utiliy.getDecimal;
import static com.example.msc.mpis_compiler.utils.Utiliy.getSplitedList;
import static com.example.msc.mpis_compiler.utils.Utiliy.isNumeric;

/**
 * Created by msc on 29/04/2019.
 */

public class Logic {

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

    public static void preProcess() {
        oppCodes.put("add", "0000");
        oppCodes.put("sub", "0001");
        oppCodes.put("slt", "0010");
        oppCodes.put("or", "0011");
        oppCodes.put("nand", "0100");
        oppCodes.put("addi", "0101");
        oppCodes.put("slti", "0110");
        oppCodes.put("ori", "0111");
        oppCodes.put("lui", "1000");
        oppCodes.put("lw", "1001");
        oppCodes.put("sw", "1010");
        oppCodes.put("beq", "1011");
        oppCodes.put("jalr", "1101");
        oppCodes.put("j", "1101");
        oppCodes.put("halt", "1110");
    }

    public static void preProcessFormates() {
        formatR.add("add");
        formatR.add("sub");
        formatR.add("slt");
        formatR.add("or");
        formatR.add("nand");

        formatI.add("addi");
        formatI.add("ori");
        formatI.add("slti");
        formatI.add("bne");
        formatI.add("beq");
        formatI.add("sw");
        formatI.add("lw");
        formatI.add("jalr");
        formatI.add("lui");

        formatJ.add("j");
        formatJ.add("halt");
    }

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
}
