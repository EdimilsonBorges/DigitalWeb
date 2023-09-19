package br.radio.DigitalWeb.Notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import br.radio.DigitalWeb.Activitys.MainActivityPrincipal;
import br.radio.DigitalWeb.AsynData.AsynDataClassStatus;
import br.radio.DigitalWeb.AsynData.AsynDataClassStatusMetaDados;
import br.radio.DigitalWeb.BroadcastReceiver.BroadcastReceiverSair;
import br.radio.DigitalWeb.R;
import br.radio.DigitalWeb.Status.Status;

/**
 * Created by Edimilson Borges on 08/11/2018.
 **/

public class RadioNotificacao {

    private static final String ID_STRING_NOTIFICATION = "15";
    public static final int ID_INT_NOTIFICATION = 15;
    private static final String LOG_TAG = RadioNotificacao.class.getSimpleName();

    /* Main class variáveis */
    private static Notification mNotification;

    /**
     * Este método serve para Gerar a notificação
     * @param context Context na qual a notificação será gerada
     * @return retorna a notificação gerada
     */
    @SuppressLint("RestrictedApi")
    private static NotificationCompat.Builder getNotificationBuilder(Context context) {

        Bitmap bitmapPause = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_pause);
        Bitmap bitmapPlay = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_play);
        Bitmap icone = AsynDataClassStatusMetaDados.bitmap;
        Bitmap sair = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_sair);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custon_notificacao);

        NotificationCompat.Builder notificacao =  new NotificationCompat.Builder(context, ID_STRING_NOTIFICATION);
        Intent notifyIntente = new Intent(context, MainActivityPrincipal.class);

        notifyIntente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivities(context,0, new Intent[]{notifyIntente},PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        notificacao.setContentIntent(pendingIntent);
        notificacao.setSmallIcon(R.drawable.icone);
        notificacao.setCustomBigContentView(remoteViews);
        notificacao.getBigContentView().setImageViewBitmap(R.id.imageViewIcone,icone);
        notificacao.getBigContentView().setImageViewBitmap(R.id.imageViewSair,sair);
        notificacao.setAutoCancel(false);
        notificacao.setOngoing(true);
        notificacao.setPriority(NotificationCompat.PRIORITY_LOW);
        notificacao.setCategory(NotificationCompat.CATEGORY_SERVICE);

        setLista(remoteViews,context);

        if(Status.isTocando()){
            notificacao.getBigContentView().setTextViewText(R.id.textViewTitulo, AsynDataClassStatus.titulo);
            notificacao.getBigContentView().setTextViewText(R.id.textViewText,AsynDataClassStatus.artista);
            notificacao.getBigContentView().setImageViewBitmap(R.id.imageViewButonPlayPause,bitmapPause);
            return notificacao;

        }else {

            if(Status.isProcurandoServidor()){
                notificacao.getBigContentView().setTextViewText(R.id.textViewTitulo,"Rádio Digital Web");
                notificacao.getBigContentView().setTextViewText(R.id.textViewText,"Procurando servidor...");
                notificacao.getBigContentView().setImageViewBitmap(R.id.imageViewButonPlayPause,bitmapPause);
                return notificacao;
            } else {

                if (Status.isConectando()) {
                    notificacao.getBigContentView().setTextViewText(R.id.textViewTitulo,"Rádio Digital Web");
                    notificacao.getBigContentView().setTextViewText(R.id.textViewText,"Conectando...");
                    notificacao.getBigContentView().setImageViewBitmap(R.id.imageViewButonPlayPause,bitmapPause);
                    return notificacao;
                } else {

                    if (Status.isErroAoEncontrarServidor()) {
                        notificacao.getBigContentView().setTextViewText(R.id.textViewTitulo,"Rádio Digital Web");
                        notificacao.getBigContentView().setTextViewText(R.id.textViewText,"Erro ao encontrar servidor...");
                        notificacao.getBigContentView().setImageViewBitmap(R.id.imageViewButonPlayPause,bitmapPlay);
                        return notificacao;

                    } else {
                        if (Status.isErroAoConectar()) {
                            notificacao.getBigContentView().setTextViewText(R.id.textViewTitulo,"Rádio Digital Web");
                            notificacao.getBigContentView().setTextViewText(R.id.textViewText,"Erro ao conectar...");
                            notificacao.getBigContentView().setImageViewBitmap(R.id.imageViewButonPlayPause,bitmapPlay);
                            return notificacao;

                        } else {
                            notificacao.getBigContentView().setTextViewText(R.id.textViewTitulo,"Rádio Digital Web");
                            notificacao.getBigContentView().setTextViewText(R.id.textViewText,"Parado...");
                            notificacao.getBigContentView().setImageViewBitmap(R.id.imageViewButonPlayPause,bitmapPlay);
                            return notificacao;
                        }
                    }
                }
            }
        }
    }

    /**
     * Este método serve para criar e exibir a notificação
     * @param service carrega um Service para criar no Foreground
     */
    public static void show(Service service) {

        // cria canal de notificação
         createNotificationChannel(service);

        // constroi notificação
           mNotification = getNotificationBuilder(service).build();


        // exibe a notificação
        try{
            service.startForeground(ID_INT_NOTIFICATION, mNotification);
        }catch (Exception e){
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
        }
    }


    /**
     * Este método serve para atualizar e exibir a notificação
     * @param service carrega um Service para criar no Foreground
     */
    public static void update(final Service service) {


        // constroi notificação
        mNotification = getNotificationBuilder(service).build();

        // exibi a notificação atualizada
        // exibe a notificação
        try{
            NotificationManager notificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ID_INT_NOTIFICATION, mNotification);
        }catch (Exception e){
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
        }

    }

    /**
     * Este método serve para adicionar as ações nos botões da notificação através do Broadcast
     * @param remoteViews RemoteViews atual
     * @param context Context atual
     */
    private static void setLista(RemoteViews remoteViews, Context context) {

        Intent intentPlayPause = new Intent("NOTIFY_PLAYPAUSE2");
        Intent intentFechar = new Intent("NOTIFY_FECHAR2");

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S){
            PendingIntent pendingIntentPlayPause = PendingIntent.getBroadcast(context,0, intentPlayPause, PendingIntent.FLAG_IMMUTABLE);
            remoteViews.setOnClickPendingIntent(R.id.imageViewButonPlayPause, pendingIntentPlayPause );

            PendingIntent pendingIntentFechar = PendingIntent.getBroadcast(context,0, intentFechar, PendingIntent.FLAG_IMMUTABLE);
            remoteViews.setOnClickPendingIntent(R.id.imageViewSair, pendingIntentFechar );
        }else{
            PendingIntent pendingIntentPlayPause = PendingIntent.getBroadcast(context,0, intentPlayPause, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.imageViewButonPlayPause, pendingIntentPlayPause );

            PendingIntent pendingIntentFechar = PendingIntent.getBroadcast(context,0, intentFechar, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.imageViewSair, pendingIntentFechar );
        }
    }


    /**
     * Cria um canal de notificação
     * @param context Context atual no qual será criado o canal de notificações.
     */
    private static void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API level 26 ("Android O") suporte de canais de notificação.
            CharSequence name = "Notificação de Controle de Mídia";
            String description = "Notificação de Controle de Mídia";
            int importance = NotificationManager.IMPORTANCE_LOW;

            // cria canal
            NotificationChannel channel = new NotificationChannel(ID_STRING_NOTIFICATION, name, importance);
            channel.setDescription(description);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(channel);

        }
    }

}
