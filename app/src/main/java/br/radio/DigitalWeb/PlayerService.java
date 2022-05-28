package br.radio.DigitalWeb;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.List;

import static br.radio.DigitalWeb.MainActivityPrincipal.btmPlay;

/**
 * Created by Edimilson Borges on 04/11/2018.
 **/

public class  PlayerService extends MediaBrowserServiceCompat {

    private NotificationManager notificationManager;
    public static Context context;
    private Handler handlerMediaPlayer;
    public static MediaSessionCompat mediaSessionCompat;
    private MediaThred mediaThred;
    public static Intent it,it2;
    @SuppressLint("StaticFieldLeak")
    public static VerificarConexao conexao;
    @SuppressLint("StaticFieldLeak")
    public static SimpleExoPlayer simpleExoPlayer;
    private TrackSelector trackSelector;
    private static final String LOG_TAG = PlayerService.class.getSimpleName();
    public static final String FECHAR_TODAS_ACTIVITYS = "br.radio.DigitalWeb.fechar";
    private static MediaSessionCompat sessionCompat;
    private RadioStatus radioStatus;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        sessionCompat = new MediaSessionCompat(context, LOG_TAG);
        mediaSessionCompat = createMediaSession(this);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);
        simpleExoPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MUSIC).build());

        it = new Intent(this, PlayerService.class);
        it2 = new Intent(this, SleepTimerService.class);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        handlerMediaPlayer = new Handler(Looper.getMainLooper());
        mediaThred = new MediaThred();
        radioStatus = new RadioStatus(this,mediaSessionCompat);
        conexao = new VerificarConexao(context);

        focoAudio();

        if(SingletonUpdateStatus.getInstance().tempo == 0 && !Funcionalidades.isErroAoConectar()){
                // ProgressBarTime();
            SingletonUpdateStatus.getInstance().atualizarProgressBarTime();
              }

        createNotification();

    }


    private void focoAudio() {

        AudioManager focoAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        // Perda permanente de foco de áudio
        // Usado para indicar uma perda de foco de áudio de duração desconhecida.
        // Pausa a reprodução
        // Usado para indicar uma perda transitória de foco de áudio.
        // Abaixe o volume, continua reproduzindo
        // Usado para indicar uma perda transitória de foco de áudio onde o perdedor do foco de
        // áudio pode diminuir seu volume de saída se quiser continuar jogando (também conhecido como "abaixamento"), já que o novo proprietário de foco não exige que os outros fiquem em silêncio .
        // Usado para indicar um ganho de foco de áudio, ou uma solicitação de foco de áudio, de duração desconhecida.
        // Seu app recebeu o foco de áudio novamente
        // Aumentar volume para normal, reiniciar a reprodução se necessário
        AudioManager.OnAudioFocusChangeListener focoDeAudio = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_LOSS: {
                        // Perda permanente de foco de áudio
                        // Usado para indicar uma perda de foco de áudio de duração desconhecida.
                        if (simpleExoPlayer != null) {
                            simpleExoPlayer.setVolume(0.0f);
                            setMediaPlaybackState(PlaybackStateCompat.STATE_STOPPED);
                        }
                        break;
                    }
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                        // Pausa a reprodução
                        //Usado para indicar uma perda transitória de foco de áudio.
                        if (simpleExoPlayer != null) {
                            simpleExoPlayer.setVolume(0.0f);
                            setMediaPlaybackState(PlaybackStateCompat.STATE_STOPPED);
                        }
                        break;
                    }
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                        // Abaixe o volume, continua reproduzindo
                        // Usado para indicar uma perda transitória de foco de áudio onde o perdedor do foco de
                        // áudio pode diminuir seu volume de saída se quiser continuar jogando (também conhecido como "abaixamento"), já que o novo proprietário de foco não exige que os outros fiquem em silêncio .
                        if (simpleExoPlayer != null) {
                            simpleExoPlayer.setVolume(0.0f);
                            setMediaPlaybackState(PlaybackStateCompat.STATE_STOPPED);
                        }
                        break;
                    }
                    case AudioManager.AUDIOFOCUS_GAIN: {
                        //Usado para indicar um ganho de foco de áudio, ou uma solicitação de foco de áudio, de duração desconhecida.
                        // Seu app recebeu o foco de áudio novamente
                        // Aumentar volume para normal, reiniciar a reprodução se necessário
                        if (simpleExoPlayer != null) {
                            simpleExoPlayer.setVolume(1.0f);
                            setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING);
                        }
                        break;
                    }
                }
            }
        };
        focoAudioManager.requestAudioFocus(focoDeAudio, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

    }

    private MediaSessionCompat createMediaSession(Context context) {

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(context, BroadcastReceiverPlayPause.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, mediaButtonIntent, 0);

        sessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        sessionCompat.setCallback(new MediaSessionCallback());
        setSessionToken(sessionCompat.getSessionToken());
        sessionCompat.setActive(true);
        sessionCompat.setMediaButtonReceiver(pendingIntent);
        
            MetadataBuild(context);

        return sessionCompat;
    }

    public static void MetadataBuild(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder();

            //Ícone de notificação no cartão
            metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, AsynDataClassStatusMetaDados.bitmap);
            metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeResource(context.getResources(), R.drawable.icone_notification));
            // ícone de tela de bloqueio para pré-pirulito
            metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, BitmapFactory.decodeResource(context.getResources(), R.drawable.icone_notification));
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, AsynDataClassStatus.titulo);
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, AsynDataClassStatus.artista);
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST,AsynDataClassStatus.artista);
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE,AsynDataClassStatus.titulo);
            metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,AsynDataClassStatusMetaDados.bitmap);

            // metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, 1);
            // metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, 1);

            sessionCompat.setMetadata(metadataBuilder.build());
        }

    }


    class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay(){
            super.onPlay();

            procurarServidor();
        }
        @Override
        public void onPause(){
            super.onPause();

            parar();
        }

        @Override
        public void onStop(){
            super.onStop();

            parar();
        }
    }

    private void setMediaPlaybackState(int state) {
        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
        if( state == PlaybackStateCompat.STATE_PLAYING ) {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PAUSE);
        } else {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PLAY);
        }
        playbackstateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0);
        mediaSessionCompat.setPlaybackState(playbackstateBuilder.build());
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        MediaButtonReceiver.handleIntent(mediaSessionCompat, intent);

          if(conexao.estaConectado()) {

            if(AsynDataClass.dataurl != null){
                if(Funcionalidades.isTocando()){

                    parar();

                }else{
                    if(!Funcionalidades.isTocando() && !Funcionalidades.isConectando()){

                        conectando();

                        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);
                        mediaThred = new MediaThred();
                        mediaThred.start();
                    }
                }

            }
            else{

                procurarServidor();

            }
        }else{

            radioStatus.parado();

            if(btmPlay != null){

                Snackbar.make(MainActivityPrincipal.content_main_activity_principal, R.string.semConecao, Snackbar.LENGTH_LONG)
                        .setAction("CONECTAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                    context.startActivity(it);
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        }).show();

            }else {

                try {
                    Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    context.startActivity(it);
                } catch (Exception e) {
                    e.getMessage();
                }
                Toast.makeText(this,"Você não está conectado com a internet",Toast.LENGTH_LONG).show();
            }
        }
        return START_STICKY ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaSessionCompat.release();

        SingletonUpdateStatus.getInstance().tempo = 0;
        SingletonUpdateStatus.getInstance().handlerSeekBar.removeCallbacks(SingletonUpdateStatus.getInstance().runnable);

        if(simpleExoPlayer!=null) {

            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

        try{
            notificationManager.cancel(RadioNotificacao.ID_INT_NOTIFICATION);
        }catch (Exception e){
            e.getMessage();
        }

        mediaThred.interrupt();
        AsynDataClass.dataurl = null;
        stopService(it2);

        if(btmPlay != null){
            handlerMediaPlayer.post(new Runnable() {
                @Override
                public void run() {
                    MainActivityPrincipal.textView_titulo.setText(R.string.paradoNome);
                    MainActivityPrincipal.textView_artista.setText("Stop");
                    MainActivityPrincipal.imageLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
                    btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_play));
                    // MainActivityPrincipal.animation.stop();
                }
            });
        }

        Funcionalidades.setTocando(false);
    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public BrowserRoot onGetRoot(String s, int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadChildren(String s, Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        AsynDataClass.dataurl = null;
        stopService(it);

        try{
            notificationManager.cancel(RadioNotificacao.ID_INT_NOTIFICATION);
        }catch (Exception e){
            e.getMessage();
        }


        super.onTaskRemoved(rootIntent);
    }

    public class MediaThred extends Thread {

        @Override
        public void run() {
            super.run();

            DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("Player Audio");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            Uri url = Uri.parse(AsynDataClass.dataurl);

            try {
                MediaSource mediaSource = new ExtractorMediaSource.Factory(factory).setExtractorsFactory(extractorsFactory).createMediaSource(url);
                simpleExoPlayer.prepare(mediaSource);

            } catch (Exception e) {
                erroAoConectar();
            }

            simpleExoPlayer.addListener(new Player.EventListener() {

                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if(playbackState == Player.STATE_BUFFERING){

                        conectando();


                    }else if(playbackState == Player.STATE_READY){

                        //AtualizarStatus();
                        SingletonUpdateStatus.getInstance().AtualizarStatus();

                        radioStatus.tocando();

                        setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING);


                        SingletonUpdateStatus.getInstance().duracao = (551*(60*1000))*60;
                        SingletonUpdateStatus.getInstance().tempo = 0;
                       // duracao = (551*(60*1000))*60;
                       // tempo = 0;


                        if(btmPlay != null){
                            handlerMediaPlayer.post(new Runnable() {
                                @Override
                                public void run(){

                                    MainActivityPrincipal.textView_titulo.setText(AsynDataClassStatus.titulo);
                                    MainActivityPrincipal.textView_artista.setText(AsynDataClassStatus.artista);
                                    btmPlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause));
                                    //.animation.start();

                                }
                            });
                        }

                        simpleExoPlayer.setPlayWhenReady(true);
                        //playerState.setState(new Playing(radioNotificacao,context,mediaSessionCompat));

                    }else if(playbackState == Player.STATE_ENDED){

                       procurarServidor();
                       //playerState.setState(new Listen(context));

                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                    erroAoConectar();

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });
        }
    }

      private void procurarServidor() {

        radioStatus.procurarServidor();
        setMediaPlaybackState(PlaybackStateCompat.STATE_CONNECTING);
        PlayerService.MetadataBuild(context);

        AsynDataClass process = new AsynDataClass(it, context, mediaSessionCompat);
        process.execute();
    }

    private void erroAoConectar() {

       radioStatus.erroAoConectar();
        setMediaPlaybackState(PlaybackStateCompat.STATE_ERROR);
        PlayerService.MetadataBuild(context);

        if(simpleExoPlayer != null){
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

        try{
            notificationManager.cancel(RadioNotificacao.ID_INT_NOTIFICATION);
        }catch (Exception e){
            e.getMessage();
        }

        mediaThred.interrupt();
        AsynDataClass.dataurl = null;
        stopService(it2);
    }

    private void parar() {

        radioStatus.parado();
        setMediaPlaybackState(PlaybackStateCompat.STATE_STOPPED);
        PlayerService.MetadataBuild(context);
    }

    private void conectando() {

        radioStatus.conectando();
        setMediaPlaybackState(PlaybackStateCompat.STATE_BUFFERING);
        PlayerService.MetadataBuild(context);
    }

    private void createNotification() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            RadioNotificacao.show(this);
        }else{
            NotificationRadio.show(this, mediaSessionCompat);
        }
    }
}