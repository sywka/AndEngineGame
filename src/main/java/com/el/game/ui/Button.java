package com.el.game.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.el.game.R;
import com.el.game.utils.SoundControl;

public class Button extends FrameLayout implements View.OnClickListener, View.OnTouchListener {

    private int textResource;
    private int textColor;
    private Gravity gravity;
    private int textPaddingLeft;
    private int textPaddingRight;

    private enum Gravity {
        CENTER, LEFT, RIGHT
    }

    private FrameLayout buttonLayout;
    private TextView buttonTextView;

    private SoundControl soundControl;
    private OnButtonClick listener;

    public Button(Context context) {
        super(context);
        initButton();
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initButton();
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initButton();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.button,
                0, 0);
        try {
            textResource = typedArray.getResourceId(R.styleable.button_text, R.string.null_text);
            textColor = typedArray.getColor(R.styleable.button_textColor, Color.WHITE);
            gravity = Gravity.values()[typedArray.getInt(R.styleable.button_gravity, Gravity.CENTER.ordinal())];
            textPaddingLeft = typedArray.getDimensionPixelOffset(R.styleable.button_textPaddingLeft, 0);
            textPaddingRight = typedArray.getDimensionPixelOffset(R.styleable.button_textPaddingRight, 0);
        } finally {
            typedArray.recycle();
        }
    }

    private void initButton() {
        soundControl = SoundControl.getInstance(getContext(),
                ((GameActivity) getContext()).getMusicManager(),
                ((GameActivity) getContext()).getSoundManager());
        inflate(getContext(), R.layout.button, this);

        buttonLayout = (FrameLayout) findViewById(R.id.button_layout);
        buttonTextView = (TextView) findViewById(R.id.button_text);

        buttonLayout.setClickable(true);
        buttonLayout.setOnClickListener(this);
        buttonLayout.setOnTouchListener(this);

        buttonTextView.setText(textResource);
        buttonTextView.setTextColor(textColor);
        switch (gravity) {
            case CENTER:
                buttonTextView.setGravity(android.view.Gravity.CENTER);
                break;
            case LEFT:
                buttonTextView.setGravity(android.view.Gravity.LEFT | android.view.Gravity.CENTER);
                break;
            case RIGHT:
                buttonTextView.setGravity(android.view.Gravity.RIGHT | android.view.Gravity.CENTER);
                break;
        }
        buttonTextView.setPadding(textPaddingLeft, 0, textPaddingRight, 0);
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(this, view);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            soundControl.playPressButtonSound();
        return false;
    }

    public void setOnClickListener(OnButtonClick listener) {
        this.listener = listener;
    }

    public String getText() {
        return getContext().getString(textResource);
    }

    public void setTextResource(int textResource) {
        this.textResource = textResource;
        buttonTextView.setText(textResource);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
