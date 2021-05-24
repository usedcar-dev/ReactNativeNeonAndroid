package com.gaadi.neon.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
/**
 * @author lakshaygirdhar
 * @version 1.0
 * @since 24/11/16
 */

public class AnimationUtils
{
    public static void translateOnXAxis(View view, int xFrom, int xTo){
        Animation animation = new TranslateAnimation(xFrom,xTo,0,0);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }


}
