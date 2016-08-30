package com.supermandelivery.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ganga on 4/16/16.
 */
public class EditTextBold extends EditText{

    public EditTextBold(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public EditTextBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public EditTextBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("fonts/ufonts.com_clanpro-bold.ttf", context);
        setTypeface(customFont);
    }
}
