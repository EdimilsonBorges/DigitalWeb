package br.radio.DigitalWeb;

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
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.media.session.MediaSessionCompat;

/**
 * NotificationRadio class
 */
public final class NotificationRadio {

    /* Define log tag */
    private static final String LOG_TAG = NotificationRadio.class.getSimpleName();
    private static final String ID_STRING_NOTIFICATION = "15";
    private static final int ID_INT_NOTIFICATION = 15;


    /* Main class variáveis */
    private static Notification mNotification;
    private static MediaSessionCompat mSession;


    /**
     * Este método serve para criar e exibir a notificação
     * @param service carrega um Service para criar no Foreground
     * @param session carrega um MediaSessionCompat
     */
    public static void show(Service service, MediaSessionCompat session) {
        // salvar serviço e sessão
        mSession = session;

        // cria canal de notificação
        createNotificationChannel(service);

        // constroi notificação
        mNotification = getNotificationBuilder(service).build();

        // notificação de exibição
        service.startForeground(ID_INT_NOTIFICATION, mNotification);
    }

    /**
     * Este método serve para atualizar e exibir a notificação
     * @param service carrega um Service para criar no Foreground
     * @param session carrega um MediaSessionCompat
     */
    public static void update(final Service service, MediaSessionCompat session) {


        // constroi notificação
        mNotification = getNotificationBuilder(service).build();
        mSession = session;

        // exibi a notificação atualizada
        NotificationManager notificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_INT_NOTIFICATION, mNotification);

    }


    /* Cria um construtor de notificações */

    /**
     * Este método serve para Gerar a notificação
     * @param context Context na qual a notificação será gerada
     * @return retorna a notificação gerada
     */
    private static NotificationCompat.Builder getNotificationBuilder(Context context) {
        Bitmap  icone;

        if(AsynDataClassStatusMetaDados.bitmap != null){
            icone = AsynDataClassStatusMetaDados.bitmap;
        }else {
            icone = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        }

        // intenção explícita de fechar a notificacao
        Intent intentFechar = new Intent(context, BroadcastReceiverSair.class);
        intentFechar.putExtra("NOTIFY_FECHAR","NOTIFY_FECHAR");
        PendingIntent pendingIntentBroadcasterSair = PendingIntent.getBroadcast(context,0, intentFechar,PendingIntent.FLAG_UPDATE_CURRENT);

        // intenção explícita para iniciar a reprodução
        Intent intentPlayPause = new Intent(context,BroadcastReceiverPlayPause.class);
        intentPlayPause.putExtra("NOTIFY_PLAYPAUSE","NOTIFY_PLAYPAUSE");
        PendingIntent pendingIntentBroadcasterPlayPause = PendingIntent.getBroadcast(context,0, intentPlayPause,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(context,MainActivityPrincipal.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        // pilha traseira artificial para Activity iniciada.
        // -> navegar para trás a partir da atividade leva à tela inicial.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // backstack: adiciona back stack para Intent (mas não o próprio Intent)
        stackBuilder.addParentStack (MainActivityPrincipal.class);
        // backstack: adicione intenção explícita para toque de notificação


        // wrapper de intenção pendente para toque de notificação
//         //PendingIntent tapActionPendingIntent = PendingIntent.getService (mService, 0, tapActionIntent, 0);
        // wrapper de intenção pendente para ação de parada de notificação
        // PendingIntent stopActionPendingIntent = PendingIntent.getService(context, 10, stopActionIntent, 0);
        // wrapper de intenção pendente para ação de início de notificação
        // PendingIntent playPauseActionPendingIntent = PendingIntent.getService(context, 11, playPauseActionIntent, 0);
        // wrapper de intenção pendente para ação de furto de notificação
        // PendingIntent fecharActionPendingIntent = PendingIntent.getService(context, 12, fecharActionIntent, 0);

        // cria estilo de mídia
        //  android.support.v4.media.app.NotificationCompat.MediaStyle style = new  android.support.v4.media.app.NotificationCompat.MediaStyle();
        // style.setMediaSession(mSession.getSessionToken());
        //style.setShowActionsInCompactView(0);
        // style.setShowCancelButton(true); // pre-Lollipop workaround
        //style.setCancelButtonIntent(swipeActionPendingIntent);

        // construir notificação no construtor
        NotificationCompat.Builder notificacao;
        notificacao = new NotificationCompat.Builder(context, ID_STRING_NOTIFICATION);
        notificacao.setAutoCancel(true);
        notificacao.setOnlyAlertOnce(true);
        notificacao.setOngoing(true);
        notificacao.setSmallIcon(R.drawable.icone);
        notificacao.setPriority(NotificationCompat.PRIORITY_LOW);
        notificacao.setCategory(NotificationCompat.CATEGORY_SERVICE);

        if(!AsynDataClassStatusMetaDados.singleParsedPlayng.isEmpty()){

            notificacao.setContentTitle(AsynDataClassStatus.titulo);
        }else {
            notificacao.setContentTitle("Rádio Digital Web");

        }

        notificacao.setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1).setShowCancelButton(true).setMediaSession(mSession.getSessionToken()));
        notificacao.setLargeIcon(icone);
        notificacao.setContentIntent(pendingIntent);


        if(Funcionalidades.isTocando()){
            notificacao.setContentTitle(AsynDataClassStatus.titulo);
            notificacao.setContentText(AsynDataClassStatus.artista);
            notificacao.addAction(R.mipmap.ic_pause,"Pause",pendingIntentBroadcasterPlayPause);
            notificacao.addAction(R.mipmap.ic_sair,"Fechar",pendingIntentBroadcasterSair);
            return notificacao;

        }else {

            if(Funcionalidades.isProcurandoServidor()){
                notificacao.setContentTitle("Rádio Digital Web");
                notificacao.setContentText("Procurando servidor...");
                notificacao.addAction(R.mipmap.ic_pause,"Pause",pendingIntentBroadcasterPlayPause);
                notificacao.addAction(R.mipmap.ic_sair,"Fechar",pendingIntentBroadcasterSair);
                return notificacao;
            } else {

                if (Funcionalidades.isConectando()) {
                    notificacao.setContentTitle("Rádio Digital Web");
                    notificacao.setContentText("Conectando...");
                    notificacao.addAction(R.mipmap.ic_pause, "Pause", pendingIntentBroadcasterPlayPause);
                    notificacao.addAction(R.mipmap.ic_sair, "Fechar", pendingIntentBroadcasterSair);
                    return notificacao;
                } else {

                    if (Funcionalidades.isErroAoEncontrarServidor()) {
                        notificacao.setContentTitle("Rádio Digital Web");
                        notificacao.setContentText("Erro ao encontrar servidor...");
                        notificacao.addAction(R.mipmap.ic_play, "Play", pendingIntentBroadcasterPlayPause);
                        notificacao.addAction(R.mipmap.ic_sair, "Fechar", pendingIntentBroadcasterSair);
                        return notificacao;

                    } else {
                        if (Funcionalidades.isErroAoConectar()) {
                            notificacao.setContentTitle("Rádio Digital Web");
                            notificacao.setContentText("Erro ao conectar...");
                            notificacao.addAction(R.mipmap.ic_play, "Play", pendingIntentBroadcasterPlayPause);
                            notificacao.addAction(R.mipmap.ic_sair, "Fechar", pendingIntentBroadcasterSair);
                            return notificacao;

                        } else {
                            notificacao.setContentTitle("Rádio Digital Web");
                            notificacao.setContentText("Parado...");
                            notificacao.addAction(R.mipmap.ic_play, "Play", pendingIntentBroadcasterPlayPause);
                            notificacao.addAction(R.mipmap.ic_sair, "Fechar", pendingIntentBroadcasterSair);
                            return notificacao;
                        }
                    }

                }
            }

        }

    }

    /* Cria um canal de notificação */
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