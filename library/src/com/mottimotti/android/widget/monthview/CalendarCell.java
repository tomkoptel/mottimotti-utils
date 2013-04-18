package com.mottimotti.android.widget.monthview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import com.mottimotti.android.widget.R;
import com.mottimotti.android.widget.SAutoLayerTextView;

import java.util.Calendar;

class CalendarCell extends SAutoLayerTextView implements View.OnClickListener {
    private int blockedStateDrawable;
    private int blockedStateTextAppearance;
    private String blockedStateTypeface;

    private int regularStateDrawable;
    private int regularStateTextAppearance;
    private String regularStateTypeface;

    private int inactiveStateDrawable;
    private int inactiveStateTextAppearance;
    private String inactiveStateTypeface;

    private int activeStateDrawable;
    private int activeStateTextAppearance;
    private String activeStateTypeface;

    private int currentStateDrawable;
    private int currentStateTextAppearance;
    private String currentStateTypeface;

    private int headerStateDrawable;
    private int headerStateTextAppearance;
    private String headerStateTypeface;

    private CalendarDay day;
    private CalendarDay previousDay;

    public CalendarCell(Context context) {
        super(context);
    }

    public CalendarCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarCell);
        loadResources(array);
        setListeners();
    }

    public CalendarCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarCell, defStyle, 0);
        loadResources(array);
        setListeners();
    }

    private void loadResources(TypedArray a) {
        blockedStateDrawable = a.getResourceId(R.styleable.CalendarCell_blockedStateDrawable, android.R.color.transparent);
        blockedStateTextAppearance = a.getResourceId(R.styleable.CalendarCell_blockedStateTextAppearance, android.R.style.TextAppearance_Medium);
        blockedStateTypeface = a.getString(R.styleable.CalendarCell_blockedStateTypeface);

        regularStateDrawable = a.getResourceId(R.styleable.CalendarCell_regularStateDrawable, android.R.color.transparent);
        regularStateTextAppearance = a.getResourceId(R.styleable.CalendarCell_regularStateTextAppearance, android.R.style.TextAppearance_Medium);
        regularStateTypeface = a.getString(R.styleable.CalendarCell_regularStateTypeface);

        inactiveStateDrawable = a.getResourceId(R.styleable.CalendarCell_inactiveStateDrawable, android.R.color.transparent);
        inactiveStateTextAppearance = a.getResourceId(R.styleable.CalendarCell_inactiveStateTextAppearance, android.R.style.TextAppearance_Medium);
        inactiveStateTypeface = a.getString(R.styleable.CalendarCell_inactiveStateTypeface);

        activeStateDrawable = a.getResourceId(R.styleable.CalendarCell_activeStateDrawable, android.R.color.transparent);
        activeStateTextAppearance = a.getResourceId(R.styleable.CalendarCell_activeStateTextAppearance, android.R.style.TextAppearance_Medium);
        activeStateTypeface = a.getString(R.styleable.CalendarCell_activeStateTypeface);

        currentStateDrawable = a.getResourceId(R.styleable.CalendarCell_currentStateDrawable, android.R.color.transparent);
        currentStateTextAppearance = a.getResourceId(R.styleable.CalendarCell_currentStateTextAppearance, android.R.style.TextAppearance_Medium);
        currentStateTypeface = a.getString(R.styleable.CalendarCell_currentStateTypeface);

        headerStateDrawable = a.getResourceId(R.styleable.CalendarCell_headerStateDrawable, android.R.color.transparent);
        headerStateTextAppearance = a.getResourceId(R.styleable.CalendarCell_headerStateTextAppearance, android.R.style.TextAppearance_Medium);
        headerStateTypeface = a.getString(R.styleable.CalendarCell_headerStateTypeface);
    }

    private void setListeners() {
        setOnClickListener(this);
    }

    public static CalendarCell instantiate(Context context, AttributeSet attrs) {
        CalendarCell textView = new CalendarCell(context, attrs);
        textView.setShadowLayer(2f, 1, 1, Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setClickable(true);
        textView.setState(DayState.REGULAR);
        return textView;
    }

    public void setState(DayState state) {
        switch (state) {
            case HEADER:
                applyHeaderState();
                break;
            case BLOCKED:
                applyBlockedState();
                break;
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
                break;
        }
    }

    private void applyHeaderState() {
        setTextAppearance(getContext(), headerStateTextAppearance);
        setBackgroundResource(headerStateDrawable);
        loadTypeface(headerStateTypeface);
        setClickable(false);
    }

    private void applyRegularSettings() {
        setTextAppearance(getContext(), regularStateTextAppearance);
        setBackgroundResource(regularStateDrawable);
        loadTypeface(regularStateTypeface);
        setClickable(true);
    }

    private void applyBlockedState() {
        setTextAppearance(getContext(), blockedStateTextAppearance);
        setBackgroundResource(blockedStateDrawable);
        loadTypeface(blockedStateTypeface);
        setClickable(false);
    }

    private void applyInActiveSettings() {
        setTextAppearance(getContext(), inactiveStateTextAppearance);
        setBackgroundResource(inactiveStateDrawable);
        loadTypeface(inactiveStateTypeface);
        setClickable(true);
    }

    private void applyActiveSettings() {
        setTextAppearance(getContext(), activeStateTextAppearance);
        setBackgroundResource(activeStateDrawable);
        loadTypeface(activeStateTypeface);
        setClickable(true);
    }

    private void applyCurrentState() {
        setTextAppearance(getContext(), currentStateTextAppearance);
        setBackgroundResource(currentStateDrawable);
        loadTypeface(currentStateTypeface);
        setClickable(true);
    }

    private void loadTypeface(String typefaceReference) {
        if (typefaceReference == null) return;
        Typeface type = Typeface.createFromAsset(getContext().getAssets(), typefaceReference);
        setTypeface(type);
    }

    public void setDay(CalendarDay day) {
        if (previousDay == null) {
            this.previousDay = day;
            setState(day.getState());
        } else {
            this.previousDay = this.day;
            if (previousDay.getState() != day.getState()) {
                setState(day.getState());
            }
        }
        this.day = day;
        setText(day.toString());
    }

    @Override
    public void onClick(View v) {
        dispatchCellClickListener();
    }

    private CellClickListener clickListener;

    public interface CellClickListener {
        public void onClick(Calendar selectedCalendar);
    }

    public void setCellClickListener(CellClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void dispatchCellClickListener() {
        if (clickListener != null && day != null) {
            clickListener.onClick(day.getCalendar());
        }
    }
}
