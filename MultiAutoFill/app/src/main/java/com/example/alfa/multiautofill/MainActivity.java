package com.example.alfa.multiautofill;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> instructionList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillInstructionList();
        MultiAutoCompleteTextView edittext = (MultiAutoCompleteTextView) findViewById(R.id.mtv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,instructionList);
        edittext.setAdapter(adapter);
        edittext.setTokenizer(new SpaceTokenizer());

    }

    private void fillInstructionList() {
        instructionList = new ArrayList<>();
        instructionList.add("add");
        instructionList.add("sub");
        instructionList.add("slt");
        instructionList.add("or");
        instructionList.add("nand");
        instructionList.add("addi");
        instructionList.add("slti");
        instructionList.add("ori");
        instructionList.add("slti");
        instructionList.add("lui");
        instructionList.add("lw");
        instructionList.add("sw");
        instructionList.add("beq");
        instructionList.add("jalr");
        instructionList.add("j");
        instructionList.add("halt");
    };
}