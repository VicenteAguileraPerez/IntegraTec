package com.vicenteaguilera.integratec.helpers.services;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotification
{
    public void sendAllDevices(String asesor,String hora, Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        JSONObject message = new JSONObject();
        String URL = "https://fcm.googleapis.com/fcm/send";
        try
        {
            message.put("to","/topics/all");
            JSONObject notification = new JSONObject();
            notification.put("title","Nueva Asesoría disponible");
            notification.put("body",asesor+" ha creado una nueva asesoría, estará disponible de: "+hora);
            message.put("notification",notification);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,message,null,null)
            {
                @Override
                public Map<String, String> getHeaders()
                {
                    Map<String, String> header = new HashMap<>();
                    header.put( "Content-Type", "application/json");
                    header.put("Authorization", "key="+"AAAAz8wfLUY:APA91bGZBMsy8Gy3HLyvmY5hRjv30nCtXnCYj98_SQyTy_18rdMYuuWTMxu_e3hJfnoa6-UhdSZ-j3uHVxS9ne16FiZ_d99yyjnej_oU91cSWONImqVEbX-zpk8UnVO9pkyUr8A7REKk");
                    return header;
                }
            };
            requestQueue.add(request);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    public void sendAllAsesores(String topic,String titulo,String mensaje, Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        JSONObject message = new JSONObject();
        String URL = "https://fcm.googleapis.com/fcm/send";
        try
        {
            message.put("to","/topics/"+topic.split(",")[0]);
            JSONObject notification = new JSONObject();
            notification.put("title",titulo);
            notification.put("body",mensaje);
            //data
            JSONObject data = new JSONObject();
            data.put("topic",topic);
            message.put("data",data);
            message.put("notification",notification);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,message,null,null)
            {
                @Override
                public Map<String, String> getHeaders()
                {
                    Map<String, String> header = new HashMap<>();
                    header.put( "Content-Type", "application/json");
                    header.put("Authorization", "key="+"AAAAz8wfLUY:APA91bGZBMsy8Gy3HLyvmY5hRjv30nCtXnCYj98_SQyTy_18rdMYuuWTMxu_e3hJfnoa6-UhdSZ-j3uHVxS9ne16FiZ_d99yyjnej_oU91cSWONImqVEbX-zpk8UnVO9pkyUr8A7REKk");
                    return header;
                }
            };
            requestQueue.add(request);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
