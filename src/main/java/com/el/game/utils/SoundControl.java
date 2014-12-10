package com.el.game.utils;

import android.content.Context;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;

import java.io.IOException;

public class SoundControl {

    public static final String MUSIC_ENABLED = "MUSIC_ENABLED";
    public static final String MUSIC_VOLUME = "MUSIC_VOLUME";

    public static final boolean DEFAULT_MUSIC_ENABLED = true;
    public static final float DEFAULT_MUSIC_VOLUME = 1f;

    public enum Volume {
        HALF_VOLUME, FULL_VOLUME, ONE_FIFTH_VOLUME, FOUR_FIFTHS_VOLUME
    }

    private static SoundControl mInstance = null;
    private Context context;
    private MusicManager musicManager;
    private Music backgroundMusic;
    private Volume volume = Volume.FULL_VOLUME;
    private float factor;
    private boolean isPlayBgMusic;

    private SoundControl(Context context, MusicManager musicManager) {
        this.context = context;
        this.musicManager = musicManager;
    }

    public synchronized static SoundControl getInstance(Context context, MusicManager musicManager) {
        if (mInstance == null)
            mInstance = new SoundControl(context, musicManager);
        return mInstance;
    }

    public void initBgMusicFromAsset(String assetPath, boolean isLooping) throws IOException {
        backgroundMusic = MusicFactory.createMusicFromAsset(musicManager, context, assetPath);
        backgroundMusic.setLooping(isLooping);
    }

    public void reloadConfig() {
        isPlayBgMusic = Utils.load(context, MUSIC_ENABLED, DEFAULT_MUSIC_ENABLED);
        factor = Utils.load(context, MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);

        if (backgroundMusic == null) return;

        if (isPlayBgMusic)
            backgroundMusic.resume();
        else
            backgroundMusic.pause();
        setVolume(volume);
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
        if (backgroundMusic != null && isPlayBgMusic)
            backgroundMusic.pause();
    }

    public void resumeBgMusic() {
        if (backgroundMusic != null && isPlayBgMusic)
            backgroundMusic.resume();
    }

    public void setVolume(Volume volume) {
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
