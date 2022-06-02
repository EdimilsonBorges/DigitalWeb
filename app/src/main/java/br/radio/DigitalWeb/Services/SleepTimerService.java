package br.radio.DigitalWeb.Services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

import br.radio.DigitalWeb.Activitys.ActivityCustonDialogTemporizador;
import br.radio.DigitalWeb.Activitys.MainActivityPrincipal;
import br.radio.DigitalWeb.AsynData.AsynDataClass;
import br.radio.DigitalWeb.AsynData.AsynDataClassStatus;
import br.radio.DigitalWeb.R;
import br.radio.DigitalWeb.Status.Status;

public class SleepTimerService extends Service {

    public static CountDownTimer countDownTimer;
    public static boolean passandoTempo;
    public static long tempoLeftInMilissegundos;
    public static Intent it;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public static String formatoDoTempo;


    @Override
    public void onCreate() {
        super.onCreate();

        it = new Intent(getApplication(), PlayerService.class);
        context = this;
    }

    private static void resetarTempo() {
        tempoLeftInMilissegundos = 0;
        atualizarCountDownTimerTexto();
        MainActivityPrincipal.textView_artista.setText(AsynDataClassStatus.artista);
        passandoTempo = false;
    }

    public static void contarTempo() {

        countDownTimer = new CountDownTimer(tempoLeftInMilissegundos,1000) {

            @Override
            public void onTick(long l) {
                tempoLeftInMilissegundos = l;
                atualizarCountDownTimerTexto();
            }

            @Override
            public void onFinish() {
                passandoTempo = false;
                context.stopService(it);
                resetarTempo();
                Toast.makeText(context,"Tempo de reprodução esgotado",Toast.LENGTH_LONG).show();
            }
        }.start();
        passandoTempo = true;
    }

    public static  void atualizarCountDownTimerTexto() {

        int horas = (int)(tempoLeftInMilissegundos / 3600000) % 24;
        int minutos = (int)(tempoLeftInMilissegundos / 60000) % 60;
        int segundos = (int)(tempoLeftInMilissegundos / 1000) % 60;

        if(horas >= 1){
            formatoDoTempo = String.format(Locale.getDefault(),"%02d:%02d:%02d",horas, minutos, segundos);
        }else{
            formatoDoTempo = String.format(Locale.getDefault(),"%02d:%02d", minutos, segundos);
        }
        String mensagem = "Desligando em:"+" ";

        String text = mensagem + formatoDoTempo;

        ActivityCustonDialogTemporizador.tempoRestante.setText(formatoDoTempo);
        MainActivityPrincipal.textView_artista.setText(text);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!passandoTempo){
            if(Status.isTocando() && AsynDataClass.dataurl != null){
                contarTempo();
                Toast.makeText(getApplication(),"Tempo de desligamento iniciado",Toast.LENGTH_LONG).show();
                ActivityCustonDialogTemporizador.ativarDesativar.setText(R.string.desativar);
                ActivityCustonDialogTemporizador.maisCinco.setVisibility(View.INVISIBLE);
                ActivityCustonDialogTemporizador.menosCinco.setVisibility(View.INVISIBLE);
            }else {
                Toast.makeText(getApplication(),"Por favor, inicie a reprodução primeiro",Toast.LENGTH_LONG).show();
            }
        }else {
            resetarTempo();
            countDownTimer.cancel();
            ActivityCustonDialogTemporizador.ativarDesativar.setText(R.string.ativar);
            ActivityCustonDialogTemporizador.maisCinco.setVisibility(View.VISIBLE);
            ActivityCustonDialogTemporizador.menosCinco.setVisibility(View.VISIBLE);
            Toast.makeText(getApplication(),"Tempo de desligamento cancelado",Toast.LENGTH_LONG).show();
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(passandoTempo){
            countDownTimer.cancel();
        }
        resetarTempo();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}
