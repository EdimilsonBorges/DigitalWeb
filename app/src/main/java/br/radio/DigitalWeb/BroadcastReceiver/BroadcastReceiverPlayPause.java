package br.radio.DigitalWeb.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.radio.DigitalWeb.Services.PlayerService;

/**
 * Created by Edimilson Borges on on 25/02/2020.
 **/

public class BroadcastReceiverPlayPause extends BroadcastReceiver {

    Intent it;
    public Context context;
    public static final String NOTIFY_PLAYPAUSE = "NOTIFY_PLAYPAUSE";


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        it = new Intent(context, PlayerService.class);
        String valorDoExtraPlayPause = intent.getStringExtra("NOTIFY_PLAYPAUSE");

        if(valorDoExtraPlayPause != null && valorDoExtraPlayPause.equals(NOTIFY_PLAYPAUSE)){

            context.startService(it);
        }
    }
}
