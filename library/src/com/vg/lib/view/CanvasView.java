package com.vg.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class CanvasView extends View {
	private OnDrawListener onDrawListener;
	
	public CanvasView(Context context) {
		super(context);
	}
	
	public CanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CanvasView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setOnDrawListener(OnDrawListener l) {
		this.onDrawListener = l;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(this.onDrawListener != null) {
			this.onDrawListener.onCanviewViewDraw(canvas);
		}
	}

	public static interface OnDrawListener {
		public void onCanviewViewDraw(Canvas canvas);
	}
}
