package colorrecognition.example.com.colorrecognition.Displaycolor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import colorrecognition.example.com.colorrecognition.R;
import colorrecognition.example.com.colorrecognition.sql.Sqldata;

public class displaymain extends AppCompatActivity {
    private String[] datastr=new String[]{"name","redmin","redmax","greenmin","greenmax","bluemin","bluemax"};
    private ListView listView;
    private  Sqldata sqldata;
    private ArrayList<HashMap<String, Object>> arrayList     ;
private String TAG="main";
private  TextView textView,displayrange,dispalyfixed;
private  BaseAdptershow  baseAdptershow;
    private  SharedPreferences qq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaymain);
        listView=findViewById(R.id.displaylv);
        displayrange=findViewById(R.id.displayrange);
        dispalyfixed=findViewById(R.id.dispalyfixed);
        arrayList =new ArrayList<>();
        sqldata =new Sqldata();
        textView=findViewById(R.id.displaysum);
        arrayList=sqldata.read(displaymain.this,datastr);
        textView.setText("统计：已绑定信息的物品有" + sqldata.readnumber() + "件");
        for(int i=0;i<arrayList.size();i++) {
            Log.d(TAG, "arrayList: "+arrayList.get(i).get("name"));
        }

         qq = getSharedPreferences("range",MODE_PRIVATE);
        float isFirst = qq.getFloat("number",0);
        int fixednumber=qq.getInt("fixednumber",0);

        if(isFirst!=0) {
            displayrange.setText("波动数字："+String.valueOf(isFirst));
        }else {
            displayrange.setText("波动数字："+getString(R.string.Dynamicnumber));

//            Log.d(TAG, "onCreate: "+R.string.Dynamicnumber);

        }


        if(fixednumber!=0) {
            dispalyfixed.setText("固定波动数字："+String.valueOf(fixednumber));
        }else {

            dispalyfixed.setText("固定波动数字："+getString(R.string.fixednumber));


//            int aa=1+Integer.valueOf(getString(R.integer.aaa));


//            Log.d(TAG, "onCreate: "+aa);
        }





         baseAdptershow=new BaseAdptershow(displaymain.this,arrayList);
        listView.setAdapter(baseAdptershow);

    }

  private class BaseAdptershow extends BaseAdapter{
      private ArrayList<HashMap<String, Object>> mList;
      private LayoutInflater mInflater;
      public BaseAdptershow(Context context, ArrayList list)
      {
          super();
          mList = list;
          mInflater = LayoutInflater.from(context);//获得孵化器对象
      }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null) {
                convertView = mInflater.inflate(R.layout.displaylayout, null);
            }
                HashMap<String,Object> hashMap =mList.get(position);

               TextView displayname;
                TextView displayred;
                TextView displayggreen;
                 TextView displayblue;
                Button displaydelete;

                displayname = (TextView)convertView.findViewById(R.id.displayname);
                displayred = (TextView) convertView.findViewById(R.id.displayred);
                displayggreen = (TextView) convertView.findViewById(R.id.displayggreen);
                displayblue = (TextView) convertView.findViewById(R.id.displayblue);
                displaydelete = (Button) convertView.findViewById(R.id.displaydelete);
//                Log.d(TAG, "getView: "+hashMap.get("name").toString());
                final String name=hashMap.get("name").toString();
            Log.d(TAG, "getViewname: "+ name);

                displayname.setText(name);
                displayred.setText("R:"+hashMap.get("redmin").toString()+"min "+hashMap.get("redmax").toString()+"max");
                displayggreen.setText("G:"+hashMap.get("greenmin").toString()+"min "+hashMap.get("greenmax").toString()+"max");
                displayblue.setText("B:"+hashMap.get("bluemin").toString()+"min "+hashMap.get("bluemax").toString()+"max");

                 displaydelete.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         sqldata.delete(displaymain.this,name);
                         mList.remove(position);
                         baseAdptershow.notifyDataSetChanged();
                     }
                 });


            return convertView;
        }
    }

}
