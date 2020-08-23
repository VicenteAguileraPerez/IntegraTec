package com.vicenteaguilera.integratec.controllers.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseQueryHelper;
import com.vicenteaguilera.integratec.helpers.utility.Status;
import com.vicenteaguilera.integratec.helpers.utility.StringHelper;

import static androidx.navigation.Navigation.findNavController;


public class LoginFragment extends Fragment  implements Status{

    private CardView cardView_ButtonRegistrarse;
    private CardView cardView_ButtonIniciarSesion;
    //private CardView cardView_ButtonOlvidastePass;
    private EditText editText_email,editText_password;
    private FirebaseAuthHelper firebaseAuthHelper = new FirebaseAuthHelper();
    private StringHelper stringHelper = new StringHelper();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Status status = this;
        firebaseAuthHelper.setOnStatusListener(status);
        firebaseAuthHelper.setContext(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText_email= view.findViewById(R.id.editText_nombre);
        editText_password = view.findViewById(R.id.editText_apellidos);

        cardView_ButtonRegistrarse = view.findViewById(R.id.cardView_ButtonRegistrarse);
        cardView_ButtonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(requireView()).navigate(R.id.action_loginFragment_to_signInFragment);
            }
        });

        cardView_ButtonIniciarSesion = view.findViewById(R.id.cardView_ButtonIniciarSesion);
        cardView_ButtonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login evaluacion que email sea email y que pass != ""
                String email = editText_email.getText().toString();
                String password  = editText_password.getText().toString();

                switch (stringHelper.loginHelper(email,password)){
                    case 1:
                        ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                                "Ingresando...", true);
                        firebaseAuthHelper.signInWithEmailAndPassword(email, password, dialog);
                        break;

                    case 2:
                        editText_email.setError("Correo electrónico requerido");
                        break;

                    case 3:
                        editText_email.setError("Correo electrónico invalido");
                        break;

                    case 4:
                        editText_password.setError("Contraseña requerida");
                        break;

                    case 5:
                        editText_password.setError("Contraseña requerida");
                        editText_email.setError("Correo electrónico requerido");
                        break;
                }
            }
        });

        view.findViewById(R.id.cardView_OlvidastePass)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialogRecoverPass();
                    }
                });

    }

    private void showDialogRecoverPass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_recover_pass, null);
        builder.setView(view)
                .setTitle("Recuperar contraseña")
        .setMessage("Para recuperar tu contraseña ingresa el correo electrónico con el que te registraste y da clic en enviar.");

        final AlertDialog dialogRecoverPass =builder.create();
        dialogRecoverPass.setCancelable(false);
        dialogRecoverPass.show();

        final EditText editText_Email = dialogRecoverPass.findViewById(R.id.textView_email);
        CardView cardView_ButtonSend = dialogRecoverPass.findViewById(R.id.cardView_ButtonSend);
        CardView cardView_ButtonCancel = dialogRecoverPass.findViewById(R.id.cardView_ButtonCancel);

        cardView_ButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag_Email=false;
                if(!editText_Email.getText().toString().isEmpty()) {
                    if(new StringHelper().isEmail(editText_Email.getText().toString())) {
                        flag_Email=true;
                    }
                    else {
                        editText_Email.setError("Correo electrónico inválido");
                    }
                }
                else {
                    editText_Email.setError("Correo electrónico requerido");
                }

                if(flag_Email)
                {
                    new FirebaseQueryHelper().BuscarCredenciales(editText_Email.getText().toString(),getActivity());
                }
            }
        });

        cardView_ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRecoverPass.dismiss();
            }
        });
    }

    @Override
    public void status(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}