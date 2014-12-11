package com.el.game.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.el.game.R;
import com.el.game.objects.Player;
import com.el.game.utils.Utils;

public class ControlButton extends Button {

    private static final String CONTROL = "control";
    public static final int CONTROL_TOUCH = 0;
    public static final int CONTROL_ACCELEROMETER = 1;
    private int control;
    private Player player;

    public ControlButton(Context context) {
        super(context);
        initButton();
    }

    public ControlButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton();
    }

    public ControlButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton();
    }

    private void initButton() {
        control = Utils.load(getContext(), CONTROL, CONTROL_TOUCH);
        switch (control) {
            case CONTROL_TOUCH:
                setBackgroundResource(R.drawable.control_touch);
                break;
            case CONTROL_ACCELEROMETER:
                setBackgroundResource(R.drawable.control_accelerometer);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (control) {
            case CONTROL_TOUCH:
                control = CONTROL_ACCELEROMETER;
                setBackgroundResource(R.drawable.control_accelerometer);
                break;
            case CONTROL_ACCELEROMETER:
                control = CONTROL_TOUCH;
                if (player != null)
                    player.setNewStep(1);
                setBackgroundResource(R.drawable.control_touch);
                break;
        }
        if (player != null)
            player.setMove(Player.IDLE);
        Utils.save(getContext(), CONTROL, control);
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
