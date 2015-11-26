package com.example.yoon.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {

    EditText input1, input2;
    RadioGroup rGroup01;
    RadioButton r_plus, r_minus, r_mul, r_div;
    Button btnCalc;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        input1 = (EditText)findViewById(R.id.Input1);
        input2 = (EditText)findViewById(R.id.Input2);
        rGroup01 = (RadioGroup)findViewById(R.id.Rgroup01);
        btnCalc = (Button)findViewById(R.id.BtnCalc);


        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(getApplicationContext(), second.class);

                if(input1.getText().toString().isEmpty() || input2.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "숫자를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("Num1", Integer.parseInt(input1.getText().toString()));
                intent.putExtra("Num2", Integer.parseInt(input2.getText().toString()));
                intent.putExtra("Type", type);
                startActivityForResult(intent, 0);
            }
        });

        rGroup01.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (rGroup01.getCheckedRadioButtonId()) {
                    case R.id.Plu:
                        type = "plus";
                        break;
                    case R.id.Min:
                        type = "minus";
                        break;
                    case R.id.Mul:
                        type = "mul";
                        break;
                    case R.id.Div:
                        type = "div";
                        break;
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        int value = data.getIntExtra("Value", 0);
        if(resultCode == 100)
            Toast.makeText(getApplicationContext(), "0으로 나눌 수 없습니다.", Toast.LENGTH_SHORT).show();
        else if(resultCode == RESULT_OK)
            Toast.makeText(getApplicationContext(), "합계 : " + value, Toast.LENGTH_SHORT).show();
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
