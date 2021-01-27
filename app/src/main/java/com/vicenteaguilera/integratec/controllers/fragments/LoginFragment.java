package com.vicenteaguilera.integratec.controllers.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseQueryHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ButtonHelper;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.helpers.utility.helpers.StringHelper;

import static androidx.navigation.Navigation.findNavController;


public class LoginFragment extends Fragment  implements Status{

    private MaterialButton button_Registrarse;
    private MaterialButton button_IniciarSesion;
    private CardView cardView_OlvidastePass;
    private TextInputLayout editText_email,editText_password;
    private FirebaseAuthHelper firebaseAuthHelper = new FirebaseAuthHelper();
    private StringHelper stringHelper = new StringHelper();
    private ButtonHelper buttonHelper = new ButtonHelper();

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

        editText_email= view.findViewById(R.id.editText_correo);
        editText_password = view.findViewById(R.id.editText_password);
        button_Registrarse = view.findViewById(R.id.button_registrarse);
        button_IniciarSesion = view.findViewById(R.id.button_iniciarsesion);
        cardView_OlvidastePass = view.findViewById(R.id.cardView_OlvidastePass);

        button_Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(requireView()).navigate(R.id.action_loginFragment_to_signInFragment);
            }
        });

        button_IniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login evaluacion que email sea email y que pass != ""
                String email = editText_email.getEditText().getText().toString();
                String password  = editText_password.getEditText().getText().toString();

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

        cardView_OlvidastePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRecoverPass();
            }
        });
        textWacher();

       buttonHelper.actionClickButton(cardView_OlvidastePass, getResources().getColor(R.color.gray), getResources().getColor(R.color.white));
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
        CardView cardView_ButtonSend = dialogRecoverPass.findViewById(R.id.cardView_Button_Send);
        CardView cardView_ButtonCancel = dialogRecoverPass.findViewById(R.id.cardView_ButtonCancel);

        buttonHelper.actionClickButton(cardView_ButtonSend, getResources().getColor(R.color.background_green), getResources().getColor(R.color.background_green_black));
        buttonHelper.actionClickButton(cardView_ButtonCancel, getResources().getColor(R.color.background_green), getResources().getColor(R.color.background_green_black));

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
                    dialogRecoverPass.dismiss();
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

    private void textWacher()
    {
        editText_email.getEditText().addTextChangedListener(
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {
                    if(charSequence.length()==0)
                    {
                        editText_email.setError("Campo vacío");
                    }
                    else if(!stringHelper.isEmail(charSequence.toString()))
                    {
                        editText_email.setError("Correo electrónico invalido");
                    }
                    else
                    {
                        editText_email.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            }
        );
        editText_password.getEditText().addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                    {
                        if(charSequence.length()==0)
                        {
                            editText_password.setError("Campo vacío");
                        }
                        else
                        {
                            editText_password.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );
    }
}