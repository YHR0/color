package colorrecognition.example.com.colorrecognition;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Range extends AppCompatActivity {
    SharedPreferences sp;
    EditText editText,editTextguding;
    float ff=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range);
        editText=findViewById(R.id.rangeetext);
        editTextguding=findViewById(R.id.editTextguding);
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
    }

    public void queren(View view) {
        String str=editText.getText().toString();

        if (str==null||"".equals(str)){
            Toast.makeText(Range.this,"波动数字不可为空",Toast.LENGTH_SHORT).show();
        }else {
            ff= Float.parseFloat(str);
            float li=5*ff;
            Toast.makeText(Range.this,"设置成功 实例5*"+ff+"="+li,Toast.LENGTH_SHORT).show();
               sp = getSharedPreferences("range",MODE_PRIVATE);
            sp.edit().putFloat("number",ff).commit();
        }


    }

    public void guding(View view) {
        String str=editTextguding.getText().toString();

        if (str==null||"".equals(str)){
            Toast.makeText(Range.this,"固定波动数字不可为空",Toast.LENGTH_SHORT).show();
        }else {
            int ints= Integer.valueOf(str);
            Toast.makeText(Range.this,"设置成功 参数："+ints,Toast.LENGTH_SHORT).show();
               sp = getSharedPreferences("range",MODE_PRIVATE);
            sp.edit().putInt("fixednumber",ints).commit();
        }






    }
}
