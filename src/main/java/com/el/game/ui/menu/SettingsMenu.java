package com.el.game.ui.menu;

import android.app.Activity;
import android.widget.FrameLayout;

import com.el.game.R;

public class SettingsMenu extends MenuWindowModel {

    public SettingsMenu(Activity activity, FrameLayout activityContent) {
        super(activity, activityContent);
    }

    @Override
    protected int getMenuLayoutId() {
        return R.layout.settings_menu;
    }

    @Override
    protected void onCreateWindow() {
        setTitleText(R.string.button_menu_settings);
    }

    @Override
    protected void onCloseWindow() {
    }

    @Override
    protected void showOpenAnimation(long startOffset) {
    }

    @Override
    protected long showCloseAnimation() {
        return 0;
    }
}
