package br.radio.DigitalWeb.Activitys;

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

import br.radio.DigitalWeb.AsynData.AsynDataClass;
import br.radio.DigitalWeb.R;

import static br.radio.DigitalWeb.Services.PlayerService.FECHAR_TODAS_ACTIVITYS;

public class ActivityPedidos extends AppCompatActivity {

    WebView myWebView;
    WebSettings webSettings;
    ProgressBar progress_pedidos;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos_activity);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);      //Ativar o botão
        Objects.requireNonNull(getSupportActionBar()).setTitle("Pedir Música"); // Título da janela

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(FECHAR_TODAS_ACTIVITYS));

        progress_pedidos = findViewById(R.id.progress_pedidos);
        myWebView = findViewById(R.id.webviewPedidos);
        // myWebView.setWebViewClient(new MyCustomWebViewClient());
        // myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webSettings = myWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(AsynDataClass.urlPedidos);

        progress_pedidos.setMax(100);

        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                progress_pedidos.setProgress(progress);

                if(progress == 100){
                    progress_pedidos.setVisibility(View.GONE);
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
                Intent it = new Intent(ActivityPedidos.this, MainActivityPrincipal.class);
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
             Intent it = new Intent(ActivityPedidos.this,MainActivityPrincipal.class);
             startActivity(it);
             finish();
            return true;
        }

        return false;
    }

}
