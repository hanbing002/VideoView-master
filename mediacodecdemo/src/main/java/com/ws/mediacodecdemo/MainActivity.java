package com.ws.mediacodecdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	private Button btnEncode = null;
	private Button btnDecode = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initWidgets();

	}
	
	private void initWidgets(){
		btnEncode = (Button)findViewById(R.id.btnEncode);
		btnEncode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, EncodeActivity.class);
				startActivity(intent);
			}
		});
		
		btnDecode = (Button)findViewById(R.id.btnDecode);
		btnDecode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, DecodeActivity.class);
				startActivity(intent);
			}
		});
	}



}
