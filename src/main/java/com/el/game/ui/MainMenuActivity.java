package com.el.game.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.el.game.R;

public class MainMenuActivity extends Activity implements OnButtonClick {

    public static final String MENU_MODIFICATION = "menu_modification";
    public static final int START_MENU = 0;
    public static final int RESUME_MENU = 1;
    public static final int RESULT_EXIT = 2;

    private int modification;
    private LinearLayout mainMenuLayout;
    private TextButton startResumeButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkMenuModification();
        setContentView(R.layout.activity_main_menu);

        mainMenuLayout = (LinearLayout) findViewById(R.id.main_menu_layout);

        if (modification == START_MENU) {
            mainMenuLayout.setBackgroundColor(getResources().getColor(R.color.main_menu_background));
            startResumeButton = new TextButton(this, R.id.button_start_resume, R.string.button_menu_start, this);
        } else {
            mainMenuLayout.setBackgroundColor(getResources().getColor(R.color.main_menu_background_with_alpha));
            startResumeButton = new TextButton(this, R.id.button_start_resume, R.string.button_menu_resume, this);
        }
        settingsButton = new TextButton(this, R.id.button_settings, R.string.button_menu_settings, this);
        exitButton = new TextButton(this, R.id.button_exit, R.string.button_menu_exit, this);

        initPosition();
        showAnimation(R.anim.main_menu_open_background, R.anim.main_menu_open, null);
    }

    @Override
    public void onClick(Button button, View view) {
        if (button == startResumeButton) {
            showCloseAnimation();

        } else if (button == settingsButton) {

        } else if (button == exitButton) {
            setResult(RESULT_EXIT);
            showCloseAnimation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        showCloseAnimation();
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
    private void showAnimation(int animIdBackground, int animIdMenu, Animation.AnimationListener listener) {
        final Animation backgroundAnimation = AnimationUtils.loadAnimation(this, animIdBackground);
        backgroundAnimation.setAnimationListener(listener);
        mainMenuLayout.post(new Runnable() {
            @Override
            public void run() {
                mainMenuLayout.startAnimation(backgroundAnimation);
            }
        });
        final Animation startResumeButtonAnimation = AnimationUtils.loadAnimation(this, animIdMenu);
        startResumeButton.getButtonLayout().post(new Runnable() {
            @Override
            public void run() {
                startResumeButton.getButtonLayout().startAnimation(startResumeButtonAnimation);
            }
        });

        final Animation settingsButtonAnimation = AnimationUtils.loadAnimation(this, animIdMenu);
        settingsButtonAnimation.setStartOffset(100);
        settingsButton.getButtonLayout().post(new Runnable() {
            @Override
            public void run() {
                settingsButton.getButtonLayout().startAnimation(settingsButtonAnimation);
            }
        });

        final Animation exitButtonAnimation = AnimationUtils.loadAnimation(this, animIdMenu);
        exitButtonAnimation.setStartOffset(200);
        exitButton.getButtonLayout().post(new Runnable() {
            @Override
            public void run() {
                exitButton.getButtonLayout().startAnimation(exitButtonAnimation);
            }
        });
    }

    private void showCloseAnimation() {
        showAnimation(R.anim.main_menu_close_background, R.anim.main_menu_close, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                overridePendingTransition(0, 0);

                if (modification == START_MENU)
                    startActivity(new Intent(MainMenuActivity.this, GameActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

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
