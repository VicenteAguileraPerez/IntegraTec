package com.vicenteaguilera.integratec.controllers.fragments;

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
import com.vicenteaguilera.integratec.helpers.utility.Status;

import static androidx.navigation.Navigation.findNavController;


public class LoginFragment extends Fragment  implements Status{

    private CardView cardView_registrarse;
    private CardView cardView_ButtonIniciarSesion;
    private EditText editText_email,editText_password;
    private FirebaseAuthHelper firebaseAuthHelper = new FirebaseAuthHelper();


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
        cardView_registrarse = view.findViewById(R.id.cardView_ButtonRegistrarse);

        cardView_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(requireView()).navigate(R.id.action_loginFragment_to_signInFragment);
            }
        });
        editText_email= view.findViewById(R.id.editText_nombre);
        editText_password = view.findViewById(R.id.editText_password);
        cardView_ButtonIniciarSesion = view.findViewById(R.id.cardView_ButtonIniciarSesion);
        cardView_ButtonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //login evaluacion que email sea email y que pass != ""
                String email = editText_email.getText().toString();
                String password  = editText_password.getText().toString();
                ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                        "Ingresando...", true);
                firebaseAuthHelper.signInWithEmailAndPassword(email, password, dialog);


            }
        });
    }
    @Override
    public void status(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}