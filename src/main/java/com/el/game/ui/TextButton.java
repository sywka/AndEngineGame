package com.el.game.ui;

import android.app.Activity;
import android.widget.FrameLayout;

public class TextButton extends Button {

    public TextButton(Activity activity, int resourceIdButton, int textId) {
        super(activity, resourceIdButton);
        getButtonText().setText(activity.getString(textId));
    }

    public TextButton(Activity activity, int resourceIdButton, int textId, OnButtonClick listener) {
        super(activity, resourceIdButton, listener);
        getButtonText().setText(activity.getString(textId));
    }

    /**
     * Метод для инициализации начальных значений, фона и тп.
     *
     * @param buttonLayout view кнопки для изменения фона.
     */
    @Override
    protected void setDefaultValues(FrameLayout buttonLayout) {
        super.setDefaultValues(buttonLayout);
        getButtonLayout().setBackgroundColor(getActivity().getResources().getColor(android.R.color.transparent));
    }
}
