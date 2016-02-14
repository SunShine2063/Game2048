package com.example.game2048;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

//给卡片添加动画效果
public class AnimationLayer extends FrameLayout
{
	private List<Card> cards = new ArrayList<>();
	
	public AnimationLayer(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initLayer();
	}

	public AnimationLayer(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		initLayer();
	}

	public AnimationLayer(Context context)
	{
		super(context);
		initLayer();
	}
	
	private void initLayer()
	{
		
	}
	public void createMoveAnimation(final Card from,final Card to,int fromX,int toX,int fromY,int toY)
	{
		final Card card = getCard(from.getNum());
		
		LayoutParams lp = new LayoutParams(Config.CARD_WIDTH,Config.CARD_WIDTH);
		lp.leftMargin = fromX*Config.CARD_WIDTH;
		lp.topMargin = fromY*Config.CARD_WIDTH;
		card.setLayoutParams(lp);
		
		if(to.getNum() <= 0)
		{
			to.getLable().setVisibility(View.INVISIBLE);
		}
		
		TranslateAnimation ta = new TranslateAnimation(0, (toX-fromX)*Config.CARD_WIDTH, 0, (toY-fromY)*Config.CARD_WIDTH);
		ta.setDuration(100);
		ta.setAnimationListener(new Animation.AnimationListener() 
		{
			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationEnd(Animation animation) 
			{
				to.getLable().setVisibility(View.VISIBLE);
				recycleCard(card);
			}
		});
		card.startAnimation(ta);
	}

	protected void recycleCard(Card card)
	{
		card.setVisibility(View.INVISIBLE);
		card.setAnimation(null);
		cards.add(card);
	}

	private Card getCard(int num) 
	{
		Card card;
		if(cards.size() > 0)
		{
			card = cards.remove(0);
		}else{
			card = new Card(getContext());
			addView(card);
		}
		card.setVisibility(View.VISIBLE);
		card.setNum(num);
		return card;
	}
	
	public void createScaleTo1(Card target)
	{
		ScaleAnimation scale = new ScaleAnimation(0.1f, 1, 0.1f, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(100);
		target.setAnimation(null);
		target.startAnimation(scale);
	}
}
