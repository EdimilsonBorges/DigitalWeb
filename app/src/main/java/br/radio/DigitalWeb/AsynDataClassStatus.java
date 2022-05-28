package br.radio.DigitalWeb;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.radio.DigitalWeb.programacao.Domingo;
import br.radio.DigitalWeb.programacao.IDiasDaSemanaStrategy;
import br.radio.DigitalWeb.programacao.Quarta;
import br.radio.DigitalWeb.programacao.Quinta;
import br.radio.DigitalWeb.programacao.Sabado;
import br.radio.DigitalWeb.programacao.Segunda;
import br.radio.DigitalWeb.programacao.Sexta;
import br.radio.DigitalWeb.programacao.Terca;

/**
 * Created by Edimilson Borges em 04/10/2019.
 */

public class AsynDataClassStatus {
    String data ="";
    public static String singleParsedStatus = "'";
    public static String titulo = "Tocando...";
    public static String artista = "No Ar";
    private final Intent intent;
    public Context context;
    public String dia_da_semana_atual;
    MediaSessionCompat mediaSessionCompat;
    private IDiasDaSemanaStrategy diasDaSemanaStrategy;
    String hora;
    String minuto;
    int horaMinuto;

    public AsynDataClassStatus(Intent it, Context context, MediaSessionCompat mediaSessionCompat){
        this.context = context;
        this.intent = it;
        this.mediaSessionCompat = mediaSessionCompat;
    }

    public void execute(){

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://radio-digital-web-default-rtdb.firebaseio.com/.json?=radiodigitalweb-export.json");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while(line != null){
                        line = bufferedReader.readLine();
                        data = data + line;
                    }


                    JSONArray JA = new JSONArray(data);
                    //for(int i =0 ;i <JA.length(); i++){
                    JSONObject JO3 = (JSONObject) JA.get(1);
                    // singleParsedStatus =  ""+JO3.get("status");

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat diaDaSemanaFormat = new SimpleDateFormat("EEE", new Locale("pt", "BR"));
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat horaFormat = new SimpleDateFormat("HH");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat minutoFormat = new SimpleDateFormat("mm");
                    //horaFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
                    horaFormat.setTimeZone(TimeZone.getTimeZone("GMT-03:00"));
                    minutoFormat.setTimeZone(TimeZone.getTimeZone("GMT-03:00"));

                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date hora_atual = cal.getTime();
                    Date minuto_atual = cal.getTime();

                    hora = horaFormat.format(hora_atual);
                    minuto = minutoFormat.format(minuto_atual);
                    horaMinuto = Integer.parseInt(hora+minuto);

                    dia_da_semana_atual = diaDaSemanaFormat.format(Calendar.getInstance().getTime());

                    try{

                        if(dia_da_semana_atual.equals("seg.") || dia_da_semana_atual.equals("seg")){
                            diasDaSemanaStrategy = new Segunda();
                        }
                        if(dia_da_semana_atual.equals("ter.") || dia_da_semana_atual.equals("ter")){
                            diasDaSemanaStrategy = new Terca();
                        }
                        if(dia_da_semana_atual.equals("qua.") || dia_da_semana_atual.equals("qua")){
                            diasDaSemanaStrategy = new Quarta();
                        }
                        if(dia_da_semana_atual.equals("qui.") || dia_da_semana_atual.equals("qui")){
                            diasDaSemanaStrategy = new Quinta();
                        }
                        if(dia_da_semana_atual.equals("sex.") || dia_da_semana_atual.equals("sex")){
                            diasDaSemanaStrategy = new Sexta();
                        }
                        if(dia_da_semana_atual.equals("sáb.") || dia_da_semana_atual.equals("sáb") || dia_da_semana_atual.equals("sab.") || dia_da_semana_atual.equals("sab")){
                            diasDaSemanaStrategy = new Sabado();
                        }
                        if(dia_da_semana_atual.equals("dom.") || dia_da_semana_atual.equals("dom")){
                            diasDaSemanaStrategy = new Domingo();
                        }

                        singleParsedStatus = diasDaSemanaStrategy.actualize(JO3,hora,minuto,horaMinuto);

                    }catch (Exception e){
                        e.getMessage();
                        singleParsedStatus = "";
                    }
                    
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                post();
            }
        });

    }

    public void post(){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    if(PlayerService.simpleExoPlayer != null){

                        if(singleParsedStatus.isEmpty()){
                            SingletonUpdateStatus.getInstance().aovivo = false;
                            try {
                                AsynDataClassStatusMetaDados processo = new AsynDataClassStatusMetaDados(intent,context,mediaSessionCompat);
                                processo.execute();
                            }catch (Exception e){
                                titulo = "Tocando...";
                                artista = "No Ar";
                            }

                            PlayerService.MetadataBuild(context);

                        } else {

                            SingletonUpdateStatus.getInstance().aovivo = true;
                            titulo = singleParsedStatus;
                            artista = "NO AR";
                            SingletonUpdateStatus.getInstance().duracao = (551*(60*1000))*60;
                            SingletonUpdateStatus.getInstance().tempo = 0;

                            if(MainActivityPrincipal.btmPlay != null && Funcionalidades.isTocando()){
                                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

                                AsynDataClassStatusMetaDados.bitmap = bitmap;
                                MainActivityPrincipal.imageLogo.setImageBitmap(bitmap);
                                MainActivityPrincipal.textView_titulo.setText(titulo);
                                MainActivityPrincipal.textView_artista.setText(artista);
                            }


                            if(Funcionalidades.isTocando()){
                                updateNotification();
                            }

                            PlayerService.MetadataBuild(context);

                        }

                        if(MainActivityPrincipal.btmPlay != null && Funcionalidades.isTocando()){

                            MainActivityPrincipal.textView_titulo.setText(titulo);
                            MainActivityPrincipal.textView_artista.setText(artista);
                        }

                        if(Funcionalidades.isTocando()){
                            updateNotification();
                        }

                    }else {
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);

                        MainActivityPrincipal.textView_titulo.setText(R.string.paradoNome);
                        MainActivityPrincipal.textView_artista.setText("Stop.");
                        MainActivityPrincipal.imageLogo.setImageBitmap(bitmap);

                        if(Funcionalidades.isTocando()){
                            updateNotification();
                        }
                    }


                }catch(Exception e){
                    e.getMessage();

                    if(MainActivityPrincipal.btmPlay != null){

                        if(Funcionalidades.isTocando()){
                            updateNotification();
                        }

                        MainActivityPrincipal.textView_titulo.setText(context.getString(R.string.erro_ao_encontrar_servidor));
                        MainActivityPrincipal.textView_artista.setText("Ops.");
                        MainActivityPrincipal.btmPlay.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_play));
                        // MainActivityPrincipal.animation.stop();
                    }
                }
            }
        });
    }

    private void updateNotification() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            RadioNotificacao.update((Service) context);
        }else{
            NotificationRadio.update((Service) context,mediaSessionCompat);
        }
    }


}
