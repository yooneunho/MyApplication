package com.example.yoon.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {
    DatePicker dp;
    EditText editDiary;
    Button btnWrite;
    String fileName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("간단한 일기장");
        setContentView(R.layout.activity_main);
        dp = (DatePicker)findViewById(R.id.datePicker1);
        editDiary=(EditText)findViewById(R.id.editDiary);
        btnWrite = (Button)findViewById(R.id.btnWrite);

        Calendar cal = Calendar.getInstance();
        final int cYear = cal.get(Calendar.YEAR);
        final int cMonth = cal.get(Calendar.MONTH);
        final int cDay = cal.get(Calendar.DAY_OF_MONTH);

        fileName = Integer.toString(cYear) + "_" + Integer.toString(cMonth+1) + "_" + Integer.toString(cDay)+".txt";
        String str = readDiary(fileName);
        editDiary.setText(str);
        btnWrite.setEnabled(true);

        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fileName = Integer.toString(year) + "_" + Integer.toString(monthOfYear+1) + "_" + Integer.toString(dayOfMonth)+".txt";
                String str = readDiary(fileName);
                editDiary.setText(str);
                btnWrite.setEnabled(true);


            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    FileOutputStream outFs = openFileOutput(fileName,Context.MODE_PRIVATE);
                    String str = editDiary.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(),fileName+"이 저장됨",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {

                }
            }
        });
    }
    String readDiary(String fName){
        String diaryStr = null;
        FileInputStream inFs;
        try{
            inFs = openFileInput(fName);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
            btnWrite.setText("수정하기");

        }catch (IOException e){
            editDiary.setHint("일기 없음");
            btnWrite.setText("새로 저장");
        }
        return diaryStr;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
