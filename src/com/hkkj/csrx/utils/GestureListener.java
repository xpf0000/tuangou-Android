package com.hkkj.csrx.utils;

import android.content.Context;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

/**
 * ʵ�ּ������һ������¼����ĸ�view��Ҫ��ʱ��ֱ��setOnTouchListener�Ϳ�������
 * @author LinZhiquan
 *
 */
public class GestureListener extends SimpleOnGestureListener implements OnTouchListener {
	/** ���һ�������̾��� */
	private int distance = 100;
	/** ���һ���������ٶ� */
	private int velocity = 200;
	
	private GestureDetector gestureDetector;
	
	public GestureListener(Context context) {
		super();
		gestureDetector = new GestureDetector(context, this);
	}

	/**
	 * ���󻬵�ʱ����õķ���������Ӧ����д
	 * @return
	 */
	public boolean left() {
		return false;
	}
	
	/**
	 * ���һ���ʱ����õķ���������Ӧ����д
	 * @return
	 */
	public boolean right() {
		return false;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		// e1����1��ACTION_DOWN MotionEvent
		// e2�����һ��ACTION_MOVE MotionEvent
		// velocityX��X���ϵ��ƶ��ٶȣ�����/�룩
		// velocityY��Y���ϵ��ƶ��ٶȣ�����/�룩

		// ����
		if (e1.getX() - e2.getX() > distance
				&& Math.abs(velocityX) > velocity) {
			left();
		}
		// ���һ�
		if (e2.getX() - e1.getX() > distance
				&& Math.abs(velocityX) > velocity) {
			right();
		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		gestureDetector.onTouchEvent(event);
		return false;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}
}

