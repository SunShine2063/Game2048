package com.example.game2048;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class GamenView extends GridLayout 
{
	//手指触摸的起始点
	private float startX;
	private float startY;
	//手指滑动的偏移坐标
	private float offsetX;
	private float offsetY;
	
	private Card[][] cardMap = new Card[Config.LINES][Config.LINES];
	private List<Point> emptyPoints = new ArrayList<>();
	
	public GamenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initGame();
	}

	public GamenView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initGame();
	}

	private void initGame()
	{
		setOrientation(LinearLayout.VERTICAL);
		//设置GridLayout的背景颜色
		setBackgroundColor(0xffbbada0);
		
		//手指触摸屏幕的监听事件
		setOnTouchListener(new View.OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch(event.getAction())
				{
				case MotionEvent.ACTION_DOWN:   //手指按下的时候
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:		//手指离开的时候
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;
					if(Math.abs(offsetX) > Math.abs(offsetY))
					{
						if(offsetX < -5)
						{
							slideLeft();
						}else if(offsetX > 5)
						{
							slideRight();
						}
					}else{
						if(offsetY < -5)
						{
							slideUp();
						}else if(offsetY > 5)
						{
							slideDown();
						}
					}
					break;
					default:break;
				}
				return true;
			}
		});
	}
	/**
	 * 动态计算手机宽高，这样可以适配任何尺寸的手机
	 * 当宽高发生改变时才会调用
	 * 只有手机直立的时候才会调用，水平时不会调用
	 * 
	 * 一般手机屏幕是长方形的，这里求出宽高的最小值，以最小值来计算卡片的宽高
	 * 减10是让卡片和手机屏幕边缘流出10像素的距离
	 * 除4，每一行放4张卡片，求出每张卡片的宽度
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		Config.CARD_WIDTH = (Math.min(w, h)-10)/Config.LINES;
		addCards(Config.CARD_WIDTH, Config.CARD_WIDTH);			//卡片是正方形的，宽高相同
		startGame();
	}
	/**
	 * 游戏开始前先将所有卡片清0
	 * 清0之后再随机产生两张卡片
	 */
	public void startGame()
	{
		MainActivity mainActivity =  MainActivity.getMainActivity();
		mainActivity.clearScore();
		mainActivity.showBestScore(mainActivity.getBestScore());
		
		for(int y = 0; y < Config.LINES; y++)
		{
			for(int x = 0; x < Config.LINES; x++)
			{
				cardMap[x][y].setNum(0);
			}
		}
		addRandom();
		addRandom();
	}
	/**
	 * 添加一个4*4矩阵的卡片
	 * @param cardWidth：卡片的宽度
	 * @param cardHeight：卡片的高度
	 */
	private void addCards(int cardWidth,int cardHeight)
	{
		Card card;
		LinearLayout line;
		LinearLayout.LayoutParams lineParams;
		for(int y = 0; y < Config.LINES; y++)
		{
			line = new LinearLayout(getContext());
			lineParams = new LinearLayout.LayoutParams(-1, cardHeight);
			addView(line, lineParams);
			for(int x = 0; x < Config.LINES; x++)
			{
				card = new Card(getContext());
				line.addView(card,cardWidth, cardHeight);
				cardMap[x][y] = card;
			}
		}
	}
	/**
	 * 添加随机数
	 */
	private void addRandom()
	{
		emptyPoints.clear();
		for(int y = 0; y < Config.LINES; y++)
		{
			for(int x = 0;  x < Config.LINES; x++)
			{
				//先判断这个点上有没有数字，没有将当做空点添加进emptyPoints集合中
				if(cardMap[x][y].getNum() <= 0)
				{
					emptyPoints.add(new Point(x,y));
				}
			}
		}
		Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));	//随机从空点集合中移除一个点并把这个点上的卡片设置数字
		cardMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);	//随机产生一个数字，判断这个数字是否大0.1，这样保证2和4随机出现的比例是1：9
		MainActivity.getMainActivity().getAnimationLayer().createScaleTo1(cardMap[p.x][p.y]);	//卡片出来的动画效果是缩放
	}
	/**
	 * 向左滑
	 */
	private void slideLeft()
	{
		//标志量。判断是否要添加卡片
		boolean meger = false;
		for(int y = 0; y < Config.LINES; y++)
		{
			for(int x = 0; x < Config.LINES ;x++)
			{
				for(int x1=x+1; x1 < Config.LINES; x1++)
				{
					if(cardMap[x1][y].getNum() > 0)
					{
						if(cardMap[x][y].getNum() <= 0)
						{
							MainActivity.getMainActivity().getAnimationLayer().createMoveAnimation(cardMap[x1][y], 
									cardMap[x][y], x1, x, y, y);
							
							cardMap[x][y].setNum(cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);
							x--;
							meger = true;
						}else if(cardMap[x][y].equals(cardMap[x1][y])){
							MainActivity.getMainActivity().getAnimationLayer().createMoveAnimation(cardMap[x1][y], 
									cardMap[x][y], x1, x, y, y);
							cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
							cardMap[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
							meger = true;
						}
						break;
					}
				}
			}
		}
		if(meger)
		{
			addRandom();
			checkComplete();
		}
	}
	/**
	 * 向右滑
	 */
	private void slideRight()
	{
		boolean meger = false;
		for(int y = 0; y < Config.LINES; y++)
		{
			for(int x = Config.LINES-1; x >= 0 ;x--)
			{
				for(int x1=x-1; x1 >= 0; x1--)
				{
					if(cardMap[x1][y].getNum() > 0)
					{
						if(cardMap[x][y].getNum() <= 0)
						{
							MainActivity.getMainActivity().getAnimationLayer().createMoveAnimation(cardMap[x1][y], 
									cardMap[x][y], x1, x, y, y);
							cardMap[x][y].setNum(cardMap[x1][y].getNum());
							cardMap[x1][y].setNum(0);
							x++;
							meger = true;
						}else if(cardMap[x][y].equals(cardMap[x1][y])){
							MainActivity.getMainActivity().getAnimationLayer().createMoveAnimation(cardMap[x1][y], 
									cardMap[x][y], x1, x, y, y);
							cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
							cardMap[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
							meger = true;
						}
						break;
					}
				}
			}
		}
		if(meger)
		{
			addRandom();
			checkComplete();
		}
	}
	/**
	 * 向上滑
	 */
	private void slideUp()
	{
		boolean meger = false;
		for(int x = 0; x < Config.LINES; x++)
		{
			for(int y = 0; y < Config.LINES ;y++)
			{
				for(int y1=y+1; y1 < Config.LINES; y1++)
				{
					if(cardMap[x][y1].getNum() > 0)
					{
						if(cardMap[x][y].getNum() <= 0)
						{
							MainActivity.getMainActivity().getAnimationLayer().createMoveAnimation(cardMap[x][y1], 
									cardMap[x][y], x, x, y1, y);
							cardMap[x][y].setNum(cardMap[x][y1].getNum());
							cardMap[x][y1].setNum(0);
							y--;
							meger = true;
						}else if(cardMap[x][y].equals(cardMap[x][y1])){
							MainActivity.getMainActivity().getAnimationLayer().createMoveAnimation(cardMap[x][y1], 
									cardMap[x][y], x, x, y1, y);
							cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
							cardMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
							meger = true;
						}
						break;
					}
				}
			}
		}
		if(meger)
		{
			addRandom();
			checkComplete();
		}
	}
	/**
	 * 向下滑
	 */
	private void slideDown()
	{
		boolean meger = false;
		for(int x = 0; x < Config.LINES; x++)
		{
			for(int y = Config.LINES-1; y >= 0 ;y--)
			{
				for(int y1=y-1; y1 >= 0; y1--)
				{
					if(cardMap[x][y1].getNum() > 0)
					{
						if(cardMap[x][y].getNum() <= 0)
						{
							MainActivity.getMainActivity().getAnimationLayer().createMoveAnimation(cardMap[x][y1], 
									cardMap[x][y], x, x, y1, y);
							cardMap[x][y].setNum(cardMap[x][y1].getNum());
							cardMap[x][y1].setNum(0);
							y++;
							meger = true;
						}else if(cardMap[x][y].equals(cardMap[x][y1])){
							MainActivity.getMainActivity().getAnimationLayer().createMoveAnimation(cardMap[x][y1], 
									cardMap[x][y], x, x, y1, y);
							cardMap[x][y].setNum(cardMap[x][y].getNum()*2);
							cardMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
							meger = true;
						}
						break;
					}
				}
			}
		}
		if(meger)
		{
			addRandom();
			checkComplete();
		}
	}
	/**
	 * 检查游戏是否退出
	 */
	public void checkComplete()
	{
		boolean complete = true;
		ALL:
		for(int y = 0; y < Config.LINES; y++)
		{
			for(int x = 0; x < Config.LINES; x++)
			{
				if(cardMap[x][y].getNum() == 0 ||
						(x>0 && x<Config.LINES-1 && y>0 && y<Config.LINES-1 && cardMap[x][y].equals(cardMap[x-1][y])) ||
						(x>0 && x<Config.LINES-1 && y>0 && y<Config.LINES-1 && cardMap[x][y].equals(cardMap[x][y-1])) ||
						(x>0 && x<Config.LINES-1 && y>0 && y<Config.LINES-1 && cardMap[x][y].equals(cardMap[x+1][y])) ||
						(x>0 && x<Config.LINES-1 && y>0 && y<Config.LINES-1 && cardMap[x][y].equals(cardMap[x][y+1])) ||
						(x==0 && y==0 && cardMap[x][y].equals(cardMap[x+1][y])) ||
						(x==0 && y==0 && cardMap[x][y].equals(cardMap[x][y+1])) ||
						(x==0 && y==Config.LINES-1 && cardMap[x][y].equals(cardMap[x][y-1])) ||
						(x==0 && y==Config.LINES-1 && cardMap[x][y].equals(cardMap[x+1][y])) ||
						(x==Config.LINES-1 && y==0 && cardMap[x][y].equals(cardMap[x-1][y])) ||
						(x==Config.LINES-1 && y==0 && cardMap[x][y].equals(cardMap[x][y+1])) ||
						(x==Config.LINES-1 && y==Config.LINES-1 && cardMap[x][y].equals(cardMap[x-1][y])) ||
						(x==Config.LINES-1 && y==Config.LINES-1 && cardMap[x][y].equals(cardMap[x][y-1])) ||
						(x==0 && y>0 && y<Config.LINES-1 && cardMap[x][y].equals(cardMap[x][y-1])) ||
						(x==0 && y>0 && y<Config.LINES-1 && cardMap[x][y].equals(cardMap[x+1][y])) ||
						(x==0 && y>0 && y<Config.LINES-1 && cardMap[x][y].equals(cardMap[x][y+1])) ||
						(x==Config.LINES-1 && y>0 && y<Config.LINES-1 && cardMap[x][y].equals(cardMap[x][y-1])) ||
						(x==Config.LINES-1 && y>0 && y<Config.LINES-1 && cardMap[x][y].equals(cardMap[x-1][y])) ||
						(x==Config.LINES-1 && y>0 && y<Config.LINES-1 && cardMap[x][y].equals(cardMap[x][y+1])) ||
						(x>0 && x<Config.LINES-1 && y==0 && cardMap[x][y].equals(cardMap[x-1][y])) ||
						(x>0 && x<Config.LINES-1 && y==0 && cardMap[x][y].equals(cardMap[x+1][y])) ||
						(x>0 && x<Config.LINES-1 && y==0 && cardMap[x][y].equals(cardMap[x][y+1])) ||
						(x>0 && x<Config.LINES-1 && y==Config.LINES-1 && cardMap[x][y].equals(cardMap[x-1][y])) ||
						(x>0 && x<Config.LINES-1 && y==Config.LINES-1 && cardMap[x][y].equals(cardMap[x+1][y])) ||
						(x>0 && x<Config.LINES-1 && y==Config.LINES-1 && cardMap[x][y].equals(cardMap[x][y-1])))
				{
					complete = false;
					break ALL;
				}
			}
		}
		if(complete)
		{
			new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("Game Over").setPositiveButton("重来", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startGame();
				}
			}).show();
		}
	}
}
