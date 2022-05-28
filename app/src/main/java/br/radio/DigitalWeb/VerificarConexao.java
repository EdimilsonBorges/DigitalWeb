package br.radio.DigitalWeb;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Edimilson Borges on 19/10/2018.
 **/

class VerificarConexao {

    private final Context context;

    VerificarConexao(Context context){
        this.context = context;

        estaConectado();
    }

     boolean estaConectado(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if(info != null){
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }
}
