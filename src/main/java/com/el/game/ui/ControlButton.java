package com.el.game.ui;

import android.view.View;
import android.widget.FrameLayout;

import com.el.game.R;
import com.el.game.objects.Player;
import com.el.game.utils.Utils;

import org.andengine.ui.activity.BaseGameActivity;

public class ControlButton extends Button implements OnButtonClick {

    private static final String CONTROL = "control";
    public static final int CONTROL_TOUCH = 0;
    public static final int CONTROL_ACCELEROMETER = 1;
    private int control;
    private Player player;

    public ControlButton(BaseGameActivity activity, int resourceIdButton) {
        super(activity, resourceIdButton);
        addListener(this);
    }

    @Override
    protected void setDefaultValues(FrameLayout buttonLayout) {
        control = Utils.load(getContext(), CONTROL, CONTROL_TOUCH);
        switch (control) {
            case CONTROL_TOUCH:
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
            case CONTROL_TOUCH:
                control = CONTROL_ACCELEROMETER;
                getButtonLayout().setBackgroundResource(R.drawable.control_accelerometer);
                break;
            case CONTROL_ACCELEROMETER:
                control = CONTROL_TOUCH;
                if (player != null)
                    player.setNewStep(1);
                getButtonLayout().setBackgroundResource(R.drawable.control_touch);
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
