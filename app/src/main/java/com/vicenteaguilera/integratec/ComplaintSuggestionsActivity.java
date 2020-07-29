package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.snackbar.Snackbar;

public class ComplaintSuggestionsActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cardView_ButtonPublicar;
    private RadioButton radioButton_queja,radioButton_sugerencia;
    private RadioGroup radioGroup_topic;
    String topic="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_suggestions);
        radioGroup_topic = findViewById(R.id.radioGroup_topic);
        cardView_ButtonPublicar = findViewById(R.id.cardView_ButtonPublicar);
        cardView_ButtonPublicar.setOnClickListener(this);
        radioButtonListener();
    }
    private void radioButtonListener()
    {
        radioGroup_topic.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radiobutton) {

                RadioButton checkedRadioButton = radioGroup.findViewById(radiobutton);
                boolean isChecked = checkedRadioButton.isChecked();

                if(isChecked)
                {
                    if(checkedRadioButton.getId()==R.id.radioButton_queja)
                    {
                       topic= getResources().getString(R.string.queja).toLowerCase();

                    }else if(checkedRadioButton.getId()==R.id.radioButton_sugerencia)
                    {
                        topic= getResources().getString(R.string.sugerencia).toLowerCase();
                    }
                    else
                    {
                        topic="";
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.cardView_ButtonPublicar:
                if(!topic.equals(""))
                {
                    Snackbar.make(view, R.string.agradecimientos + topic, Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }
}