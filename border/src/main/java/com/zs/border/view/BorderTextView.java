package com.zs.border.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.zs.border.R;
import com.zs.border.util.DrawableUtil;

/**
 * Created by zs
 * Date：2017年 11月 21日
 * Time：10:15
 * —————————————————————————————————————
 * About: 带边框属性的TextView
 * —————————————————————————————————————
 */
@SuppressLint("AppCompatCustomView")
public class BorderTextView extends TextView {

    private int strokeWidth;    // 边框线宽
    private int strokeColor;    // 边框颜色
    private int enableColor;    // 不可点击颜色
    private int contentColor;   // 背景颜色
    private int pressedColor;   // 按下背景颜色
    private int cornerRadius;   // 圆角半径
    private boolean mFollowTextColor; // 边框颜色是否跟随文字颜色

    private Paint mPaint = new Paint();                 // 画边框所使用画笔对象
    private RectF mRectF = new RectF();                 // 画边框要使用的矩形


    public BorderTextView(Context context) {
        this(context, null);
    }

    public BorderTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 读取属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderTextView);
        contentColor = ta.getColor(R.styleable.BorderTextView_contentBackColor, Color.TRANSPARENT);
        pressedColor = ta.getColor(R.styleable.BorderTextView_contentPressedColor, contentColor);
        enableColor = ta.getColor(R.styleable.BorderTextView_enableBackColor, Color.parseColor("#999999"));
        strokeWidth = ta.getDimensionPixelSize(R.styleable.BorderTextView_strokeWidth, 0);
        strokeColor = ta.getColor(R.styleable.BorderTextView_strokeColor, Color.TRANSPARENT);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.BorderTextView_cornerRadius, 0);
        mFollowTextColor = ta.getBoolean(R.styleable.BorderTextView_followTextColor, false);
        ta.recycle();
        initView();
    }

    private void initView(){
        // 初始化画笔
        mPaint.setStyle(Paint.Style.STROKE);     // 空心效果
        mPaint.setAntiAlias(true);               // 设置画笔为无锯齿
        mPaint.setStrokeWidth(strokeWidth);      // 线宽
        // 设置边框线的颜色, 如果声明为边框跟随文字颜色且当前边框颜色与文字颜色不同时重新设置边框颜色
        if (mFollowTextColor && strokeColor != getCurrentTextColor())
            strokeColor = getCurrentTextColor();
        // 设置背景
        setBackground(DrawableUtil.getPressedSelector(enableColor , contentColor , pressedColor , cornerRadius));

    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        // 设置画笔颜色
        mPaint.setColor(strokeColor);
        // 画空心圆角矩形
        if (strokeWidth > 0){
            mRectF.left = mRectF.top = 0.5f * strokeWidth;
            mRectF.right = getMeasuredWidth() - 0.5f * strokeWidth;
            mRectF.bottom = getMeasuredHeight() - 0.5f * strokeWidth;
            canvas.drawRoundRect(mRectF, cornerRadius, cornerRadius, mPaint);
        }
    }


    /**
     * 修改边框宽度
     * @param roederWidth  传值：px
     */
    public void setStrokeWidth(int roederWidth){
        try {
            strokeWidth = roederWidth;
            invalidate();
        }catch (Exception e){
            Log.e("My_Error",e.toString());
        }

    }

    /**
     * 修改边框颜色
     * @param colorResource  传值：R.color.XXXX
     */
    public void setStrokeColor(int colorResource){
        try {
            strokeColor = ContextCompat.getColor(getContext(), colorResource);
            invalidate();
        }catch (Exception e){
            Log.e("My_Error",e.toString());
        }

    }

    /**
     * 修改背景颜色
     * @param colorResource  传值：R.color.XXXX
     */
    public void setContentColorResource(int colorResource){
        try {
            contentColor = ContextCompat.getColor(getContext(), colorResource);
            setBackground(DrawableUtil.getPressedSelector(enableColor , contentColor , contentColor , cornerRadius));
        }catch (Exception e){
            Log.e("My_Error",e.toString());
        }

    }
}