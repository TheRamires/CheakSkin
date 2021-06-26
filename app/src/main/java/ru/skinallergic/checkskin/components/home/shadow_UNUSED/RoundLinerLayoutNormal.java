package ru.skinallergic.checkskin.components.home.shadow_UNUSED;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import ru.skinallergic.checkskin.R;

public class RoundLinerLayoutNormal extends LinearLayout {

    public void changeColor(int shadowColor){
        initBackground(shadowColor);
    }

    public RoundLinerLayoutNormal(Context context) {
        super(context);
        initBackground();
    }

    public RoundLinerLayoutNormal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBackground();
    }

    public RoundLinerLayoutNormal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackground();
    }

    private void initBackground() {
        setBackground(ViewUtils.generateBackgroundWithShadow(this,R.color.white,
                R.dimen.radius_corner,R.color.shadow_color2,R.dimen.elevation, Gravity.CENTER));
    }
    private void initBackground(int shadowColor) {
        setBackground(ViewUtils.generateBackgroundWithShadow(this,R.color.white,
                R.dimen.radius_corner,shadowColor,R.dimen.elevation, Gravity.CENTER));
    }
}