package com.el.game.utils;

import android.content.Context;

import com.el.game.R;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;

import java.io.IOException;

public class SoundControl {

    public static final String MUSIC_ENABLED = "MUSIC_ENABLED";
    public static final String MUSIC_VOLUME = "MUSIC_VOLUME";

    public static final boolean DEFAULT_MUSIC_ENABLED = true;
    public static final float DEFAULT_VOLUME = 1f;

    public enum Volume {
        HALF_VOLUME, FULL_VOLUME, ONE_FIFTH_VOLUME, FOUR_FIFTHS_VOLUME
    }

    private static SoundControl mInstance = null;
    private Context context;
    private MusicManager musicManager;
    private SoundManager soundManager;

    private Music backgroundMusic;
    private Sound pressButtonSound;

    private Volume volume = Volume.FULL_VOLUME;
    private float factor;
    private boolean isPlayMusicSound;

    private SoundControl(Context context, MusicManager musicManager, SoundManager soundManager) {
        this.context = context;
        this.musicManager = musicManager;
        this.soundManager = soundManager;
    }

    public synchronized static SoundControl getInstance(Context context, MusicManager musicManager, SoundManager soundManager) {
        if (mInstance == null)
            mInstance = new SoundControl(context, musicManager, soundManager);
        return mInstance;
    }

    public void initBgMusicFromAsset() throws IOException {
        backgroundMusic = MusicFactory.createMusicFromAsset(musicManager, context, "snd/background_music.mp3");
        backgroundMusic.setLooping(true);
    }

    public void initSoundsFromAsset() throws IOException {
        pressButtonSound = SoundFactory.createSoundFromResource(soundManager, context, R.raw.press_sound);
        pressButtonSound.setLooping(false);
    }

    public void reloadConfig() {
        isPlayMusicSound = Utils.load(context, MUSIC_ENABLED, DEFAULT_MUSIC_ENABLED);
        factor = Utils.load(context, MUSIC_VOLUME, DEFAULT_VOLUME);

        if (backgroundMusic == null) return;

        if (isPlayMusicSound)
            backgroundMusic.resume();
        else
            backgroundMusic.pause();
        setBgVolume(volume);

        if (pressButtonSound != null)
            pressButtonSound.setVolume(DEFAULT_VOLUME * factor);
    }

    public void playPressButtonSound() {
        if (pressButtonSound != null && isPlayMusicSound)
            pressButtonSound.play();
    }

    public void playBgMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.play();
            reloadConfig();
        }
    }

    public void stopBgMusic() {
        if (backgroundMusic != null)
            backgroundMusic.stop();
    }

    public void pauseBgMusic() {
        if (backgroundMusic != null && isPlayMusicSound)
            backgroundMusic.pause();
    }

    public void resumeBgMusic() {
        if (backgroundMusic != null && isPlayMusicSound)
            backgroundMusic.resume();
    }

    public void setBgVolume(Volume volume) {
        if (backgroundMusic != null) {
            this.volume = volume;
            float v;
            switch (volume) {
                case HALF_VOLUME:
                    v = 0.5f;
                    break;
                case FULL_VOLUME:
                    v = 1.0f;
                    break;
                case ONE_FIFTH_VOLUME:
                    v = 0.2f;
                    break;
                case FOUR_FIFTHS_VOLUME:
                    v = 0.8f;
                    break;
                default:
                    v = 0.0f;
                    break;
            }
            backgroundMusic.setVolume(v * factor);
        }
    }
}
