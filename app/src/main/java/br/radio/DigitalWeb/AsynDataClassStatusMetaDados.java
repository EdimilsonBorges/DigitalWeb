package br.radio.DigitalWeb;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Edimilson Borges em 20/06/2021.
 */

public class AsynDataClassStatusMetaDados {
        String singleParsedTitulo = "Tocando";
        String singleParsedArtista ="No Ar";
        String singleParsedDuracao;
        String singleParsedArte;
        String singleParsedUrl;
        String singleParsedRestante;
        String singleParsedDecorrido;

        public static String duracao;
        public static String restante;
        public static String decorrido;
        public static String imagem;
        public static String url;
        public static String titulo;
        public static String artista;
        public static String singleParsedPlayng ="";
        public Intent intent;
        public Context context;
        public static Bitmap bitmap;
        MediaSessionCompat mediaSessionCompat;
        private final Handler handlerMediaPlayer;

        public AsynDataClassStatusMetaDados(Intent it, Context context, MediaSessionCompat mediaSessionCompat){
            this.context = context;
            this.intent = it;
            this.mediaSessionCompat = mediaSessionCompat;
            this.handlerMediaPlayer = new Handler(Looper.getMainLooper());
        }


    public void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (!SingletonUpdateStatus.getInstance().aovivo) {
                    try {
                        HttpHandler sh = new HttpHandler();
                        // Making a request to url and getting response
                        url = AsynDataClass.urlMetaData;

                        String jsonStr = sh.makeServiceCall(url);


                        if (jsonStr != null) {
                            try {
                                JSONObject jsonObj = new JSONObject(jsonStr);

                                JSONObject station = jsonObj.getJSONObject("station");
                                singleParsedUrl = station.getString("listen_url");

                                JSONObject now_playing = jsonObj.getJSONObject("now_playing");
                                singleParsedDuracao = now_playing.getString("duration");
                                singleParsedDecorrido = now_playing.getString("elapsed");
                                singleParsedRestante = now_playing.getString("remaining");

                                JSONObject c = now_playing.getJSONObject("song");
                                singleParsedTitulo = c.getString("title");
                                singleParsedArtista = c.getString("artist");
                                singleParsedArte = c.getString("art");
                                singleParsedPlayng = "id";

                                post();

                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }


                        }

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        });

    }

    public void post(){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (!SingletonUpdateStatus.getInstance().aovivo) {

                    url = singleParsedUrl;
                    titulo = singleParsedTitulo;
                    artista = singleParsedArtista;
                    imagem = singleParsedArte;
                    duracao = singleParsedDuracao;
                    restante = singleParsedRestante;
                    decorrido = singleParsedDecorrido;

                    if(decorrido != null){
                        SingletonUpdateStatus.getInstance().tempo = Integer.parseInt(decorrido) * 1000;
                        SingletonUpdateStatus.getInstance().duracao = Integer.parseInt(duracao) * 1000;
                    }


                    if (!singleParsedTitulo.isEmpty()) {
                        AsynDataClassStatus.titulo = titulo;
                    } else {
                        AsynDataClassStatus.titulo = "Tocando";
                    }
                    if (!singleParsedArtista.isEmpty()) {
                        AsynDataClassStatus.artista = artista;
                    } else {
                        AsynDataClassStatus.artista = "No Ar";
                    }

                    PlayerService.MetadataBuild(context);

                    if (imagem != null && imagem.equals("http://34.67.184.188/static/img/generic_song.jpg")) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

                        if(Funcionalidades.isTocando()){

                            updateNotification();

                            if(MainActivityPrincipal.btmPlay != null){

                                MainActivityPrincipal.imageLogo.setImageBitmap(bitmap);
                            }
                        }

                    } else {

                        try {
                            setBitmapFromURL(imagem, MainActivityPrincipal.imageLogo);
                        } catch (Exception e) {
                            e.getMessage();

                            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

                            if(Funcionalidades.isTocando()){

                                updateNotification();

                                if(MainActivityPrincipal.btmPlay != null) {
                                    MainActivityPrincipal.imageLogo.setImageBitmap(bitmap);
                                }

                            }

                        }
                    }


                    if (MainActivityPrincipal.btmPlay != null && Funcionalidades.isTocando()) {
                        MainActivityPrincipal.textView_titulo.setText(AsynDataClassStatus.titulo);
                        MainActivityPrincipal.textView_artista.setText(AsynDataClassStatus.artista);
                    }
                }
            }
        });

    }

    public void setBitmapFromURL(final String url, final ImageView imageView) {
        new Thread() {
            public void run() {
                try {
                    URL src = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) src.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    final Bitmap myBitmapImagemItem = BitmapFactory.decodeStream(input);

                    handlerMediaPlayer.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(myBitmapImagemItem);
                            bitmap = myBitmapImagemItem;

                            if(Funcionalidades.isTocando()){
                                updateNotification();
                            }

                            PlayerService.MetadataBuild(context);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void updateNotification() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            RadioNotificacao.update((Service) PlayerService.context);
        }else{
            NotificationRadio.update((Service) context,mediaSessionCompat);
        }
    }
}


