package com.math.mathgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class MemoryActivity extends Activity {
    private static int ROW_COUNT = -1;
	private static int COL_COUNT = -1;
	private Context context;
	private int [] [] cards;
	private int ans,round=1,time=30;
	private Card firstCard;
	ArrayList<Card> bb = new ArrayList<Card>();
	private ButtonListener buttonListener;
	private TableLayout mainTable;
	private UpdateCardsHandler handler;
	int scoreN=0;
	private boolean gamestart=false;
	private drawTextToBitmap drawTextToBitmap=new drawTextToBitmap(); 
	MyCount counter = new MyCount(30000,1000);
	MediaPlayer mMediaBtnClick;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        handler = new UpdateCardsHandler();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_memory); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        buttonListener = new ButtonListener();        
        mainTable = (TableLayout)findViewById(R.id.TableLayout01);       
        context  = mainTable.getContext();
        
        newGame(4,3);
        ((Button)findViewById(R.id.btnstart)).setOnClickListener(new OnClickListener() {	
    		@Override
    		public void onClick(View v) {
    			counter.cancel();
    			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	
    			alertDialogBuilder.setTitle("วิธีการเล่น");
    			alertDialogBuilder
    			.setMessage("กดกดกดเข้าไป")
    			.setCancelable(true)
    			.setPositiveButton("ตกลง",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								counter.start();
							}
						});
    			AlertDialog alertDialog = alertDialogBuilder.create();			
    			alertDialog.show();    					
    		}		
           });
       ((Button)findViewById(R.id.ButtonNew)).setOnClickListener(new OnClickListener() {	
		@Override
		public void onClick(View v) {
			newGame(4,3);			
		}		
       });
       ((Button)findViewById(R.id.ButtonEnd)).setOnClickListener(new OnClickListener() {	
   		@Override
   		public void onClick(View v) {
   			Intent intent = new Intent(MemoryActivity.this,MainActivity.class);
   			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   			startActivity(intent);   			
            counter.cancel();
            finish();
   		}		
          });
       ((RatingBar)findViewById(R.id.ratingBar1)).setOnTouchListener(new OnTouchListener() {		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
	        if (event.getAction() == MotionEvent.ACTION_UP) {
	        	((RatingBar)findViewById(R.id.ratingBar1)).setProgress(scoreN);
	        }
	        return true;
	    }
       });

    }
    
    private void newGame(int c, int r) {
    	ROW_COUNT = r;
    	COL_COUNT = c;
    	
    	cards = new int [COL_COUNT] [ROW_COUNT];
    
    	
    	TableRow tr = ((TableRow)findViewById(R.id.TableRow2));
    	tr.removeAllViews();
    	final TextView question = ((TextView)findViewById(R.id.question));
    	final TextView section = ((TextView)findViewById(R.id.section));
    	//final TextView score = ((TextView)findViewById(R.id.score));
    	section.setText("ด่าน :"+(round));
    	//score.setText("คะแนน :"+(scoreN));
    	((RatingBar)findViewById(R.id.ratingBar1)).setProgress(scoreN);
    	
    	question.setText("");
    	mainTable = new TableLayout(context);
    	tr.addView(mainTable);
    	loadImages();
    	 for (int y = 0; y < ROW_COUNT; y++) {
    		 mainTable.addView(createRow(y));
          }
    	 
    	firstCard=null; 
    	time=30;
    	counter.cancel();
     	counter.start();
     	final Handler handler = new Handler();
     	handler.postDelayed(new Runnable(){
     		@Override
     		public void run(){
     			for(int i=0;i<bb.size();i++){
     	    		bb.get(i).button.setBackgroundResource(R.drawable.memory_back);
     	    	}
     	    	question.setText(String.format( "%d X %d = ?", round,ans/round ));  	
     	    	gamestart=true;
     		}
     	}, 30000);	
	}
    public ArrayList<Integer> randN(){
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 1; i <= 12; i++) {
        	numbers.add(i*round);
        }       
        Collections.shuffle(numbers);      
        return numbers;
	}
    private void loadImages() {
    	ArrayList<Integer> images = new ArrayList<Integer>();
    	images=randN();
    	ArrayList<Integer> ansN = new ArrayList<Integer>(images);
    	Collections.shuffle(ansN);
    	int size = ROW_COUNT*COL_COUNT;
    	ans=ansN.get(0);
    	//Log.e("ans", String.valueOf(ans));
    	//Log.e("size", String.valueOf(size));
    	for(int i=size-1;i>=0;i--){
    		cards[i%COL_COUNT][i/COL_COUNT]=images.get(i);
    		//Log.i("loadCards()", "card["+(i%COL_COUNT)+"]["+(i/COL_COUNT)+"]="+cards[i%COL_COUNT][i/COL_COUNT] );
    	}
	}
    private TableRow createRow(int y){
    	 TableRow row = new TableRow(context);
    	 row.setHorizontalGravity(Gravity.CENTER);
         
         for (int x = 0; x < COL_COUNT; x++) {
		         row.addView(createImageButton(x,y));
         }
         return row;
    }
    
	@SuppressWarnings("deprecation")
	private View createImageButton(int x, int y){
    	Button button = new Button(context);
    	 	
    	Bitmap bmp = drawTextToBitmap.draw(context,R.drawable.memory_front,String.valueOf(cards[x][y]),Color.YELLOW);
		button.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bmp));
    	button.setId(100*x+y);
    	button.setOnClickListener(buttonListener);
    	bb.add(new Card(button,x,y));
    	return button;
    }
    
    class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
				if(!gamestart){
				return;
				}
				if(firstCard!=null){
					return;
				}
				if(mMediaBtnClick != null){
					mMediaBtnClick.release();
		        }
				mMediaBtnClick=MediaPlayer.create(MemoryActivity.this, R.raw.click);
				mMediaBtnClick.start();
				int id = v.getId();
				int x = id/100;
				int y = id%100;
				turnCard((Button)v,x,y);
				

			
		}

		@SuppressWarnings("deprecation")
		private void turnCard(Button button,int x, int y) {
			Bitmap bmp =drawTextToBitmap.draw(context,R.drawable.memory_front,String.valueOf(cards[x][y]),Color.YELLOW);
			
			button.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bmp));
			((TextView)findViewById(R.id.timecount)).setText("");
			//Log.e("turnCard",String.valueOf(cards[x][y]));
			
				firstCard = new Card(button,x,y);
				TimerTask timetask = new TimerTask() {
					
					@Override
					public void run() {
						try{
							 handler.sendEmptyMessage(0);
							
						}
						catch (Exception e) {
							Log.e("E1", e.getMessage());
						}
					}
				};
				
				  Timer timer = new Timer(false);
				  timer.schedule(timetask, 1000);
				  
			
				
		   }
			
		}
    
    class UpdateCardsHandler extends Handler{
    	
    	@Override
    	public void handleMessage(Message msg) {
    		
    			checkCards();

    	}
    	 public void checkCards(){
    		 //Log.i("checkCards()", "card["+(firstCard.x)+"]["+(firstCard.y)+"]="+cards[firstCard.x][firstCard.y] );
    	    	if(ans == cards[firstCard.x][firstCard.y]){                  
                    scoreN += 1;
                    
                    //final TextView score = ((TextView)findViewById(R.id.score));
                    //score.setText("คะแนน :"+(scoreN));
                    ((RatingBar)findViewById(R.id.ratingBar1)).setProgress(scoreN);
                    //Log.e("score",""+(score));
    				round++;   				
    				gamestart=false;
    				checkWin();   				
    				
    			}
    			else {
    				((TextView)findViewById(R.id.timecount)).setText("ยังไม่ถูกต้อง");
    				firstCard.button.setBackgroundResource(R.drawable.memory_back);
    			}
    	    	
    	    	firstCard=null;
    			
    	    }
    } 
    
	public void checkWin(){
		if(round>12){
		counter.cancel();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	
		alertDialogBuilder.setTitle("ยินดีด้วยคุณผ่านเกมนี้แล้ว");
		alertDialogBuilder
		.setMessage("ต้องการเล่นต่อหรือไม่?")
		.setCancelable(false)
		.setPositiveButton("ใช้",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						round=1;
						scoreN=0;
						gamestart=false;
						newGame(4,3);
					}
				})
		.setNegativeButton("ไม่ใช้",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						Intent intent = new Intent(MemoryActivity.this,MainActivity.class);
	           			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	           			startActivity(intent);
	           			dialog.cancel();
	                    finish();
						
					}
				});

			AlertDialog alertDialog = alertDialogBuilder.create();			
			alertDialog.show();
	
		}else{
			newGame(4,3);
		}
	}

    public class MyCount extends CountDownTimer{
    	public MyCount(long millisInFuture, long countDownInterval) {
    	super(millisInFuture, countDownInterval);
    	}
    	@Override
    	public void onFinish() {
    		counter.cancel();
    		((TextView)findViewById(R.id.timecount)).setText("");
    	}
    	@Override
    	public void onTick(long millisUntilFinished) {
    		time-=1;
    		((TextView)findViewById(R.id.timecount)).setText(String.valueOf(time));
    	}
    	}
    
}

