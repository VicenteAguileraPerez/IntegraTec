package com.vicenteaguilera.integratec.helpers.utility.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper
{
    private Context context;
    private final String DATA = "datos_asesorias";
    SharedPreferences sharedPref;
    public SharedPreferencesHelper(Context context)
    {
        this.context = context;
        sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
    }
    public  Set<String> getPreferences()
    {
        Set<String> data= new HashSet<>();
        SharedPreferences sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
        Set<String> datos = sharedPref.getStringSet("data", null);
        if(datos!=null)
            data=datos;
        return data;

    }
    public void deletePreferences()
    {
        SharedPreferences sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();
    }
    public void addPreferences(Set<String> saveAsesoria)
    {
        SharedPreferences sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet("data", saveAsesoria);
        editor.apply();
    }
}
