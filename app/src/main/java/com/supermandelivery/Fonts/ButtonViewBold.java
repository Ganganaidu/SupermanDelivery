package com.supermandelivery.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by ganga on 4/16/16.
 */
public class ButtonViewBold extends Button {

    public ButtonViewBold(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public ButtonViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public ButtonViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/ufonts.com_clanpro-bold.ttf", context);
        setTypeface(customFont);
    }
}
