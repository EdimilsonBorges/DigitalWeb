package br.radio.DigitalWeb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.FlipHorizontalAnimation;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MyAdapterReclycle extends RecyclerView.Adapter<MyAdapterReclycle.MyViewHolder>{
    public List<SetDados> setDadosList;
    public Context context;
    public Bitmap bitmap;
    public Handler handler;


    public MyAdapterReclycle(Context context, List<SetDados> setDados){
        this.context = context;
        setDadosList = setDados;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_recycleview,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        String nome = setDadosList.get(position).getNomeDoApresentador();
        String prog = setDadosList.get(position).getNomeDoPrograma();
        String hora = setDadosList.get(position).getHorario();
        String imagem = setDadosList.get(position).getUrlImagemItem();

        myViewHolder.setData(prog,nome,hora,imagem);

         // myViewHolder.linear_item.setAnimation(AnimationUtils.loadAnimation(context,R.anim.move));
          myViewHolder.imageViewItem.setAnimation(AnimationUtils.loadAnimation(context,R.anim.bounce));

    }

    @Override
    public int getItemCount() {
        return setDadosList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewNome;
        TextView textViewApresent;
        TextView textViewhora;
        ImageView imageViewItem;
        LinearLayout linear_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewApresent = itemView.findViewById(R.id.textViewApresent);
            textViewhora = itemView.findViewById(R.id.textViewhora);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            linear_item = itemView.findViewById(R.id.linear_item);
        }
        private void setData(String nomeDoPrograma, String nomeDoApresentador, String horario, String urlImagemItem){

            textViewNome.setText(nomeDoPrograma);
            textViewApresent.setText(nomeDoApresentador);
            textViewhora.setText(horario);

            if(urlImagemItem.equals("nulo")){
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_imagem_item);
                imageViewItem.setImageBitmap(bitmap);
            }else{
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_imagem_item);
                imageViewItem.setImageBitmap(bitmap);
                handler = new Handler(Looper.getMainLooper());
                try{
                    setBitmapFromURL(urlImagemItem,imageViewItem);
                }catch (Exception e){
                    e.getMessage();
                }
            }

            imageViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewItem.setClickable(false);
                    new FlipHorizontalAnimation(imageViewItem).setDuration(700).setListener(new AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            imageViewItem.setClickable(true);
                        }

                    }).animate();
                }
            });

        }
    }


    public void setBitmapFromURL(final String url, final ImageView imageView) {
        new Thread(){
            public void run(){
                try {
                    URL src = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) src.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    final Bitmap myBitmapImagemItem = BitmapFactory.decodeStream(input);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(myBitmapImagemItem);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

}