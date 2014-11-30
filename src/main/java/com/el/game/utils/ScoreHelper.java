package com.el.game.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.el.game.R;

import org.andengine.ui.activity.BaseGameActivity;

public class ScoreHelper {

    private static final String SCORE = "score";
    private BaseGameActivity activity;
    private TextView currentScoreView;
    private TextView highScoreView;

    private int currentScore = 0;
    private int highScore = 0;

    public ScoreHelper(BaseGameActivity activity) {
        this.activity = activity;
        currentScoreView = (TextView) activity.findViewById(R.id.current_score);
        highScoreView = (TextView) activity.findViewById(R.id.high_score);
        highScore = load();
        initView();
    }

    public void updateScore() {
        currentScore++;
        initView();
    }

    public void resetScore() {
        if (highScore < currentScore)
            highScore = currentScore;
        currentScore = 0;
        initView();
        save(highScore);
    }

    private void initView() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentScoreView.setText(String.valueOf(currentScore));
                highScoreView.setText(String.valueOf(highScore));
            }
        });
    }

    private void save(int value) {
        SharedPreferences prefs;
        SharedPreferences.Editor editor;

        prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        editor = prefs.edit();

        editor.putInt(SCORE, value);
        editor.commit();
    }

    private int load() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return prefs.getInt(SCORE, 0);
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}
