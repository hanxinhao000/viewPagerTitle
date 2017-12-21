package com.xinhao_han.viewpagerindex;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * ViewPager 的顶层框架
 */

public class ViewPagerTitle extends FrameLayout implements XHHorizontalScrollView.SizeChanged {

    //默认颜色

    private int color = Color.parseColor("#131313");
    //选中颜色
    private int colorChecd = Color.parseColor("#f25843");

    //默认字体大小
    private int textSize = 17;
    //默认标题
    private String[] title;

    //默认的大View
    private View bagView;
    //4View的布局
    private View view4;
    //4+View的布局
    private View view4Up;
    //大布局的添加容器的子ID
    private RelativeLayout bagRl;
    //内边距
    private int textPaing = 16;

    //ViewPager

    private ViewPager viewPager;
    private Context context;

    private XHHorizontalScrollView hsv;


    private Activity activity;

    public ViewPagerTitle(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ViewPagerTitle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ViewPagerTitle(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    //初始化
    private void initView(Context context) {
        //大View的布局
        bagView = View.inflate(context, R.layout.view_pager_title, null);
        // 4View的布局
        view4 = View.inflate(context, R.layout.view_pager_title_min, null);
        // 4+View的布局
        view4Up = View.inflate(context, R.layout.view_pager_title_max, null);
        //大布局的子ID
        bagRl = bagView.findViewById(R.id.bagRl);

        this.context = context;

        addView(bagView);
    }

    //设置颜色

    public ViewPagerTitle setTextColor(int color) {
        this.color = color;
        return this;

    }
    //设置字体大小

    public ViewPagerTitle setTextSize(int textSize) {
        this.textSize = textSize;
        return this;

    }

    //设置ViewPager

    public ViewPagerTitle setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        return this;
    }

    //获取当前Activity
    public ViewPagerTitle setActivity(Activity activity) {
        this.activity = activity;


        return this;
    }

    //设置标题(显示多少东西)
    public ViewPagerTitle setPagerTitle(String[] title) {
        this.title = title;
        return this;
    }

    //获取标题

    private void getTitle() {


        title = new String[viewPager.getAdapter().getCount()];
        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {

            title[i] = viewPager.getAdapter().getPageTitle(i).toString();


        }

    }

    //开始预编译
    public void build() {

        getTitle();

        if (title.length >= 5) {
            //表示是多布局
            bagRl.addView(view4Up);
            //清除占用
            view4 = null;
            for4UpBuild();
        } else {
            //表示是少布局
            bagRl.addView(view4);
            //清除占用
            view4Up = null;
            for4Build();
        }

    }

    //这个表示是多布局可能会有4+个,表示能左右滑动的

    private void for4UpBuild() {

        //多布局的添加,保证每个的大小最低是屏幕的1/4
        final LinearLayout max_li = view4Up.findViewById(R.id.max_li);


        hsv = view4Up.findViewById(R.id.hsv);

        hsv.setXHOnSizeChanged(this);
        //获取屏幕宽度


        int w = getW();

        int textW = (w / 5) + dp2Px(10);
        //这块我感觉还能抽取,应为一样重复了,可以写一个方法抽出来,撞门添加View的方法,感兴趣的老铁,可以自己尝试一下

        for (int i = 0; i < title.length; i++) {


            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // rl.leftMargin = dp2Px(10);

            TextView textView = new TextView(context);

            if (i == 0)
                textView.setTextColor(colorChecd);
            else
                textView.setTextColor(color);

            textView.setWidth(textW);
            textView.setPadding(textPaing, textPaing, textPaing, textPaing);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(rl);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(finalI);
                }
            });

            textView.setTextSize(textSize);
            textView.setText(title[i]);


