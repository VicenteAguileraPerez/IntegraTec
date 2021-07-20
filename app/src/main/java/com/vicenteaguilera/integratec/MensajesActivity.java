package com.vicenteaguilera.integratec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vicenteaguilera.integratec.controllers.BandejaActivity;
import com.vicenteaguilera.integratec.controllers.MainAdviserActivityApp;
import com.vicenteaguilera.integratec.helpers.services.FirebaseFirestoreAsesorHelper;
import com.vicenteaguilera.integratec.helpers.services.FirebaseFirestoreMensajesHelper;
import com.vicenteaguilera.integratec.helpers.utility.interfaces.Status;
import com.vicenteaguilera.integratec.models.Mensaje;

import java.util.ArrayList;

public class MensajesActivity extends AppCompatActivity implements Status {

    private FirebaseFirestoreMensajesHelper firebaseFirestoreMensajesHelper = new FirebaseFirestoreMensajesHelper();
    ListView listView;
    public static  int pos=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        setTitle("Mis mensajes");
        listView = findViewById(R.id.lista);
        ArrayList<String> mensaje = new ArrayList<>();
        for (int i = 0; i < BandejaActivity.myList.size(); i++) {
            mensaje.add(BandejaActivity.myList.get(i).getFrom());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mensaje );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent nf = new Intent(MensajesActivity.this, BandejaActivity.class);
                pos=position;
                startActivity(nf);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id)
            {
                // TODO Auto-generated method stub

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MensajesActivity.this);
                alertDialogBuilder.setTitle("Eliminar");
                alertDialogBuilder.setMessage("Desea eliminar el mensaje enviado por " + BandejaActivity.myList.get(pos).getFrom());
                alertDialogBuilder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface alertDialog, int i)
                            {
                                MensajesActivity.pos=pos;
                                firebaseFirestoreMensajesHelper.eliminarMensaje( BandejaActivity.myList.get(pos).getId(),MensajesActivity.this,MensajesActivity.this);
                            }
                        });
                alertDialogBuilder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface alertDialog, int i) {

                        alertDialog.cancel();
                    }
                });
                alertDialogBuilder.show();
                return true;
            }

        });

    }

    @Override
    public void status(String message)
    {
        Toast.makeText(MensajesActivity.this,message,Toast.LENGTH_LONG).show();
        if(message.equals("Eliminación exitosa"))
        {
            BandejaActivity.myList.remove(pos);
            pos=-1;
            onBackPressed();
        }

    }
}