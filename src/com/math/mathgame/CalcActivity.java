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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class CalcActivity extends Activity {
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
	static long scoreN=0;
	private boolean gamestart=false;
	MyCount counter = new MyCount(30000,1000);
	private drawTextToBitmap drawTextToBitmap=new drawTextToBitmap();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        handler = new UpdateCardsHandler();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calc); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
          
        getResources().getDrawable(R.drawable.calc_back);
        buttonListener = new ButtonListener();        
        mainTable = (TableLayout)findViewById(R.id.TableLayout01);       
        context  = mainTable.getContext();
        newGame(4,3);
        ((Button)findViewById(R.id.btnstart)).setOnClickListener(new OnClickListener() {	
    		@Override
    		public void onClick(View v) {
    			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	
    			alertDialogBuilder.setTitle("�Ըա�����");
    			alertDialogBuilder
    			.setMessage("����������")
    			.setCancelable(true); 
    			AlertDialog alertDialog = alertDialogBuilder.create();			
    			alertDialog.show();    					
    		}		
           });
       ((Button)findViewById(R.id.ButtonNew)).setOnClickListener(new OnClickListener() {	
		@Override
		public void onClick(View v) {
			round=1;
			scoreN=0;
			gamestart=false;
			newGame(4,3);			
		}		
       });
       ((Button)findViewById(R.id.ButtonEnd)).setOnClickListener(new OnClickListener() {	
   		@Override
   		public void onClick(View v) {
   			Intent intent = new Intent(CalcActivity.this,MainActivity.class);
   			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   			startActivity(intent);
   			counter.cancel();
            finish();
   		}		
          });
    }
    
    private void newGame(int c, int r) {
    	ROW_COUNT = r;
    	COL_COUNT = c;
    	
    	cards = new int [COL_COUNT] [ROW_COUNT];
    
    	
    	TableRow tr = ((TableRow)findViewById(R.id.tableRow2));
    	tr.removeAllViews();
    	final TextView question = ((TextView)findViewById(R.id.question));
    	final TextView score = ((TextView)findViewById(R.id.score));
    	score.setText("��ṹ :"+(scoreN));
    	
    	question.setText("");
    	mainTable = new TableLayout(context);
    	tr.addView(mainTable);
    	loadImages();
    	 for (int y = 0; y < ROW_COUNT; y++) {
    		 mainTable.addView(createRow(y));
          }
    	 
    	firstCard=null; 
    	question.setText(String.format( "%d x ? = %d", round,ans*round ));  	
     	gamestart=true;
     	time=30;
     	counter.cancel();
     	counter.start();
     		
	}
    public ArrayList<Integer> randN(){
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 1; i <= 12; i++) {
        	numbers.add(i);
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
    	for(int i=size-1;i>=0;i--){
    		cards[i%COL_COUNT][i/COL_COUNT]=images.get(i);
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
    	Bitmap bmp =drawTextToBitmap.draw(context,R.drawable.calc_front,String.valueOf(cards[x][y]),Color.BLUE);
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
				int id = v.getId();
				int x = id/100;
				int y = id%100;
				turnCard((Button)v,x,y);

			
		}

		@SuppressWarnings("deprecation")
		private void turnCard(Button button,int x, int y) {
			Bitmap bmp =drawTextToBitmap.draw(context,R.drawable.calc_back,String.valueOf(cards[x][y]),Color.BLUE);			
			button.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bmp));
			final TextView question = ((TextView)findViewById(R.id.question));
            question.setText(String.format( "%d x %d = %d", round,cards[x][y],ans*round ));
			Log.e("turnCard",String.valueOf(cards[x][y]));
			
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
				  timer.schedule(timetask, 100);
				  
			
				
		   }
			
		}
    

    public void checkWin(){
		if(scoreN>=20){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	
			alertDialogBuilder.setTitle("�Թ�մ��¤س��ҹ���������");
			alertDialogBuilder
			.setMessage("��ͧ�����蹵���������?")
			.setCancelable(false)
			.setPositiveButton("��",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {		
			round=1;
			scoreN=0;
			gamestart=false;
			newGame(4,3);
		}
		})
			.setNegativeButton("�����",
					new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
	                	Intent intent = new Intent(CalcActivity.this,MainActivity.class);
	           			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	           			startActivity(intent);
	                    dialog.cancel();
	                    counter.cancel();
	                    finish();
		}
		}); 
			AlertDialog alertDialog = alertDialogBuilder.create();			
			alertDialog.show();
		}else{
			newGame(4,3);
		}
	}
    public void gameLose(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	
		alertDialogBuilder.setTitle("������� �س�Ӥ�ṹ��� "+scoreN);
		alertDialogBuilder
		.setMessage("��ͧ�����蹵���������?")
		.setCancelable(false)
		.setPositiveButton("��",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						round=1;
						scoreN=0;
						gamestart=false;
						newGame(5,2);
					}
				})
		.setNegativeButton("�����",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						Intent intent = new Intent(CalcActivity.this,MainActivity.class);
	           			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	           			startActivity(intent);
	           			dialog.cancel();
	                    counter.cancel();
	                    finish();
					}
				});

			AlertDialog alertDialog = alertDialogBuilder.create();			
			alertDialog.show();

	}
        class UpdateCardsHandler extends Handler{
    	
    	@Override
    	public void handleMessage(Message msg) {
    		
    			checkCards();

    	}
    	@SuppressWarnings("deprecation")
		public void checkCards(){
    		 //Log.i("checkCards()", "card["+(firstCard.x)+"]["+(firstCard.y)+"]="+cards[firstCard.x][firstCard.y] );
    	    	if(ans == cards[firstCard.x][firstCard.y]){                 
                    scoreN +=2;
                    final TextView score = ((TextView)findViewById(R.id.score));
                    score.setText("��ṹ :"+(scoreN));
                    
                    //Log.e("score",""+(score));
    				round++;   				
    				gamestart=false;
    				checkWin();   				
    				
    			}
    			else {
    				
    				for(int i=0;i<bb.size();i++){
    					Bitmap bmp =drawTextToBitmap.draw(context,R.drawable.calc_front,String.valueOf(cards[bb.get(i).x][bb.get(i).y]),Color.BLUE);        				
        				bb.get(i).button.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bmp));
         	    	}
    				final TextView question = ((TextView)findViewById(R.id.question));
    	            question.setText(String.format( "%d x ? = %d", round,ans*round ));
    			}
    	    	
    	    	firstCard=null;
    			
    	    }
    }
        public class MyCount extends CountDownTimer{
        	public MyCount(long millisInFuture, long countDownInterval) {
        	super(millisInFuture, countDownInterval);
        	}
        	@Override
        	public void onFinish() {
        		gameLose();
        	}
        	@Override
        	public void onTick(long millisUntilFinished) {
        		ProgressBar progressBar =((ProgressBar)findViewById(R.id.progressBar1));
				progressBar.setProgress(time-=1);
        	}
        	}
        	
    
}
