package com.el.game.ui.menu;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.el.game.R;
import com.el.game.ui.GameActivity;
import com.el.game.utils.SoundControl;
import com.el.game.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenu extends MenuWindowModel implements SeekBar.OnSeekBarChangeListener {

    private CheckBox soundEnabled;
    private SeekBar soundVolume;

    private static final int SEEKBAR_SOUND_MAX = 50;
    private static final int SEEKBAR_CENCITY_MAX = 10;

    private List<View> animationViews;
    private SoundControl soundControl;

    public SettingsMenu(Activity activity, FrameLayout activityContent) {
        super(activity, activityContent);
        animationViews = new ArrayList<>();
    }

    @Override
    protected int getWindowLayoutId() {
        return R.layout.settings_menu;
    }

    @Override
    protected void onCreateWindow() {
        setTitleText(R.string.button_menu_settings);
        animationViews.clear();
        soundControl = SoundControl.getInstance(getContext(),
                ((GameActivity) getActivity()).getMusicManager(),
                ((GameActivity) getActivity()).getSoundManager());

        initSoundPrefLayout();
        initControlPrefLayout();

        animationViews.add(getWindowLayout().findViewById(R.id.sound_pref_layout));
        animationViews.add(getWindowLayout().findViewById(R.id.control_pref_layout));
    }

    public void initSoundPrefLayout() {
        boolean isSoundEnabled = Utils.load(getContext(), SoundControl.MUSIC_ENABLED, SoundControl.DEFAULT_MUSIC_ENABLED);

        soundVolume = (SeekBar) getWindowLayout().findViewById(R.id.seekbar_sound_volume);
        soundVolume.setMax(SEEKBAR_SOUND_MAX);
        soundVolume.setProgress((int) (Utils.load(getContext(), SoundControl.MUSIC_VOLUME,
                SoundControl.DEFAULT_VOLUME) * SEEKBAR_SOUND_MAX));
        soundVolume.setEnabled(isSoundEnabled);
        soundVolume.setOnSeekBarChangeListener(this);

        soundEnabled = (CheckBox) getWindowLayout().findViewById(R.id.checkbox_sound);
        soundEnabled.setChecked(isSoundEnabled);
        soundEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                soundVolume.setEnabled(isChecked);
                Utils.save(getContext(), SoundControl.MUSIC_ENABLED, isChecked);
                soundControl.reloadConfig();
            }
        });
    }

    public void initControlPrefLayout() {

    }

    @Override
    protected void onCloseWindow() {
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
        for (View view : animationViews) {
            startOffset += 100;
            animation = AnimationUtils.loadAnimation(activity, animResourceId);
            animation.setStartOffset(startOffset);
            animation.setFillAfter(true);
            view.startAnimation(animation);
        }
        return startOffset;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == soundVolume) {
            float factor = (float) progress / (float) SEEKBAR_SOUND_MAX;
            Utils.save(getContext(), SoundControl.MUSIC_VOLUME, factor);
            soundControl.reloadConfig();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
