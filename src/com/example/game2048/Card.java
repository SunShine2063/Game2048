package com.example.game2048;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout
{
	private int num;
	//���ÿ�Ƭ�е�����
	private TextView lable;
	private View background;
	
	public Card(Context context) {
		super(context);
		LayoutParams params = null;
		//���ֲ�����-1��-1����������������������
		params = new LayoutParams(-1, -1);
		//���ÿ�Ƭ֮����10����
		params.setMargins(10, 10, 0, 0);
		background = new View(getContext());
		background.setBackgroundColor(0x33ffffff);
		addView(background,params);
		
		lable = new TextView(getContext());
		lable.setTextSize(28);
		lable.setGravity(Gravity.CENTER);//���ı��������м�
		params = new LayoutParams(-1, -1);
		params.setMargins(10, 10, 0, 0);
		addView(lable, params);
		
		setNum(0);
	}

	public int getNum() 
	{
		return num;
	}

	public void setNum(int num)
	{
		this.num = num;
		if(num <= 0)
		{
			lable.setText("");
		}else{
			lable.setText(num+"");
		}
		//����ͬ���ֵĿ�Ƭ������ɫ
		switch(num)
		{
		case 0:
			lable.setBackgroundColor(0x00000000);
			break;
		case 2:
			lable.setBackgroundColor(0xffeee4da);
			break;
		case 4:
			lable.setBackgroundColor(0xffede0c8);
			break;
		case 8:
			lable.setBackgroundColor(0xfff2b179);
			break;
		case 16:
			lable.setBackgroundColor(0xfff59563);
			break;
		case 32:
			lable.setBackgroundColor(0xfff67c5f);
			break;
		case 64:
			lable.setBackgroundColor(0xfff65e3b);
			break;
		case 128:
			lable.setBackgroundColor(0xffedcf72);
			break;
		case 256:
			lable.setBackgroundColor(0xffedcc61);
			break;
		case 512:
			lable.setBackgroundColor(0xffedc850);
			break;
		case 1024:
			lable.setBackgroundColor(0xffedc53f);
			break;
		case 2048:
			lable.setBackgroundColor(0xffedc22e);
			break;
		default:
			lable.setBackgroundColor(0xff3c3a32);
				break;
		}
	}
	
	//�ж����ſ�Ƭ�е��ı������Ƿ���ͬ����ͬ������۵�
	public boolean equals(Card o) {
		return getNum() == o.getNum();
	}

	public TextView getLable() {
		return lable;
	}
	
	protected Card clone()
	{
		Card card = new Card(getContext());
		card.setNum(getNum());
		return card;
	}
}
