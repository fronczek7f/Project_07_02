package com.android.fronc.project_07_02;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String testInternalFileName = "internalFileName";
    private static final String testExternalFileNamePrivate = "externalFileNamePrivate.txt";
    private static final String testExternalFileNamePublic = "externalFileNamePublic.txt";
    private static final String saveSuccessful = "Save done!";
    private static final String saveFailed = "Save failed!";

    private EditText fileTitle;
    private EditText fileDescription;

    private int counter = 0;
    private int counterTest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        fileTitle = (EditText) findViewById(R.id.etFileTitle);
        fileDescription = (EditText) findViewById(R.id.etFileDescription);
        isStoragePermissionGranted();
    }

    public void onClickSave(View view) {
        if (!fileTitle.getText().toString().equals("") && !fileTitle.getText().toString().replaceAll("\\s+", "").equals("")) {
            String title = fileTitle.getText().toString();
            String description = fileDescription.getText().toString();
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = openFileOutput(title + ".txt", MODE_PRIVATE);
                fileOutputStream.write(description.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(this, saveSuccessful, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, saveFailed, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void onClickTest(View view) {
        String testString = "Test";
        String state = Environment.getExternalStorageState();
        counterTest++;

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = openFileOutput(testInternalFileName + counterTest + ".txt", MODE_PRIVATE);
            fileOutputStream.write(testInternalFileName.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(this, saveSuccessful, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, saveFailed, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }

        File filePublic = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), testExternalFileNamePublic);

        Context context = getApplicationContext();
        File filePrivate = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS), testExternalFileNamePrivate);

        FileOutputStream outputStreamPrivate = null;
        try {
            filePrivate.createNewFile();
            outputStreamPrivate = new FileOutputStream(filePrivate, true);
            outputStreamPrivate.write(testString.getBytes());
            outputStreamPrivate.flush();
            outputStreamPrivate.close();
            Toast.makeText(this, saveSuccessful, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, saveFailed, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        FileOutputStream outputStreamPublic = null;
        try {
            filePublic.createNewFile();
            outputStreamPublic = new FileOutputStream(filePublic, true);
            outputStreamPublic.write(testString.getBytes());
            outputStreamPublic.flush();
            outputStreamPublic.close();
            Toast.makeText(this, saveSuccessful, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, saveFailed, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }
}
