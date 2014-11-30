package com.el.game.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;

import com.el.game.R;
import com.el.game.objects.Player;

import org.andengine.ui.activity.BaseGameActivity;

public class ControlButton extends Button implements OnButtonClick {

    private static final String CONTROL = "control";
    public static final int CONRTOL_TOUCH = 0;
    public static final int CONTROL_ACCELEROMETER = 1;
    private int control;
    private Player player;

    public ControlButton(BaseGameActivity activity, int resourceIdButton) {
        super(activity, resourceIdButton);
        addListener(this);

    }

    @Override
    protected void setDefaultValues(FrameLayout buttonLayout) {
        control = load();
        switch (control) {
            case CONRTOL_TOUCH:
                buttonLayout.setBackgroundResource(R.drawable.control_touch);
                break;
            case CONTROL_ACCELEROMETER:
                buttonLayout.setBackgroundResource(R.drawable.control_accelerometer);
                break;
        }
    }

    @Override
    public void onClick(Button button, View view) {
        switch (control) {
            case CONRTOL_TOUCH:
                control = CONTROL_ACCELEROMETER;
                getButtonLayout().setBackgroundResource(R.drawable.control_accelerometer);
                break;
            case CONTROL_ACCELEROMETER:
                control = CONRTOL_TOUCH;
                getButtonLayout().setBackgroundResource(R.drawable.control_touch);
                break;
        }
        if (player != null)
            player.setMove(Player.IDLE);
        save(control);
    }

    private void save(int value) {
        SharedPreferences prefs;
        SharedPreferences.Editor editor;

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        editor = prefs.edit();

        editor.putInt(CONTROL, value);
        editor.commit();
    }

    private int load() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        return prefs.getInt(CONTROL, CONRTOL_TOUCH);
    }

    public int getControl() {
        return control;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
