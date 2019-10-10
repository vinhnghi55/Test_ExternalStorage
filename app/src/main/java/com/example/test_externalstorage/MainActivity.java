package com.example.test_externalstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvData;
    private Button btnSaveData, btnReadData;

    private final String fileName = "vinhnghi.com";
    private final String content = "Đẹp trai";

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWidget();

        btnSaveData.setOnClickListener(this);
        btnReadData.setOnClickListener(this);
        checkAndRequestPermissions();
    }

    private void setWidget() {
        tvData = (TextView) findViewById(R.id.tv_data);
        btnSaveData = (Button) findViewById(R.id.btn_save_data);
        btnReadData = (Button) findViewById(R.id.btn_read_data);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_data:
//                saveData();
                saveData2();
                break;
            case R.id.btn_read_data:
                readData();
                break;
            default:
                break;
        }
    }

    //xin quyền
    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    //kiểm tra thiết bị có bộ nhớ ngoài và có thể ghi file được không
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void saveData() {
        if(isExternalStorageReadable()){
            FileOutputStream fileOutputStream = null;
            File file = null;
            try {
                file = new File(Environment.getExternalStorageDirectory(), fileName);
                Log.d(TAG, "saveData: " + Environment.getExternalStorageDirectory().getAbsolutePath());
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(content.getBytes());
                fileOutputStream.close();
                Toast.makeText(this, "Save data successfully", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "Can't save file", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveData2() {
        if(isExternalStorageReadable()){
            FileOutputStream fileOutputStream = null;
            File file = null;
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(content.getBytes());
                fileOutputStream.close();
                Toast.makeText(this, "Save data successfully", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "Can't save file", Toast.LENGTH_SHORT).show();
        }
    }

    public void  readData(){
        BufferedReader bufferedReader =null;
        File file = null;
        file = new File(Environment.getExternalStorageDirectory(), fileName);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine())!= null){
                buffer.append(line);
            }
            tvData.setText(buffer.toString());
            Log.d(TAG, "readData: "+buffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
