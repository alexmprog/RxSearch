package com.renovavision.rxsearch.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.renovavision.rxsearch.R;

public class TintedProgressBar extends ProgressBar {

    public TintedProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public TintedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TintedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.TintedProgressBar);

            if (a.hasValue(R.styleable.TintedProgressBar_tintColor)) {

                @ColorInt
                int defaultColor = getColor(context, R.color.white);
                int tintColor = a.getColor(R.styleable.TintedProgressBar_tintColor, defaultColor);
                int tintMode = a.getInt(R.styleable.TintedProgressBar_tintMode, 0);
                PorterDuff.Mode mode = parseTintMode(tintMode, PorterDuff.Mode.SRC_IN);

                Drawable indeterminateDrawable = getIndeterminateDrawable();
                if (indeterminateDrawable != null && tintColor != -1 && mode != null) {
                    indeterminateDrawable.setColorFilter(tintColor, mode);
                }

                Drawable progressDrawable = getProgressDrawable();
                if (progressDrawable != null && tintColor != -1 && mode != null) {
                    progressDrawable.setColorFilter(tintColor, mode);
                }
            } else {
                clearColorFilters();
            }
            a.recycle();
        } else {
            clearColorFilters();
        }
    }

    @ColorInt
    @TargetApi(16)
    @SuppressWarnings("deprecation")
    public int getColor(@NonNull Context context, @ColorRes int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(colorId);
        }

        return context.getResources().getColor(colorId);
    }

    private void clearColorFilters() {
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable != null) {
            indeterminateDrawable.clearColorFilter();
        }

        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable != null) {
            progressDrawable.clearColorFilter();
        }
    }

    private static PorterDuff.Mode parseTintMode(int value, PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3:
                return PorterDuff.Mode.SRC_OVER;
            case 5:
                return PorterDuff.Mode.SRC_IN;
            case 9:
                return PorterDuff.Mode.SRC_ATOP;
            case 14:
                return PorterDuff.Mode.MULTIPLY;
            case 15:
                return PorterDuff.Mode.SCREEN;
            case 16:
                return PorterDuff.Mode.ADD;
            default:
                return defaultMode;
        }
    }

}

