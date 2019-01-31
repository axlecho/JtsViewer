package com.axlecho.jtsviewer.untils;

import android.content.Context;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.aesthetic.NavigationViewMode;
import com.axlecho.jtsviewer.R;

import androidx.core.content.ContextCompat;
import james.metronome.utils.ColorUtils;


public class JtsThemeUnitls {
    private int nameRes;
    private int colorPrimaryRes;
    private int colorAccentRes;
    private int colorBackgroundRes;
    private volatile static JtsThemeUnitls singleton;

    public static JtsThemeUnitls getSingleton() {
        if (singleton == null) {
            synchronized (JtsThemeUnitls.class) {
                if (singleton == null) {
                    singleton = new JtsThemeUnitls();
                }
            }
        }
        return singleton;
    }

    private JtsThemeUnitls() {
        this.colorPrimaryRes = R.color.colorPrimary;
        this.colorAccentRes = R.color.colorAccent;
        this.colorBackgroundRes = R.color.colorBackground;
    }

    public String getName(Context context) {
        return context.getString(nameRes);
    }

    public void apply(Context context) {
        int backgroundColor = ContextCompat.getColor(context, colorBackgroundRes);
        boolean isBackgroundDark = ColorUtils.isColorDark(backgroundColor);

        Aesthetic.get()
                
                .colorPrimary(ContextCompat.getColor(context, colorPrimaryRes), null)
                .colorAccent(ContextCompat.getColor(context, colorAccentRes), null)
                .colorWindowBackground(backgroundColor, null)
                .textColorPrimary(ContextCompat.getColor(context, isBackgroundDark ? R.color.textColorPrimary : R.color.textColorPrimaryInverse), null)
                .textColorPrimaryInverse(ContextCompat.getColor(context, isBackgroundDark ? R.color.textColorPrimaryInverse : R.color.textColorPrimary), null)
                .textColorSecondary(ContextCompat.getColor(context, isBackgroundDark ? R.color.textColorSecondary : R.color.textColorSecondaryInverse), null)
                .textColorSecondaryInverse(ContextCompat.getColor(context, isBackgroundDark ? R.color.textColorSecondaryInverse : R.color.textColorSecondary), null)
                .colorStatusBarAuto()
                .navigationViewMode(NavigationViewMode.SELECTED_ACCENT)
                .toolbarIconColor(ContextCompat.getColor(context,isBackgroundDark ? R.color.textColorSecondaryInverse : R.color.textColorSecondary),null)
                .apply();
    }
}
