package com.el.game.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.el.game.R;

public class MainMenuActivity extends Activity implements OnButtonClick {

    public static final String MENU_MODIFICATION = "menu_modification";
    public static final int START_MENU = 0;
    public static final int RESUME_MENU = 1;
    public static final int RESULT_EXIT = 2;

    private int modification;
    private TextButton startResumeButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkMenuModification();
        setContentView(R.layout.activity_main_menu);

        if (modification == START_MENU)
            startResumeButton = new TextButton(this, R.id.button_start_resume, R.string.button_menu_start, this);
        else
            startResumeButton = new TextButton(this, R.id.button_start_resume, R.string.button_menu_resume, this);
        settingsButton = new TextButton(this, R.id.button_settings, R.string.button_menu_settings, this);
        exitButton = new TextButton(this, R.id.button_exit, R.string.button_menu_exit, this);

        initPosition();
        startOpenAnimation();
    }

    @Override
    public void onClick(Button button, View view) {
        if (button == startResumeButton) {
            finish();
            if (modification == START_MENU)
                startActivity(new Intent(this, GameActivity.class));

        } else if (button == settingsButton) {

        } else if (button == exitButton) {
            setResult(RESULT_EXIT);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    /**
     * Назначаем положение текста и отступы.
     */
    private void initPosition() {
        startResumeButton.getButtonText().setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        startResumeButton.getButtonLayout().setPadding((int) getResources().getDimension(R.dimen.button_main_menu_padding_left), 0, 0, 0);
        settingsButton.getButtonText().setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        settingsButton.getButtonLayout().setPadding((int) getResources().getDimension(R.dimen.button_main_menu_padding_left), 0, 0, 0);
        exitButton.getButtonText().setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        exitButton.getButtonLayout().setPadding((int) getResources().getDimension(R.dimen.button_main_menu_padding_left), 0, 0, 0);
    }

    /**
     * Показываем анимацию открытия
     */
    private void startOpenAnimation() {
//        Animation backgroundAnimation = AnimationUtils.loadAnimation(this, R.anim.main_menu_open_background);
//        findViewById(R.id.main_menu_layout).setAnimation(backgroundAnimation);

        Animation startResumeButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.main_menu_open);
        startResumeButton.getButtonLayout().startAnimation(startResumeButtonAnimation);

        Animation settingsButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.main_menu_open);
        settingsButtonAnimation.setStartOffset(100);
        settingsButton.getButtonLayout().startAnimation(settingsButtonAnimation);

        Animation exitButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.main_menu_open);
        exitButtonAnimation.setStartOffset(200);
        exitButton.getButtonLayout().startAnimation(exitButtonAnimation);
    }

    private void checkMenuModification() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getIntent() != null && getIntent().getIntExtra(MENU_MODIFICATION, START_MENU) == START_MENU) {
            setTheme(R.style.AppTheme_MainMenu);
            modification = START_MENU;
        } else {
            setTheme(R.style.AppTheme_MainMenu_Transparent);
            modification = RESUME_MENU;
        }
    }
}
