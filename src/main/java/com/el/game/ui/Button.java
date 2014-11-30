package com.el.game.ui;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.el.game.R;

import java.util.ArrayList;
import java.util.List;

abstract public class Button implements View.OnClickListener {

    private FrameLayout buttonLayout;                   // контейнер для содержимого кнопки
    private View buttonView;                            // слой поверх содержимого кнопки, служит для индикации нажатия на кнопку
    private TextView buttonText;                        // контейнер для текста
    private Activity activity;                          // активность к которой привязана кнопка
    private List<OnButtonClick> listeners;

    public Button(Activity activity, int resourceIdButton) {
        this.activity = activity;
        initVar(resourceIdButton);
        listeners = new ArrayList<OnButtonClick>();
        setDefaultValues(buttonLayout);
    }

    public Button(Activity activity, int resourceIdButton, OnButtonClick listener) {
        this.activity = activity;
        initVar(resourceIdButton);
        listeners = new ArrayList<OnButtonClick>();
        listeners.add(listener);
        setDefaultValues(buttonLayout);
    }

    /**
     * Метод для инициализации начальных значений, фона и тп.
     *
     * @param buttonLayout view кнопки для изменения фона.
     */
    protected void setDefaultValues(FrameLayout buttonLayout) {
        buttonLayout.setBackgroundResource(R.drawable.button);
    }

    /**
     * получаем ссылки на View, назначаем onClickListner
     */
    private void initVar(int resourceIdButton) {
        View button = activity.findViewById(resourceIdButton);
        buttonLayout = (FrameLayout) button.findViewById(R.id.button_layout);
        buttonView = button.findViewById(R.id.button_onclick_view);
        buttonText = (TextView) button.findViewById(R.id.button_text);
        buttonView.setOnClickListener(this);
    }

    @Override
    final public void onClick(View view) {
        for (OnButtonClick listener : listeners)
            listener.onClick(this, view);
    }

    public FrameLayout getButtonLayout() {
        return buttonLayout;
    }

    public Activity getActivity() {
        return activity;
    }

    public TextView getButtonText() {
        return buttonText;
    }

    public void addListener(OnButtonClick listener) {
        listeners.add(listener);
    }

    public void removeListener(OnButtonClick listener) {
        listeners.remove(listener);
    }
}
