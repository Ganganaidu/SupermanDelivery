package com.supermandelivery.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ganga on 4/16/16.
 */
public class EditTextNormal extends EditText{


    public EditTextNormal(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public EditTextNormal(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public EditTextNormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/ufonts.com_clanpro-news.ttf", context);
        setTypeface(customFont);
    }
}
