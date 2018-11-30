### 简介
在项目开发中，设计经常会有这样一个需求，添加一个圆角的按钮，虽然这很很简单的事，设置drawable的selector就可以了，但是如果有很多地方都需要这样的按钮，而且背景的圆角还各不相同，这就需要加很多的drawable文件。而这个自定义的TextView可以解决如上所有问题，自定义增加了几个属性，可以设置圆角背景，圆角大小，边框粗细，圆角背景颜色，和圆角按下效果的颜色，统统都可以设置。
### 效果图
![191BF0477FE195C187D64287D7C9FA02.gif](https://upload-images.jianshu.io/upload_images/3183047-db90962fcc887ab6.gif?imageMogr2/auto-orient/strip)

### 引入方式
1、项目的build文件中添加：maven { url 'https://jitpack.io' }
```Java
allprojects {
    repositories {
        jcenter()
        google()
        maven { url 'https://jitpack.io' }
    }
}
```
2、app的build文件中引入：
```Java
implementation 'com.github.QQzs:BorderView:1.0.6'
```
### 代码分析
代码逻辑很简单，圆角边框是在ondraw方法里面用画布和画笔画一个矩形的边框，形状和圆角根据属性设置。而圆角的背景，是要在代码里生成selector，代码生成并添加圆角的shape，只是把drawable文件在代码里生成，下面看完整代码。
```Java
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
        strokeColor = ta.getColor(R.styleable.BorderTextView_strokeColor, contentColor);
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
```
```Java
package com.zs.border.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by zs
 * Date：2017年 12月 18日
 * Time：10:49
 * —————————————————————————————————————
 * About:
 * —————————————————————————————————————
 */

public class DrawableUtil {

    /**
     * 用java代码的方式动态生成状态选择器
     */
    public static Drawable getPressedSelector(int enabledColor , int normalColor , int pressedColor , int radius) {
        Drawable enabled = createShape(enabledColor , radius);
        Drawable pressed = createShape(pressedColor , radius);
        Drawable normal = createShape(normalColor , radius);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressed);    // 按下状态 , 设置按下的图片
        drawable.addState(new int[]{android.R.attr.state_enabled}, normal);     // 默认状态,默认状态下的图片
        drawable.addState(new int[]{}, enabled);                                // 不可点击状态
        //设置状态选择器过度动画/渐变选择器/渐变动画
//        drawable.setEnterFadeDuration(500);
//        drawable.setExitFadeDuration(500);
        return drawable;
    }

    public static GradientDrawable createShape(int color , int radius){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);//设置4个角的弧度
        drawable.setColor(color);// 设置颜色
        return drawable;

    }


}

```
### 添加属性
```Java

    <!-- 有边框和背景的View -->
    <declare-styleable name="BorderTextView">
        <attr name="strokeWidth" format="dimension"/>
        <attr name="cornerRadius" format="dimension"/>
        <attr name="strokeColor" format="color"/>
        <attr name="enableBackColor" format="color" />
        <attr name="contentBackColor" format="color"/>
        <attr name="contentPressedColor" format="color"/>
        <attr name="followTextColor" format="boolean"/>
    </declare-styleable>
```
### 控件使用
```Java
<com.zs.border.view.BorderTextView
        android:id="@+id/tv_border"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:text="dddd"
        android:textColor="@color/white"
        app:cornerRadius="20dp"
        app:strokeWidth="2dp"
        app:strokeColor="@color/color_4"
        app:contentBackColor="@color/color_5"
        app:contentPressedColor="@color/color_0"
        android:layout_margin="20dp"
        android:gravity="center"
        >
 </com.zs.border.view.BorderTextView>
```
源码地址：https://github.com/QQzs/BorderView
简书地址：https://www.jianshu.com/p/067f3ea26e5a
### 完成
