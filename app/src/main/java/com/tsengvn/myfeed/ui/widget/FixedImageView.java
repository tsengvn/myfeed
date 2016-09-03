package com.tsengvn.myfeed.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author Hien Ngo
 * @since Sep 03, 2016.
 */
public class FixedImageView extends ImageView {
    private float ratio = 1;

    public FixedImageView(Context context) {
        super(context);
    }

    public FixedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width, height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(getResources().getDisplayMetrics().widthPixels, widthSize);
        } else {
            width = getResources().getDisplayMetrics().widthPixels;
        }

        if (ratio <= 0) {
            height = 0;
        } else {
            height = (int) (width / ratio);
        }

        setMeasuredDimension(width, height);

        int finalWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(finalWidthMeasureSpec, finalHeightMeasureSpec);

    }
}
