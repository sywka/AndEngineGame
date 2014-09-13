package core;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.el.game.R;

public class ControlButton implements View.OnClickListener {

    private FrameLayout buttonLayout;
    private View buttonView;
    private Activity activity;

    public static final int CONRTOL_TOUCH = 0;
    public static final int CONTROL_ACCELEROMETER = 1;
    private int control;

    public ControlButton(Activity activity) {
        this.activity = activity;
        buttonLayout = (FrameLayout) activity.findViewById(R.id.button_layout);
        buttonView = activity.findViewById(R.id.button_onclick_view);
        buttonView.setOnClickListener(this);
        setDefaultValues();
    }

    private void setDefaultValues() {
        control = CONRTOL_TOUCH;
        buttonLayout.setBackgroundResource(R.drawable.control_touch);
    }

    @Override
    public void onClick(View view) {
        switch (control) {
            case CONRTOL_TOUCH:
                control = CONTROL_ACCELEROMETER;
                buttonLayout.setBackgroundResource(R.drawable.control_accelerometer);
                break;
            case CONTROL_ACCELEROMETER:
                control = CONRTOL_TOUCH;
                buttonLayout.setBackgroundResource(R.drawable.control_touch);
                break;
        }
    }

    public int getControl() {
        return control;
    }
}
