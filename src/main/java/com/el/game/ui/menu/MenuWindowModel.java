package com.el.game.ui.menu;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.el.game.R;
import com.el.game.ui.Button;
import com.el.game.ui.ImageButton;
import com.el.game.ui.OnButtonClick;

import java.util.ArrayList;
import java.util.List;

abstract public class MenuWindowModel {

    protected static List<MenuWindowModel> backListMenu = new ArrayList<>();

    protected Activity activity;
    protected FrameLayout menuLayout;
    protected FrameLayout activityContent;
    protected TextView title;
    protected Button backButton;
    protected FrameLayout actionBar;

    private boolean isAnimateNow;

    protected MenuWindowModel(Activity activity, FrameLayout activityContent) {
        this.activity = activity;
        this.activityContent = activityContent;
    }

    abstract protected int getMenuLayoutId();

    abstract protected void onCreateWindow();

    abstract protected void onCloseWindow();

    abstract protected void showOpenAnimation(long startOffset);

    abstract protected long showCloseAnimation();

    public void showWindow(boolean isWithBackgroundAnimation) {
        addToBackList();

        removeWindowLayout(false);
        menuLayout = (FrameLayout) activity.getLayoutInflater().inflate(getMenuLayoutId(), activityContent, false);
        actionBar = (FrameLayout) activity.getLayoutInflater().inflate(R.layout.menu_title, menuLayout, false);
        menuLayout.addView(actionBar);
        activityContent.addView(menuLayout);
        title = (TextView) actionBar.findViewById(R.id.title);
        backButton = new ImageButton(getActivity(), R.id.button_back, R.drawable.ic_back, new OnButtonClick() {
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
                removeWindowLayout(true);
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
        menuLayout.startAnimation(animation);
        return animation.getDuration();
    }

    private void showAnimationActionBar(int animResourceId) {
        Animation animation = AnimationUtils.loadAnimation(activity, animResourceId);
        animation.setFillAfter(true);
        actionBar.startAnimation(animation);
    }

    private void changeClickable(ViewGroup viewGroup, boolean flag) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setClickable(flag);
            if (view instanceof ViewGroup) {
                changeClickable((ViewGroup) view, flag);
            }
        }
    }

    private void removeWindowLayout(boolean isClickableActivityContent) {
        changeClickable(activityContent, isClickableActivityContent);
        activityContent.removeView(menuLayout);
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
        title.setText(resource);
    }

    protected FrameLayout getActivityContent() {
        return activityContent;
    }

    protected View getMenuLayout() {
        return menuLayout;
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
