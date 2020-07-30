package com.vicenteaguilera.integratec.helpers.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;
import com.vicenteaguilera.integratec.controllers.MainAdviserActivityApp;
import com.vicenteaguilera.integratec.helpers.utility.Status;
import com.vicenteaguilera.integratec.models.Asesor;
import com.vicenteaguilera.integratec.models.User;

import java.util.HashMap;
import java.util.Map;

public class FirestoreHelper
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference UserCollection = db.collection("asesores");
    private final CollectionReference AsesoriaPublicaCollection = db.collection("asesoria_publica");
    private Status status;
    private ProgressDialog dialog;
    void addData(Status status, ProgressDialog dialog, Context context, String[] param)
   {

       if(param.length==3)
       {
           //asesor
           Map<String, Object> asesor = new HashMap<>();
           asesor.put("nombre", param[0]);
           asesor.put("apellido", param[1]);
           asesor.put("carrera", param[2]);
           asesor.put("email", FirebaseAuthHelper.getUser().getEmail());
           asesor.put("password", FirebaseAuthHelper.getUser().getPassword());
           asesor.put("uri_image", "");
           Log.e("lista",  asesor.values().toArray().length+"");
           registerDataUserToFirestore(UserCollection,FirebaseAuthHelper.getUser().getUid(),status,dialog, asesor,context);

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
                               status.status("Ingresando al sistema...");
                               dialog.dismiss();
                               User user = new Asesor(document,data.get("nombre").toString(),data.get("apellido").toString(),data.get("email").toString(),data.get("password").toString(),data.get("carrera").toString(),data.get("uri_image").toString());
                               FirebaseAuthHelper.setUser(user);
                               Intent intent = new Intent(context, MainAdviserActivityApp.class);
                               context.startActivity(intent);
                           }
                           else
                           {
                               dialog.dismiss();
                               status.status("Error del registro de los datos. Inténtelo de nuevo");
                               FirebaseAuthHelper.getCurrentUser().delete();
                               FirebaseAuthHelper.setUser(null);
                           }
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
