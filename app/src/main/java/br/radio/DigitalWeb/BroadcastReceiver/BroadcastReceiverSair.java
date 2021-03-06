package br.radio.DigitalWeb.BroadcastReceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import br.radio.DigitalWeb.AsynData.AsynDataClassStatusMetaDados;
import br.radio.DigitalWeb.Services.PlayerService;
import br.radio.DigitalWeb.Notification.RadioNotificacao;

import static br.radio.DigitalWeb.Services.PlayerService.FECHAR_TODAS_ACTIVITYS;

/**
 * Created by Edimilson Borges on on 25/02/2020.
 **/

public class BroadcastReceiverSair extends BroadcastReceiver {

    Intent it;
    public Context context;
    public static final String NOTIFY_FECHAR = "NOTIFY_FECHAR";
    private final String LOG_TAG = BroadcastReceiverSair.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        it = new Intent(context, PlayerService.class);
        String valorDoExtraSair = intent.getStringExtra("NOTIFY_FECHAR");


        if(valorDoExtraSair != null && valorDoExtraSair.equals(NOTIFY_FECHAR)){

            try{
                notificationManager.cancel(RadioNotificacao.ID_INT_NOTIFICATION);
            }catch (Exception e){
                Log.e(LOG_TAG, "Exception: " + e.getMessage());
            }

            context.stopService(it);
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FECHAR_TODAS_ACTIVITYS));
        }

    }

}
