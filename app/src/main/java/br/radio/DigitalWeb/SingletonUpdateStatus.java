package br.radio.DigitalWeb;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static br.radio.DigitalWeb.MainActivityPrincipal.btmPlay;

public class SingletonUpdateStatus {
    private final Handler handlerMediaPlayer;
    public Handler handlerSeekBar;
    public Runnable runnable;
    public int tempo = 0;
    public int duracao = (551*(60*1000))*60;
    public boolean  aovivo;
    private static SingletonUpdateStatus instance;

    private SingletonUpdateStatus(){

        handlerMediaPlayer = new Handler(Looper.getMainLooper());
        handlerSeekBar = new Handler(Looper.getMainLooper());
    }

    public static SingletonUpdateStatus getInstance(){

        if(instance == null){
            instance = new SingletonUpdateStatus();
        }
        return instance;
    }

    public void atualizarProgressBarTime(){
        if(Funcionalidades.isTocando()){
            TempoParaAtualizar();

           // handlerMediaPlayer.post(new Runnable() {
            handlerMediaPlayer.post(() -> { // Lambda

                tempo = tempo+1000;

                int correntPosition = tempo;
                // int correntBuffer = (int)simpleExoPlayer.getBufferedPosition();
                int correntMax = duracao;


                if(tempo >= duracao || tempo < 0){
                    tempo = 0;

                    if(Funcionalidades.isTocando() && !aovivo){
                        AtualizarMetaData();
                    }
                }


                int tempoDecorridoLeftInMilissegundos = tempo;
                int tempoTotalLeftInMilissegundos = duracao;

                if(btmPlay != null){

                   // handlerMediaPlayer.post(new Runnable() {
                    handlerMediaPlayer.post(() -> { // Lambda

                        MainActivityPrincipal.seekBar2.setMax(correntMax);
                        MainActivityPrincipal.seekBar2.setProgress(correntPosition);
                        // MainActivityPrincipal.seekBar2.setSecondaryProgress(correntBuffer);

                        int horas = (tempoDecorridoLeftInMilissegundos / 3600000) % 24;
                        int minutos = (tempoDecorridoLeftInMilissegundos / 60000) % 60;
                        int segundos = (tempoDecorridoLeftInMilissegundos / 1000) % 60;

                        int horas2 = (tempoTotalLeftInMilissegundos / 3600000) % 24;
                        int minutos2 = (tempoTotalLeftInMilissegundos / 60000) % 60;
                        int segundos2 = (tempoTotalLeftInMilissegundos / 1000) % 60;

                        if(horas2 >= 1){
                            String formatoDoTempoDecorrido = String.format(Locale.getDefault(),"%02d:%02d:%02d",horas, minutos, segundos);
                            String formatoDoTempoTotal = String.format(Locale.getDefault(),"%02d:%02d:%02d",horas2, minutos2, segundos2);

                            if(horas2 != 23){
                                MainActivityPrincipal.textViewTimeDecorrido.setText(formatoDoTempoDecorrido);
                                MainActivityPrincipal.textViewTimeTotal.setText(formatoDoTempoTotal);
                            }else {
                                MainActivityPrincipal.textViewTimeDecorrido.setText(formatoDoTempoDecorrido);
                                MainActivityPrincipal.textViewTimeTotal.setText("-- : -- : --");
                            }
                        }else {
                            String formatoDoTempoDecorrido = String.format(Locale.getDefault(),"%02d:%02d", minutos, segundos);
                            String formatoDoTempoTotal = String.format(Locale.getDefault(),"%02d:%02d", minutos2, segundos2);

                            MainActivityPrincipal.textViewTimeDecorrido.setText(formatoDoTempoDecorrido);
                            MainActivityPrincipal.textViewTimeTotal.setText(formatoDoTempoTotal);
                        }
                    });
                }
            });
        }

       // Runnable runnable = new Runnable() {

        runnable = this::atualizarProgressBarTime; // Lambda
        handlerSeekBar.postDelayed(runnable,1000);
    }

    public void TempoParaAtualizar(){

        // "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
        // "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
        // "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
        // "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
        // "yyMMddHHmmssZ"-------------------- 010704120856-0700
        // "K:mm a, z" ----------------------- 0:08 PM, PDT
        // "h:mm a" -------------------------- 12:08 PM
        // "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01


        @SuppressLint("SimpleDateFormat") SimpleDateFormat segundosFormat = new SimpleDateFormat("ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat minutoFormat = new SimpleDateFormat("mm");
        minutoFormat.setTimeZone(TimeZone.getTimeZone("GMT-03:00"));

        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date segundo_atual = cal.getTime();
        Date minuto_atual = cal.getTime();

        String segundos = segundosFormat.format(segundo_atual);
        String minutos = minutoFormat.format(minuto_atual);
        int minutoSegundo = Integer.parseInt(minutos+segundos);

        if(minutoSegundo >= 100 && minutoSegundo <= 101){
            AsynDataClassStatus.singleParsedStatus = "";
            AtualizarStatus();
            AtualizarMetaData();
        }else if(minutoSegundo >= 3100 && minutoSegundo <= 3101){
            AsynDataClassStatus.singleParsedStatus = "";
            AtualizarStatus();
            AtualizarMetaData();
        }else if(minutoSegundo >= 4100 && minutoSegundo <= 4101){
            AsynDataClassStatus.singleParsedStatus = "";
            AtualizarStatus();
            AtualizarMetaData();
        }
    }

    public void AtualizarMetaData(){

        try {
            AsynDataClassStatusMetaDados processo = new AsynDataClassStatusMetaDados(PlayerService.it,PlayerService.context,PlayerService.mediaSessionCompat);
            processo.execute();

        }catch (Exception e){
            e.getMessage();
        }
    }

    public void AtualizarStatus() {

        try {

            AsynDataClassStatus process = new AsynDataClassStatus(PlayerService.it, PlayerService.context, PlayerService.mediaSessionCompat);
            process.execute();

        }catch (Exception e){

            AsynDataClassStatusMetaDados.bitmap = BitmapFactory.decodeResource(PlayerService.context.getResources(), R.drawable.logo);

            if(btmPlay != null){
                handlerMediaPlayer.post(() -> {

                    MainActivityPrincipal.textView_titulo.setText(R.string.tocandoNome);
                    MainActivityPrincipal.textView_artista.setText("No Ar");
                    MainActivityPrincipal.imageLogo.setImageDrawable(PlayerService.context.getResources().getDrawable(R.drawable.logo));
                    btmPlay.setImageDrawable(PlayerService.context.getResources().getDrawable(R.mipmap.ic_pause));
                    // MainActivityPrincipal.animation.start();

                });
            }
        }
    }
}
