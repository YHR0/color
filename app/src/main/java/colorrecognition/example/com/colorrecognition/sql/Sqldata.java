package colorrecognition.example.com.colorrecognition.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Sqldata {
    private Mysql mysql;
    private SQLiteDatabase sqLitewrite,sqLiteread;
    private Cursor cursor;
    private String TAG="main";
    private int number=0;
    public boolean write(Context context,String name,String[] strings,int[] data){

           mysql=new Mysql(context,"sqlcolor.db",null,1);
        sqLitewrite=mysql.getWritableDatabase();
           ContentValues contentValues =new ContentValues();
        contentValues.put("name",name);
           for(int i=0;i<strings.length;i++){
               contentValues.put(strings[i],data[i]);
               Log.d(TAG, "write: "+strings[i]+"----"+data[i]);
           }

           long boo=sqLitewrite.insert("color",null,contentValues);
        sqLitewrite.close();
          if(boo==-1){
              return false;
          }

        return true;
    }

    public ArrayList<HashMap<String, Object>> read(Context context, String[] strings){
        mysql=new Mysql(context,"sqlcolor.db",null,1);
             sqLiteread=mysql.getWritableDatabase();
             cursor=sqLiteread.query("color",strings,null,null,null,null,"_id desc");
             number=cursor.getCount();
             ArrayList<HashMap<String, Object>> arrayList =new ArrayList<>();

             while (cursor.moveToNext()) {
                 HashMap<String,Object> hashMap =new HashMap<>();
                 for(int i=0;i<strings.length;i++) {
                     hashMap.put(strings[i], cursor.getString(cursor.getColumnIndex(strings[i])));
                     Log.d(TAG, "cursor "+cursor.getString(cursor.getColumnIndex(strings[i])));
                 }
                 arrayList.add(hashMap);
         }

        return arrayList;
    }


    public String[][] query(Context context,int red ,int green,int blue){
        mysql=new Mysql(context,"sqlcolor.db",null,1);
        sqLiteread=mysql.getWritableDatabase();
        String selection ="redmin<="+red+" and redmax>="+red+" and greenmin<="+green+" and greenmax>="+green+" and bluemin<="+blue+" and bluemax>="+blue;
        cursor=sqLiteread.query("color",new String[]{"name","relaynumber"},selection,null,null,null,"_id desc");
        number=cursor.getCount();
        String[][] data =new String[number][2];
        int i=0;
        while (cursor.moveToNext()){
            data[i][0]=cursor.getString(cursor.getColumnIndex("name"));
            data[i][1]=cursor.getString(cursor.getColumnIndex("relaynumber"));
            i++;

        }
return data;
    }

    public void delete(Context context,String name){
        mysql=new Mysql(context,"sqlcolor.db",null,1);
        sqLitewrite=mysql.getWritableDatabase();

        String strDel="delete from color where name='"+name+"'";
        Log.d(TAG,"strDel"+strDel);
        sqLitewrite.execSQL(strDel);


//        sqLitewrite.execSQL("delete from evironment where name=?",new Object[]{name});




    }

    public int readnumber(){
        return number;
    }
}
