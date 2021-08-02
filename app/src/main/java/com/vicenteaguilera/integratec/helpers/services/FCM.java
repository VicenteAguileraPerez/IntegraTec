package com.vicenteaguilera.integratec.helpers.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.controllers.BandejaActivity;
import com.vicenteaguilera.integratec.controllers.ListAdviserActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FCM extends FirebaseMessagingService
{
    //token del dispositivo
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("token","mi token es: "+s);
       // guardarToken(s);
    }

    private void guardarToken(String s)
    {
        Map<String, String> dataToken= new HashMap<>();
        dataToken.put("token",s);

    }

    // recibe notificaciones
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null)
        {
                String titulo = remoteMessage.getNotification().getTitle();
                String body = remoteMessage.getNotification().getBody();

               if(remoteMessage.getData().size()==0)
               {
                   Log.e("err","no topic");
                   Log.e("Mensaje", "Message Notification Body: " + titulo + " " + body);
                   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                       oreoMayorNormal(titulo, body,true);

                   } else {
                       oreoMenorNormal(titulo, body,true);
                   }
               }
               else
               {
                   String topic[] = remoteMessage.getData().get("topic").split(",");
                   if(topic[0].equals("asesores"))
                   {
                       Log.e("err","false asesores");
                       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                       {

                           oreoMayorNormal(titulo+","+topic[1], body,false);

                       } else {
                           oreoMenorNormal(titulo+","+topic[1], body,false);
                       }
                   }
                   else
                   {
                       Log.e("err","false personal");
                       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                       {
                           oreoMayorNormal(titulo+","+topic[1], body,false);

                       } else {
                           oreoMenorNormal(titulo+","+topic[1], body,false);
                       }
                   }

               }





        }

    }
    private void oreoMenorNormal(String titulo, String text,boolean isNormal)
    {
        String id= "mensaje";
        NotificationManager nm= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,id);
        mBuilder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo.split(",")[0])
                .setSmallIcon(R.mipmap.ic_launcher_2)
                .setContentText(text)
                .setContentIntent(isNormal?clickNotification():clickNotificationAsesor(titulo.split(",")[1],text))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text).setBigContentTitle(titulo.split(",")[0]))
                .setContentInfo("nuevo");
        Random r = new Random();
        int idNotified = r.nextInt(8000);
        assert nm != null;
        nm.notify(idNotified,mBuilder.build());
    }
    private void oreoMayorNormal(String titulo, String text,boolean isNormal)
    {
        String id= "mensaje";
        NotificationManager nm= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel nc = new NotificationChannel(id,"Nuevo",NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }
        mBuilder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo.split(",")[0])
                .setSmallIcon(R.mipmap.ic_launcher_2)
                .setContentText(text)
                .setContentIntent(isNormal?clickNotification():clickNotificationAsesor(titulo.split(",")[1],text))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text).setBigContentTitle(titulo.split(",")[0]))
                .setContentInfo("nuevo");
        Random r = new Random();
        int idNotified = r.nextInt(8000);
        assert nm != null;
        nm.notify(idNotified,mBuilder.build());
    }
    public PendingIntent clickNotification()
    {
        Intent nf = new Intent(getApplicationContext(), ListAdviserActivity.class);
        nf.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this,0,nf,0);
    }
    public PendingIntent clickNotificationAsesor(String from,String texto)
    {
        Intent nf = new Intent(getApplicationContext(), BandejaActivity.class);
        Bundle parmetros = new Bundle();
        parmetros.putString("titulo",from);
        parmetros.putString("mensaje",texto);
        nf.putExtras(parmetros);
        nf.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return PendingIntent.getActivity(
                getApplicationContext(),
                0,
                nf,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
}
