package com.el.game.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import com.el.game.R;

import org.andengine.engine.Engine;
import org.andengine.ui.activity.BaseGameActivity;

public class MenuButton extends Button {

    public MenuButton(BaseGameActivity activity, int resourceIdButton) {
        super(activity, resourceIdButton);
        getButtonText().setText(R.string.button_menu);
    }

    @Override
    public void onClick(View view) {
        final Engine engine = getActivity().getEngine();
        if (engine == null) return;

        engine.stop();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Что-то вроде меню")
                .setMessage(" блаблабла \n настройки \n блаблабла \n выход")
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        engine.start();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
