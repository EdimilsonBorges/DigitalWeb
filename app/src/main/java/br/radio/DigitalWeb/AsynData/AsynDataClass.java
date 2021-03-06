package br.radio.DigitalWeb.AsynData;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.radio.DigitalWeb.Activitys.MainActivityPrincipal;
import br.radio.DigitalWeb.Status.RadioStatus;

/**
 * Created by Edimilson Borges em 04/10/2019.
 */

public class AsynDataClass {
    String data = "";
    String singleParsedUrl;
    String singleParsedUrl2 = "";
    String singleParsedUrlMetaData = "";
    public static String urlMetaData;
    public static String singleParsedWhat = "";
    public static String dataurl;
    public static String urlHistorico = "";
    public static String urlPedidos = "";
    private final Intent intent;
    private final Context context;
    MediaSessionCompat mediaSessionCompat;
    private final RadioStatus radioStatus;
    private final String LOG_TAG = AsynDataClass.class.getSimpleName();

    public AsynDataClass(Intent it, Context context, MediaSessionCompat mediaSessionCompat) {
        this.context = context;
        this.intent = it;
        this.mediaSessionCompat = mediaSessionCompat;
        radioStatus = new RadioStatus(context, mediaSessionCompat);
    }

    public void execute() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {

                URL url = new URL("https://radio-digital-web-default-rtdb.firebaseio.com/.json?=radiodigitalweb-export.json");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data += line;
                }

                JSONArray JA = new JSONArray(data);
                JSONObject JO1 = (JSONObject) JA.get(0);
                JSONObject JO2 = (JSONObject) JA.get(1);
                singleParsedUrl = "" + JO1.get("url");
                urlHistorico = "" + JO1.get("xUrlHistorico");
                urlPedidos = "" + JO1.get("xUrlPedidos");
                singleParsedUrlMetaData = "" + JO1.get("metadados");
                singleParsedWhat = "" + JO1.get("what");
                singleParsedUrl2 = "" + JO2.get("url2");

            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "MalformedURLException: " + e.getMessage());

            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException: " + e.getMessage());

            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSONException: " + e.getMessage());
            }

            post();
        });

    }

    public void post() {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {

            try {
                urlMetaData = singleParsedUrlMetaData.trim().replaceAll("\\s+", "");
                dataurl = singleParsedUrl.trim().replaceAll("\\s+", "");
                context.startService(intent);

            } catch (Exception e) {
                radioStatus.erroAoEncontrarServidor();
                dataurl = null;
            }
        });

    }
}