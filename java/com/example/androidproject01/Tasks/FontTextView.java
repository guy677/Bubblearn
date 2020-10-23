package com.example.androidproject01.Tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class FontTextView extends TextView {

    public FontTextView(Context context) {
        super(context);
        setTypeface(null,Typeface.BOLD_ITALIC);
        setBackgroundColor(Color.YELLOW);
        setTextSize(30);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTypeface(null,Typeface.BOLD_ITALIC);
        setBackgroundColor(Color.YELLOW);
        setTextSize(30);


    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(null,Typeface.BOLD_ITALIC);
        setBackgroundColor(Color.YELLOW);
        setTextSize(30);


    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeface(null,Typeface.BOLD_ITALIC);
        setBackgroundColor(Color.YELLOW);
        setTextSize(30);

    }
}