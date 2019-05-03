package com.example.msc.mpis_compiler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msc.mpis_compiler.containers.CompileState;
import com.example.msc.mpis_compiler.listeners.EditorTextWatcher;
import com.example.msc.mpis_compiler.containers.MapsContainer;
import com.example.msc.mpis_compiler.utilities.Utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    Button fileBt;
    Button newBt;
    Button compileBt;
    EditText codeEt;
    TextView outputTV;

    Uri filePath;

    public static MapsContainer maps;
    public static CompileState state = new CompileState();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        maps = new MapsContainer(this);

        fileBt = findViewById(R.id.file_bt);
        newBt = findViewById(R.id.new_bt);
        compileBt = findViewById(R.id.compile_bt);
        codeEt = findViewById(R.id.code_et);
        outputTV = findViewById(R.id.output_tv);


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        fileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // Set your required file type
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose file..."), 1001);
            }
        });

        newBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeEt.setText("");
                outputTV.setText("");
                Utility.resetAll(maps, state);
            }
        });

        codeEt.addTextChangedListener(new EditorTextWatcher(this, codeEt, maps, state));

        compileBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codeEt.getEditableText().toString().isEmpty())
                    return;
                if (state.errors.isEmpty()) {
                    final String output = Utility.mainScan(maps, state, codeEt.getText().toString());
                    outputTV.setText(output);

                    String fileName = filePath.getPath().substring(filePath.getPath().lastIndexOf("/") + 1);
                    Utility.saveFile(MainActivity.this, output, fileName);
                } else
                    Toast.makeText(MainActivity.this, "Please fix the following errors before compiling: " + Utility.reportErrors(state), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            try {
                if (data != null) {
                    filePath = data.getData();
                    InputStream fis = getContentResolver().openInputStream(filePath);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    codeEt.setText(sb.toString());
//                    Toast.makeText(this, "Code loaded.", Toast.LENGTH_SHORT).show();
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(this, "Code format is wrong!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Code syntax error: " + e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage; Cannot save outputs!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
