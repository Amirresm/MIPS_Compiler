package com.example.msc.mpis_compiler;

import android.graphics.Color;

import java.util.HashMap;

/**
 * Created by eaz on 19/03/28.
 */

public class KeywordMap {
    public HashMap<String, Integer> kwMap = new HashMap<>();

    KeywordMap() {
        kwMap.put("add", Color.RED);
        kwMap.put("deq", Color.RED);
        kwMap.put("rem", Color.RED);
        kwMap.put("swl", Color.RED);
        kwMap.put(";", Color.GRAY);
        kwMap.put("del", Color.GREEN);
        kwMap.put("low", Color.GREEN);
        kwMap.put("til", Color.GREEN);
        kwMap.put("find", Color.YELLOW);
        kwMap.put("burn", Color.YELLOW);
        kwMap.put("kill", Color.YELLOW);
    }
}
