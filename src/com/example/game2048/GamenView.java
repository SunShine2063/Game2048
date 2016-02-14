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
	//��ָ��������ʼ��
	private float startX;
	private float startY;
	//��ָ������ƫ������
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
		//����GridLayout�ı�����ɫ
		setBackgroundColor(0xffbbada0);
		
		//��ָ������Ļ�ļ����¼�
		setOnTouchListener(new View.OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch(event.getAction())
				{
				case MotionEvent.ACTION_DOWN:   //��ָ���µ�ʱ��
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:		//��ָ�뿪��ʱ��
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
	 * ��̬�����ֻ���ߣ��������������κγߴ���ֻ�
	 * ����߷����ı�ʱ�Ż����
	 * ֻ���ֻ�ֱ����ʱ��Ż���ã�ˮƽʱ�������
	 * 
	 * һ���ֻ���Ļ�ǳ����εģ����������ߵ���Сֵ������Сֵ�����㿨Ƭ�Ŀ��
	 * ��10���ÿ�Ƭ���ֻ���Ļ��Ե����10���صľ���
	 * ��4��ÿһ�з�4�ſ�Ƭ�����ÿ�ſ�Ƭ�Ŀ��
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		Config.CARD_WIDTH = (Math.min(w, h)-10)/Config.LINES;
		addCards(Config.CARD_WIDTH, Config.CARD_WIDTH);			//��Ƭ�������εģ������ͬ
		startGame();
	}
	/**
	 * ��Ϸ��ʼǰ�Ƚ����п�Ƭ��0
	 * ��0֮��������������ſ�Ƭ
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
	 * ���һ��4*4����Ŀ�Ƭ
	 * @param cardWidth����Ƭ�Ŀ��
	 * @param cardHeight����Ƭ�ĸ߶�
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
	 * ��������
	 */
	private void addRandom()
	{
		emptyPoints.clear();
		for(int y = 0; y < Config.LINES; y++)
		{
			for(int x = 0;  x < Config.LINES; x++)
			{
				//���ж����������û�����֣�û�н������յ���ӽ�emptyPoints������
				if(cardMap[x][y].getNum() <= 0)
				{
					emptyPoints.add(new Point(x,y));
				}
			}
		}
		Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));	//����ӿյ㼯�����Ƴ�һ���㲢��������ϵĿ�Ƭ��������
		cardMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);	//�������һ�����֣��ж���������Ƿ��0.1��������֤2��4������ֵı�����1��9
		MainActivity.getMainActivity().getAnimationLayer().createScaleTo1(cardMap[p.x][p.y]);	//��Ƭ�����Ķ���Ч��������
	}
	/**
	 * ����
	 */
	private void slideLeft()
	{
		//��־�����ж��Ƿ�Ҫ��ӿ�Ƭ
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
	 * ���һ�
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
	 * ���ϻ�
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
	 * ���»�
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
	 * �����Ϸ�Ƿ��˳�
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
			new AlertDialog.Builder(getContext()).setTitle("���").setMessage("Game Over").setPositiveButton("����", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startGame();
				}
			}).show();
		}
	}
}
