package com.el.game.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.el.game.R;

import org.andengine.audio.music.Music;
import org.andengine.engine.Engine;

public class MenuButton extends Button {

    public MenuButton(GameActivity activity, int resourceIdButton) {
        super(activity, resourceIdButton);
        getButtonText().setText(R.string.button_menu);
    }

    @Override
    public void onClick(View view) {
        final Engine engine = getActivity().getEngine();
        final Music backgroundMusic = ((GameActivity) getActivity()).getBackgroundMusic();
        if (engine == null) return;

        engine.stop();
        backgroundMusic.pause();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Что-то вроде меню")
                .setMessage(" блаблабла \n настройки \n блаблабла \n выход")
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        engine.start();
                        backgroundMusic.resume();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
