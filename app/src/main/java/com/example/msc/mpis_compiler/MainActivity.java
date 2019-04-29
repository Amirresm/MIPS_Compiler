package com.example.msc.mpis_compiler;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.msc.mpis_compiler.listener.EditorTextWatcher;
import com.example.msc.mpis_compiler.utils.Maps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    Button fileBt;
    Button compileBt;
    EditText codeEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileBt = findViewById(R.id.file_bt);
        compileBt = findViewById(R.id.compile_bt);
        codeEt = findViewById(R.id.code_et);

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

        codeEt.addTextChangedListener(new EditorTextWatcher(this, codeEt, new Maps.kwColorMap()));

    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            Uri currFileURI = data.getData();

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
