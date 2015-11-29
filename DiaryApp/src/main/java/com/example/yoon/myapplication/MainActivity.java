package com.example.yoon.myapplication;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends Activity {
    TextView dateText;
    Button saveBtn;
    EditText diaryEdt;
    DatePicker datePicker;
    View dialogView,optionView;
    String todayDate;//오늘 날짜
    String date_selected;
    String fileName;

    Calendar calendar = Calendar.getInstance();
    int cYear = calendar.get(Calendar.YEAR);
    int cMonth = calendar.get(Calendar.MONTH);
    int cDay = calendar.get(Calendar.DAY_OF_MONTH);

    final String strSDpath = Environment.getExternalStorageDirectory().getAbsolutePath();
    final File mydiary = new File(strSDpath + "/mydiary");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("DiaryApp");


        //mydiary 폴더를 생성함
        mydiary.mkdir();


        dateText = (TextView) findViewById(R.id.dateText);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        diaryEdt = (EditText) findViewById(R.id.diaryEdt);


        //처음실행시 TextView에 오늘 날짜가 나타나고 오늘 날짜의 일기내용이 존재하면 EditText에 보여줌
        todayDate = String.valueOf(cYear) + "년" + String.valueOf(cMonth+1) + "월" + String.valueOf(cDay) + "일";
        dateText.setText(todayDate);
        String str = readDiary(todayDate+".txt");
        diaryEdt.setText(str);

        //날짜가 표시된 TextView를 터치하면 날짜를 선택할수 있는 다이얼로그창이 나타남
        dateText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialogView = (View) View.inflate(MainActivity.this, R.layout.dateselect, null);
                datePicker =(DatePicker) dialogView.findViewById(R.id.datePicker);
                //DatePicker에서 날짜를 선택함
                datePicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_selected = String.valueOf(year) + "년" + String.valueOf(monthOfYear+1) + "월" + String.valueOf(dayOfMonth) + "일";
                        cYear = year;
                        cMonth = monthOfYear;
                        cDay = dayOfMonth;
                    }
                });
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("날짜선택");
                dlg.setView(dialogView);

                //확인버튼을 누르면 TextView에 선택한 날짜가 나타나고 해당날짜의 일기파일이 존재하면 읽어옴
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateText.setText(date_selected);
                        //해당날짜 일기내용이 존재하면 읽어옴
                        String str = readDiary(date_selected+".txt");
                        diaryEdt.setText(str);
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
                return false;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = String.valueOf(cYear) + "년" + String.valueOf(cMonth+1) + "월" + String.valueOf(cDay) + "일.txt";
                try{
                    String temp = mydiary + "/" + fileName;
                    FileOutputStream outFs = new FileOutputStream(temp);
                    String str = diaryEdt.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(), fileName +"이 저장됨",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),fileName +" 저장 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    String readDiary(String fName) {
        String strSDpath1 = Environment.getExternalStorageDirectory().getAbsolutePath();
        String diaryStr = null;
        FileInputStream inFs;
        try {
            inFs = new FileInputStream("/sdcard/mydiary/"+fName);
            byte[] txt = new byte[inFs.available()];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
            diaryEdt.setText(diaryStr);


        } catch (IOException e) {
            diaryEdt.setHint("내용 입력");
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

        final String delfileName = dateText.getText().toString();
//        final String strSDpath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        final File mydiary = new File(strSDpath + "/mydiary");

        switch(item.getItemId()){
            case R.id.reRead:
                String fileName = dateText.getText().toString();
                String str = readDiary(fileName+".txt");
                diaryEdt.setText("re:"+str);
                break;

            case R.id.delDiary:

                optionView = (View) View.inflate(MainActivity.this, R.layout.deletefiledialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle(delfileName + " 일기를 삭제?");
                dlg.setView(optionView);

                //확인 버튼을 누르면 해당날짜의 일기내용을 삭제
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String temp = mydiary + "/" + delfileName + ".txt";
                        File file = new File(temp);
                        file.delete();
                        Toast.makeText(getApplicationContext(),delfileName + "일기 삭제됨",Toast.LENGTH_SHORT).show();
                        diaryEdt.setText("");
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
                break;

            //글씨크기를 변경하는 옵션메뉴
            case R.id.sizeUp:
                diaryEdt.setTextSize(30);
                Toast.makeText(getApplicationContext(), "글씨 크게", Toast.LENGTH_LONG).show();
                return true;
            case R.id.sizeNormal:
                diaryEdt.setTextSize(20);
                Toast.makeText(getApplicationContext(), "글씨 중간", Toast.LENGTH_LONG).show();
                return true;
            case R.id.sizeSmall:
                diaryEdt.setTextSize(10);
                Toast.makeText(getApplicationContext(), "글씨 작게", Toast.LENGTH_LONG).show();
                return true;
        }
        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        return true;
    }

    //   return super.onOptionsItemSelected(item);
}
