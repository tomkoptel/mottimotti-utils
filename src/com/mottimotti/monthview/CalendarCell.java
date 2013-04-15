package com.mottimotti.monthview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;

import static com.mottimotti.monthview.CalendarDay.State.*;


public class CalendarCell extends HelveticaNeueTextView {

    public CalendarCell(Context context) {
        super(context);
    }

    public CalendarCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static CalendarCell instantiate(Context context) {
        CalendarCell textView = new CalendarCell(context);
        textView.setTextAppearance(context, R.style.CellFont);
        textView.setShadowLayer(2f, 1, 1, Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setClickable(true);
        textView.setState(REGULAR);
        return textView;
    }

    public void setState(CalendarDay.State state) {
        switch (state) {
            case REGULAR:
                applyRegularSettings();
                break;
            case INACTIVE:
                applyInActiveSettings();
                break;
            case ACTIVE:
                applyActiveSettings();
                break;
            case CURRENT:
                applyCurrentState();
        }
    }

    private void applyRegularSettings() {
        setTextColor(getResources().getColor(R.color.cell_regular));
        setBackgroundResource(R.drawable.cell);
    }

    private void applyInActiveSettings() {
        setTextColor(getResources().getColor(R.color.cell_inactive));
        setBackgroundResource(R.drawable.cell);
    }

    private void applyActiveSettings() {
        setTextColor(getResources().getColor(R.color.cell_regular));
        setBackgroundResource(R.drawable.cell);
    }

    private void applyCurrentState() {
        setTextColor(getResources().getColor(android.R.color.white));
        setBackgroundResource(R.drawable.cell);
    }
}
