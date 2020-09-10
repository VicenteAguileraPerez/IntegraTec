package com.vicenteaguilera.integratec.helpers.utility.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import java.util.Objects;


public class InternetHelper
{


    public boolean timeAutomatically(ContentResolver contentResolver)
    {
        boolean status=false;
        try {
            if(Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME) == 1)
            {
                // Enabled
                status=true;
            }
            else
            {
                // Disabed
                status=false;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return status;

    }
    //si hay red esto significa que estoy conectado ya sea con DATOS O WIFI
    public boolean isNetDisponible(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(context).getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
    // significa o evalua que la señar esta lista para conectarse a la red.
    public Boolean isOnlineNet()
    {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");

            int val = p.waitFor();
            return (val == 0);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}

