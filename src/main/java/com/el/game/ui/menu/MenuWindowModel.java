package com.el.game.ui.menu;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.el.game.R;

import java.util.ArrayList;
import java.util.List;

abstract public class MenuWindowModel {

    protected static List<MenuWindowModel> backListMenu = new ArrayList<MenuWindowModel>();

    protected Activity activity;
    protected View menuLayout;
    protected FrameLayout activityContent;

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

        removeMenu(false);
        menuLayout = activity.getLayoutInflater().inflate(getMenuLayoutId(), null);
        activityContent.addView(menuLayout);
        onCreateWindow();

        isAnimateNow = true;
        long startOffset = showAnimationBackground(R.anim.main_menu_open_background, 0, isWithBackgroundAnimation, new Animation.AnimationListener() {
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
        showOpenAnimation(startOffset);
    }

    public void closeWindow(final boolean isWithBackgroundAnimation, final MenuWindowModel newWindow) {
        isAnimateNow = true;
        long startOffset = showCloseAnimation();
        showAnimationBackground(R.anim.main_menu_close_background, startOffset, isWithBackgroundAnimation, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onCloseWindow();
                removeMenu(true);
                isAnimateNow = false;
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

    private void removeMenu(boolean isClickableActivityContent) {
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

    public FrameLayout getActivityContent() {
        return activityContent;
    }

    public View getMenuLayout() {
        return menuLayout;
    }

    public Activity getActivity() {
        return activity;
    }

    public static List<MenuWindowModel> getBackListMenu() {
        return backListMenu;
    }
}
