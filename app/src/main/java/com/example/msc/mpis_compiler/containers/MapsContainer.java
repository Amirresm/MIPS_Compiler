package com.example.msc.mpis_compiler.containers;

import android.graphics.Color;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by msc on 01/05/2019.
 */

public class MapsContainer {
    public ArrayList<String> lines = new ArrayList<>();
    public HashMap<String, Integer> labels = new HashMap<>();
    public ArrayList<String> dupLabels = new ArrayList<>();
    public HashMap<String, Map.Entry<String, String>> oppCodes = new HashMap<>();
    public ArrayList<String> directives = new ArrayList<>();
    public HashMap<String, Integer> kwColorMap = new HashMap<>();
    public HashMap<String, Integer> labelsColorMap = new HashMap<>();
    public HashMap<String, Integer> errorsColorMap = new HashMap<>();
    public MapsContainer() {
        directives.add(".fill");
        directives.add(".space");

        //R-type
        oppCodes.put("add", new AbstractMap.SimpleEntry<>("0000","r"));
        oppCodes.put("sub", new AbstractMap.SimpleEntry<>("0001","r"));
        oppCodes.put("slt", new AbstractMap.SimpleEntry<>("0010","r"));
        oppCodes.put("or", new AbstractMap.SimpleEntry<>("0011","r"));
        oppCodes.put("nand", new AbstractMap.SimpleEntry<>("0100","r"));
        //I-type
        oppCodes.put("addi", new AbstractMap.SimpleEntry<>("0101","i"));
        oppCodes.put("slti", new AbstractMap.SimpleEntry<>("0110","i"));
        oppCodes.put("ori", new AbstractMap.SimpleEntry<>("0111","i"));
        oppCodes.put("lui", new AbstractMap.SimpleEntry<>("1000","i"));
        oppCodes.put("lw", new AbstractMap.SimpleEntry<>("1001","i"));
        oppCodes.put("sw", new AbstractMap.SimpleEntry<>("1010","i"));
        oppCodes.put("beq", new AbstractMap.SimpleEntry<>("1011","i"));
        oppCodes.put("jalr", new AbstractMap.SimpleEntry<>("1100","i"));
        //J-type
        oppCodes.put("j", new AbstractMap.SimpleEntry<>("1101","j"));
        oppCodes.put("halt", new AbstractMap.SimpleEntry<>("1110","j"));


        kwColorMap.put("add", Color.parseColor("#CC6F50"));
        kwColorMap.put("sub", Color.parseColor("#CC6F50"));
        kwColorMap.put("slt", Color.parseColor("#CC6F50"));
        kwColorMap.put("or", Color.parseColor("#CC6F50"));
        kwColorMap.put("nand", Color.parseColor("#CC6F50"));
        kwColorMap.put("addi", Color.parseColor("#CC6F50"));
        kwColorMap.put("slti", Color.parseColor("#CC6F50"));
        kwColorMap.put("ori", Color.parseColor("#CC6F50"));
        kwColorMap.put("lui", Color.parseColor("#CC6F50"));
        kwColorMap.put("lw", Color.parseColor("#CC6F50"));
        kwColorMap.put("sw", Color.parseColor("#CC6F50"));
        kwColorMap.put("beq", Color.parseColor("#CC6F50"));
        kwColorMap.put("jalr", Color.parseColor("#CC6F50"));
        kwColorMap.put("j", Color.parseColor("#CC6F50"));
        kwColorMap.put("halt", Color.parseColor("#CC6F50"));

        kwColorMap.put(".fill", Color.parseColor("#E2BA68"));
        kwColorMap.put(".space", Color.parseColor("#E2BA68"));

        kwColorMap.put("comment", Color.parseColor("#336270"));
    }
}
