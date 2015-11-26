package com.example.yoon.myapplication;

/**
 * Created by yoon on 2015-11-26.
 */
import android.app.Activity;
import android.os.Bundle;


public class second extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTitle("Second Activity");
        int value=0; ;
        boolean divByZero = false;
        android.content.Intent inIntent = getIntent();

        switch(inIntent.getStringExtra("Type")) {
            case "plus":
                value = inIntent.getIntExtra("Num1", 0) + inIntent.getIntExtra("Num2", 0);
                break;
            case "minus":
                value = inIntent.getIntExtra("Num1", 0) - inIntent.getIntExtra("Num2", 0);
                break;
            case "mul":
                value = inIntent.getIntExtra("Num1", 0) * inIntent.getIntExtra("Num2", 0);
                break;
            case "div":
                if(inIntent.getIntExtra("Num2", 0) == 0) {
                    value = 0;
                    divByZero = true;
                }
                else
                    value = inIntent.getIntExtra("Num1", 0) / inIntent.getIntExtra("Num2", 0);
                break;
        }

        android.content.Intent outIntent = new android.content.Intent(getApplicationContext(), MainActivity.class);
        outIntent.putExtra("Value", value);
        if(divByZero)   setResult(100, outIntent);
        else            setResult(RESULT_OK, outIntent);

        finish();
    }


}
