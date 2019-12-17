package colorrecognition.example.com.colorrecognition.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class Mysql extends SQLiteOpenHelper {


    public Mysql(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table color(_id integer primary key autoincrement,name text UNIQUE ,timemain integer,redmin integer,redmax integer,greenmin integer,greenmax integer,bluemin integer,bluemax integer,relaynumber integer)";
        //新增字段relaynumber字段储存要打开第几个继电器
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
