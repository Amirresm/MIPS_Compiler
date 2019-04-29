package com.example.msc.mpis_compiler.utils;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eaz on 19/04/24.
 */

public class Maps {
    public Maps () {}

    public static class registers extends HashMap<String, Integer> {
        public registers() {

        }
    }
    public static class lines extends ArrayList<String> {
        public lines(){

        }
    }
    public static class labels extends HashMap<String, Integer> {
        public labels() {

        }
    }
    public static class used extends ArrayList<String> {
        public used () {

        }
    }

    public static class directives extends ArrayList<String> {
        public directives() {
            this.add(".fill");
            this.add(".space");
        }
    }
    public static class oppCodes extends HashMap<String, String> {
        public oppCodes() {
            this.put("add", "0000");
            this.put("sub", "0001");
            this.put("slt", "0010");
            this.put("or", "0011");
            this.put("nand", "0100");
            this.put("addi", "0101");
            this.put("slti", "0110");
            this.put("ori", "0111");
            this.put("lui", "1000");
            this.put("lw", "1001");
            this.put("sw", "1010");
            this.put("beq", "1011");
            this.put("jalr", "1101");
            this.put("j", "1101");
            this.put("halt", "1110");
        }
    }

    public static class formatR extends ArrayList<String> {
        public formatR() {
            this.add("add");
            this.add("sub");
            this.add("slt");
            this.add("or");
            this.add("nand");
        }
    }
    public static class formatI extends ArrayList<String> {
        public formatI() {
            this.add("addi");
            this.add("ori");
            this.add("slti");
            this.add("bne");
            this.add("beq");
            this.add("sw");
            this.add("lw");
            this.add("jalr");
            this.add("lui");
        }
    }
    public static class formatJ extends ArrayList<String> {
        public formatJ() {
            this.add("j");
            this.add("halt");
        }
    }

    public static class kwColorMap extends HashMap<String, Integer> {
        public kwColorMap() {
            this.put(" add", Color.RED);
            this.put(" sub", Color.RED);
            this.put(" slt", Color.RED);
            this.put(" or", Color.RED);
            this.put(" nand", Color.RED);
            this.put(" addi", Color.RED);
            this.put(" slti", Color.RED);
            this.put(" ori", Color.RED);
            this.put(" lui", Color.RED);
            this.put(" lw", Color.RED);
            this.put(" sw", Color.RED);
            this.put(" beq", Color.RED);
            this.put(" jalr", Color.RED);
            this.put(" j", Color.RED);
            this.put(" halt", Color.RED);
        }
    }
}
