package colorrecognition.example.com.colorrecognition.Identifyingcolors;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import colorrecognition.example.com.colorrecognition.HSV;
import colorrecognition.example.com.colorrecognition.R;
import colorrecognition.example.com.colorrecognition.SerialPortActivity;
import colorrecognition.example.com.colorrecognition.sql.Sqldata;

public class identifyingmain extends SerialPortActivity {
    private String[] mac={"b37d3402004b1200","c8c36f02004b1200","7c2e1402004b1200"};//对应继电器1、2、3的MAC地址
    private  byte jidianqi[]={(byte)0xFD,(byte)0x04,(byte)0xFF,(byte)0xFF,(byte)0x00,(byte)0x00,(byte)'K',(byte)0xFF,(byte)0xFF,(byte)0x00}; //继电器

    private TextView identitytv;
    private TextView identityred;
    private TextView identitygreen;
    private TextView identityblue;
    private TextView identityGoods;
    private TextView identityname;
    private byte macaddr[]={(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
    private int relaynumber;
    private String TAG="main";
    private TextView one,two,three;
    private Button button2;

    byte sendorder[]={(byte) 0XFD,(byte)0x04,(byte)0xFF,(byte)0xFF,(byte)0x00,(byte)0x00,(byte)'D',(byte)0xFF,(byte)0xFF,(byte)0x00};
    byte relaybyte[]={(byte) 0xFD,(byte)0x0F,(byte)0x0A,(byte)0xFF,(byte)0x00,(byte)0x00,(byte)'K',(byte)0x0A,(byte)0xFF,(byte)0xDD,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
    //用来发送的
//下面三个分别是三个继电器的MAC和短地址
    byte relayone[]={(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
    byte duan1[]={(byte)0x00,(byte)0x00};

    byte relaytwo[]={(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
    byte duan2[]={(byte)0x00,(byte)0x00};

    byte relaythree[]={(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
    byte duan3[]={(byte)0x00,(byte)0x00};


    private Sqldata sqldata;
    private Button identitybutton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifyingmain);

        one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        three=findViewById(R.id.three);

        identitytv = (TextView) findViewById(R.id.identitytv);
        identityred = (TextView) findViewById(R.id.identityred);
            identitygreen = (TextView) findViewById(R.id.identitygreen);
        identityblue = (TextView) findViewById(R.id.identityblue);
        identityGoods = (TextView) findViewById(R.id.identityGoods);
        identityname = (TextView) findViewById(R.id.identityname);
        button2 = findViewById(R.id.button2);
        sqldata=new Sqldata();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mOutputStream.write(jidianqi);
                } catch (IOException e) {
                    Toast.makeText(identifyingmain.this,"触发失败",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: "+e);
                    e.printStackTrace();
                }
            }
        });
        identitybutton2=findViewById(R.id.identitybutton2);
        identitybutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mOutputStream.write(sendorder);
                } catch (IOException e) {
                    Toast.makeText(identifyingmain.this,"触发失败",Toast.LENGTH_SHORT).show();
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
                        if((buffer[6]==(byte)'K')) {
                            //第一个继电器：
                            int ibegin=(int)buffer[1];
                            String smacaddr0="";
                            for(int ic=(6+ibegin-8);ic<(6+ibegin-8)+8;ic++)
                            {
                                smacaddr0=smacaddr0+String.format("%02x", buffer[ic]);
                            }
                            System.out.println(smacaddr0);
                            if (smacaddr0.equals(mac[0])) {
                                one.setText("第一个已连线");
                                //第一个继电器
                                int kk=0;
                                int ibegin1=(int)buffer[1];
                                String smacaddr1="";
                                for(int ie=(6+ibegin1-8);ie<(6+ibegin1-8)+8;ie++)
                                {
                                    relayone[kk]=buffer[ie];
                                    ++kk;
                                    smacaddr1=smacaddr1+String.format("%02x", buffer[ie]);
                                }
                                if((buffer[6]==(byte)'K')&&(buffer[7]==(byte)0xAA)&&(buffer[8]==(byte)0xAA)&&(buffer[9]==(byte)0xAA))
                                {
                                    duan1[0]=buffer[4];
                                    duan1[1]=buffer[5];
                                }
                                if((buffer[6]==(byte)'K')&&(buffer[7]==(byte)0xBB)&&(buffer[8]==(byte)0xBB)&&(buffer[9]==(byte)0xBB))
                                {
                                    duan1[0]=buffer[4];
                                    duan1[1]=buffer[5];
                                }
                            }
                            if (smacaddr0.equals(mac[1])) {
                                two.setText("第二个已连线");
                                int kk=0;
                                int ibegin1=(int)buffer[1];
                                String smacaddr1="";
                                for(int ie=(6+ibegin1-8);ie<(6+ibegin1-8)+8;ie++)
                                {
                                    relaytwo[kk]=buffer[ie];
                                    ++kk;
                                    smacaddr1=smacaddr1+String.format("%02x", buffer[ie]);
                                }
                                if((buffer[6]==(byte)'K')&&(buffer[7]==(byte)0xAA)&&(buffer[8]==(byte)0xAA)&&(buffer[9]==(byte)0xAA))
                                {
                                    duan2[0]=buffer[4];
                                    duan2[1]=buffer[5];
                                }
                                if((buffer[6]==(byte)'K')&&(buffer[7]==(byte)0xBB)&&(buffer[8]==(byte)0xBB)&&(buffer[9]==(byte)0xBB))
                                {
                                    duan2[0]=buffer[4];
                                    duan2[1]=buffer[5];
                                }
                            }
                            if (smacaddr0.equals(mac[2])) {
                                three.setText("第三个已连线");
                                int kk=0;
                                int ibegin1=(int)buffer[1];
                                String smacaddr1="";
                                for(int ie=(6+ibegin1-8);ie<(6+ibegin1-8)+8;ie++)
                                {
                                    relaythree[kk]=buffer[ie];
                                    ++kk;
                                    smacaddr1=smacaddr1+String.format("%02x", buffer[ie]);
                                }
                                if((buffer[6]==(byte)'K')&&(buffer[7]==(byte)0xAA)&&(buffer[8]==(byte)0xAA)&&(buffer[9]==(byte)0xAA))
                                {
                                    duan3[0]=buffer[4];
                                    duan3[1]=buffer[5];
                                }
                                if((buffer[6]==(byte)'K')&&(buffer[7]==(byte)0xBB)&&(buffer[8]==(byte)0xBB)&&(buffer[9]==(byte)0xBB))
                                {
                                    duan3[0]=buffer[4];
                                    duan3[1]=buffer[5];
                                }
                            }
                            }
                        }


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
                            buf=HSV.HSVnum(buf);
                            identitytv.setText("已检测到传感器数据");
                            identityred.setText(String.valueOf(buf[0]));
                            identitygreen.setText(String.valueOf(buf[1]));
                            identityblue.setText(String.valueOf(buf[2]));
                            identityGoods.setText("已识别到绑定的物品");

                            String[][] data=sqldata.query(identifyingmain.this,buf[0],buf[1],buf[2]);
                            String string="";
                            if (data.length==1){//成功识别到对应的物品
                                string=data[0][0];
                                relaynumber=Integer.valueOf(data[0][1]);

                                if(relaynumber==1){
                                    relaybyte[2]=duan1[0];
                                    relaybyte[3]=duan1[1];
                                    relaybyte[6]=(byte)'K';
                                    relaybyte[7]=duan1[0];
                                    relaybyte[8]=duan1[1];
                                    relaybyte[9]=(byte)0xDD;
                                    relaybyte[10]=(byte)0x01;
                                    relaybyte[11]=(byte)0xAA;
                                    relaybyte[13]=relayone[0];
                                    relaybyte[14]=relayone[1];
                                    relaybyte[15]=relayone[2];
                                    relaybyte[16]=relayone[3];
                                    relaybyte[17]=relayone[4];
                                    relaybyte[18]=relayone[5];
                                    relaybyte[19]=relayone[6];
                                    relaybyte[20]=relayone[7];
                                }else if(relaynumber==2){
                                    relaybyte[2]=duan2[0];
                                    relaybyte[3]=duan2[1];
                                    relaybyte[6]=(byte)'K';
                                    relaybyte[7]=duan2[0];
                                    relaybyte[8]=duan2[1];
                                    relaybyte[9]=(byte)0xDD;
                                    relaybyte[10]=(byte)0x01;
                                    relaybyte[11]=(byte)0xAA;
                                    relaybyte[13]=relaytwo[0];
                                    relaybyte[14]=relaytwo[1];
                                    relaybyte[15]=relaytwo[2];
                                    relaybyte[16]=relaytwo[3];
                                    relaybyte[17]=relaytwo[4];
                                    relaybyte[18]=relaytwo[5];
                                    relaybyte[19]=relaytwo[6];
                                    relaybyte[20]=relaytwo[7];
                                }else{
                                    relaybyte[2]=duan3[0];
                                    relaybyte[3]=duan3[1];
                                    relaybyte[6]=(byte)'K';
                                    relaybyte[7]=duan3[0];
                                    relaybyte[8]=duan3[1];
                                    relaybyte[9]=(byte)0xDD;
                                    relaybyte[10]=(byte)0x01;
                                    relaybyte[11]=(byte)0xAA;
                                    relaybyte[13]=relaythree[0];
                                    relaybyte[14]=relaythree[1];
                                    relaybyte[15]=relaythree[2];
                                    relaybyte[16]=relaythree[3];
                                    relaybyte[17]=relaythree[4];
                                    relaybyte[18]=relaythree[5];
                                    relaybyte[19]=relaythree[6];
                                    relaybyte[20]=relaythree[7];
                                }

                                                            try {
                                    mOutputStream.write(relaybyte);
                                } catch (IOException e) {
                                    Toast.makeText(identifyingmain.this,"触发失败",Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onClick: "+e);
                                    e.printStackTrace();
                                }

                                //延时五秒钟关闭
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        relaybyte[10] = (byte) 0x01;
                                        relaybyte[11] = (byte) 0xBB;
                                        try {
                                            mOutputStream.write(relaybyte);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },8500);


                            }else if (data.length==0){


                                Toast.makeText(identifyingmain.this,"未检测到对应的物品",Toast.LENGTH_SHORT).show();

                            }


                            else {
                                Toast.makeText(identifyingmain.this,"检测到此物品可能是以下多个物品的其中之一，故不打开继电器",Toast.LENGTH_SHORT).show();
                                for (int i=0;i<data.length;i++){
                                    if(i==0){
                                        string=data[i][0];
                                    }else {
                                        string+=","+data[i][0];
                                    }
                                }
                            }

                            identityname.setText(string);




                        }
                    }
                }

        });
    }
}
