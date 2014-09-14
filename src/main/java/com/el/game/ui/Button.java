package com.el.game.ui;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.el.game.R;

public class Button implements View.OnClickListener {

    private FrameLayout buttonLayout;           // контейнер для содержимого кнопки
    private View buttonView;                    // слой поверх содержимого кнопки, служит для индикации нажатия на кнопку
    private Activity activity;                  // активность к которой привязана кнопка

    public Button(Activity activity) {
        this.activity = activity;
        initVar();
    }

    /**
     * получаем ссылки на View, назначаем onClickListner
     */
    private void initVar() {
        buttonLayout = (FrameLayout) activity.findViewById(R.id.button_layout);
        buttonView = activity.findViewById(R.id.button_onclick_view);
        buttonView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    public View getButtonView() {
        return buttonView;
    }

    public FrameLayout getButtonLayout() {
        return buttonLayout;
    }

    public Activity getActivity() {
        return activity;
    }
}
