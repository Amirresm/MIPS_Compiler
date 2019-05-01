package com.example.msc.mpis_compiler.containers;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;

import com.example.msc.mpis_compiler.R;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amirreza on 25/04/2019.
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
    public MapsContainer(Context context) {
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

        int oppCodeColor = ContextCompat.getColor(context, R.color.oppCodeColor);
        int directiveColor = ContextCompat.getColor(context, R.color.directiveColor);
        int commentColor = ContextCompat.getColor(context, R.color.commentColor);

        kwColorMap.put("add", oppCodeColor);
        kwColorMap.put("sub", oppCodeColor);
        kwColorMap.put("slt", oppCodeColor);
        kwColorMap.put("or", oppCodeColor);
        kwColorMap.put("nand", oppCodeColor);
        kwColorMap.put("addi", oppCodeColor);
        kwColorMap.put("slti", oppCodeColor);
        kwColorMap.put("ori", oppCodeColor);
        kwColorMap.put("lui", oppCodeColor);
        kwColorMap.put("lw", oppCodeColor);
        kwColorMap.put("sw", oppCodeColor);
        kwColorMap.put("beq", oppCodeColor);
        kwColorMap.put("jalr", oppCodeColor);
        kwColorMap.put("j", oppCodeColor);
        kwColorMap.put("halt", oppCodeColor);

        kwColorMap.put(".fill", directiveColor);
        kwColorMap.put(".space", directiveColor);

        kwColorMap.put("comment", commentColor);
    }
}
