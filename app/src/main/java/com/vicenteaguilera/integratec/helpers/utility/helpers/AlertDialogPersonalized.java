package com.vicenteaguilera.integratec.helpers.utility.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogPersonalized
{
    public void alertDialogInformacion(String message, Context context)
    {
        final AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Aviso");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface alertDialog, int i)
                    {
                        alertDialog.cancel();
                    }
        }
        );

        alertDialogBuilder.show();
    }
}
