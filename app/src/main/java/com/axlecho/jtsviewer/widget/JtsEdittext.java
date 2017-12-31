package com.axlecho.jtsviewer.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.axlecho.jtsviewer.untils.JtsViewerLog;

import java.util.List;

@CoordinatorLayout.DefaultBehavior(JtsEdittext.Behavior.class)
public class JtsEdittext extends AppCompatEditText {
    private static final String TAG = "widget";
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


    public static class Behavior extends CoordinatorLayout.Behavior<JtsEdittext> {
        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, JtsEdittext child, View dependency) {
            return dependency instanceof Snackbar.SnackbarLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, JtsEdittext child, View dependency) {
            float translationY = getFabTranslationYForSnackbar(parent, child);
            float percentComplete = -translationY / dependency.getHeight();
            float scaleFactor = 1 - percentComplete;

            child.setScaleX(scaleFactor);
            child.setScaleY(scaleFactor);
            return false;
        }


        private float getFabTranslationYForSnackbar(CoordinatorLayout parent, JtsEdittext fab) {
            float minOffset = 0;
            final List<View> dependencies = parent.getDependencies(fab);
            for (int i = 0, z = dependencies.size(); i < z; i++) {
                final View view = dependencies.get(i);
                if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                    minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - view.getHeight());
                }
            }
            return minOffset;
        }
    }
}