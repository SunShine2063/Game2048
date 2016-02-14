package com.example.game2048;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static final String SP_KEY_BEST_SCORE = "bestscore";
	private TextView tvScore;
	private TextView bestScore;
	private static MainActivity mainActivity = null;
	private int score = 0;
	
	private LinearLayout root = null;
	private Button newGame;
	private  GamenView gameView;
	private AnimationLayer animationLayer = null;
	
	public MainActivity() 
	{
		mainActivity = this;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		root = (LinearLayout) findViewById(R.id.container);
		root.setBackgroundColor(0xfffaf8ef);
		tvScore = (TextView) findViewById(R.id.tvscore);
		bestScore = (TextView) findViewById(R.id.bestscore);
		newGame = (Button) findViewById(R.id.newgamebtn);
		gameView = (GamenView) findViewById(R.id.gameview);
		animationLayer = (AnimationLayer) findViewById(R.id.animationlayer);
		
		newGame.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				gameView.startGame();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public static MainActivity getMainActivity() 
	{
		return mainActivity;
	}
	
	public void clearScore()
	{
		score = 0;
		showScore();
	}
	
	public void showScore()
	{
		tvScore.setText(score+"");
	}
	
	public void addScore(int s)
	{
		score+=s;
		showScore();
		
		int maxScore = Math.max(score, getBestScore());
		saveBestScore(maxScore);
		showBestScore(maxScore);
	}
	public void showBestScore(int maxScore) 
	{
		bestScore.setText(maxScore+"");
	}
	private void saveBestScore(int maxScore) 
	{
		Editor editor = getPreferences(MODE_PRIVATE).edit();
		editor.putInt(SP_KEY_BEST_SCORE, maxScore);
		editor.commit();
	}
	public int getBestScore() 
	{
		return getPreferences(MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
	}
	public AnimationLayer getAnimationLayer() 
	{
		return animationLayer;
	}
	
	
}