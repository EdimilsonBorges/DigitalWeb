package br.radio.DigitalWeb.Status;

import android.app.Service;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;

import br.radio.DigitalWeb.Activitys.MainActivityPrincipal;
import br.radio.DigitalWeb.AsynData.AsynDataClassStatusMetaDados;
import br.radio.DigitalWeb.Notification.NotificationRadio;
import br.radio.DigitalWeb.Notification.RadioNotificacao;
import br.radio.DigitalWeb.R;
import br.radio.DigitalWeb.Services.PlayerService;

import static br.radio.DigitalWeb.Activitys.MainActivityPrincipal.btmPlay;

/**
 * Created by Edimilson Borges em 04/05/2022.
 */

public class RadioStatus {

    private final Context context;
    private final MediaSessionCompat mediaSessionCompat;
    private final Handler handlerMediaPlayer;

    public RadioStatus(Context context, MediaSessionCompat mediaSessionCompat){

        this.context = context;
        this.mediaSessionCompat = mediaSessionCompat;
        handlerMediaPlayer = new Handler(Looper.getMainLooper());
    }

    public void tocando(){

        Status.setTocando(true);
        Status.setConectando(false);
        Status.setProcurandoServidor(false);
        Status.setErroAoConectar(false);
        Status.setErroAoEncontrarServidor(false);

        updateNotification();

        if(btmPlay != null){
            handlerMediaPlayer.post(() -> {
                btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_pause));
                // MainActivityPrincipal.animation.stop();
            });
        }
    }

    public void conectando() {

        Status.setTocando(false);
        Status.setConectando(true);
        Status.setProcurandoServidor(false);
        Status.setErroAoConectar(false);
        Status.setErroAoEncontrarServidor(false);

        AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        updateNotification();

        if(btmPlay != null){
            handlerMediaPlayer.post(() -> {
                MainActivityPrincipal.textView_titulo.setText(R.string.conectandoNome);
                MainActivityPrincipal.textView_artista.setText(R.string.aguarde);
                MainActivityPrincipal.imageLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
                btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_pause));
                // MainActivityPrincipal.animation.stop();
            });
        }
    }

    public void parado() {

        Status.setTocando(false);
        Status.setConectando(false);
        Status.setProcurandoServidor(false);
        Status.setErroAoConectar(false);
        Status.setErroAoEncontrarServidor(false);

        AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        updateNotification();

        if(btmPlay != null){
            handlerMediaPlayer.post(() -> {
                MainActivityPrincipal.textView_titulo.setText(R.string.paradoNome);
                MainActivityPrincipal.textView_artista.setText(R.string.stop);
                MainActivityPrincipal.imageLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
                btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_play));
                //MainActivityPrincipal.animation.stop();
            });
        }
        if(PlayerService.simpleExoPlayer != null){
        PlayerService.simpleExoPlayer.stop();
        PlayerService.simpleExoPlayer.release();
        PlayerService.simpleExoPlayer = null;
        }
    }

    public void erroAoConectar() {

        Status.setTocando(false);
        Status.setConectando(false);
        Status.setProcurandoServidor(false);
        Status.setErroAoConectar(true);
        Status.setErroAoEncontrarServidor(false);

        AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        if(Status.isTocando() || MainActivityPrincipal.btmPlay != null){

            updateNotification();
        }

        if(btmPlay != null){
            handlerMediaPlayer.post(() -> {
                MainActivityPrincipal.textView_titulo.setText(context.getString(R.string.erroConexaoNome));
                MainActivityPrincipal.textView_artista.setText(R.string.ops);
                MainActivityPrincipal.imageLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
                btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_play));
                // MainActivityPrincipal.animation.stop();
            });
        }
    }

    public void procurarServidor() {

        Status.setTocando(false);
        Status.setConectando(false);
        Status.setProcurandoServidor(true);
        Status.setErroAoConectar(false);
        Status.setErroAoEncontrarServidor(false);

        AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        updateNotification();

        if(btmPlay != null){
            handlerMediaPlayer.post(() -> {
                MainActivityPrincipal.textView_titulo.setText(R.string.procurando_Servidor);
                MainActivityPrincipal.textView_artista.setText(R.string.aguarde);
                MainActivityPrincipal.imageLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
                btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_pause));
                //MainActivityPrincipal.animation.stop();
            });
        }
    }

    public void erroAoEncontrarServidor(){

        Status.setTocando(false);
        Status.setConectando(false);
        Status.setProcurandoServidor(false);
        Status.setErroAoConectar(false);
        Status.setErroAoEncontrarServidor(true);

        AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        if(Status.isTocando() || MainActivityPrincipal.btmPlay != null){
            updateNotification();
        }


        if(MainActivityPrincipal.btmPlay != null){
            handlerMediaPlayer.post(() -> {

                MainActivityPrincipal.textView_titulo.setText(context.getString(R.string.erro_ao_encontrar_servidor));
                MainActivityPrincipal.textView_artista.setText(R.string.ops);
                MainActivityPrincipal.imageLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
                MainActivityPrincipal.btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_play));
                //MainActivityPrincipal.animation.stop();
            });

        }
    }

    private void updateNotification() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            RadioNotificacao.update((Service) PlayerService.context);
        }else{
            NotificationRadio.update((Service) context,mediaSessionCompat);
        }
    }
}
