package com.vicenteaguilera.integratec.controllers.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.vicenteaguilera.integratec.R;

import static androidx.navigation.Navigation.createNavigateOnClickListener;
import static androidx.navigation.Navigation.findNavController;

public class SignInFragment extends Fragment  {

    private CardView cardView_ButtonSiguiente;
    private ImageButton imageButton;
    private EditText editText_email,editText_password,editText_password_confirm,editText_codigo;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_in, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardView_ButtonSiguiente = view.findViewById(R.id.cardView_ButtonSiguiente);
        editText_email= view.findViewById(R.id.editText_nombre);
        editText_password = view.findViewById(R.id.editText_apellidos);
        editText_password_confirm = view.findViewById(R.id.editText_password_confirm);
        editText_codigo = view.findViewById(R.id.editText_codigo);


        cardView_ButtonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String email = editText_email.getText().toString();
                String password  = editText_password.getText().toString();
                String password_confirm = editText_password_confirm.getText().toString();
                String codigo = editText_codigo.getText().toString();

                ///evaluaciones
                //email sea un email
                //evaluar que email no sea vacío
                //evaluar que password no sea vacio
                //evaluar que pass>6 caracteres
                //password == al campo pass_confirm
                //codigo de CB = constante de String helper
                //Snackbar.make(requireView(),message,Snackbar.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("password",password);

                findNavController(requireView()).navigate(R.id.action_signInFragment_to_singInTwoFragment,bundle);

            }
        });
        imageButton = view.findViewById(R.id.imageButton_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SignInFragment.super.getActivity().onBackPressed();
            }
        });
    }




}