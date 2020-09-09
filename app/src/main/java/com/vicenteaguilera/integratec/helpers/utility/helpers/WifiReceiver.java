package com.vicenteaguilera.integratec.helpers.utility.helpers;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.vicenteaguilera.integratec.controllers.MainAdviserActivityApp;

public class WifiReceiver extends BroadcastReceiver {
    ProgressDialog dialog= null;
    InternetHelper internetHelper = new InternetHelper();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(dialog==null)
        {
            dialog = ProgressDialog.show(context, "Conectando a la red",
                    "Esperando la disponibilidad de la red", true);
        }
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        if (activeNetwork != null)
        {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE && internetHelper.isOnlineNet())
            {
                // connected to wifi
                dialog.dismiss();
            }
            else
            {
                dialog.show();
            }
        } else {
            // not connected to the internet

            dialog.show();
            Toast.makeText(context,"no tengo red",Toast.LENGTH_SHORT).show();
        }

    }
}
