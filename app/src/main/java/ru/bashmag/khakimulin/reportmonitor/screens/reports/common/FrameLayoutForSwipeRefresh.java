package ru.bashmag.khakimulin.reportmonitor.screens.reports.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Mark Khakimulin on 16.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class FrameLayoutForSwipeRefresh extends FrameLayout {

    public FrameLayoutForSwipeRefresh(Context context) {
        super(context);
    }

    public FrameLayoutForSwipeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayoutForSwipeRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean canScrollVertically(int direction) {
        if (super.canScrollVertically(direction)) {
            return true;
        }

        int cc = getChildCount();
        for (int i = 0; i < cc; i++) {
            if (getChildAt(i).canScrollVertically(direction)) {
                return true;
            }
        }

        return false;
    }
}