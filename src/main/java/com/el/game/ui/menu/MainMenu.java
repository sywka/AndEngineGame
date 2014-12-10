package com.el.game.ui.menu;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.el.game.R;
import com.el.game.ui.Button;
import com.el.game.ui.OnButtonClick;
import com.el.game.ui.OnResumeMainMenu;
import com.el.game.ui.TextButton;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends MenuWindowModel implements OnButtonClick {

    public enum Modification {
        START_MENU, RESUME_MENU
    }

    public enum Result {
        RESUME, SETTINGS, FINISH
    }

    private Modification modification;
    private List<Button> buttons;
    private Result result;
    private OnResumeMainMenu listener;

    public MainMenu(Activity activity, FrameLayout activityContent, Modification modification, OnResumeMainMenu listener) {
        super(activity, activityContent);
        this.listener = listener;
        this.modification = modification;
        this.buttons = new ArrayList<>();
    }

    @Override
    public int getMenuLayoutId() {
        return R.layout.main_menu;
    }

    @Override
    protected void onCreateWindow() {
        setTitleText(R.string.button_menu);
        result = Result.RESUME;
        buttons.clear();
        switch (modification) {
            case START_MENU:
                getMenuLayout().setBackgroundColor(activity.getResources().getColor(R.color.main_menu_background));
                buttons.add(new TextButton(activity, R.id.button_start_resume, R.string.button_menu_start, this));
                break;
            case RESUME_MENU:
                getMenuLayout().setBackgroundColor(activity.getResources().getColor(R.color.main_menu_background_with_alpha));
                buttons.add(new TextButton(activity, R.id.button_start_resume, R.string.button_menu_resume, this));
                break;
        }
        buttons.add(new TextButton(activity, R.id.button_settings, R.string.button_menu_settings, this));
        buttons.add(new TextButton(activity, R.id.button_exit, R.string.button_menu_exit, this));

        for (Button button : buttons) {
            button.getButtonText().setTextColor(activity.getResources().getColor(android.R.color.white));
            button.getButtonText().setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            button.getButtonText().setPadding((int) activity.getResources().getDimension(R.dimen.menu_elements_padding), 0, 0, 0);
        }
    }

    @Override
    protected void onCloseWindow() {
        if (result != null)
            switch (result) {
                case RESUME:
                    listener.resume();
                    break;
                case SETTINGS:
                    new SettingsMenu(getActivity(), getActivityContent())
                            .showWindow(true);
                    break;
                case FINISH:
                    new ExitConfirmMenu(getActivity(), getActivityContent(), ExitConfirmMenu.Modification.RESUME_MENU, null)
                            .showWindow(true);
                    break;
            }
    }

    @Override
    protected void showOpenAnimation(long startOffset) {
        showAnimation(startOffset, R.anim.menu_button_open);
    }

    @Override
    protected long showCloseAnimation() {
        return showAnimation(0, R.anim.menu_button_close);
    }

    private long showAnimation(long startOffset, int animResourceId) {
        Animation animation;
        for (Button button : buttons) {
            startOffset += 100;
            animation = AnimationUtils.loadAnimation(activity, animResourceId);
            animation.setStartOffset(startOffset);
            animation.setFillAfter(true);
            button.getButtonLayout().startAnimation(animation);
        }
        return startOffset;
    }

    @Override
    public void onClick(Button button, View view) {
        switch (buttons.indexOf(button)) {
            case 0:
                result = Result.RESUME;
                onBackPressed();
                break;
            case 1:
                result = Result.SETTINGS;
                closeWindow(true, null);
                break;
            case 2:
                result = Result.FINISH;
                closeWindow(true, null);
                break;
        }
    }
}
