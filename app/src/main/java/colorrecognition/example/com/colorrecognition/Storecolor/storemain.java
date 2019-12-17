package colorrecognition.example.com.colorrecognition.Storecolor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import colorrecognition.example.com.colorrecognition.R;
import colorrecognition.example.com.colorrecognition.SerialPortActivity;
import colorrecognition.example.com.colorrecognition.sql.Sqldata;

public class storemain extends SerialPortActivity {
    private TextView textView5;
    private Button storebuttonsure;
    private Button storebuttonReset;
    private  Button storebutton;

    private ListView storelv;
    private int number=0;
    private SimpleAdapter arrayAdapter;
    private EditText editText,relaynumber;
    private   String TAG="main";
    private String[] datastr=new String[]{"redmin","redmax","greenmin","greenmax","bluemin","bluemax","relaynumber"};  //relaynumber字段储存要打开第几个继电器

    private  ArrayList<HashMap<String, Object>> arrayList;
    private byte macaddr[]={(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
    byte sendorder[]={(byte) 0XFD,(byte)0x04,(byte)0xFF,(byte)0xFF,(byte)0x00,(byte)0x00,(byte)'D',(byte)0xFF,(byte)0xFF,(byte)0x00};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storemain);
        textView5 = (TextView) findViewById(R.id.textView5);



        editText=findViewById(R.id.storewrite);
        relaynumber=findViewById(R.id.relaynumber);//新增语句
        storelv = (ListView) findViewById(R.id.storelv);
         arrayList=new ArrayList();

        storebuttonsure = (Button) findViewById(R.id.storebuttonsure);
        storebuttonsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number>=3){

                    int rmin=255,rmax=0,rsum=0,rmean=0,gmin=255,gmax=0,gsum=0,gmean=0,bmin=255,bmax=0,bsum=0,bmean=0;
                    for(int i=0;i<arrayList.size();i++){
                        int r= (int) arrayList.get(i).get("red");
                        int g= (int) arrayList.get(i).get("green");
                        int b= (int) arrayList.get(i).get("blue");
                        if(rmin>r)rmin=r;
                        if(rmax<r)rmax=r;
                        rsum+=r;
                        if(gmin>g)gmin=g;
                        if(gmax<g)gmax=g;
                        gsum+=g;
                        if(bmin>b)bmin=b;
                        if(bmax<b)bmax=b;
                        bsum+=b;
                    }
                    rmean=rsum/number;
                    gmean=gsum/number;
                    bmean=bsum/number;
                    Log.d(TAG, "rgb: r"+rmax+"--"+rmin+" g"+gmax+"--"+gmin+"b"+bmax+"--"+bmin);
                    Log.d(TAG, "平均值: r"+rmean+" g"+gmean+"b"+bmean);


                    SharedPreferences qq = getSharedPreferences("range",MODE_PRIVATE);
                    float isFirst = qq.getFloat("number",0);
                    if(isFirst!=0){
                        float maxr=(rmax-rmean)/isFirst;
                        float minr=(rmean-rmin)/isFirst;
                        float maxg=(gmax-gmean)/isFirst;
                        float ming=(gmean-gmin)/isFirst;
                        float maxb=(bmax-bmean)/isFirst;
                        float minb=(bmean-bmin)/isFirst;

                        rmax= (int) (rmax+maxr);
                        rmin= (int) (rmin-minr);

                        gmax= (int) (gmax+maxg);
                        gmin= (int) (gmin-ming);

                        bmax= (int) (bmax+maxb);
                        bmin= (int) (bmin-minb);
                        Log.d(TAG, "波动范围的: r"+rmax+"--"+rmin+" g"+gmax+"--"+gmin+"b"+bmax+"--"+bmin);
                    }else {
                        int Dynamicnumber=Integer.valueOf(getString(R.string.Dynamicnumber));
                        rmax=rmax+(rmax-rmean)/Dynamicnumber;
                        rmin=rmin-(rmean-rmin)/Dynamicnumber;

                        gmax=gmax+(gmax-gmean)/Dynamicnumber;
                        gmin=gmin-(gmean-gmin)/Dynamicnumber;

                        bmax=bmax+(bmax-bmean)/Dynamicnumber;
                        bmin=bmin-(bmean-bmin)/Dynamicnumber;
                        Log.d(TAG, "默认波动范围的: r"+rmax+"--"+rmin+" g"+gmax+"--"+gmin+"b"+bmax+"--"+bmin);

                    }
                    int fixednumber=qq.getInt("fixednumber",0);
                    if(fixednumber!=0) {
                        rmax=rmax+fixednumber;
                        rmin=rmin-fixednumber;

                        gmax=gmax+fixednumber;
                        gmin=gmin-fixednumber;

                        bmax=bmax+fixednumber;
                        bmin=bmin-fixednumber;
                        Log.d(TAG, "固定范围存到数据库的: r"+rmax+"--"+rmin+" g"+gmax+"--"+gmin+"b"+bmax+"--"+bmin);

                    }else {
                        int stringfixednumber=Integer.valueOf(getString(R.string.fixednumber));
                        rmax=rmax+stringfixednumber;
                        rmin=rmin-stringfixednumber;

                        gmax=gmax+stringfixednumber;
                        gmin=gmin-stringfixednumber;

                        bmax=bmax+stringfixednumber;
                        bmin=bmin-stringfixednumber;

                        Log.d(TAG, "默认固定范围存到数据库的: r"+rmax+"--"+rmin+" g"+gmax+"--"+gmin+"b"+bmax+"--"+bmin);
                    }
//
//




                    Sqldata sqldata=new Sqldata();
                    String name=editText.getText().toString();
                    String relaystr=relaynumber.getText().toString();//新增语句




                    if("".equals(name)||"".equals(relaystr)){//新增语句"".equals(relaystr)

                        Toast.makeText(storemain.this,"请输入物品名称后再确认",Toast.LENGTH_SHORT).show();
                    }else {


                        int[] dataint =new int[]{rmin,rmax,gmin,gmax,bmin,bmax,Integer.valueOf(relaystr)};//新增语句Integer.valueOf(relaystr)

                        Boolean boo =sqldata.write(storemain.this,name,datastr,dataint);
                        if (boo){
                            Toast.makeText(storemain.this,"绑定成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(storemain.this,"绑定失败，请查看错误原因,是否重复物品名",Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Toast.makeText(storemain.this,"至少采集三次颜色信息",Toast.LENGTH_SHORT).show();
                }




            }
        });
        storebuttonReset = (Button) findViewById(R.id.storebuttonReset);
        storebuttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "重置 1");
                if(arrayAdapter!=null){
                    Log.d(TAG, "重置 2");
                    number=1;
                    arrayList.clear();
                    Log.d(TAG, "size: "+arrayList.size());
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
        storebutton=findViewById(R.id.storebutton);
        storebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mOutputStream.write(sendorder);
                } catch (IOException e) {
                    Toast.makeText(storemain.this,"触发失败",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: "+e);
                    e.printStackTrace();

                }

            }
        });







    }

    @Override
    protected void onDataReceived(final byte[] buffer, int size) {
        // TODO Auto-generated method stub
        runOnUiThread(new Runnable() {
            public void run() {
                {
                    if(buffer[0]==(byte)0xFD||buffer[0]==(byte)0xFA)
                    {
                        if((buffer[6]==(byte)'D'))//(buffer[1]==(byte)0x06)&&
                        {
                            int jj=0;
                            int ibegin=(int)buffer[1];
                            String smacaddr="";
                            for(int ic=(6+ibegin-8);ic<(6+ibegin-8)+8;ic++)
                            {
                                macaddr[jj]=buffer[ic];
                                ++jj;
                                smacaddr+=String.format("%02x", buffer[ic]);
                            }
                            int shu=0;
                            int[] buf = new int[3];
                            for(int i=7,b=0;i<=9;i++,b++){
                                shu=buffer[i];
                                shu=0-shu;
                                shu=shu+127;
                                buf[b]=shu;
                            }
                            HashMap<String,Object> hashMap =new HashMap<>();
                            hashMap.put("number",number+1);number++;
                            hashMap.put("red",buf[0]);
                            hashMap.put("green",buf[1]);
                            hashMap.put("blue",buf[2]);
                            arrayList.add(hashMap);

                            arrayAdapter = new SimpleAdapter(storemain.this,arrayList,R.layout.storelayout,new String[]{"number","red","green","blue"},new int[]{R.id.storetop,R.id.storered,R.id.storegreen,R.id.storeblue});
                            storelv.setAdapter(arrayAdapter);
                        }
                    }
                }
            }
        });
    }


}
