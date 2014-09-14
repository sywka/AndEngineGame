package com.el.game.ui;

import android.view.View;
import android.widget.FrameLayout;

import com.el.game.R;
import com.el.game.objects.Player;

import org.andengine.ui.activity.BaseGameActivity;

public class ControlButton extends Button {

    public static final int CONRTOL_TOUCH = 0;
    public static final int CONTROL_ACCELEROMETER = 1;
    private int control;
    private Player player;

    public ControlButton(BaseGameActivity activity, int resourceIdButton) {
        super(activity, resourceIdButton);
    }

    @Override
    protected void setDefaultValues(FrameLayout buttonLayout) {
        control = CONRTOL_TOUCH;
        buttonLayout.setBackgroundResource(R.drawable.control_touch);
    }

    @Override
    public void onClick(View view) {
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
