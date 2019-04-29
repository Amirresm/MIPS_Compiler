package com.example.msc.mpis_compiler;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msc.mpis_compiler.listener.EditorTextWatcher;
import com.example.msc.mpis_compiler.utils.Maps;
import com.example.msc.mpis_compiler.utils.Utiliy;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.msc.mpis_compiler.utils.Logic.scanner;

public class MainActivity extends AppCompatActivity {

    Button fileBt;
    Button compileBt;
    EditText codeEt;
    TextView outputTV;


//    public static HashMap<String, Integer> registers = new Maps.registers();
    public static ArrayList<String> lines = new Maps.lines();
    public static HashMap<String, Integer> labels = new Maps.labels();
    public static HashMap<String, String> oppCodes = new Maps.oppCodes();
    public static ArrayList<String> directives = new Maps.directives();
    public static ArrayList<String> used = new Maps.used();
    public static HashMap<String, Integer> kwColorMap = new Maps.kwColorMap();

    public static ArrayList<String> formatR = new Maps.formatR();
    public static ArrayList<String> formatI = new Maps.formatI();
    public static ArrayList<String> formatJ = new Maps.formatJ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utiliy.CounterProperties.pc = 0;
        Utiliy.CounterProperties.length = 0;

        getSupportActionBar().hide();

        fileBt = findViewById(R.id.file_bt);
        compileBt = findViewById(R.id.compile_bt);
        codeEt = findViewById(R.id.code_et);
        outputTV= findViewById(R.id.output_tv);

        fileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // Set your required file type
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose file..."),1001);
            }
        });

        codeEt.addTextChangedListener(new EditorTextWatcher(this, codeEt, kwColorMap));

        compileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String allCode = codeEt.getText().toString();
                String binaryCode = null;
                try {
                    binaryCode = scanner(allCode);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Code syntax error!", Toast.LENGTH_LONG).show();
                    System.out.println(e);
                    return;
                }
                outputTV.setText(binaryCode);
                Utiliy.resetEverything();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            try {
                InputStream fis = getContentResolver().openInputStream(data.getData());
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                codeEt.setText(sb.toString());
                Toast.makeText(this, "Code loaded.", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "File not found!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(this, "Code format is wrong!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }}

}
