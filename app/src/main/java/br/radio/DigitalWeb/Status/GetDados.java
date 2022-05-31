package br.radio.DigitalWeb.Status;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import br.radio.DigitalWeb.Activitys.ProgramacaoActivity;
import br.radio.DigitalWeb.AsynData.AsynDataClass;


public class GetDados {

        private MyAdapterReclycle myAdapterReclycle;
        private final List<SetDados> segSexSetDadosList, segSetDadosList, terSetDadosList, quaSetDadosList, quiSetDadosList, sexSetDadosList, sabSetDadosList, domSetDadosList;
        private Context context;

        private final ArrayList<String> segSexNome, segNome, terNome, quaNome, quiNome, sexNome, sabNome, domNome;
        private final ArrayList<String> segSexApresentador,segApresentador, terApresentador, quaApresentador, quiApresentador, sexApresentador, sabApresentador, domApresentador;
        private final ArrayList<String> segSexHorario,segHorario, terHorario, quaHorario, quiHorario, sexHorario, sabHorario, domHorario;
        private final ArrayList<String> segSexImagem,segImagem, terImagem, quaImagem, quiImagem, sexImagem, sabImagem, domImagem;
        public static ArrayList<String> urlMetadata;
        private final RadioStatus radioStatus;

    public GetDados(Context ctx, MediaSessionCompat mediaSessionCompat) {
        radioStatus = new RadioStatus(context,mediaSessionCompat);

        this.context = ctx;

            urlMetadata= new ArrayList<>();
            segSexSetDadosList = new ArrayList<>();
            segSetDadosList = new ArrayList<>();
            terSetDadosList = new ArrayList<>();
            quaSetDadosList = new ArrayList<>();
            quiSetDadosList = new ArrayList<>();
            sexSetDadosList = new ArrayList<>();
            sabSetDadosList = new ArrayList<>();
            domSetDadosList = new ArrayList<>();


            segSexNome = new ArrayList<>();
            segSexApresentador = new ArrayList<>();
            segSexHorario = new ArrayList<>();
            segSexImagem = new ArrayList<>();

            segNome = new ArrayList<>();
            segApresentador = new ArrayList<>();
            segHorario = new ArrayList<>();
            segImagem= new ArrayList<>();

            terNome = new ArrayList<>();
            terApresentador = new ArrayList<>();
            terHorario = new ArrayList<>();
            terImagem = new ArrayList<>();

            quaNome = new ArrayList<>();
            quaApresentador = new ArrayList<>();
            quaHorario = new ArrayList<>();
            quaImagem = new ArrayList<>();

            quiNome = new ArrayList<>();
            quiApresentador = new ArrayList<>();
            quiHorario = new ArrayList<>();
            quiImagem = new ArrayList<>();

            sexNome = new ArrayList<>();
            sexApresentador = new ArrayList<>();
            sexHorario = new ArrayList<>();
            sexImagem = new ArrayList<>();

            sabNome = new ArrayList<>();
            sabApresentador = new ArrayList<>();
            sabHorario = new ArrayList<>();
            sabImagem = new ArrayList<>();

            domNome = new ArrayList<>();
            domApresentador = new ArrayList<>();
            domHorario = new ArrayList<>();
            domImagem = new ArrayList<>();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference metadataUrl = database.getReference();
            DatabaseReference segSexRef = database.getReference("1").child("segsex");
            DatabaseReference segRef = database.getReference("1").child("seg");
            DatabaseReference terRef = database.getReference("1").child("ter");
            DatabaseReference quaRef = database.getReference("1").child("qua");
            DatabaseReference quiRef = database.getReference("1").child("qui");
            DatabaseReference sexRef = database.getReference("1").child("sex");
            DatabaseReference sabRef = database.getReference("1").child("sab");
            DatabaseReference domRef = database.getReference("1").child("dom");


            metadataUrl.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("0").exists() && ProgramacaoActivity.seglistViewProgramacao == null) {
                        for (DataSnapshot snapshot : dataSnapshot.child("0").getChildren()) {
                            urlMetadata.add(snapshot.getValue().toString());
                        }

                        try {
                            AsynDataClass.urlMetaData = GetDados.urlMetadata.get(0);
                            AsynDataClass.singleParsedWhat = GetDados.urlMetadata.get(3);
                            AsynDataClass.dataurl = GetDados.urlMetadata.get(2);

                        } catch (Exception e) {
                            e.getMessage();

                            AsynDataClass.dataurl = null;
                           radioStatus.erroAoEncontrarServidor();

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    AsynDataClass.dataurl = null;
                    radioStatus.erroAoEncontrarServidor();
                }
            });


            segSexRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Este método é chamado uma vez com o valor inicial e novamente
                    // sempre que os dados neste local são atualizados.
                    segSexNome.clear();
                    segSexApresentador.clear();
                    segSexHorario.clear();
                    segSexImagem.clear();
                    segSexSetDadosList.clear();

                    //   for (DataSnapshot snapshot : dataSnapshot.child("seg").getChildren()) {
                    // SetDados getDados = snapshot.getValue( SetDados.class );
                    if (dataSnapshot.child("nome").exists() && dataSnapshot.child("apresentador").exists() && dataSnapshot.child("horario").exists() && dataSnapshot.child("imagem").exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.child("nome").getChildren()) {
                            segSexNome.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("apresentador").getChildren()) {
                            segSexApresentador.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("horario").getChildren()) {

                            segSexHorario.add(snapshot.getValue().toString());
                        }

                        for (DataSnapshot snapshot : dataSnapshot.child("imagem").getChildren()) {
                            segSexImagem.add(snapshot.getValue().toString());
                        }

                        if (segSexNome.size() == segSexApresentador.size() && segSexNome.size() == segSexHorario.size() && segSexNome.size() == segSexImagem.size() && segSexApresentador.size() == segSexHorario.size() && segSexApresentador.size() == segSexImagem.size() && segSexHorario.size() == segSexImagem.size()) {

                            for (DataSnapshot ignored : dataSnapshot.child("nome").getChildren()) {

                                segSexSetDadosList.add(new SetDados(segSexNome.get(segSexSetDadosList.size()), segSexApresentador.get(segSexSetDadosList.size()), segSexHorario.get(segSexSetDadosList.size()), segSexImagem.get(segSexSetDadosList.size())));
                                myAdapterReclycle = new MyAdapterReclycle(context, segSexSetDadosList);
                                if(ProgramacaoActivity.segSexlistViewProgramacao != null){
                                    ProgramacaoActivity.segSexlistViewProgramacao.setAdapter(myAdapterReclycle);
                                }
                            }
                            if(ProgramacaoActivity.segSexlistViewProgramacao != null){
                                ProgramacaoActivity.segSextextView.setVisibility(View.VISIBLE);
                                ProgramacaoActivity.progress_programacao.setVisibility(View.GONE);
                            }
                        }

                        myAdapterReclycle.notifyDataSetChanged();
                        if(ProgramacaoActivity.segSexlistViewProgramacao != null){
                        ProgramacaoActivity.mSwipeToRefresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // // Falha ao ler o valor
                    // Log.w(TAG, "Failed to read value.", error.toException());
                    // AsynDataClassStatus.status = "Erro";
                }
            });

            segRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Este método é chamado uma vez com o valor inicial e novamente
                    // sempre que os dados neste local são atualizados.
                    segNome.clear();
                    segApresentador.clear();
                    segHorario.clear();
                    segImagem.clear();
                    segSetDadosList.clear();

                    //   for (DataSnapshot snapshot : dataSnapshot.child("seg").getChildren()) {
                    // SetDados getDados = snapshot.getValue( SetDados.class );
                    if (dataSnapshot.child("nome").exists() && dataSnapshot.child("apresentador").exists() && dataSnapshot.child("horario").exists() && dataSnapshot.child("imagem").exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.child("nome").getChildren()) {
                            segNome.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("apresentador").getChildren()) {
                            segApresentador.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("horario").getChildren()) {

                            segHorario.add(snapshot.getValue().toString());
                        }

                        for (DataSnapshot snapshot : dataSnapshot.child("imagem").getChildren()) {
                            segImagem.add(snapshot.getValue().toString());
                        }

                        if (segNome.size() == segApresentador.size() && segNome.size() == segHorario.size() && segNome.size() == segImagem.size() && segApresentador.size() == segHorario.size() && segApresentador.size() == segImagem.size() && segHorario.size() == segImagem.size()) {

                            for (DataSnapshot ignored : dataSnapshot.child("nome").getChildren()) {

                                segSetDadosList.add(new SetDados(segNome.get(segSetDadosList.size()), segApresentador.get(segSetDadosList.size()), segHorario.get(segSetDadosList.size()), segImagem.get(segSetDadosList.size())));
                                myAdapterReclycle = new MyAdapterReclycle(context, segSetDadosList);
                                if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.seglistViewProgramacao.setAdapter(myAdapterReclycle);
                                }
                            }
                            if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.segSextextView.setVisibility(View.VISIBLE);
                                ProgramacaoActivity.progress_programacao.setVisibility(View.GONE);
                            }
                        }

                        myAdapterReclycle.notifyDataSetChanged();
                        if(ProgramacaoActivity.segSexlistViewProgramacao != null){
                            ProgramacaoActivity.mSwipeToRefresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // // Falha ao ler o valor
                    // Log.w(TAG, "Failed to read value.", error.toException());
                    // AsynDataClassStatus.status = "Erro";
                }
            });

            terRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Este método é chamado uma vez com o valor inicial e novamente
                    // sempre que os dados neste local são atualizados.
                    terNome.clear();
                    terApresentador.clear();
                    terHorario.clear();
                    terImagem.clear();
                    terSetDadosList.clear();

                    //   for (DataSnapshot snapshot : dataSnapshot.child("seg").getChildren()) {
                    // SetDados getDados = snapshot.getValue( SetDados.class );

                    if (dataSnapshot.child("nome").exists() && dataSnapshot.child("apresentador").exists() && dataSnapshot.child("horario").exists() && dataSnapshot.child("imagem").exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.child("nome").getChildren()) {
                            terNome.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("apresentador").getChildren()) {
                            terApresentador.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("horario").getChildren()) {
                            terHorario.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("imagem").getChildren()) {
                            terImagem.add(snapshot.getValue().toString());
                        }
                        if (terNome.size() == terApresentador.size() && terNome.size() == terHorario.size() && terNome.size() == terImagem.size() && terApresentador.size() == terHorario.size() && terApresentador.size() == terImagem.size() && terHorario.size() == terImagem.size()) {

                            for (DataSnapshot ignored : dataSnapshot.child("nome").getChildren()) {

                                terSetDadosList.add(new SetDados(terNome.get(terSetDadosList.size()), terApresentador.get(terSetDadosList.size()), terHorario.get(terSetDadosList.size()), terImagem.get(terSetDadosList.size())));
                                myAdapterReclycle = new MyAdapterReclycle(context, terSetDadosList);
                                if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.terlistViewProgramacao.setAdapter(myAdapterReclycle);
                            }}
                            if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.tertextView.setVisibility(View.VISIBLE);
                                ProgramacaoActivity.progress_programacao.setVisibility(View.GONE);
                        }}

                        myAdapterReclycle.notifyDataSetChanged();
                        if(ProgramacaoActivity.segSexlistViewProgramacao != null){
                            ProgramacaoActivity.mSwipeToRefresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // // Falha ao ler o valor
                    // Log.w(TAG, "Failed to read value.", error.toException());
                    // AsynDataClassStatus.status = "Erro";
                }
            });

            quaRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Este método é chamado uma vez com o valor inicial e novamente
                    // sempre que os dados neste local são atualizados.
                    quaNome.clear();
                    quaApresentador.clear();
                    quaHorario.clear();
                    quaImagem.clear();
                    quaSetDadosList.clear();

                    //   for (DataSnapshot snapshot : dataSnapshot.child("seg").getChildren()) {
                    // SetDados getDados = snapshot.getValue( SetDados.class );
                    if (dataSnapshot.child("nome").exists() && dataSnapshot.child("apresentador").exists() && dataSnapshot.child("horario").exists() && dataSnapshot.child("imagem").exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.child("nome").getChildren()) {
                            quaNome.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("apresentador").getChildren()) {
                            quaApresentador.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("horario").getChildren()) {

                            quaHorario.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("imagem").getChildren()) {
                            quaImagem.add(snapshot.getValue().toString());
                        }
                        if (quaNome.size() == quaApresentador.size() && quaNome.size() == quaHorario.size() && quaNome.size() == quaImagem.size() && quaApresentador.size() == quaHorario.size() && quaApresentador.size() == quaImagem.size() && quaHorario.size() == quaImagem.size()) {

                            for (DataSnapshot ignored : dataSnapshot.child("nome").getChildren()) {

                                quaSetDadosList.add(new SetDados(quaNome.get(quaSetDadosList.size()), quaApresentador.get(quaSetDadosList.size()), quaHorario.get(quaSetDadosList.size()), quaImagem.get(quaSetDadosList.size())));
                                myAdapterReclycle = new MyAdapterReclycle(context, quaSetDadosList);
                                if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.qualistViewProgramacao.setAdapter(myAdapterReclycle);
                            }}
                            if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.quatextView.setVisibility(View.VISIBLE);
                                ProgramacaoActivity.progress_programacao.setVisibility(View.GONE);
                        }}

                        myAdapterReclycle.notifyDataSetChanged();
                        if(ProgramacaoActivity.segSexlistViewProgramacao != null){
                            ProgramacaoActivity.mSwipeToRefresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // // Falha ao ler o valor
                    // Log.w(TAG, "Failed to read value.", error.toException());
                    // AsynDataClassStatus.status = "Erro";
                }
            });

            quiRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Este método é chamado uma vez com o valor inicial e novamente
                    // sempre que os dados neste local são atualizados.
                    quiNome.clear();
                    quiApresentador.clear();
                    quiHorario.clear();
                    quiImagem.clear();
                    quiSetDadosList.clear();

                    //   for (DataSnapshot snapshot : dataSnapshot.child("seg").getChildren()) {
                    // SetDados getDados = snapshot.getValue( SetDados.class );
                    if (dataSnapshot.child("nome").exists() && dataSnapshot.child("apresentador").exists() && dataSnapshot.child("horario").exists() && dataSnapshot.child("imagem").exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.child("nome").getChildren()) {
                            quiNome.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("apresentador").getChildren()) {
                            quiApresentador.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("horario").getChildren()) {

                            quiHorario.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("imagem").getChildren()) {
                            quiImagem.add(snapshot.getValue().toString());
                        }
                        if (quiNome.size() == quiApresentador.size() && quiNome.size() == quiHorario.size() && quiNome.size() == quiImagem.size() && quiApresentador.size() == quiHorario.size() && quiApresentador.size() == quiImagem.size() && quiHorario.size() == quiImagem.size()) {

                            for (DataSnapshot ignored : dataSnapshot.child("nome").getChildren()) {

                                quiSetDadosList.add(new SetDados(quiNome.get(quiSetDadosList.size()), quiApresentador.get(quiSetDadosList.size()), quiHorario.get(quiSetDadosList.size()), quiImagem.get(quiSetDadosList.size())));
                                myAdapterReclycle = new MyAdapterReclycle(context, quiSetDadosList);
                                if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.quilistViewProgramacao.setAdapter(myAdapterReclycle);
                            }}
                            if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.quitextView.setVisibility(View.VISIBLE);
                                ProgramacaoActivity.progress_programacao.setVisibility(View.GONE);
                        }}

                        myAdapterReclycle.notifyDataSetChanged();
                        if(ProgramacaoActivity.segSexlistViewProgramacao != null){
                            ProgramacaoActivity.mSwipeToRefresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // // Falha ao ler o valor
                    // Log.w(TAG, "Failed to read value.", error.toException());
                    // AsynDataClassStatus.status = "Erro";
                }
            });

            sexRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Este método é chamado uma vez com o valor inicial e novamente
                    // sempre que os dados neste local são atualizados.
                    sexNome.clear();
                    sexApresentador.clear();
                    sexHorario.clear();
                    sexImagem.clear();
                    sexSetDadosList.clear();

                    //   for (DataSnapshot snapshot : dataSnapshot.child("seg").getChildren()) {
                    // SetDados getDados = snapshot.getValue( SetDados.class );
                    if (dataSnapshot.child("nome").exists() && dataSnapshot.child("apresentador").exists() && dataSnapshot.child("horario").exists() && dataSnapshot.child("imagem").exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.child("nome").getChildren()) {
                            sexNome.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("apresentador").getChildren()) {
                            sexApresentador.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("horario").getChildren()) {

                            sexHorario.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("imagem").getChildren()) {
                            sexImagem.add(snapshot.getValue().toString());
                        }
                        if (sexNome.size() == sexApresentador.size() && sexNome.size() == sexHorario.size() && sexNome.size() == sexImagem.size() && sexApresentador.size() == sexHorario.size() && sexApresentador.size() == sexImagem.size() && sexHorario.size() == sexImagem.size()) {

                            for (DataSnapshot ignored : dataSnapshot.child("nome").getChildren()) {

                                sexSetDadosList.add(new SetDados(sexNome.get(sexSetDadosList.size()), sexApresentador.get(sexSetDadosList.size()), sexHorario.get(sexSetDadosList.size()), sexImagem.get(sexSetDadosList.size())));
                                myAdapterReclycle = new MyAdapterReclycle(context, sexSetDadosList);
                                if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.sexlistViewProgramacao.setAdapter(myAdapterReclycle);
                            }}
                            if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.sextextView.setVisibility(View.VISIBLE);
                                ProgramacaoActivity.progress_programacao.setVisibility(View.GONE);
                        }}

                        myAdapterReclycle.notifyDataSetChanged();
                        if(ProgramacaoActivity.segSexlistViewProgramacao != null){
                            ProgramacaoActivity.mSwipeToRefresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // // Falha ao ler o valor
                    // Log.w(TAG, "Failed to read value.", error.toException());
                    // AsynDataClassStatus.status = "Erro";
                }
            });

            sabRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Este método é chamado uma vez com o valor inicial e novamente
                    // sempre que os dados neste local são atualizados.
                    sabNome.clear();
                    sabApresentador.clear();
                    sabHorario.clear();
                    sabImagem.clear();
                    sabSetDadosList.clear();

                    //   for (DataSnapshot snapshot : dataSnapshot.child("seg").getChildren()) {
                    // SetDados getDados = snapshot.getValue( SetDados.class );
                    if (dataSnapshot.child("nome").exists() && dataSnapshot.child("apresentador").exists() && dataSnapshot.child("horario").exists() && dataSnapshot.child("imagem").exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.child("nome").getChildren()) {
                            sabNome.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("apresentador").getChildren()) {
                            sabApresentador.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("horario").getChildren()) {

                            sabHorario.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("imagem").getChildren()) {
                            sabImagem.add(snapshot.getValue().toString());
                        }
                        if (sabNome.size() == sabApresentador.size() && sabNome.size() == sabHorario.size() && sabNome.size() == sabImagem.size() && sabApresentador.size() == sabHorario.size() && sabApresentador.size() == sabImagem.size() && sabHorario.size() == sabImagem.size()) {

                            for (DataSnapshot ignored : dataSnapshot.child("nome").getChildren()) {

                                sabSetDadosList.add(new SetDados(sabNome.get(sabSetDadosList.size()), sabApresentador.get(sabSetDadosList.size()), sabHorario.get(sabSetDadosList.size()), sabImagem.get(sabSetDadosList.size())));
                                myAdapterReclycle = new MyAdapterReclycle(context, sabSetDadosList);
                                if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.sablistViewProgramacao.setAdapter(myAdapterReclycle);
                            }}
                            if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.sabtextView.setVisibility(View.VISIBLE);
                                ProgramacaoActivity.progress_programacao.setVisibility(View.GONE);
                        }}

                        myAdapterReclycle.notifyDataSetChanged();
                        if(ProgramacaoActivity.segSexlistViewProgramacao != null){
                            ProgramacaoActivity.mSwipeToRefresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // // Falha ao ler o valor
                    // Log.w(TAG, "Failed to read value.", error.toException());
                    // AsynDataClassStatus.status = "Erro";
                }
            });

            domRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Este método é chamado uma vez com o valor inicial e novamente
                    // sempre que os dados neste local são atualizados.
                    domNome.clear();
                    domApresentador.clear();
                    domHorario.clear();
                    domImagem.clear();
                    domSetDadosList.clear();

                    // for (DataSnapshot snapshot : dataSnapshot.child("seg").getChildren()) {
                    // SetDados getDados = snapshot.getValue( SetDados.class );

                    if (dataSnapshot.child("nome").exists() && dataSnapshot.child("apresentador").exists() && dataSnapshot.child("horario").exists() && dataSnapshot.child("imagem").exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.child("nome").getChildren()) {
                            domNome.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("apresentador").getChildren()) {
                            domApresentador.add(snapshot.getValue().toString());
                        }
                        for (DataSnapshot snapshot : dataSnapshot.child("horario").getChildren()) {
                            domHorario.add(snapshot.getValue().toString());
                        }

                        for (DataSnapshot snapshot : dataSnapshot.child("imagem").getChildren()) {
                            domImagem.add(snapshot.getValue().toString());
                        }

                        if (domNome.size() == domApresentador.size() && domNome.size() == domHorario.size() && domNome.size() == domImagem.size() && domApresentador.size() == domHorario.size() && domApresentador.size() == domImagem.size() && domHorario.size() == domImagem.size()) {
                            for (DataSnapshot ignored : dataSnapshot.child("nome").getChildren()) {

                                domSetDadosList.add(new SetDados(domNome.get(domSetDadosList.size()), domApresentador.get(domSetDadosList.size()), domHorario.get(domSetDadosList.size()), domImagem.get(domSetDadosList.size())));
                                myAdapterReclycle = new MyAdapterReclycle(context, domSetDadosList);
                                if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.domlistViewProgramacao.setAdapter(myAdapterReclycle);
                            }}
                            if(ProgramacaoActivity.seglistViewProgramacao != null){
                                ProgramacaoActivity.domtextView.setVisibility(View.VISIBLE);
                                ProgramacaoActivity.progress_programacao.setVisibility(View.GONE);
                        }}

                        myAdapterReclycle.notifyDataSetChanged();
                        if(ProgramacaoActivity.segSexlistViewProgramacao != null){
                            ProgramacaoActivity.mSwipeToRefresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // // Falha ao ler o valor
                    // Log.w(TAG, "Failed to read value.", error.toException());
                    // AsynDataClassStatus.status = "Erro";
                }
            });

        }


    }

