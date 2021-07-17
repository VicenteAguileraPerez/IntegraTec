package com.vicenteaguilera.integratec.controllers.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.ButtonHelper;
import com.vicenteaguilera.integratec.helpers.utility.helpers.StaticHelper;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;

import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;

public class SignInTwoFragment extends Fragment implements Status {

    private ImageButton imageButton;

    private MaterialButton button_registrarse;
    private TextInputLayout editText_nombre,editText_apellidos,spinner_Carrera;
    private  String email, password;
    private FirebaseAuthHelper firebaseAuthHelper = new FirebaseAuthHelper();
    private ButtonHelper buttonHelper = new ButtonHelper();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Status status = this;
        firebaseAuthHelper.setOnStatusListener(status);
        firebaseAuthHelper.setContext(getContext());
        Bundle bundle = getArguments();
        email = bundle.getString("email");
        password = bundle.getString("password");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_two, container, false);

        spinner_Carrera = view.findViewById(R.id.spinner_carrera);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(view.getContext(), R.layout.custom_spinner_item, StaticHelper.CARRERAS);
        ((AutoCompleteTextView)spinner_Carrera.getEditText()).setAdapter(arrayAdapter);

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageButton = view.findViewById(R.id.imageButton_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SignInTwoFragment.super.getActivity().onBackPressed();

            }
        });
        editText_nombre = view.findViewById(R.id.editText_nombre);
        editText_apellidos = view.findViewById(R.id.editText_apellidos);

        button_registrarse = view.findViewById(R.id.button_registrarse);
        button_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSetErrors();
                ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                        "Registrándose...", true);
                boolean flag_nombre = false;
                boolean flag_apellidos = false;
                boolean flag_spinner = false;
                String nombre= Objects.requireNonNull(editText_nombre.getEditText()).getText().toString();
                String apellidos = Objects.requireNonNull(editText_apellidos.getEditText()).getText().toString();

                //evaluaciones
                //nombre y apellidos no sean ""

                if(!nombre.isEmpty()){
                    flag_nombre = true;
                }else {
                    editText_nombre.setError("Nombre requerido");
                    Snackbar.make(v,"Nombre requerido",Snackbar.LENGTH_LONG).show();

                }

                if(!apellidos.isEmpty()){
                    flag_apellidos = true;
                }else {
                    editText_apellidos.setError("Apellidos requeridos");
                    Snackbar.make(v,"Apellidos requeridos",Snackbar.LENGTH_LONG).show();
                }

                if(Objects.requireNonNull(spinner_Carrera.getEditText()).getText().length()>0){
                    flag_spinner = true;
                }else {
                    //Toast.makeText(getContext(), "Seleccione una carrera", Toast.LENGTH_SHORT).show();
                    spinner_Carrera.setError("Selecione una carrera");
                    Snackbar.make(v,"Selecione una carrera",Snackbar.LENGTH_LONG).show();
                }

                if(flag_apellidos && flag_nombre && flag_spinner){
                    dialog.show();
                    String carrera = spinner_Carrera.getEditText().getText().toString();
                    firebaseAuthHelper.createUserEmailAndPassword(email,password,dialog,new String[]{nombre,apellidos,carrera});
                    editText_apellidos.setError(null);
                    editText_apellidos.setError(null);
                    spinner_Carrera.setError(null);
                }else {
                    Snackbar.make(v, "Información invalida, favor de revisar",Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void status(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }


    private void hideSetErrors(){
        editText_apellidos.setError(null);
        editText_nombre.setError(null);
    }
    private void textWachers()
    {
        Objects.requireNonNull(editText_nombre.getEditText()).addTextChangedListener(
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {
                    if(charSequence.length()==0)
                    {
                        editText_nombre.setError("Campo vacío");
                    }
                    else
                    {
                        editText_nombre.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            }
        );
        Objects.requireNonNull(editText_apellidos.getEditText()).addTextChangedListener(
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {
                    if(charSequence.length()==0)
                    {
                        editText_apellidos.setError("Campo vacío");
                    }
                    else
                    {
                        editText_apellidos.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            }
        );

    }
}