package com.zs.demo.border.util;

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
