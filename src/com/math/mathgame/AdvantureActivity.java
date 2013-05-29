package com.math.mathgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class AdvantureActivity extends Activity {
    private static int ROW_COUNT = -1;
	private static int COL_COUNT = -1;
	private Context context;
	private int [] [] cards;
	private int ans,item,peaces;
	private Card firstCard;
	ArrayList<Card> bb = new ArrayList<Card>();
	private ButtonListener buttonListener;
	private TableLayout mainTable;
	private UpdateCardsHandler handler;
	static long scoreN=0;
	private List<Integer> fruitimages;
	private drawTextToBitmap drawTextToBitmap=new drawTextToBitmap();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        handler = new UpdateCardsHandler();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advanture); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
          
        getResources().getDrawable(R.drawable.advanture_back);
        buttonListener = new ButtonListener();        
        mainTable = (TableLayout)findViewById(R.id.TableLayout01);       
        context  = mainTable.getContext();
    	fruitimages = new ArrayList<Integer>();
    	fruitimages.add(R.drawable.advanture_card1);
    	fruitimages.add(R.drawable.advanture_card2);
    	fruitimages.add(R.drawable.advanture_card6);
    	fruitimages.add(R.drawable.advanture_card7);
    	fruitimages.add(R.drawable.advanture_card8);
    	fruitimages.add(R.drawable.advanture_card10);
    	fruitimages.add(R.drawable.advanture_card18);
	
        newGame(5,2);
        ((Button)findViewById(R.id.btnstart)).setOnClickListener(new OnClickListener() {	
    		@Override
    		public void onClick(View v) {
    			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	
    			alertDialogBuilder.setTitle("วิธีการเล่น");
    			alertDialogBuilder
    			.setMessage("กดกดกดเข้าไป")
    			.setCancelable(true); 
    			AlertDialog alertDialog = alertDialogBuilder.create();			
    			alertDialog.show();    					
    		}		
           });
       ((Button)findViewById(R.id.ButtonNew)).setOnClickListener(new OnClickListener() {	
		@Override
		public void onClick(View v) {
			scoreN=0;
			newGame(5,2);			
		}		
       });
       ((Button)findViewById(R.id.ButtonEnd)).setOnClickListener(new OnClickListener() {	
   		@Override
   		public void onClick(View v) {
   			Intent intent = new Intent(AdvantureActivity.this,MainActivity.class);
   			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   			startActivity(intent);
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
    	
    	
    	final TextView score = ((TextView)findViewById(R.id.score));
    	score.setText("คะแนน :"+(scoreN));    	
    	
    	mainTable = new TableLayout(context);
    	tr.addView(mainTable);
    	loadImages();
    	
    	 for (int y = 0; y < ROW_COUNT; y++) {
    		 mainTable.addView(createRow(y));
          }
    	 
    	firstCard=null; 

     		
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

    	ArrayList<Integer> number = new ArrayList<Integer>(); 
    	
    	number=randN();
    	item=number.get(0);
    	peaces=	number.get(1);
    	
    	final GridView gView1 = (GridView)findViewById(R.id.GridView1);
    	gView1.setAdapter(new ImageAdapter(this));
    	
    	
    	final TextView question = ((TextView)findViewById(R.id.question));
    	question.setText(String.format( "ขนม 1 ถุง มีขนม %d ชิ้น\nจำนวน %d ถุง", number.get(0),number.get(1) ));
    	
    	final TextView answer = ((TextView)findViewById(R.id.answer));
		answer.setText("มีขนมรวมกันทั้งหมด ? ชิ้น");
		
    	int size = ROW_COUNT*COL_COUNT;
    	ans=number.get(0)*number.get(1);
    	
    	ArrayList<Integer> numberN = new ArrayList<Integer>();
    	for(int i=ans;i<=144;i++){
    		numberN.add(i);
    	}
    	Collections.shuffle(numberN);
    	
    	boolean found=false;
    	do{
    		found=false;
    		Collections.shuffle(numberN);
    		for(int i=0;i<=8;i++){
    		if(numberN.get(i)==ans){
    			found=true;
    			break;
    		}
    		}
    		
    	Log.e("Loop",""+(1));	
    	}while(found);
    	
    	numberN.add(0, ans);
    	ArrayList<Integer> ansN = new ArrayList<Integer>();
    	for(int i=0;i<=9;i++){
    		ansN.add(numberN.get(i));
    	}
    	Collections.shuffle(ansN);
    	for(int i=size-1;i>=0;i--){
    		cards[i%COL_COUNT][i/COL_COUNT]=ansN.get(i);
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
    	Bitmap bmp =drawTextToBitmap.draw(context,R.drawable.advanture_front,String.valueOf(cards[x][y]),Color.BLUE);
		button.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bmp));
    	button.setId(100*x+y);
    	button.setOnClickListener(buttonListener);
    	bb.add(new Card(button,x,y));
    	return button;
    }
    
    class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
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
			Bitmap bmp =drawTextToBitmap.draw(context,R.drawable.advanture_back,String.valueOf(cards[x][y]),Color.BLUE);			
			button.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bmp));
			final TextView answer = ((TextView)findViewById(R.id.answer));
			answer.setText(String.format( "มีขนมรวมกันทั้งหมด %d ชิ้น", cards[x][y] ));
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
			alertDialogBuilder.setTitle("ยินดีด้วยคุณผ่านเกมนี้แล้ว และได้คะแนน "+scoreN);
			alertDialogBuilder
			.setMessage("ต้องการเล่นต่อหรือไม่?")
			.setCancelable(false)
			.setPositiveButton("ใช้",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {		
							scoreN=0;
							newGame(5,2);
		}
		})
			.setNegativeButton("ไม่ใช้",
					new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
	                	Intent intent = new Intent(AdvantureActivity.this,MainActivity.class);
	           			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	           			startActivity(intent);
	                    dialog.cancel();
	                    finish();
		}
		}); 
			AlertDialog alertDialog = alertDialogBuilder.create();			
			alertDialog.show();
		}else{
			newGame(5,2);
		}
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
                    score.setText("คะแนน :"+(scoreN));
                    
                    //Log.e("score",""+(score));  				
    				checkWin();   				
    				
    			}
    			else {
    				
    				for(int i=0;i<bb.size();i++){
    					Bitmap bmp =drawTextToBitmap.draw(context,R.drawable.advanture_front,String.valueOf(cards[bb.get(i).x][bb.get(i).y]),Color.BLUE);        				
        				bb.get(i).button.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bmp));
         	    	}
    				final TextView answer = ((TextView)findViewById(R.id.answer));
    				answer.setText("มีขนมรวมกันทั้งหมด ? ชิ้น");
    			}
    	    	
    	    	firstCard=null;
    			
    	    }
    }
        public class ImageAdapter extends BaseAdapter 
        {
            private Context context;
            
            public ImageAdapter(Context c) 
            {
                context = c;
            }
     
            public int getCount() {
                return peaces;
            }
     
            public Object getItem(int position) {
                return position;
            }
     
            public long getItemId(int position) {
                return position;
            }
     
    		public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView;
                //imageView
                if (convertView == null) {
                    imageView = new ImageView(context);
                    imageView.setLayoutParams(new GridView.LayoutParams(64, 64));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    
                } else {
                    imageView = (ImageView) convertView;
                }
                Collections.shuffle(fruitimages);
                Bitmap bmp =drawTextToBitmap.draw(context,fruitimages.get(0),String.valueOf(item),Color.BLACK);
                imageView.setImageBitmap(bmp);
                return imageView;
    		}
        }
        	
    
}
