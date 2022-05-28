package br.radio.DigitalWeb;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.util.Objects;

import static br.radio.DigitalWeb.PlayerService.FECHAR_TODAS_ACTIVITYS;

public class ActivityHistorico extends AppCompatActivity {

    public ProgressBar progress_historico;
    public WebView myWebView;
    public WebSettings webSettings;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historico_activity);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true); //Ativar o botão
        Objects.requireNonNull(getSupportActionBar()).setTitle("Reproduzidas Recentemente");

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(FECHAR_TODAS_ACTIVITYS));

        myWebView = (WebView) findViewById(R.id.webviewHistorico);
        progress_historico = (ProgressBar) findViewById(R.id.progress_historico);
        //myWebView.setWebViewClient(new MyCustomWebViewClient());
       // myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings = myWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

       // myWebView.loadUrl(AsynDataClass.urlHistorico);
       // Uri uri = Uri.parse("http://34.67.184.188/public/digital_web/history");//Link por defeito
          myWebView.loadUrl("http://34.67.184.188/public/digital_web/history");

         progress_historico.setMax(100);

        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progress_historico.setProgress(progress);

                if (progress == 100) {
                    progress_historico.setVisibility(View.GONE);
                }

            }
        });

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // fecha a activity quando pressiona o botão fechar da notificação
        @Override
        public void onReceive(Context context, Intent intent) {
            // Esse método será chamado ao lançar um broadcast
            // pela activity B
            finish();
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        if (item.getItemId() == android.R.id.home) {  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
            if (myWebView.canGoBack()) {
                myWebView.goBack();
            } else {
                Intent it = new Intent(ActivityHistorico.this, MainActivityPrincipal.class);
                startActivity(it);
                finish();
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }else if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent it = new Intent(ActivityHistorico.this,MainActivityPrincipal.class);
            startActivity(it);
            finish();
            return true;
        }

        return false;
    }

}