            max_li.addView(textView);

        }

        //加入下标


        final RelativeLayout max_rl = view4Up.findViewById(R.id.max_rl);


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) max_rl.getLayoutParams();
        layoutParams.width = textW;


        //加入下标View

        RelativeLayout.LayoutParams textRL = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);

        //计算
        //计算左边要多少
        int wM = ((textW) / 5);


        textRL.leftMargin = wM;
        textRL.rightMargin = wM;
        textView.setHeight(5);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(colorChecd);
        textView.setLayoutParams(textRL);


        max_rl.setLayoutParams(layoutParams);
        max_rl.addView(textView);


        final int[] mid = new int[1];

        max_li.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mid[0] = max_li.getChildAt(1).getLeft() - max_li.getChildAt(0).getLeft();
                max_li.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

                int mL = (int) ((position + positionOffset) * mid[0]);
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) max_rl.getLayoutParams();
                layoutParams1.leftMargin = mL;
                max_rl.setLayoutParams(layoutParams1);


                //加入滑动


                int maxScrollAmount = hsv.getMaxScrollAmount();

                //屏幕总宽度
                int totleSize = (getW() + maxScrollAmount) + (textPaing * 2) * title.length;

                //每个View的宽度
                int viewSize = totleSize / title.length;


                hsv.smoothScrollTo((int) (viewSize * (positionOffset + position)), 0);


               // Log.e("滑动距离", "滑动距离: " + viewSize * position);

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < max_li.getChildCount(); i++) {

                    TextView childAt = (TextView) max_li.getChildAt(i);
                    childAt.setTextColor(color);

                }


                TextView childAt = (TextView) max_li.getChildAt(position);
                childAt.setTextColor(colorChecd);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {

        Log.e("宽度", "onSizeChanged: " + w);
    }


    //只添加4个
    private void for4Build() {

        //获取要添加的TextView
        final LinearLayout min_li = view4.findViewById(R.id.min_li);


        for (int i = 0; i < title.length; i++) {


            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.weight = 1;
            ll.gravity = Gravity.CENTER;
            TextView textView = new TextView(context);
            textView.setText(title[i]);
            textView.setTextSize(textSize);
            textView.setGravity(Gravity.CENTER);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(finalI);
                }
            });
            textView.setPadding(textPaing, textPaing, textPaing, textPaing);
            if (i == 0)
                textView.setTextColor(colorChecd);
            else
                textView.setTextColor(color);
            textView.setLayoutParams(ll);
            min_li.addView(textView);


        }
        //放入TextView底层的一些东东


        //这个是要滑动的东东
        final RelativeLayout min_rl = view4.findViewById(R.id.min_rl);

        //加入红色地布局
        TextView textView = new TextView(context);
        RelativeLayout.LayoutParams textRl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setBackgroundColor(colorChecd);
        textView.setText(" ");
        textView.setHeight(5);
        textView.setGravity(Gravity.CENTER);

        //计算左边要多少
        int wL = getW() / title.length;

        int wM = (wL / 4) - 20;


        textRl.rightMargin = wM;
        textRl.leftMargin = wM;
        textView.setLayoutParams(textRl);
        min_rl.addView(textView);

        ViewGroup.LayoutParams layoutParams = min_rl.getLayoutParams();
        layoutParams.width = (getW() / title.length);
        min_rl.setLayoutParams(layoutParams);


        final int[] mid = new int[1];

        min_li.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mid[0] = min_li.getChildAt(1).getLeft() - min_li.getChildAt(0).getLeft();
                min_li.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) min_rl.getLayoutParams();

                int liftM = (int) ((position + positionOffset) * mid[0]);
                layoutParams1.leftMargin = liftM;

                min_rl.setLayoutParams(layoutParams1);
            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < min_li.getChildCount(); i++) {

                    TextView childAt = (TextView) min_li.getChildAt(i);
                    childAt.setTextColor(color);

                }


                TextView childAt = (TextView) min_li.getChildAt(position);
                childAt.setTextColor(colorChecd);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    //--------------------------------------工具类


    private int getW() {

        DisplayMetrics displayMetrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

        return displayMetrics.widthPixels;


    }

    /**
     * dp转换
     */
    public int dp2Px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


}
