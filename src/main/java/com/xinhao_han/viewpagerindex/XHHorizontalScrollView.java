package com.xinhao_han.viewpagerindex;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.HorizontalScrollView;

/**
 * Created by 14178 on 2017/12/21.
 */

public class XHHorizontalScrollView extends HorizontalScrollView {
    private SizeChanged sizeChanged;

    public XHHorizontalScrollView(Context context) {
        super(context);
    }

    public XHHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XHHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        sizeChanged.onSizeChanged(l, t, oldl, oldt);
        Log.e("宽度", "onSizeChanged: " + l );
    }

    public void setXHOnSizeChanged(SizeChanged sizeChanged) {
        this.sizeChanged = sizeChanged;


    }


    public interface SizeChanged {
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }
}
