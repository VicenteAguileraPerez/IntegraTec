package com.vicenteaguilera.integratec.helpers.services;


import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;

public class FirebaseStorageHelper
{
   private FirebaseFirestoreAsesorHelper firebaseFirestoreAsesorHelper = new FirebaseFirestoreAsesorHelper();
   private FirebaseStorage mStorage = FirebaseStorage.getInstance();
   private StorageReference asesoresFiles = mStorage.getReference().child("asesores");

   private Status status;
   public void addImage(final String uid, Uri uri)
   {
      //Register observers to listen for when the download is done or if it fails
      asesoresFiles.child(uid).putFile(uri).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception exception)
         {
            status.status("Error de actualización de imagen, verifica tu conexión a Internet");
         }
      }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
         @Override
         public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            status.status("Imagen Actualizada");

            asesoresFiles.child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                  firebaseFirestoreAsesorHelper.updateImageAsesor(uri.toString(),status);
               }
            });

         }
      });

   }
   public void deleteImage(String uid)
   {
         // Delete the file
         asesoresFiles.child(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               status.status("Imagen Actualizada");
               firebaseFirestoreAsesorHelper.updateImageAsesor("",status);

            }
         }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               //status.status("Error de actualización de imagen, verifica tu conexión a Internet");
            }
         });
   }

   public void setStatusListener(Status status)
   {
       this.status=status;
   }

}
