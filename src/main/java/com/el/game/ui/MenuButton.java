package com.el.game.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;

import com.el.game.R;

import org.andengine.audio.music.Music;
import org.andengine.engine.Engine;

public class MenuButton extends Button {

    public MenuButton(Activity activity, int resourceIdButton, int textId) {
        super(activity, resourceIdButton);
        getButtonText().setText(activity.getString(textId));
    }

    public MenuButton(Activity activity, int resourceIdButton, int textId, OnButtonClick listener) {
        super(activity, resourceIdButton, listener);
        getButtonText().setText(activity.getString(textId));
    }
}
