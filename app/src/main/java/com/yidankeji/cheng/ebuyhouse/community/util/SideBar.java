package com.yidankeji.cheng.ebuyhouse.community.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;


/**
 * 
 * �Ҳ����ĸ����View 
 * 
 * @author 
 *
 */

public class SideBar extends View {
	
	//�����¼�
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	
	 // 26����ĸ  
//    public String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
//            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
//            "W", "X", "Y", "Z", "#" };
	 public String[] b = {};
	//ѡ��
    private int choose = -1;
	
    private Paint paint = new Paint();
    
    private TextView mTextDialog;
    
    /**
     * ΪSideBar��ʾ��ĸ��TextView
     * 
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog){
    	this.mTextDialog = mTextDialog;
    }
    

	public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SideBar(Context context) {
		super(context);
	}
	/**
	 * 
	 *
	 * 
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int singleHeight = 0;
		if(b.length!=0){
			singleHeight	 = height/b.length;
		}

		for (int i = 0; i < b.length; i++) {
			paint.setColor(Color.rgb(248, 82, 82));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(40);

			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}

			float xPos = width / 2- paint.measureText(b[i])/2;
			float yPos = singleHeight*i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		
		final int c = (int)(y / getHeight() * b.length);
		
		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_background);
			if (oldChoose != c) {
				if (c>=0 && c<b.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null) {
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					choose = c;
					invalidate();
				}
			}
			break;
		}
		
		
		
		
		return true;
	}
	/**
	 * �����ɿ��ķ���
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}
	
	/**
	 * 
	 * �ӿ�
	 * 
	 * @author 
	 *
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}


	public void reflesh(String[] b){
		this.b = b;
		invalidate();
	}

}
