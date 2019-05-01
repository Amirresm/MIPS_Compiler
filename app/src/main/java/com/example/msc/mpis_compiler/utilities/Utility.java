package com.example.msc.mpis_compiler.utilities;

import android.graphics.Color;

import com.example.msc.mpis_compiler.containers.CompileState;
import com.example.msc.mpis_compiler.containers.MapsContainer;

import java.util.HashMap;
import java.util.regex.Pattern;


public class Utility {
    private static HashMap<String, String> lineToElements (MapsContainer maps, String line) {
        HashMap<String,String> elements = new HashMap<>();
        if (line.contains("#"))
            line = line.substring(0, line.indexOf("#"));
        line = line.replaceAll(",*\\s+,*",",");
        String[] splitString = line.split(",");
        String[] elmnts = {"","","","",""};
        if(splitString.length >= 1)
            elmnts[0] = splitString[0];
        if(splitString.length >= 2)
            elmnts[1] = splitString[1];
        if(splitString.length >= 3)
            elmnts[2] = splitString[2];
        if(splitString.length >= 4)
            elmnts[3] = splitString[3];
        if(splitString.length >= 5)
            elmnts[4] = splitString[4];

//        System.out.println("elm0 = " + elmnts[0] + " : " + splitString[0]);
        Pattern pattern = Pattern.compile("\\w(\\w|\\d)*");

        int lblModifier = 0;
        if(elmnts[1].equals("")) {
            if(maps.oppCodes.containsKey(elmnts[0])) {
                elements.put("oppcode", elmnts[0]);
            }
            else if(maps.directives.contains(elmnts[0])) {
                elements.put("directive", elmnts[0]);
            }
            else {
                elements.put("label", elmnts[0]);
            }
        }
        //Looking for labels
        else if (!maps.oppCodes.containsKey(elmnts[0]) && pattern.matcher(elmnts[0]).matches() && elmnts[1].matches("[^0-9]+")) {
            elements.put("label", elmnts[0]);
            lblModifier = 1;
        }

        //If its oppCode:
        if (maps.oppCodes.containsKey(elmnts[0 + lblModifier])) {
            elements.put("oppcode", elmnts[0 + lblModifier]);
            if (maps.oppCodes.get(elmnts[0 + lblModifier]).getValue().equals("r")) {
                elements.put("rd", elmnts[1 + lblModifier]);
                elements.put("rs", elmnts[2 + lblModifier]);
                elements.put("rt", elmnts[3 + lblModifier]);
            } else if (maps.oppCodes.get(elmnts[0 + lblModifier]).getValue().equals("i")) {
                if (elmnts[0 + lblModifier].equals("lui")) {
                    elements.put("rt", elmnts[1 + lblModifier]);
                    elements.put("rs", "0000");
                    elements.put("imm", elmnts[2 + lblModifier]);
                } else if (elmnts[0 + lblModifier].equals("jalr")) {
                    elements.put("rt", elmnts[1 + lblModifier]);
                    elements.put("rs", elmnts[2 + lblModifier]);
                    elements.put("imm", "0000000000000000");
                } else {
                    elements.put("rt", elmnts[1 + lblModifier]);
                    elements.put("rs", elmnts[2 + lblModifier]);
                    elements.put("imm", elmnts[3 + lblModifier]);
                }
            } else if (maps.oppCodes.get(elmnts[0 + lblModifier]).getValue().equals("j")) {
                if (elmnts[0 + lblModifier].equals("j")) {
                    elements.put("offset", elmnts[1 + lblModifier]);
                }
                else if (elmnts[0 + lblModifier].equals("halt")) {
                    elements.put("offset", "0000000000000000");
                }
            }
        }
        //If its directive:
        else if (maps.directives.contains(elmnts[0 + lblModifier])) {
            elements.put("directive", elmnts[0 + lblModifier]);
            elements.put("dValue", elmnts[1 + lblModifier]);
        }
        //If none:
        else {
            elements.put("ERROR", "syntax");
        }
        return elements;
    }

    private static boolean checkLabels(MapsContainer maps, String label, int addr) {
        if (maps.labels.containsKey(label)) {
            maps.errorsColorMap.put(label, Color.RED);//ADDING ERROR COLOR
            return false;
        }
        else {
            maps.labels.put(label, addr);
            return true;
        }
    }

