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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.helpers.services.FirebaseAuthHelper;
import com.vicenteaguilera.integratec.helpers.utility.PropiertiesHelper;
import com.vicenteaguilera.integratec.helpers.utility.Status;

import static androidx.navigation.Navigation.findNavController;

public class SignInTwoFragment extends Fragment implements Status {

    private ImageButton imageButton;
    private Spinner spinner_Carreras;
    private CardView cardView_ButtonRegistrarse;
    private EditText editText_nombre,editText_apellidos;
    private  String email, password;
    private FirebaseAuthHelper firebaseAuthHelper = new FirebaseAuthHelper();


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

        spinner_Carreras = view.findViewById(R.id.spinner_carreras);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, PropiertiesHelper.CARRERAS);
        spinner_Carreras.setAdapter(arrayAdapter);

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

        cardView_ButtonRegistrarse = view.findViewById(R.id.cardView_ButtonRegistrarse);
        cardView_ButtonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                        "Registrándose...", true);
                dialog.show();

                String nombre= editText_nombre.getText().toString();
                String apellidos = editText_apellidos.getText().toString();

                //evaluaciones
                //nombre y apellidos no sean ""
                if(spinner_Carreras.getSelectedItemPosition()>0)
                {
                    String carrera = spinner_Carreras.getSelectedItem().toString();
                    firebaseAuthHelper.createUserEmailAndPassword(email,password,dialog,new String[]{nombre,apellidos,carrera});
                }
                else{
                    Snackbar.make(v,"Selecione una carrera",Snackbar.LENGTH_SHORT).show();
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
}