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

        if (modification == START_MENU) {
            findViewById(R.id.main_menu_layout).setBackgroundColor(getResources().getColor(R.color.main_menu_background));
            startResumeButton = new TextButton(this, R.id.button_start_resume, R.string.button_menu_start, this);
        } else {
            findViewById(R.id.main_menu_layout).setBackgroundColor(getResources().getColor(R.color.main_menu_background_with_alpha));
            startResumeButton = new TextButton(this, R.id.button_start_resume, R.string.button_menu_resume, this);
        }
        settingsButton = new TextButton(this, R.id.button_settings, R.string.button_menu_settings, this);
        exitButton = new TextButton(this, R.id.button_exit, R.string.button_menu_exit, this);

        initPosition();
        startOpenAnimation();
    }

    @Override
    public void onClick(Button button, View view) {
        if (button == startResumeButton) {
            finish();
            overridePendingTransition(0, 0);
            if (modification == START_MENU)
                startActivity(new Intent(this, GameActivity.class));

        } else if (button == settingsButton) {

        } else if (button == exitButton) {
            setResult(RESULT_EXIT);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
        overridePendingTransition(0, 0);
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
        final Animation backgroundAnimation = AnimationUtils.loadAnimation(this, R.anim.main_menu_open_background);
        findViewById(R.id.main_menu_layout).post(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.main_menu_layout).startAnimation(backgroundAnimation);
            }
        });
        final Animation startResumeButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.main_menu_open);
        startResumeButton.getButtonLayout().post(new Runnable() {
            @Override
            public void run() {
                startResumeButton.getButtonLayout().startAnimation(startResumeButtonAnimation);
            }
        });

        final Animation settingsButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.main_menu_open);
        settingsButtonAnimation.setStartOffset(100);
        settingsButton.getButtonLayout().post(new Runnable() {
            @Override
            public void run() {
                settingsButton.getButtonLayout().startAnimation(settingsButtonAnimation);
            }
        });

        final Animation exitButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.main_menu_open);
        exitButtonAnimation.setStartOffset(200);
        exitButton.getButtonLayout().post(new Runnable() {
            @Override
            public void run() {
                exitButton.getButtonLayout().startAnimation(exitButtonAnimation);
            }
        });
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
