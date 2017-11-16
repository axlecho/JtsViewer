package com.axlecho.jtsviewer.untils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by Administrator on 2017/11/16.
 * http://blog.csdn.net/hnxylc8818/article/details/52368939
 */

public class JtsImageGetter implements Html.ImageGetter {
    private static final String TAG = "detail";
    private URLDrawable urlDrawable;
    private TextView textView;
    private Context context;

    public JtsImageGetter(Context context, TextView textView) {
        this.textView = textView;
        this.context = context;
    }

    @Override
    public Drawable getDrawable(final String source) {
        urlDrawable = new URLDrawable();

        Glide.with(context).load(source).asBitmap().fitCenter().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                JtsViewerLog.d(TAG, "loading thread picture");
                urlDrawable.bitmap = resource;
                urlDrawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                textView.invalidate();
            }
        });
        return urlDrawable;
    }

    public class URLDrawable extends BitmapDrawable {
        public Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }
}