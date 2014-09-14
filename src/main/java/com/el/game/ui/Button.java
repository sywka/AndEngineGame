package com.el.game.ui;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.el.game.R;

import org.andengine.ui.activity.BaseGameActivity;

abstract public class Button implements View.OnClickListener {

    private FrameLayout buttonLayout;                   // контейнер для содержимого кнопки
    private View buttonView;                            // слой поверх содержимого кнопки, служит для индикации нажатия на кнопку
    private TextView buttonText;                        // контейнер для текста
    private BaseGameActivity activity;                  // активность к которой привязана кнопка

    public Button(BaseGameActivity activity, int resourceIdButton) {
        this.activity = activity;
        initVar(resourceIdButton);
        setDefaultValues(buttonLayout);
    }

    /**
     * Метод для инициализации начальных значений, фона и тп.
     *
     * @param buttonLayout view кнопки для изменения фона.
     */
    abstract protected void setDefaultValues(FrameLayout buttonLayout);

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
    abstract public void onClick(View view);

    public FrameLayout getButtonLayout() {
        return buttonLayout;
    }

    public BaseGameActivity getActivity() {
        return activity;
    }

    public TextView getButtonText() {
        return buttonText;
    }
}
