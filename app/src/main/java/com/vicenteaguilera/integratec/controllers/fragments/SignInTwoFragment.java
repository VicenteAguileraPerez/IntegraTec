package com.vicenteaguilera.integratec.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.controllers.MainAdviserActivityApp;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInTwoFragment extends Fragment  {

    private ImageButton imageButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String [] CARRERAS ={"Seleccione una carrera","Ingeniería en Sistemas Computacionales", "Ingeniería en Administración", "Ingeniería en Mecatrónica", "Ingeniería Industrial", "Ingeniería en Mecánica", "Ingeniería en Industrias Alimentarias", "Ingeniería Civil"};
    private Spinner spinner_Carreras;
    private CardView cardView_ButtonRegistrarse;

    public SignInTwoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SingInTwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInTwoFragment newInstance(String param1, String param2) {
        SignInTwoFragment fragment = new SignInTwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_two, container, false);

        spinner_Carreras = view.findViewById(R.id.spinner_Carreras);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, CARRERAS);
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

        cardView_ButtonRegistrarse = view.findViewById(R.id.cardView_ButtonRegistrarse);
        cardView_ButtonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainAdviserActivityApp.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}