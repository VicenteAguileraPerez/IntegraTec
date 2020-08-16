package com.vicenteaguilera.integratec.helpers.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vicenteaguilera.integratec.controllers.mainapp.MainAppActivity;
import com.vicenteaguilera.integratec.helpers.utility.Status;
import com.vicenteaguilera.integratec.helpers.utility.StringHelper;

import java.util.Objects;

public class FirebaseAuthHelper
{
    // Initialize Firebase Auth
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Status status;
    private Context context;
    private final StringHelper stringHelper = new StringHelper();
    private final FirestoreHelper firestoreHelper = new FirestoreHelper();
    public void setContext(Context context)
    {
        this.context=context;
    }
    public static FirebaseUser getCurrentUser()
    {
        return mAuth.getCurrentUser();
    }



    //registrarse
    public void createUserEmailAndPassword(final String email, final String password, final ProgressDialog dialog, final String[]args)
    {

        if(stringHelper.isNotEmptyCredentials(email,password))
        {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();

                                //llamar a la creacion del usuario
                                //createUser(user,password, args);
                                firestoreHelper.addData(status,dialog, context, user.getUid(),email,password,args);

                            }
                            else
                            {
                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                if(Objects.equals(error, "The email address is already in use by another account."))
                                {
                                    status.status("Error en el registro, email registrado");
                                }
                                else if(Objects.equals(error, "The given password is invalid. [ Password should be at least 6 characters ]"))
                                {
                                    status.status("La contraseña debe de tener por lo menos 6 caracteres");
                                }
                                else
                                {
                                    status.status("Error al registrarse");
                                }
                                //createUser(null,"",null);
                                dialog.dismiss();
                            }
                        }
                    });

        }
        else
        {
            dialog.dismiss();
            status.status("Error en el email o en el password");
        }

    }
    //ingresar
    public void signInWithEmailAndPassword(final String email, String password, final ProgressDialog dialog)
    {
        if(stringHelper.isNotEmptyCredentials(email,password))
        {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                firestoreHelper.getData(Objects.requireNonNull(user).getUid(),dialog,status,context);
                            }
                            else
                            {
                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                switch (Objects.requireNonNull(error))
                                {
                                    case "There is no user record corresponding to this identifier. The user may have been deleted.":
                                        status.status("Esas credenciales no existen en la base de datos o el email es inválido");
                                        break;
                                    case "The password is invalid or the user does not have a password.":
                                        status.status("La contraseña es incorrecta");
                                        break;
                                    case "The user account has been disabled by an administrator.":
                                        status.status("Cuenta inhabilidada, contacta al administrador");
                                        break;
                                    default:
                                        status.status("Verifica tu conexión a Internet");
                                        break;
                                }
                                Log.e("error",error);
                                dialog.dismiss();
                            }
                        }
                    });
        }
        else
        {
            status.status("Error en el email o en el password");
            dialog.dismiss();
        }
    }
    ///salir
    public  void signout(final ProgressDialog dialog)
    {
        mAuth.signOut();
        FirestoreHelper.asesor=null;
        Log.e("er",mAuth.getCurrentUser()+"");
        dialog.dismiss();
        Intent intent = new Intent(context, MainAppActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }


    public void setOnStatusListener(Status status)
    {
        this.status=status;
    }

}
