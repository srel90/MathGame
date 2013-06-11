package com.math.mathgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (global.mMedia != null) {
			global.mMedia.release();
		}
		global.mMedia = MediaPlayer.create(this, R.raw.bgsound1);
		global.mMedia.setLooping(true);
		global.mMedia.start();
		global.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		SeekBar seekBar1 = ((SeekBar) findViewById(R.id.seekBar1));
		seekBar1.setMax(global.audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekBar1.setProgress(global.audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));
		seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar arg0) {
			}

			public void onStartTrackingTouch(SeekBar arg0) {
			}

			public void onProgressChanged(SeekBar arg0, int progress,
					boolean arg2) {
				global.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						progress, 0);
			}
		});

		Button btn1 = (Button) findViewById(R.id.btn1);
		Button btn2 = (Button) findViewById(R.id.btn2);
		Button btn3 = (Button) findViewById(R.id.btn3);
		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				new Handler().postDelayed(new Runnable() {
					public void run() {
						Intent newActivity = new Intent(MainActivity.this,
								MemoryHowtoActivity.class);
						startActivity(newActivity);
						finish();
					}
				}, 100);
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				new Handler().postDelayed(new Runnable() {
					public void run() {
						Intent newActivity = new Intent(MainActivity.this,
								CalcHowtoActivity.class);
						startActivity(newActivity);
						finish();
					}
				}, 100);
			}
		});
		btn3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				new Handler().postDelayed(new Runnable() {
					public void run() {
						Intent newActivity = new Intent(MainActivity.this,
								AdvantureHowtoActivity.class);
						startActivity(newActivity);
						finish();
					}
				}, 100);
			}
		});
	}

	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// if(global.mMedia != null){
	// global.mMedia.release();
	// }
	// }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
