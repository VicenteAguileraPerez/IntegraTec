package com.vicenteaguilera.integratec.helpers.services;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.ListaAsesores;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;

import java.util.HashMap;
import java.util.Map;

public class FirebaseAsesorHelper
{
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference AsesoresCollection = db.collection("asesores");

    public void getAllAsesores(final Status status, final ProgressDialog dialog, final Context context, ListaAsesores listaAsesores)
    {

        AsesoresCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isComplete())
                        {
                            String nombre;
                            Map<String, String> asesores = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                nombre = document.getData().get("nombre").toString()+" "+document.getData().get("apellido").toString();
                                asesores.put(document.getId(),nombre);
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            listaAsesores.getAsesoresAll(asesores);
                            status.status("Contactos obtenidos");
                        }
                        else {
                           status.status("Error al obtener los asesores");
                        }
                        dialog.dismiss();
                    }
                });
    }
}
