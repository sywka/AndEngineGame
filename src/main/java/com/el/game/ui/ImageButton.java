package com.el.game.ui;

import android.app.Activity;
import android.widget.FrameLayout;

public class ImageButton extends Button {

    private int resourceIdImage;

    public ImageButton(Activity activity, int resourceIdButton, int resourceIdImage) {
        super(activity, resourceIdButton);
        this.resourceIdImage = resourceIdImage;
        getButtonLayout().setBackgroundResource(resourceIdImage);
    }

    public ImageButton(Activity activity, int resourceIdButton, int resourceIdImage, OnButtonClick listener) {
        super(activity, resourceIdButton, listener);
        this.resourceIdImage = resourceIdImage;
        getButtonLayout().setBackgroundResource(resourceIdImage);
    }

    /**
     * Метод для инициализации начальных значений, фона и тп.
     *
     * @param buttonLayout view кнопки для изменения фона.
     */
    @Override
    protected void setDefaultValues(FrameLayout buttonLayout) {
        buttonLayout.setBackgroundResource(resourceIdImage);
    }
}
