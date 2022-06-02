package br.radio.DigitalWeb.BroadcastReceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import br.radio.DigitalWeb.Notification.RadioNotificacao;
import br.radio.DigitalWeb.R;
import br.radio.DigitalWeb.Services.PlayerService;

import static br.radio.DigitalWeb.Services.PlayerService.FECHAR_TODAS_ACTIVITYS;

/**
 * Created by Edimilson Borges on on 05/11/2018.
 **/

public class MyRadioNotificationBroadcastReceiver extends BroadcastReceiver {
    Intent it;
    Bitmap bitmapPause,bitmapPlay;

    public Context context;
    public static final String NOTIFY_PLAYPAUSE = "NOTIFY_PLAYPAUSE2";
    public static final String NOTIFY_FECHAR = "NOTIFY_FECHAR2";
    private final String LOG_TAG = MyRadioNotificationBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        bitmapPause = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_pause);
        bitmapPlay = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_play);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        it = new Intent(context, PlayerService.class);

        if(intent.getAction().equals(NOTIFY_PLAYPAUSE)) {

            context.startService(it);
        }
        if(intent.getAction().equals(NOTIFY_FECHAR)){

            try{
                notificationManager.cancel(RadioNotificacao.ID_INT_NOTIFICATION);
            }catch (Exception e){
                Log.e(LOG_TAG, "Exception: " + e.getMessage());
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(FECHAR_TODAS_ACTIVITYS));
            context.stopService(it);
        }
    }
}