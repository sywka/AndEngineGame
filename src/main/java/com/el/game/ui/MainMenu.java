package com.el.game.ui;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.el.game.R;

public class MainMenu implements OnButtonClick {

    public enum Modification {
        START_MENU, RESUME_MENU
    }

    public enum Result {
        RESUME, FINISH
    }

    private Activity activity;
    private FrameLayout content;

    private LinearLayout mainMenuLayout;
    private TextButton startResumeButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    private OnMainAction listener;

    public MainMenu(Activity activity, FrameLayout content, Modification modification, OnMainAction listener) {
        this.activity = activity;
        this.listener = listener;
        this.content = content;

        changeClickable((ViewGroup) content.getChildAt(0), false);
        content.addView(activity.getLayoutInflater().inflate(R.layout.main_menu, null));
        mainMenuLayout = (LinearLayout) content.findViewById(R.id.main_menu_layout);

        switch (modification) {
            case START_MENU:
                mainMenuLayout.setBackgroundColor(activity.getResources().getColor(R.color.main_menu_background));
                startResumeButton = new TextButton(activity, R.id.button_start_resume, R.string.button_menu_start, this);
                break;
            case RESUME_MENU:
                mainMenuLayout.setBackgroundColor(activity.getResources().getColor(R.color.main_menu_background_with_alpha));
                startResumeButton = new TextButton(activity, R.id.button_start_resume, R.string.button_menu_resume, this);
                break;
        }
        settingsButton = new TextButton(activity, R.id.button_settings, R.string.button_menu_settings, this);
        exitButton = new TextButton(activity, R.id.button_exit, R.string.button_menu_exit, this);

        initButtons();
        showAnimation(R.anim.main_menu_open_background, R.anim.main_menu_open, null);
    }

    public void changeClickable(ViewGroup viewGroup, boolean flag) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setClickable(flag);
            if (view instanceof ViewGroup) {
                changeClickable((ViewGroup) view, flag);
            }
        }
    }

    @Override
    public void onClick(Button button, View view) {
        if (button == startResumeButton) {
            showCloseAnimation(Result.RESUME);

        } else if (button == settingsButton) {

        } else if (button == exitButton) {
            showCloseAnimation(Result.FINISH);
        }
    }

    /**
     * Назначаем отступы и положение/цвет текста.
     */
    private void initButtons() {
        startResumeButton.getButtonText().setTextColor(activity.getResources().getColor(android.R.color.white));
        startResumeButton.getButtonText().setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        startResumeButton.getButtonLayout().setPadding((int) activity.getResources().getDimension(R.dimen.button_main_menu_padding_left), 0, 0, 0);
        settingsButton.getButtonText().setTextColor(activity.getResources().getColor(android.R.color.white));
        settingsButton.getButtonText().setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        settingsButton.getButtonLayout().setPadding((int) activity.getResources().getDimension(R.dimen.button_main_menu_padding_left), 0, 0, 0);
        exitButton.getButtonText().setTextColor(activity.getResources().getColor(android.R.color.white));
        exitButton.getButtonText().setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        exitButton.getButtonLayout().setPadding((int) activity.getResources().getDimension(R.dimen.button_main_menu_padding_left), 0, 0, 0);
    }

    /**
     * Показываем анимацию
     */
    private void showAnimation(int animIdBackground, int animIdMenu, Animation.AnimationListener listener) {
        Animation backgroundAnimation = AnimationUtils.loadAnimation(activity, animIdBackground);
        backgroundAnimation.setAnimationListener(listener);
        mainMenuLayout.startAnimation(backgroundAnimation);

        Animation startResumeButtonAnimation = AnimationUtils.loadAnimation(activity, animIdMenu);
        startResumeButton.getButtonLayout().startAnimation(startResumeButtonAnimation);

        Animation settingsButtonAnimation = AnimationUtils.loadAnimation(activity, animIdMenu);
        settingsButtonAnimation.setStartOffset(100);
        settingsButton.getButtonLayout().startAnimation(settingsButtonAnimation);

        Animation exitButtonAnimation = AnimationUtils.loadAnimation(activity, animIdMenu);
        exitButtonAnimation.setStartOffset(200);
        exitButton.getButtonLayout().startAnimation(exitButtonAnimation);
    }

    private void showCloseAnimation(final Result result) {
        showAnimation(R.anim.main_menu_close_background, R.anim.main_menu_close, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                changeClickable((ViewGroup) content.getChildAt(0), true);
                content.removeView(mainMenuLayout);
                listener.action(result);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
