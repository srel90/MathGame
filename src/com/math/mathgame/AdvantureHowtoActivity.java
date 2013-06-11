package com.math.mathgame;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdvantureHowtoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_advanture_howto);
		((Button)findViewById(R.id.btnstart)).setOnClickListener(new OnClickListener() {	
	   		@Override
	   		public void onClick(View v) {
	   			Intent intent = new Intent(AdvantureHowtoActivity.this,AdvantureActivity.class);
	   			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   			startActivity(intent);   			
	            finish();
	   		}		
	          });
//		((Button)findViewById(R.id.ButtonEnd)).setOnClickListener(new OnClickListener() {	
//	   		@Override
//	   		public void onClick(View v) {
//	   			Intent intent = new Intent(AdvantureHowtoActivity.this,MainActivity.class);
//	   			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	   			startActivity(intent);
//	            finish();
//	   		}		
//	          });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.advanture_howto, menu);
		return true;
	}

}
