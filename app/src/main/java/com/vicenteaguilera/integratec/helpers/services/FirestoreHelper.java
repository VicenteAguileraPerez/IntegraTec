package com.vicenteaguilera.integratec.helpers.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.vicenteaguilera.integratec.SplashScreenActivity;
import com.vicenteaguilera.integratec.controllers.MainAdviserActivityApp;
import com.vicenteaguilera.integratec.controllers.OptionsActivity;
import com.vicenteaguilera.integratec.controllers.mainapp.MainAppActivity;
import com.vicenteaguilera.integratec.helpers.utility.Status;
import com.vicenteaguilera.integratec.models.Asesor;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirestoreHelper
{
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference AsesoresCollection = db.collection("asesores");
    private final CollectionReference AsesoriaPublicaCollection = db.collection("asesoria_publica");
    private static Status status;
    private ProgressDialog dialog;
    public static Asesor asesor=null;
    public void addData(Status status, ProgressDialog dialog, Context context, String uid, String email, String password, String[] param)
   {
       if(param.length==3)
       {
           //asesor
           Map<String, Object> asesor = new HashMap<>();
           asesor.put("nombre", param[0]);
           asesor.put("apellido", param[1]);
           asesor.put("carrera", param[2]);
           asesor.put("email", email);
           asesor.put("password", password);
           asesor.put("uri_image", "");
           asesor.put("activo",false);
           Log.e("lista",  asesor.values().toArray().length+"");
           registerDataUserToFirestore(AsesoresCollection,uid,status,dialog, asesor,context);

       }
       else if(param.length==2)
       {
           //es un profesor
       }
   }
   private void registerDataUserToFirestore(CollectionReference collectionReference, final String document, final Status status, final ProgressDialog dialog, final Map<String, Object> data, final Context context)
   {
       // Add a new document with a generated ID
       collectionReference.document(document).set(data)
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task)
                       {
                           if(task.isSuccessful())
                           {
                               status.status("Registrado comuníquese con el administrador para habilitar su cuenta...");
                               Intent intent = new Intent(context, OptionsActivity.class);
                               context.startActivity(intent);
                           }
                           else
                           {
                               status.status("Error del registro de los datos. Inténtelo de nuevo");
                           }
                           dialog.dismiss();
                       }
               });
   }
   //getAsesor
    public void getData(String document, final ProgressDialog dialog, final Status status,final Context context)
    {
        dialog.show();
        AsesoresCollection.document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (Objects.requireNonNull(document).exists())
                    {
                        Map<String,Object>data=document.getData();
                        asesor = new Asesor(document.getId(),String.valueOf(data.get("nombre")),String.valueOf(data.get("apellido")),String.valueOf(data.get("email")),String.valueOf(data.get("password")),String.valueOf(data.get("carrera")),String.valueOf(data.get("uri_image")),(boolean)data.get("activo"));
                        if((boolean)document.get("activo"))
                        {
                            status.status("Bienvenido "+asesor.getNombre()+" "+asesor.getApellidos());
                            Intent intent = new Intent(context, MainAdviserActivityApp.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }
                        else
                        {
                            if(!(((Activity)context) instanceof OptionsActivity))
                            {

                                status.status("Usted se ha registrado, comuníquese con el administrador para habilitar su cuenta...");
                                Intent intent = new Intent(context, OptionsActivity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                            else{
                                status.status("Usted se ha registrado, comuníquese con el administrador para habilitar su cuenta...");
                            }
                        }
                    }
                    else
                    {
                        status.status("No existe esa cuenta");
                    }
                }
                else
                {
                    status.status("Error, verifique su conexión a Internet, si los problemas continuan contacte al administrado");
                }
                dialog.dismiss();
            }
        });

    }

    private void registerDataAsesoriaPublicaToFirestore(CollectionReference collectionReference, String document, final Status status, final ProgressDialog dialog, Map<String, Object> data, final Context context)
    {
        // Add a new document with a generated ID
        collectionReference.document(document).set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            status.status("asesoría pública...");
                            dialog.dismiss();
                        }
                        else
                        {
                            dialog.dismiss();
                            status.status("Error del registro de los datos. Inténtelo de nuevo");

                        }
                    }
                });
    }

}
