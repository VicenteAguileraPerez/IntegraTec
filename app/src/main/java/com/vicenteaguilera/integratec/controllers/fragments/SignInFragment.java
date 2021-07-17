package com.vicenteaguilera.integratec.controllers.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ButtonHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.StaticHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.StringHelper;

import static androidx.navigation.Navigation.createNavigateOnClickListener;
import static androidx.navigation.Navigation.findNavController;

public class SignInFragment extends Fragment  {

    private MaterialButton button_siguiente;
    private ImageButton imageButton;
    private TextInputLayout editText_email,editText_password,editText_password_confirm,editText_codigo;
    private StringHelper stringHelper = new StringHelper();
    private ButtonHelper buttonHelper = new ButtonHelper();

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
        button_siguiente = view.findViewById(R.id.button_siguiente);
        editText_email= view.findViewById(R.id.editText_correo);
        editText_password = view.findViewById(R.id.editText_password);
        editText_password_confirm = view.findViewById(R.id.editText_password_confirm);
        editText_codigo = view.findViewById(R.id.editText_codigo);


        button_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSetErrors();
                String email = editText_email.getEditText().getText().toString();
                String password  = editText_password.getEditText().getText().toString();
                String password_confirm = editText_password_confirm.getEditText().getText().toString();
                String codigo = editText_codigo.getEditText().getText().toString();

                boolean flag_email = false;
                boolean flag_password = false;
                boolean flag_codigo = false;

                ///evaluaciones
                //email sea un email
                //evaluar que email no sea vacío
                //evaluar que password no sea vacio
                //evaluar que pass>6 caracteres
                //password == al campo pass_confirm
                //codigo de CB = constante de String helper
                //Snackbar.make(requireView(),message,Snackbar.LENGTH_SHORT).show();

                if(!email.isEmpty() && stringHelper.isEmail(email)){
                    flag_email = true;
                }else {
                    if(email.isEmpty()){
                        editText_email.setError("Correo electrónico requerido");
                    }else if(!stringHelper.isEmail(email)){
                        editText_email.setError("Correo electrónico invalido");
                    }
                }

                if(!password.isEmpty() && password.length()>=6 && !password_confirm.isEmpty() && password_confirm.length()>=6){
                    if(password.equals(password_confirm)) {
                        flag_password=true;
                    }
                    else {
                        editText_password.setError("Las contraseñas no son iguales.");
                        editText_password_confirm.setError("Las contraseñas no son iguales.");
                    }
                }
                else {
                    if(password.isEmpty()) {
                        editText_password.setError("Contraseña requerida");
                    }else if(password.length()<6) {
                        editText_password.setError("La contraseña debe tener mínimo 6 carácteres");
                    }

                    if(password_confirm.isEmpty()) {
                        editText_password_confirm.setError("Contraseña requerida");
                    }else if(password_confirm.length()<6) {
                        editText_password_confirm.setError("La contraseña debe tener mínimo 6 carácteres");
                    }
                }

                if(!codigo.isEmpty() && codigo.equals(StaticHelper.CODIGO_CB)){
                    flag_codigo = true;
                }else {
                    if(codigo.isEmpty()){
                        editText_codigo.setError("Código de ciencias básicas requerido");
                    }else if(!codigo.equals(StaticHelper.CODIGO_CB)){
                        editText_codigo.setError("Código de ciencias básicas incorrecto");
                    }
                }

                Bundle bundle = new Bundle();

                if(flag_email && flag_codigo && flag_password){
                    bundle.putString("email",email);
                    bundle.putString("password",password);

                    findNavController(requireView()).navigate(R.id.action_signInFragment_to_singInTwoFragment,bundle);
                }else {
                    Snackbar.make(view, "Información invalida, favor de revisar",Snackbar.LENGTH_LONG).show();
                }

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

    private void hideSetErrors(){
        editText_email.setError(null);
        editText_codigo.setError(null);
        editText_password.setError(null);
        editText_password_confirm.setError(null);
    }


}