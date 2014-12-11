package com.el.game.ui.menu;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.el.game.R;
import com.el.game.ui.Button;
import com.el.game.ui.OnButtonClick;

import java.util.ArrayList;
import java.util.List;

abstract public class MenuWindowModel {

    protected static List<MenuWindowModel> backListMenu = new ArrayList<>();

    protected Activity activity;
    protected FrameLayout windowLayout;
    protected FrameLayout containerLayout;
    protected TextView windowTitle;
    protected FrameLayout windowActionBar;
    protected Button windowBackButton;

    private boolean isAnimateNow;

    protected MenuWindowModel(Activity activity, FrameLayout containerLayout) {
        this.activity = activity;
        this.containerLayout = containerLayout;
    }

    abstract protected int getWindowLayoutId();

    abstract protected void onCreateWindow();

    abstract protected void onCloseWindow();

    abstract protected void showOpenAnimation(long startOffset);

    abstract protected long showCloseAnimation();

    public void showWindow(boolean isWithBackgroundAnimation) {
        addToBackList();

        containerLayout.removeView(windowLayout);
        windowLayout = (FrameLayout) activity.getLayoutInflater().inflate(getWindowLayoutId(), null);
        windowLayout.setClickable(true);
        windowActionBar = (FrameLayout) activity.getLayoutInflater().inflate(R.layout.menu_title, windowLayout, false);
        windowLayout.addView(windowActionBar);
        containerLayout.addView(windowLayout);
        windowTitle = (TextView) windowActionBar.findViewById(R.id.title);
        windowBackButton = (Button) windowActionBar.findViewById(R.id.button_back);
        windowBackButton.setOnClickListener(new OnButtonClick() {
            @Override
            public void onClick(Button button, View view) {
                onBackPressed();
            }
        });

        onCreateWindow();

        isAnimateNow = true;
        long startOffset = showAnimationBackground(R.anim.menu_open_background, 0, isWithBackgroundAnimation, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimateNow = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        showAnimationActionBar(R.anim.menu_title_open);
        showOpenAnimation(startOffset);
    }

    public void closeWindow(final boolean isWithBackgroundAnimation, final MenuWindowModel newWindow) {
        isAnimateNow = true;
        showAnimationActionBar(R.anim.menu_title_close);
        long startOffset = showCloseAnimation();
        showAnimationBackground(R.anim.menu_close_background, startOffset, isWithBackgroundAnimation, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimateNow = false;
                onCloseWindow();
                containerLayout.removeView(windowLayout);
                if (newWindow != null) newWindow.showWindow(isWithBackgroundAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private long showAnimationBackground(int animResourceId, long startOffset,
                                         boolean isWithBackgroundAnimation, Animation.AnimationListener listener) {
        Animation animation = AnimationUtils.loadAnimation(activity, animResourceId);
        animation.setAnimationListener(listener);
        if (!isWithBackgroundAnimation) animation.setDuration(1);
        animation.setStartOffset(startOffset);
        animation.setFillAfter(true);
        windowLayout.startAnimation(animation);
        return animation.getDuration();
    }

    private void showAnimationActionBar(int animResourceId) {
        Animation animation = AnimationUtils.loadAnimation(activity, animResourceId);
        animation.setFillAfter(true);
        windowActionBar.startAnimation(animation);
    }

    protected void addToBackList() {
        if (!backListMenu.contains(this))
            backListMenu.add(this);
    }

    protected void removeFromBackList() {
        backListMenu.remove(this);
    }

    public static void onBackPressed() {
        if (backListMenu == null || backListMenu.isEmpty()) return;

        if (backListMenu.get(backListMenu.size() - 1).isAnimateNow) return;

        if (backListMenu.size() > 1) {
            backListMenu.get(backListMenu.size() - 1).closeWindow(true, backListMenu.get(backListMenu.size() - 2));
            backListMenu.remove(backListMenu.size() - 1);
        } else {
            backListMenu.get(0).closeWindow(true, null);
            backListMenu.remove(0);
        }
    }

    protected void setTitleText(int resource) {
        windowTitle.setText(resource);
    }

    protected FrameLayout getContainerLayout() {
        return containerLayout;
    }

    protected View getWindowLayout() {
        return windowLayout;
    }

    protected Activity getActivity() {
        return activity;
    }

    protected Context getContext() {
        return activity.getApplicationContext();
    }

    public static List<MenuWindowModel> getBackListMenu() {
        return backListMenu;
    }
}
