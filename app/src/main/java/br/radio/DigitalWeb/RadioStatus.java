package br.radio.DigitalWeb;

import android.app.Service;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;

import static br.radio.DigitalWeb.MainActivityPrincipal.btmPlay;

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

        Funcionalidades.setTocando(true);
        Funcionalidades.setConectando(false);
        Funcionalidades.setProcurandoServidor(false);
        Funcionalidades.setErroAoConectar(false);
        Funcionalidades.setErroAoEncontrarServidor(false);

        updateNotification();

        if(btmPlay != null){
            handlerMediaPlayer.post(() -> {
                btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_pause));
                // MainActivityPrincipal.animation.stop();
            });
        }
    }

    public void conectando() {

        Funcionalidades.setTocando(false);
        Funcionalidades.setConectando(true);
        Funcionalidades.setProcurandoServidor(false);
        Funcionalidades.setErroAoConectar(false);
        Funcionalidades.setErroAoEncontrarServidor(false);

        AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        updateNotification();

        if(btmPlay != null){
            handlerMediaPlayer.post(() -> {
                MainActivityPrincipal.textView_titulo.setText(R.string.conectandoNome);
                MainActivityPrincipal.textView_artista.setText("Aguarde...");
                MainActivityPrincipal.imageLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
                btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_pause));
                // MainActivityPrincipal.animation.stop();
            });
        }
    }

    public void parado() {

        Funcionalidades.setTocando(false);
        Funcionalidades.setConectando(false);
        Funcionalidades.setProcurandoServidor(false);
        Funcionalidades.setErroAoConectar(false);
        Funcionalidades.setErroAoEncontrarServidor(false);

        AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        updateNotification();

        if(btmPlay != null){
            handlerMediaPlayer.post(() -> {
                MainActivityPrincipal.textView_titulo.setText(R.string.paradoNome);
                MainActivityPrincipal.textView_artista.setText("Stop");
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

        Funcionalidades.setTocando(false);
        Funcionalidades.setConectando(false);
        Funcionalidades.setProcurandoServidor(false);
        Funcionalidades.setErroAoConectar(true);
        Funcionalidades.setErroAoEncontrarServidor(false);

        AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        if(Funcionalidades.isTocando() || MainActivityPrincipal.btmPlay != null){

            updateNotification();
        }

        if(btmPlay != null){
            handlerMediaPlayer.post(() -> {
                MainActivityPrincipal.textView_titulo.setText(context.getString(R.string.erroConexaoNome));
                MainActivityPrincipal.textView_artista.setText("Ops");
                MainActivityPrincipal.imageLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
                btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_play));
                // MainActivityPrincipal.animation.stop();
            });
        }
    }

    public void procurarServidor() {

        Funcionalidades.setTocando(false);
        Funcionalidades.setConectando(false);
        Funcionalidades.setProcurandoServidor(true);
        Funcionalidades.setErroAoConectar(false);
        Funcionalidades.setErroAoEncontrarServidor(false);

        AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        updateNotification();

        if(btmPlay != null){
            handlerMediaPlayer.post(() -> {
                MainActivityPrincipal.textView_titulo.setText(R.string.procurando_Servidor);
                MainActivityPrincipal.textView_artista.setText("Aguarde...");
                MainActivityPrincipal.imageLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
                btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_pause));
                //MainActivityPrincipal.animation.stop();
            });
        }
    }

    public void erroAoEncontrarServidor(){

        Funcionalidades.setTocando(false);
        Funcionalidades.setConectando(false);
        Funcionalidades.setProcurandoServidor(false);
        Funcionalidades.setErroAoConectar(false);
        Funcionalidades.setErroAoEncontrarServidor(true);

        AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

        if(Funcionalidades.isTocando() || MainActivityPrincipal.btmPlay != null){
            updateNotification();
        }


        if(MainActivityPrincipal.btmPlay != null){
            handlerMediaPlayer.post(() -> {

                MainActivityPrincipal.textView_titulo.setText(context.getString(R.string.erro_ao_encontrar_servidor));
                MainActivityPrincipal.textView_artista.setText("Ops.");
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
