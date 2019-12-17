package colorrecognition.example.com.colorrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import colorrecognition.example.com.colorrecognition.Displaycolor.displaymain;
import colorrecognition.example.com.colorrecognition.Identifyingcolors.identifyingmain;
import colorrecognition.example.com.colorrecognition.Storecolor.storemain;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
String TAG="main";
	private Button mainbuttonidentifying;
	private Button mainbuttonstore;
	private Button mainbuttondisplay,mainbuttonRange;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainbuttonidentifying = (Button) findViewById(R.id.mainbuttonidentifying);
		mainbuttonidentifying.setOnClickListener(this);
		mainbuttonstore = (Button) findViewById(R.id.mainbuttonstore);
		mainbuttonstore.setOnClickListener(this);
		mainbuttondisplay = (Button) findViewById(R.id.mainbuttondisplay);
		mainbuttondisplay.setOnClickListener(this);
		mainbuttonRange = (Button) findViewById(R.id.mainbuttonRange);
		mainbuttonRange.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.mainbuttonidentifying :
				Log.d(TAG, "onClick: 11");
				Intent intent =new Intent(this,identifyingmain.class);
				startActivity(intent);break;
			case R.id.mainbuttonstore :
				Log.d(TAG, "onClick: 22");
				Intent intent1 =new Intent(this,storemain.class);
				startActivity(intent1);break;
			case R.id.mainbuttondisplay :
				Log.d(TAG, "onClick: 33");
				Intent intent2 =new Intent(this,displaymain.class);
				startActivity(intent2);break;
			case R.id.mainbuttonRange :
				Log.d(TAG, "onClick: 44");
				Intent intent3 =new Intent(this,Range.class);
				startActivity(intent3);break;
		}
	}
}