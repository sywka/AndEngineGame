package com.el.game.ui.menu;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.el.game.R;
import com.el.game.ui.Button;
import com.el.game.ui.OnButtonClick;
import com.el.game.ui.OnResumeMainMenu;

import java.util.ArrayList;
import java.util.List;

public class ExitConfirmMenu extends MenuWindowModel implements OnButtonClick {

    public enum Modification {
        START_MENU, RESUME_MENU
    }

    public enum Result {
        RESUME, FINISH
    }

    private Modification modification;
    private List<Button> buttons;
    private Result result;
    private OnResumeMainMenu listener;

    public ExitConfirmMenu(Activity activity, FrameLayout activityContent, Modification modification, OnResumeMainMenu listener) {
        super(activity, activityContent);
        this.listener = listener;
        this.modification = modification;
        this.buttons = new ArrayList<>();
    }

    @Override
    protected int getWindowLayoutId() {
        return R.layout.exit_confirm_menu;
    }

    @Override
    protected void onCreateWindow() {
        setTitleText(R.string.exit_confirm_menu_title);
        result = Result.RESUME;
        switch (modification) {
            case START_MENU:
                getWindowLayout().setBackgroundColor(getActivity().getResources().getColor(R.color.main_menu_background));

                break;
            case RESUME_MENU:
                getWindowLayout().setBackgroundColor(getActivity().getResources().getColor(R.color.main_menu_background_with_alpha));
                break;
        }
        buttons.clear();
        buttons.add((Button) getWindowLayout().findViewById(R.id.button_confirm1));
        buttons.add((Button) getWindowLayout().findViewById(R.id.button_not_confirm1));

        for (Button button : buttons)
            button.setOnClickListener(this);
    }

    @Override
    protected void onCloseWindow() {
        if (result != null)
            switch (result) {
                case RESUME:
                    if (listener != null)
                        listener.resume();
                    break;
                case FINISH:
                    getActivity().finish();
                    break;
            }
    }

    @Override
    protected void showOpenAnimation(long startOffset) {
        showAnimation(startOffset, R.anim.menu_button_open);
    }

    @Override
    protected long showCloseAnimation() {
        return showAnimation(0, R.anim.menu_button_close);
    }

    private long showAnimation(long startOffset, int animResourceId) {
        Animation animation;
        for (Button button : buttons) {
            startOffset += 100;
            animation = AnimationUtils.loadAnimation(activity, animResourceId);
            animation.setStartOffset(startOffset);
            animation.setFillAfter(true);
            button.startAnimation(animation);
        }
        return startOffset;
    }

    @Override
    public void onClick(Button button, View view) {
        switch (buttons.indexOf(button)) {
            case 0:
                result = Result.FINISH;
                closeWindow(true, null);
                break;
            case 1:
                result = Result.RESUME;
                onBackPressed();
                break;
        }
    }
}
