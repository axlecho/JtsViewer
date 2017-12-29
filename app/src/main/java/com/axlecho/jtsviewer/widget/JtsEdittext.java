package com.axlecho.jtsviewer.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class JtsEdittext extends AppCompatEditText {
    private OnCompoundDrawableClickListener compoundDrawableClickListener;

    public JtsEdittext(Context context) {
        super(context);
    }

    public JtsEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JtsEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if (event.getAction() == MotionEvent.ACTION_UP && this.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {

            Rect rBounds = this.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds();
            final int x = (int) event.getX();
            final int y = (int) event.getY();

            if (x >= (this.getRight() - rBounds.width()) && x <= (this.getRight() - this.getPaddingRight())
                    && y >= this.getPaddingTop() && y <= (this.getHeight() - this.getPaddingBottom())) {
                if (compoundDrawableClickListener != null) {
                    compoundDrawableClickListener.onClick();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setCompoundDrawableClickListener(OnCompoundDrawableClickListener compoundDrawableClickListener) {
        this.compoundDrawableClickListener = compoundDrawableClickListener;
    }

    public interface OnCompoundDrawableClickListener {
        void onClick();
    }
}