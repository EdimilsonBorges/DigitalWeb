package br.radio.DigitalWeb;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.FlipHorizontalAnimation;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

import static br.radio.DigitalWeb.PlayerService.FECHAR_TODAS_ACTIVITYS;

public class MainActivityPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //public static AnimationDrawable animation;
    //  public Animation animBounce;
    @SuppressLint("StaticFieldLeak")
    public static TextView textView_titulo,textViewTimeDecorrido, textView_artista,textViewTimeTotal;
    @SuppressLint("StaticFieldLeak")
    public static ImageView btmPlay;
    public static CircleImageView imageLogo;
    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout content_main_activity_principal;
    @SuppressLint("StaticFieldLeak")
    public static SeekBar seekBar,seekBar2;
    public ImageView imageView_somAlto;
    private Intent it;
    public int n2,volume;
    public static boolean mudo;
    //public CardView cadViewLogo,cadViewCorpo;
    public NotificationManager notificationManager;
    private RadioStatus radioStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_principal);

        MultiDex.install(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initControls();
        mensagemSharedPreferences(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(FECHAR_TODAS_ACTIVITYS));

        //start();

        //radioNotificacao = new RadioNotificacao();
        //radioNotificacao2 = new RadioNotificacao2();

        it = new Intent(MainActivityPrincipal.this, PlayerService.class);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        radioStatus = new RadioStatus(PlayerService.context, PlayerService.mediaSessionCompat);

        imageLogo = (CircleImageView) findViewById(R.id.imageLogo);
        //imageLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        //animation = (AnimationDrawable) imageLogo.getDrawable();
        btmPlay = (ImageView) findViewById(R.id.btmPlay);
        textView_titulo = (TextView) findViewById(R.id.textView_titulo);
        textView_artista = (TextView) findViewById(R.id.textView_artista);
        content_main_activity_principal = (RelativeLayout) findViewById(R.id.content_main_activity_principal);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        imageView_somAlto = (ImageView) findViewById(R.id.imageView_somAlto);
        textViewTimeDecorrido =(TextView)findViewById(R.id.textViewTimeDecorrido);
        textViewTimeTotal =(TextView)findViewById(R.id.textViewTimeTotal);
        //cadViewCorpo = (CardView) findViewById(R.id.cadViewCorpo);
        //cadViewLogo = (CardView) findViewById(R.id.cadViewLogo);
        n2 = seekBar.getProgress();
        //Funcionalidades.setSegundoPlano(false);

        pegarPosicaoVolumeIcone(n2);

        //  animBounce = AnimationUtils.loadAnimation(getApplicationContext(),
        //    R.anim.bounce);
        // imageAnimation.startAnimation(animBounce);


        resetarValores();


        if (AsynDataClass.dataurl == null && !Funcionalidades.isTocando()) {

            startService(it);
            seekBar2.setProgress(0);
        }
        // WifiManager.WifiLock wifiLock = ((WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL,"mylock");
        // wifiLock.acquire();
        // mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);

        imageLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageLogo.setClickable(false);


              //  new FlipHorizontalAnimation(imageLogo).setDuration(1000).setListener(new AnimationListener() {
              //      @Override
              //      public void onAnimationEnd(Animation animation) {
              //          imageLogo.setClickable(true);
              //           Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_imagem_item);
              //          imageLogo.setImageBitmap(bitmap);
              //      }

              //  }).animate();

                new FlipHorizontalAnimation(imageLogo).setDuration(700).setListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageLogo.setClickable(true);
                    }

                }).animate();

            }
        });


        imageView_somAlto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);
                if (mudo) {
                    seekBar.setProgress(volume);
                    mudo = false;

                } else {
                    seekBar.setProgress(0);
                    mudo = true;
                }

            }
        });

        btmPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if (Funcionalidades.isClicavel()) {
                    startService(it);
              //  }
            }
        });

        ImageView btmCompartilhar = (ImageView) findViewById(R.id.btmCompartilhar);
        btmCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                compartilhar();
            }
        });
        ImageView btmLigar = (ImageView) findViewById(R.id.btmLigar);
        btmLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contato();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void mensagemSharedPreferences(Context context) {

        SharedPreferences sharedPreferences = getSharedPreferences("firstRun", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstRun", true)) {
            sharedPreferences.edit().putBoolean("firstRun", false).apply();

            AlertDialog.Builder mensagem = new AlertDialog.Builder(context);
            mensagem.setTitle(R.string.bemVindoNome);
            mensagem.setMessage(R.string.bemVindoMensagem);
            mensagem.setNeutralButton("OK", null);
            mensagem.create();
            mensagem.show();
        }
    }


   /* private void showGooglePlayServicesStatus() {

        final String TAG = "TAG";

        GoogleApiAvailability apiAvail = GoogleApiAvailability.getInstance();
        int errorCode = apiAvail.isGooglePlayServicesAvailable(this);
        String msg = "Play Services: " + apiAvail.getErrorString(errorCode);
        Log.d(TAG, msg);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }*/

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // fecha a activity quando pressiona o botão fechar da notificação
        @Override
        public void onReceive(Context context, Intent intent) {
            // Esse método será chamado ao lançar um broadcast
            // pela activity B
            finish();
        }
    };

    private void compartilhar() {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.conviteMensagem));
        startActivity(Intent.createChooser(share, getString(R.string.compartilharNome)));
    }

    private void contato() {
// Aqui, esta atividade é a atividade atual
      if(PlayerService.conexao.estaConectado()){
          if(!AsynDataClass.singleParsedWhat.isEmpty()){
              try{
                  String link = AsynDataClass.singleParsedWhat;
                  Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                  startActivity(myIntent);
              }catch(Exception erro){
                  System.out.println(erro);
              }
          }else {
            //  dsfsdfsdf
          }

      }else {

          radioStatus.parado();

          Snackbar.make(MainActivityPrincipal.content_main_activity_principal, R.string.semConecao, Snackbar.LENGTH_LONG)
                  .setAction("CONECTAR", new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          try {
                              Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                              startActivity(it);
                          } catch (Exception e) {
                              e.getMessage();
                          }
                      }
                  }).show();
      }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.nav_ligar: {
                contato();
                break;
            }
            case R.id.nav_avaliar: {
                avaliar();
                break;
            }
            case R.id.nav_compartilhar: {
                compartilhar();
                break;
            }
            case R.id.nav_sobre: {
                sobre();
                //politicaDePrivacidade();
                break;
            }
            case R.id.nav_pedir_musica: {
                if(PlayerService.conexao.estaConectado()){
                    if(!AsynDataClass.urlPedidos.isEmpty()){
                        if(SingletonUpdateStatus.getInstance().aovivo){
                            AlertDialog.Builder mensagem = new AlertDialog.Builder(this);
                            mensagem.setTitle("Estamos ao Vivo!");
                            mensagem.setMessage("No momento só é possível pedir música através do nosso Whatsapp. \n\nDeseja pedir música via Whatsapp?");
                            mensagem.setNeutralButton("NÃO", null);
                            mensagem.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try{
                                        String link = AsynDataClass.singleParsedWhat;
                                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                        startActivity(myIntent);
                                    }catch(Exception erro){
                                        System.out.println(erro);
                                    }
                                }
                            });
                            mensagem.create();
                            mensagem.show();
                        }else{
                        try{
                            Intent it = new Intent(MainActivityPrincipal.this, ActivityPedidos.class);
                            startActivity(it);
                            finish();
                        }catch(Exception erro){
                            System.out.println(erro);
                        }
                        }
                    }else {
                        //  dsfsdfsdf
                    }

                }else {

                    radioStatus.parado();

                    Snackbar.make(MainActivityPrincipal.content_main_activity_principal, R.string.semConecao, Snackbar.LENGTH_LONG)
                            .setAction("CONECTAR", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                        startActivity(it);
                                    } catch (Exception e) {
                                        e.getMessage();
                                    }
                                }
                            }).show();
                }

                break;
            }
            case R.id.historico: {

                if(PlayerService.conexao.estaConectado()){
                    if(!AsynDataClass.urlHistorico.isEmpty()){
                        try{
                            Intent it = new Intent(MainActivityPrincipal.this, ActivityHistorico.class);
                            startActivity(it);
                            finish();
                        }catch(Exception erro){
                            System.out.println(erro);
                        }
                    }else {
                        //  dsfsdfsdf
                    }

                }else {

                    radioStatus.parado();

                    Snackbar.make(MainActivityPrincipal.content_main_activity_principal, R.string.semConecao, Snackbar.LENGTH_LONG)
                            .setAction("CONECTAR", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                        startActivity(it);
                                    } catch (Exception e) {
                                        e.getMessage();
                                    }
                                }
                            }).show();
                }

                break;
            }
            case R.id.nav_sair: {

                try{
                    notificationManager.cancel(RadioNotificacao.ID_INT_NOTIFICATION);
                }catch (Exception e){
                    e.getMessage();
                }

                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(FECHAR_TODAS_ACTIVITYS));
                stopService(it);


            }
            break;

            case R.id.programacao:{
                Intent it = new Intent(MainActivityPrincipal.this, ProgramacaoActivity.class);
                startActivity(it);
                finish();
            }
            break;
            default:
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void sobre() {

        AlertDialog.Builder mensagem = new AlertDialog.Builder(MainActivityPrincipal.this);
        String versionName = BuildConfig.VERSION_NAME;
        mensagem.setTitle(R.string.sobreNome);
        mensagem.setMessage("Sobre o APP:\n\n Versão: "+versionName+"\n Desenvolvido por Edimilson Borges\n\nInformações do desenvolvedor\nWhatsapp: (89) 98112-5275 - E-mail: edimilson-borges159@hotmail.com");
        mensagem.setNeutralButton("OK", null);
        mensagem.show();
    }

    private void avaliar() {

        String id = getPackageName();
        String url = "https://play.google.com/store/apps/details?id=";

        String urlString = url+id;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.vending");

        try {
            this.startActivity(intent);

        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            this.startActivity(intent);
        }
    }

    public void pegarPosicaoVolumeIcone(int n2) {

        volume = seekBar.getProgress();

        if (n2 >= 1 && n2 <= 2) {
            imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sombaixo));
        }
        if (n2 >= 3 && n2 <= 11) {
            imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sommetade));
        }

        if (n2 >= 10 && n2 <= 15) {
            imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_somalto));
        }
        if (n2 <= 2) {
            imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mudo));
        }
        if (n2 <= 5 && n2 >= 2) {
            imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sombaixo));
        }
        if (n2 <= 11 && n2 >= 5) {
            imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sommetade));
        }
    }

    private void initControls() {

        try {
            final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);
            final ImageView imageView_somAlto = (ImageView) findViewById(R.id.imageView_somAlto);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);


                    if(arg2){
                        volume = progress;
                    }

                    if (progress <= 2) {
                        imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mudo));
                    }
                    if (progress >= 1 && progress <= 4) {
                        imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sombaixo));
                    }
                    if (progress <= 11 && progress >= 5) {
                        imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sommetade));
                    }
                    if (progress >= 10 && progress <= 15) {
                        imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_somalto));
                    }
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }
    @Override
    public boolean onKeyDown (int keyCode,KeyEvent event){

        int n3 = seekBar.getProgress();

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            volume = seekBar.getProgress();

            seekBar.setProgress(n3 + 1);

            if (n3 >= 1 && n3 <= 2) {
                imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sombaixo));
            }
            if (n3 >= 3 && n3 <= 11) {
                imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sommetade));
            }

            if (n3 >= 10 && n3 <= 15) {
                imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_somalto));
            }

            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

            volume = seekBar.getProgress();

            seekBar.setProgress(n3 - 1);
            if (n3 <= 2) {
                imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mudo));
            }
            if (n3 <= 5 && n3 >= 2) {
                imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sombaixo));
            }
            if (n3 <= 11 && n3 >= 5) {
                imageView_somAlto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sommetade));

            }
            return true;
        }
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){

            if (Funcionalidades.isTocando()) {

                AlertDialog.Builder mensagem = new AlertDialog.Builder(MainActivityPrincipal.this);
                mensagem.setTitle("Alerta!");
                mensagem.setMessage("O que deseja fazer?");
                mensagem.setNeutralButton("SAIR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        LocalBroadcastManager.getInstance(MainActivityPrincipal.this).sendBroadcast(new Intent(FECHAR_TODAS_ACTIVITYS));
                        stopService(it);

                    }
                });
                mensagem.setPositiveButton("MINIMIZAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocalBroadcastManager.getInstance(MainActivityPrincipal.this).sendBroadcast(new Intent(FECHAR_TODAS_ACTIVITYS));
                    }
                });
                mensagem.show();
            } else {

                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(FECHAR_TODAS_ACTIVITYS));
                stopService(it);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.temporizador){
            Intent it = new Intent(MainActivityPrincipal.this, ActivityCustonDialogTemporizador.class);
            startActivity(it);
            finish();
        }else if(id == R.id.pedir_musica){

            if(PlayerService.conexao.estaConectado()){
                if(!AsynDataClass.urlPedidos.isEmpty()){
                    if(SingletonUpdateStatus.getInstance().aovivo){
                        AlertDialog.Builder mensagem = new AlertDialog.Builder(this);
                        mensagem.setTitle("Estamos ao Vivo!");
                        mensagem.setMessage("No momento só é possível pedir música através do nosso Whatsapp. \n\nDeseja pedir música via Whatsapp?");
                        mensagem.setNeutralButton("NÃO", null);
                        mensagem.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    String link = AsynDataClass.singleParsedWhat;
                                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                    startActivity(myIntent);
                                }catch(Exception erro){
                                    System.out.println(erro);
                                }
                            }
                        });
                        mensagem.create();
                        mensagem.show();
                    }else{
                        try{
                            Intent it = new Intent(MainActivityPrincipal.this, ActivityPedidos.class);
                            startActivity(it);
                            finish();
                        }catch(Exception erro){
                            System.out.println(erro);
                        }
                    }
                }else {
                    //  dsfsdfsdf
                }

            }else {

                radioStatus.parado();

                Snackbar.make(MainActivityPrincipal.content_main_activity_principal, R.string.semConecao, Snackbar.LENGTH_LONG)
                        .setAction("CONECTAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                    startActivity(it);
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        }).show();
            }

        }

        return true;
    }

   // public void start() {

   //     int timeInSec = 10;

   //     Intent intent = new Intent(this, AlarmReceiver.class);
   //     PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234, intent, 0);
   //     AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
   //     alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (timeInSec * 1000), pendingIntent);
   //     Toast.makeText(this, "Alarm set to after "  + timeInSec +" seconds", Toast.LENGTH_LONG).show();
   // }

    /**
     * Lê os status que o aplicativo está e carrega para os status atual
     */
    private void resetarValores() {

        if (Funcionalidades.isTocando()) {

            btmPlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause));
            imageLogo.setImageBitmap(AsynDataClassStatusMetaDados.bitmap);
            textView_titulo.setText(AsynDataClassStatus.titulo);
            textView_artista.setText(AsynDataClassStatus.artista);
            //animation.start();

        } else {

            if (Funcionalidades.isProcurandoServidor()) {
                textView_titulo.setText(R.string.procurando_Servidor);
                textView_artista.setText("Aguarde...");
                MainActivityPrincipal.imageLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                btmPlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause));
               // animation.stop();
            } else {

                if (Funcionalidades.isConectando()) {
                    textView_titulo.setText(R.string.conectandoNome);
                    textView_artista.setText("Aguarde...");
                    MainActivityPrincipal.imageLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                    btmPlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pause));
                   // animation.stop();
                } else {

                    if (Funcionalidades.isErroAoEncontrarServidor()) {
                        textView_titulo.setText(R.string.erro_ao_encontrar_servidor);
                        textView_artista.setText("Ops.");
                        MainActivityPrincipal.imageLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                        btmPlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                       // animation.stop();

                    } else {
                        if (Funcionalidades.isErroAoConectar()) {
                            textView_titulo.setText(R.string.erroConexaoNome);
                            textView_artista.setText("Ops.");
                            MainActivityPrincipal.imageLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                            btmPlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                           // animation.stop();

                        } else {
                            textView_titulo.setText(R.string.paradoNome);
                            textView_artista.setText("Stop");
                            MainActivityPrincipal.imageLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
                            btmPlay.setImageDrawable(getResources().getDrawable(R.mipmap.ic_play));
                           // animation.stop();
                        }
                    }

                }
            }

        }

    }

    /**
     * Esse método é executado sempre que o aplicativo é fechado
     *
     */
    @Override
    public void onDestroy(){
        super.onDestroy(); // chama o método da classe pai
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver); // desrregistra o broadcast receiver
    }

}
