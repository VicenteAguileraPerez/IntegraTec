package com.vicenteaguilera.integratec.helpers.utility.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
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
    public  Map<String, Object> getPreferences()
    {
        Map<String,Object> saveAsesoria = new HashMap<>();

        SharedPreferences sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
        saveAsesoria.put("estado",sharedPref.getString("estado", null));
        saveAsesoria.put("tipo",sharedPref.getString("tipo", null));
        saveAsesoria.put("lugar",sharedPref.getString("lugar", null));
        saveAsesoria.put("lugar2",sharedPref.getString("lugar2", null));
        saveAsesoria.put("materia",sharedPref.getString("materia",null));
        saveAsesoria.put("url",sharedPref.getString("url", null));
        saveAsesoria.put("h_inicio",sharedPref.getString("h_inicio", null));
        saveAsesoria.put("h_fin",sharedPref.getString("h_fin", null));
        saveAsesoria.put("info",sharedPref.getString("info", null));

        return  saveAsesoria;
    }
    public void deletePreferences()
    {
        SharedPreferences sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();
    }
    public void addPreferences(Map<String,Object> saveAsesoria)
    {
        SharedPreferences sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("estado", String.valueOf( saveAsesoria.get("estado")));
        editor.putString("tipo",String.valueOf(saveAsesoria.get("tipo")));
        editor.putString("lugar2",String.valueOf(saveAsesoria.get("lugar2")));
        editor.putString("lugar",String.valueOf(saveAsesoria.get("lugar")));
        editor.putString("url",String.valueOf(saveAsesoria.get("url")));
        editor.putString("materia",String.valueOf(saveAsesoria.get("materia")));
        editor.putString("h_inicio",String.valueOf(saveAsesoria.get("h_inicio")));
        editor.putString("h_fin",String.valueOf(saveAsesoria.get("h_fin")));
        editor.putString("info",String.valueOf(saveAsesoria.get("info")));
        editor.apply();
    }
    public boolean hasData()
    {
        SharedPreferences sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
        String estado = sharedPref.getString("estado", "");
        return !Objects.requireNonNull(estado).isEmpty();
    }
}
