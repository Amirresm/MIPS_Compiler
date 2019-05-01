package com.example.msc.mpis_compiler.utilities;

import android.graphics.Color;

import com.example.msc.mpis_compiler.containers.CompileState;
import com.example.msc.mpis_compiler.containers.MapsContainer;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by msc on 01/05/2019.
 */

public class Utility {
    public static HashMap<String, String> lineToElements (MapsContainer maps, String line) {
        HashMap<String,String> elements = new HashMap<>();
        if (line.contains("#"))
            line = line.substring(0, line.indexOf("#"));
        line = line.replaceAll("\\s+",",");
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
                if (elmnts[1].equals("lui")) {
                    elements.put("rt", elmnts[1 + lblModifier]);
                    elements.put("imm", elmnts[2 + lblModifier]);
                } else if (elmnts[0 + lblModifier].equals("jalr")) {
                    elements.put("rt", elmnts[1 + lblModifier]);
                    elements.put("rs", elmnts[2 + lblModifier]);
                } else {
                    elements.put("rt", elmnts[1 + lblModifier]);
                    elements.put("rs", elmnts[2 + lblModifier]);
                    elements.put("imm", elmnts[3 + lblModifier]);
                }
            } else if (maps.oppCodes.get(elmnts[0 + lblModifier]).getValue().equals("j")) {
                if (elmnts[1].equals("j")) {
                    elements.put("offset", elmnts[1 + lblModifier]);
                }
            }
        }
        //If its directive:
        else if (maps.directives.contains(elmnts[0 + lblModifier])) {
            elements.put("directive", elmnts[0 + lblModifier]);
        }
        //If none:
        else {
            elements.put("ERROR", "syntax");
        }
        return elements;
    }

    public static boolean checkLabels(MapsContainer maps, String label, int addr) {
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
            boolean noDuplicate = true;
            maps.labelsColorMap.clear();
            maps.labels.clear();
            maps.dupLabels.clear();
            maps.errorsColorMap.clear();
            state.isScannedForLabels = true;

            String[] lines = codeText.split("\\r?\\n");
            for (int addr = 0; addr < lines.length; addr++) {
                HashMap<String,String> elements = lineToElements(maps, lines[addr]);
                if(elements.containsKey("label"))
                    if (!checkLabels(maps, elements.get("label"), addr))
                        state.errors.add(ERROR.DUPLICATELABEL);
            }
            for (int addr = 0; addr < lines.length; addr++) {
                HashMap<String,String> elements = lineToElements(maps, lines[addr]);
                if(elements.containsKey("imm")) {
                    String imm = elements.get("imm");
                    if(imm.matches("\\d+")) {
                        int numImm = 0;
                        try {//in case the number is too big
                            numImm = Integer.parseInt(imm);
                        } catch (NumberFormatException e) {
                            numImm = 65536;//this value is to cause an error
                            e.printStackTrace();
                        }
                        System.out.println("num imm = " + imm);
                        if (numImm > 65535) {
                            state.errors.add(ERROR.OFFSET);
                            maps.errorsColorMap.put(imm, Color.RED);//ADDING ERROR COLOR
                        }
                    }
                    else if(imm.matches("\\w(\\w|\\d)*")) {
                        System.out.println("word imm = " + imm);
                        if(!maps.labels.containsKey(imm)) {
                            state.errors.add(ERROR.UNKNOWNLABEL);
                            maps.errorsColorMap.put(imm, Color.RED);//ADDING ERROR COLOR
                        }
                    }
                }
                if(elements.containsKey("ERROR")) {
                    String error = elements.get("ERROR");
                    state.errors.add(ERROR.OPPCODE);
                }
            }

            //update label colors
            for (String label : maps.labels.keySet())
                maps.labelsColorMap.put(label, Color.parseColor("#206F50"));
//            System.out.println("labels " + maps.labels.size() + " | lblkw " + maps.labelsColorMap.size());
            if (!noDuplicate)
                return false;
            return true;
        }
        return true;
    }

    public ERROR findError (MapsContainer maps, String line) {
        ERROR error = ERROR.NOERROR;
        return error;
    }

    public enum ERROR {
        NOERROR,
        UNKNOWNLABEL,
        DUPLICATELABEL,
        OFFSET,
        OPPCODE
    }
}
