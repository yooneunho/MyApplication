package yoon.myapplication;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    myDBHelper myHelper;
    EditText editName, editNum, editNameResult, editNumResult;
    //Button btnClear, btnInsert, btnModify, btnDelete, btnInQuiry;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("가수 그룹 관리 DB");


        editName = (EditText)findViewById(R.id.editName);
        editNum = (EditText)findViewById(R.id.editNum);
        editNameResult = (EditText)findViewById(R.id.editNameResult);
        editNumResult = (EditText)findViewById(R.id.editNumResult);
        myHelper = new myDBHelper(this);
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "groupDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO
            db.execSQL("CREATE TABLE groupTBL ( gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClear:
                sqlDB = myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDB, 1, 2);
                sqlDB.close();
                break;
            case R.id.btnInsert:
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO groupTBL VALUES ('"
                        + editName.getText().toString() + "' , "
                        + editNum.getText().toString() + ");");
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
                inQuiry();
                break;
            case R.id.btnModify:
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE groupTBL SET gNumber = " + editNum.getText().toString()
                        + " WHERE gName = '" + editName.getText().toString() + "';");
                Toast.makeText(getApplicationContext(), "수정됨", Toast.LENGTH_SHORT).show();
                inQuiry();
                break;
            case R.id.btnDelete:
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("DELETE FROM groupTBL WHERE gName = '"
                        + editName.getText().toString() + "';");
                Toast.makeText(getApplicationContext(), "삭제됨", Toast.LENGTH_SHORT).show();
                inQuiry();
                break;
            case R.id.btnInQuiry:
                inQuiry();
                break;
        }
    }

    public void inQuiry() {
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);

        String strNames = "그룹 이름" + "\r\n" + "----------" + "\r\n";
        String strNums = "인원" + "\r\n" + "----------" + "\r\n";

        while(cursor.moveToNext()) {
            strNames += cursor.getString(0) + "\r\n";
            strNums += cursor.getString(1) + "\r\n";
        }

        editNameResult.setText(strNames);
        editNumResult.setText(strNums);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu1, menu);
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

