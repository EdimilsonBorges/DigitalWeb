package br.radio.DigitalWeb.Connect;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Edimilson Borges on 19/10/2018.
 **/

public class CheckConnection {

    private final Context context;

    public CheckConnection(Context context){
        this.context = context;

        isConnect();
    }

     public boolean isConnect(){
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
