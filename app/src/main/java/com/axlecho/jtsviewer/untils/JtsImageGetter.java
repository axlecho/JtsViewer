package com.axlecho.jtsviewer.untils;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.axlecho.jtsviewer.network.JtsConf;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.pixplicity.htmlcompat.HtmlCompat;

import org.xml.sax.Attributes;

/**
 * Created by Administrator on 2017/11/16.
 * http://blog.csdn.net/hnxylc8818/article/details/52368939
 */

public class JtsImageGetter implements HtmlCompat.ImageGetter {
    private static final int MAX_WIDTH = 64 * 4;
    private static final String TAG = "detail";
    private TextView container;

    public JtsImageGetter(TextView v) {
        this.container = v;
    }

    @Override
    public Drawable getDrawable(String source, Attributes attributes) {
        final UrlDrawable urlDrawable = new UrlDrawable();

        if (source.contains("none.gif")) {
            source = attributes.getValue("", "file");
        }

        if (source.contains("static/image")) {
            source = JtsConf.DESKTOP_HOST_URL + "/" + source;
        }

        JtsViewerLog.d(TAG, "loading image " + source);

        SimpleTarget<GlideDrawable> target = new SimpleTarget<GlideDrawable>(300,300) {

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                JtsViewerLog.d(TAG,"load start");
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                JtsViewerLog.d(TAG,"load failed");
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {
                super.onLoadCleared(placeholder);
                JtsViewerLog.d(TAG,"load cleared");
            }


            @Override
            public void onResourceReady(GlideDrawable d, GlideAnimation<? super GlideDrawable> glideAnimation) {
                JtsViewerLog.d(TAG, "get image " + d.getIntrinsicWidth() + " * " + d.getIntrinsicHeight());

                int width = d.getIntrinsicWidth();
                int height = d.getIntrinsicHeight();

                if (d.getIntrinsicWidth() > MAX_WIDTH) {
                    height = MAX_WIDTH * height / width;
                    width = MAX_WIDTH;
                }

                d.setBounds(0, 0, width, height);
                urlDrawable.setBounds(0, 0, width, height);
                urlDrawable.drawable = d;
                container.invalidate();
                container.setText(container.getText());
            }
        };

        Glide.with(container.getContext()).load(source).into(target);
        return urlDrawable;
    }


    public class UrlDrawable extends GlideDrawable {
        public GlideDrawable drawable;

        public UrlDrawable() {
            super();
        }

        @Override
        public boolean isAnimated() {
            if (drawable != null) {
                return drawable.isAnimated();
            }
            return false;
        }

        @Override
        public void setLoopCount(int i) {
            if (drawable != null) {
                drawable.setLoopCount(i);
            }
        }

        @Override
        public void start() {
            if (drawable != null) {
                drawable.start();
            }
        }

        @Override
        public void stop() {
            if (drawable != null) {
                drawable.stop();
            }
        }

        @Override
        public boolean isRunning() {
            if (drawable != null) {
                return drawable.isRunning();
            }
            return false;
        }

        @Override
        public void setAlpha(int alpha) {
            if (drawable != null) {
                drawable.setAlpha(alpha);
            }
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            if (drawable != null) {
                drawable.setColorFilter(cf);
            }
        }

        @Override
        public int getOpacity() {
            if (drawable != null) {
                return drawable.getOpacity();
            }
            return PixelFormat.UNKNOWN;
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
                drawable.start();
            }
        }
    }
}