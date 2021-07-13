package com.myapplicationdev.android.demofilereadwriting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    String folderLocation;
    Button btnRead, btnWrite;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRead = findViewById(R.id.btnRead);
        btnWrite = findViewById(R.id.btnWrite);
        tv = findViewById(R.id.tv);

        //folder creation
//        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//        ActivityCompat.requestPermissions(MainActivity.this, permission, 0);

        if (checkPermission() == false){
//            finish();
            return;
        }


        folderLocation = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/Folder";
        File folder_I = new File(folderLocation);
        if (folder_I.exists() == false) {
            boolean result = folder_I.mkdir();
            if (result == true) {
                Log.d("File Read/Write", "Folder created");
            }else{
                Log.d("File Read/Write", "Folder failed created");
            }
        }

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    try {
                        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Folder";
                        File targetFile = new File(folderLocation, "data.txt");
                        FileWriter writer = new FileWriter(targetFile, true);
                        writer.write("Hello world"+"\n");
                        writer.flush();
                        writer.close();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to write!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Folder";
                File targetFile = new File(folderLocation, "data.txt");
                if (targetFile.exists() == true){
                    String data ="";
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null){
                            data += line + "\n";
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    Log.d("Content", data);
                    tv.setText(data);
                }
            }
        });

    }
    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);



        if(permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED && permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED){
            return true;
        }else{
            String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(MainActivity.this, permission, 0);
            return false;
        }
    }
}