package kr.ac.yeonsung.seoj.mobile1project_1121_02;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    MyDBHelper dbHelper;
    EditText editName, editCount, editOut1, editOut2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.edit_name);
        editCount = findViewById(R.id.edit_count);
        editOut1 = findViewById(R.id.edit_out1);
        editOut2 = findViewById(R.id.edit_out2);
        Button btnInit = findViewById(R.id.btn_init);
        Button btnInput = findViewById(R.id.btn_input);
        Button btnSearch = findViewById(R.id.btn_search);
        Button btnUpdate = findViewById(R.id.btn_update);
        Button btnDelete = findViewById(R.id.btn_delete);
        dbHelper = new MyDBHelper(this);

        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dbHelper.getWritableDatabase(); //읽기쓰기 가능한 데이터베이스를 반환받을 수 있음
                dbHelper.onUpgrade(db,1,2);
                //기존에 있던 테이블을 삭제하고, 새 테이블 생성가능 = 초기화 의미
                db.close(); //디비 다썼으면 클로즈
            }
        });
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dbHelper.getWritableDatabase();
                String sql = "insert into groupTBL values(' "+editName.getText().toString()+" ', "+editCount.getText().toString()+");";
                db.execSQL(sql); //sql문 실행
                db.close();
                Toast.makeText(MainActivity.this, "insert가 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dbHelper.getReadableDatabase();
                String sql = "select * from groupTBL;" ;
                Cursor cursor = db.rawQuery(sql, null);

                String strName = " 아이돌명\r\n____________\r\n";
                String strCount = " 인원 수\r\n____________\r\n";

                while (cursor.moveToNext()) {
                    strName += cursor.getString(0) + "\r\n";
                    strCount += cursor.getInt(1) + "\r\n";
                }

                editOut1.setText(strName); //누적된 strName값 출력
                editOut2.setText(strCount); //누적된 strCount값 출력

                cursor.close();
                db.close();
                btnSearch.callOnClick();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dbHelper.getWritableDatabase();
                String sql = " delete from groupTBL where gName='"+editName.getText().toString()+"' ";
                db.execSQL(sql);
                db.close();
                btnDelete.callOnClick();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dbHelper.getWritableDatabase();
                String sql = "update groupTBL set gNumber ="+editCount.getText().toString()+
                        " where gName ='"+editName.getText().toString()+"'";
                db.execSQL(sql);
                db.close();
                btnSearch.callOnClick(); //조회버튼 눌렀을 때와 똑같이 실행되게 해야함

            }
        });
    }

    public class MyDBHelper extends SQLiteOpenHelper{


        public MyDBHelper(@Nullable Context context) {
            super(context, "groupDB", null, 1);
        }
         //테이블생성
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table groupTBL("  +
                                                "gName char(50) primary key, "  +
                                                "gNumber integer);   ";
            db.execSQL(sql);

        }
        //테이블 초기화
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "drop table if exists groupTBL;"; //만약에 그룹TBL이 존재한다면 테이블을 드랍해라.
            db.execSQL(sql);
            onCreate(db);

        }
    }
}