    public static boolean firstScan(MapsContainer maps, CompileState state, String codeText) {
//        System.out.println("scan for labels");
        if(!state.isScannedForLabels) {
            //on each scan we start over
            maps.labelsColorMap.clear();
            maps.labels.clear();
            maps.dupLabels.clear();
            maps.errorsColorMap.clear();
            state.errors.clear();
            state.isScannedForLabels = true;

            String[] lines = codeText.split("\\r?\\n");

            //first run to find all the labels
            for (int addr = 0; addr < lines.length; addr++) {
                HashMap<String,String> elements = lineToElements(maps, lines[addr]);
                if(elements.containsKey("label"))
                    if (!checkLabels(maps, elements.get("label"), addr))
                        state.errors.add(ERROR.DUPLICATELABEL);
            }
            //second run to find possible errors
            for (int addr = 0; addr < lines.length; addr++) {
                HashMap<String,String> elements = lineToElements(maps, lines[addr]);
                //imm related errors
                if(elements.containsKey("imm")) {
                    String imm = elements.get("imm");
                    //if its numerical
                    if(imm.matches("\\d+")) {
                        int numImm;
                        try {//in case the number is too big
                            numImm = Integer.parseInt(imm);
                        } catch (NumberFormatException e) {
                            numImm = 65536;//this value is to cause an error
                            e.printStackTrace();
                        }
//                        System.out.println("num imm = " + imm);
                        if (numImm > 65535) {
                            state.errors.add(ERROR.OFFSET);
                            maps.errorsColorMap.put(imm, Color.RED);//ADDING ERROR COLOR
                        }
                    }
                    //if its a label
                    else if(imm.matches("\\w(\\w|\\d)*")) {
//                        System.out.println("word imm = " + imm);
                        if(!maps.labels.containsKey(imm)) {
                            state.errors.add(ERROR.UNKNOWNLABEL);
                            maps.errorsColorMap.put(imm, Color.RED);//ADDING ERROR COLOR
                        }
                    }
                }
                //offset related errors(treated the same as imm)
                if(elements.containsKey("offset")) {
                    String offset = elements.get("offset");
                    //if its numerical
                    if(offset.matches("\\d+")) {
                        int numImm;
                        try {//in case the number is too big
                            numImm = Integer.parseInt(offset);
                        } catch (NumberFormatException e) {
                            numImm = 65536;//this value is to cause an error
                            e.printStackTrace();
                        }
//                        System.out.println("num imm = " + offset);
                        if (numImm > 65535) {
                            state.errors.add(ERROR.OFFSET);
                            maps.errorsColorMap.put(offset, Color.RED);//ADDING ERROR COLOR
                        }
                    }
                    //if its a label
                    else if(offset.matches("\\w(\\w|\\d)*")) {
//                        System.out.println("word imm = " + offset);
                        if(!maps.labels.containsKey(offset)) {
                            state.errors.add(ERROR.UNKNOWNLABEL);
                            maps.errorsColorMap.put(offset, Color.RED);//ADDING ERROR COLOR
                        }
                    }
                }
                //unknown syntax errors
                if(elements.containsKey("ERROR")) {
                    state.errors.add(ERROR.OPPCODE);
                }
            }

            //update label colors
            for (String label : maps.labels.keySet())
                maps.labelsColorMap.put(label, Color.parseColor("#206F50"));
//            System.out.println("labels " + maps.labels.size() + " | lblkw " + maps.labelsColorMap.size());
            return true;
        }
        return true;
    }


    private static String generateMachineCode(MapsContainer maps, String codeLine, int addr) {
        String machineCode = "";
        HashMap<String,String> elements = lineToElements(maps, codeLine);
        String codeType;
        if (elements.containsKey("oppcode"))
            codeType = maps.oppCodes.get(elements.get("oppcode")).getValue();
        else
            codeType = "directive";
        switch (codeType){
            case "r":
                machineCode = "0000"
                        + maps.oppCodes.get(elements.get("oppcode")).getKey()
                        + decimalToBinary(elements.get("rs"), 4)
                        + decimalToBinary(elements.get("rt"), 4)
                        + decimalToBinary(elements.get("rd"), 4)
                        + "000000000000";
                machineCode = String.valueOf(Integer.parseInt(machineCode,2));
                break;
            case "i":
                String imm = elements.get("imm");
//                System.out.println(codeLine + " :: " + imm);
                if(!elements.get("imm").matches("\\d+"))
                    imm = String.valueOf(maps.labels.get(elements.get("imm")));
//                System.out.println(":: " + imm);
                machineCode = "0000"
                        + maps.oppCodes.get(elements.get("oppcode")).getKey()
                        + decimalToBinary(elements.get("rs"), 4)
                        + decimalToBinary(elements.get("rt"), 4)
                        + decimalToBinary(imm, 16);
                machineCode = String.valueOf(Integer.parseInt(machineCode,2));
                break;
            case "j":
                String offset = elements.get("offset");
                if(!elements.get("offset").matches("\\d+"))
                    offset = String.valueOf(maps.labels.get(elements.get("offset")));
                machineCode = "0000"
                        + maps.oppCodes.get(elements.get("oppcode")).getKey()
                        + "00000000"
                        + decimalToBinary(offset, 16);
                machineCode = String.valueOf(Integer.parseInt(machineCode,2));
                break;
            case "directive":
                String dValue = elements.get("dValue");
                if(!elements.get("dValue").matches("(\\d+)|(-\\d+)"))
                    dValue = String.valueOf(maps.labels.get(elements.get("dValue")));
                machineCode = dValue;
                break;
        }

        return machineCode;
    }

    public static String mainScan (MapsContainer maps, CompileState state, String codeText) {
        String[] lines = codeText.split("\\r?\\n");
        String lineOutput;
        StringBuilder sb = new StringBuilder();
        for (int addr = 0; addr < lines.length; addr++) {
            lineOutput = generateMachineCode(maps, lines[addr], addr);
            sb.append(lineOutput).append("\n");
        }
        return sb.toString();
    }

    private static String decimalToBinary (String decimal , int length) {
        String binaryString = Integer.toBinaryString(Integer.parseInt(decimal));
        while (binaryString.length() < length) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

    public enum ERROR {
        NOERROR,
        UNKNOWNLABEL,
        DUPLICATELABEL,
        OFFSET,
        OPPCODE
    }

    public static String reportErrors (CompileState state) {
        String errors = "";
        if (state.errors.contains(ERROR.UNKNOWNLABEL))
            errors += "\"Unknown Labels\" ";
        if (state.errors.contains(ERROR.DUPLICATELABEL))
            errors += "\"Duplicate Labels\" ";
        if (state.errors.contains(ERROR.OFFSET))
            errors += "\"Incorrect Offset\" ";
        if (state.errors.contains(ERROR.OPPCODE))
            errors += "\"Incorrect OppCode\" ";
        errors = errors.trim();
        return errors;
    }
}
