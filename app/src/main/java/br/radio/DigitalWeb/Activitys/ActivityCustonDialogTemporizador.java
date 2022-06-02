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
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import br.radio.DigitalWeb.R;
import br.radio.DigitalWeb.Services.SleepTimerService;

import static br.radio.DigitalWeb.Services.PlayerService.FECHAR_TODAS_ACTIVITYS;

/**
 * Esta classe é um Activity para mostrar um temporizador na tela para fazer um desligamento automático
 * fechando o aplicativo e parando a música
 */
public class ActivityCustonDialogTemporizador extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static TextView tempoRestante;
    private Intent it2;
    @SuppressLint("StaticFieldLeak")
    public static Button menosCinco, maisCinco, ativarDesativar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custon_dialog_temporizador);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);      //Ativar o botão
        Objects.requireNonNull(getSupportActionBar()).setTitle("Temporizador");     //Titulo para ser exibido na sua Action Bar em frente à seta

        it2 = new Intent(ActivityCustonDialogTemporizador.this, SleepTimerService.class);
        tempoRestante = findViewById(R.id.tempo_restante);
        menosCinco = findViewById(R.id.menos_cinco_mn);
        maisCinco = findViewById(R.id.mais_cinco_mn);
        ativarDesativar = findViewById(R.id.ligar_temporizador);

        SleepTimerService.atualizarCountDownTimerTexto();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(FECHAR_TODAS_ACTIVITYS));

        if(SleepTimerService.passandoTempo){
            ativarDesativar.setText(R.string.cancelar);
            maisCinco.setVisibility(View.INVISIBLE);
            menosCinco.setVisibility(View.INVISIBLE);
        }else {
            ativarDesativar.setText(R.string.ativar);
            maisCinco.setVisibility(View.VISIBLE);
            menosCinco.setVisibility(View.VISIBLE);
        }

        menosCinco.setOnClickListener(view -> {

            if(SleepTimerService.tempoLeftInMilissegundos >= 300000 && !SleepTimerService.passandoTempo){
                SleepTimerService.tempoLeftInMilissegundos = SleepTimerService.tempoLeftInMilissegundos - 300000;
                SleepTimerService.atualizarCountDownTimerTexto();
            }
        });
        maisCinco.setOnClickListener(view -> {
            if(!SleepTimerService.passandoTempo && SleepTimerService.tempoLeftInMilissegundos <= 82800000){
                SleepTimerService.tempoLeftInMilissegundos = SleepTimerService.tempoLeftInMilissegundos + 300000;
                SleepTimerService.atualizarCountDownTimerTexto();
                menosCinco.setVisibility(View.VISIBLE);
            }
        });

        ativarDesativar.setOnClickListener(view -> {

            if(SleepTimerService.tempoLeftInMilissegundos >= 1000){

                startService(it2);

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent it = new Intent(ActivityCustonDialogTemporizador.this, MainActivityPrincipal.class);

            if(!SleepTimerService.passandoTempo){
                stopService(it2);
            }
            startActivity(it);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        if (item.getItemId() == android.R.id.home) {  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
            Intent it = new Intent(ActivityCustonDialogTemporizador.this, MainActivityPrincipal.class);
            startActivity(it);
            finish();
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!SleepTimerService.passandoTempo) {
            stopService(it2);
        }
    }
}
