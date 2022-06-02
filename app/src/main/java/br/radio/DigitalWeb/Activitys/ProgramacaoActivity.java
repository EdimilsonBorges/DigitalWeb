package br.radio.DigitalWeb.Activitys;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Objects;

import br.radio.DigitalWeb.Status.GetDados;
import br.radio.DigitalWeb.Services.PlayerService;
import br.radio.DigitalWeb.R;

import static br.radio.DigitalWeb.Services.PlayerService.FECHAR_TODAS_ACTIVITYS;

public class ProgramacaoActivity extends AppCompatActivity {

    public LinearLayoutManager segSexLinearLayoutManager, segLinearLayoutManager, terLinearLayoutManager, quaLinearLayoutManager, quiLinearLayoutManager, sexLinearLayoutManager, sabLinearLayoutManager, domLinearLayoutManager;
    @SuppressLint("StaticFieldLeak")
    public static RecyclerView segSexlistViewProgramacao, seglistViewProgramacao, terlistViewProgramacao, qualistViewProgramacao, quilistViewProgramacao, sexlistViewProgramacao, sablistViewProgramacao, domlistViewProgramacao;
    @SuppressLint("StaticFieldLeak")
    public static TextView segSextextView, segtextView,tertextView,quatextView,quitextView,sextextView,sabtextView,domtextView;
    @SuppressLint("StaticFieldLeak")
    public static SwipeRefreshLayout mSwipeToRefresh;
    public GetDados getDados;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progress_programacao;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);      //Ativar o botão
        Objects.requireNonNull(getSupportActionBar()).setTitle("Grade de Programação");     //Titulo para ser exibido na sua Action Bar em frente à seta

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(FECHAR_TODAS_ACTIVITYS));

        segSexlistViewProgramacao = findViewById(R.id.segSexlistViewProgramacao);
        seglistViewProgramacao = findViewById(R.id.seglistProgramacao);
        terlistViewProgramacao = findViewById(R.id.terlistProgramacao);
        qualistViewProgramacao = findViewById(R.id.qualistProgramacao);
        quilistViewProgramacao = findViewById(R.id.quilistProgramacao);
        sexlistViewProgramacao = findViewById(R.id.sexlistProgramacao);
        sablistViewProgramacao = findViewById(R.id.sablistProgramacao);
        domlistViewProgramacao = findViewById(R.id.domlistProgramacao);

        segSextextView = findViewById(R.id.segSextextView);
        segtextView = findViewById(R.id.segtextView);
        tertextView = findViewById(R.id.tertextView);
        quatextView = findViewById(R.id.quatextView);
        quitextView = findViewById(R.id.quitextView);
        sextextView = findViewById(R.id.sextextView);
        sabtextView = findViewById(R.id.sabtextView);
        domtextView = findViewById(R.id.domtextView);

        progress_programacao = findViewById(R.id.progress_programacao);


        segSexLinearLayoutManager = new LinearLayoutManager(this);
        segSexLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Configurando um dividr entre linhas, para uma melhor visualização.
        // seglistViewProgramacao.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        segSexlistViewProgramacao.setLayoutManager(segSexLinearLayoutManager);

        segLinearLayoutManager = new LinearLayoutManager(this);
        segLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Configurando um dividr entre linhas, para uma melhor visualização.
        // seglistViewProgramacao.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        seglistViewProgramacao.setLayoutManager(segLinearLayoutManager);

        terLinearLayoutManager = new LinearLayoutManager(this);
        terLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Configurando um dividr entre linhas, para uma melhor visualização.
        // terlistViewProgramacao.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        terlistViewProgramacao.setLayoutManager(terLinearLayoutManager);

        quaLinearLayoutManager = new LinearLayoutManager(this);
        quaLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Configurando um dividr entre linhas, para uma melhor visualização.
        // qualistViewProgramacao.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        qualistViewProgramacao.setLayoutManager(quaLinearLayoutManager);

        quiLinearLayoutManager = new LinearLayoutManager(this);
        quiLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Configurando um dividr entre linhas, para uma melhor visualização.
        // quilistViewProgramacao.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        quilistViewProgramacao.setLayoutManager(quiLinearLayoutManager);

        sexLinearLayoutManager = new LinearLayoutManager(this);
        sexLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Configurando um dividr entre linhas, para uma melhor visualização.
        // sexlistViewProgramacao.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        sexlistViewProgramacao.setLayoutManager(sexLinearLayoutManager);

        sabLinearLayoutManager = new LinearLayoutManager(this);
        sabLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Configurando um dividr entre linhas, para uma melhor visualização.
        // sablistViewProgramacao.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        sablistViewProgramacao.setLayoutManager(sabLinearLayoutManager);

        domLinearLayoutManager = new LinearLayoutManager(this);
        domLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Configurando um dividr entre linhas, para uma melhor visualização.
        // domlistViewProgramacao.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        domlistViewProgramacao.setLayoutManager(domLinearLayoutManager);

        getDados = new GetDados(this, PlayerService.mediaSessionCompat);

        // Recupera o SwipeRefreshLayout
        mSwipeToRefresh = findViewById(R.id.swipe_refresh_container);

// Seta o Listener para atualizar o conteudo quando o gesto for feito
        // mSwipeToRefresh.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);

// O esquema de cores
        mSwipeToRefresh.setColorSchemeResources(
                R.color.md_light_green_A700,
                R.color.md_red_A400,
                R.color.md_light_blue_A700,
                R.color.md_orange_500_75,
                R.color.md_lime_A700,
                R.color.md_light_blue_A700,
                R.color.md_pink_500_75
        );

        mSwipeToRefresh.setOnRefreshListener(() -> {
            ProgramacaoActivity.progress_programacao.setVisibility(View.GONE);
            getDados = new GetDados(ProgramacaoActivity.this, PlayerService.mediaSessionCompat);
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
            seglistViewProgramacao = null;
            Intent it = new Intent(ProgramacaoActivity.this, MainActivityPrincipal.class);
            startActivity(it);
            finish();
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            seglistViewProgramacao = null;
            Intent it = new Intent(ProgramacaoActivity.this,MainActivityPrincipal.class);

            startActivity(it);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
