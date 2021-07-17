package com.vicenteaguilera.integratec.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vicenteaguilera.integratec.R;
import com.vicenteaguilera.integratec.helpers.services.FirestoreHelper;
import com.vicenteaguilera.integratec.models.RealtimeAsesoria;

import java.util.List;

public class AsesoriaRealtimeAdapter extends BaseAdapter
{
    //ArrayAdapter x = new ArrayAdapter(context,listitem,[]);
    private LayoutInflater layoutInflater;
    private Context context;
    private int idLayout;
    private List<RealtimeAsesoria> asesorias;
    public List<RealtimeAsesoria> getAsesorias()
    {
        return asesorias;
    }

    public void setAsesorias(List<RealtimeAsesoria> asesorias)
    {
        this.asesorias = asesorias;
        notifyDataSetChanged();
    }

    public AsesoriaRealtimeAdapter(Context context, int idLayout, List<RealtimeAsesoria> asesorias) {
        this.context = context;
        this.idLayout = idLayout;
        this.asesorias = asesorias;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /// indicare cuantos elementos va a mostras
    @Override
    public int getCount() {
        return asesorias.size();
    }
    //retornar Objetos de tipo Telefono (Modelo)
    @Override
    public Object getItem(int position) {
        return asesorias.get(position);
    }
    //retorna la posicion del elemento de la lista
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
       final RealtimeAsesoria realtimeAsesoria = asesorias.get(position);
       AsesoriaRealtimeHolder asesoriaRealtimeHolder = null;
       if(contentView==null)
       {

           //no poner el root en el inflate truena el sistema
           contentView = layoutInflater.inflate(idLayout,null);
           asesoriaRealtimeHolder = new AsesoriaRealtimeHolder();
           asesoriaRealtimeHolder.image = contentView.findViewById(R.id.imageView_perfil);
           asesoriaRealtimeHolder.textView_nombre = contentView.findViewById(R.id.textView_Nombre_Asesor);
           asesoriaRealtimeHolder.textView_Lugar_Asesoria = contentView.findViewById(R.id.textView_Lugar_Asesoria);
           asesoriaRealtimeHolder.textView_Horario = contentView.findViewById(R.id.textView_Horario);
           asesoriaRealtimeHolder.textView_Materia = contentView.findViewById(R.id.textView_Materia);
           asesoriaRealtimeHolder.textView_Informacion_Extra = contentView.findViewById(R.id.textView_Informacion_Extra);
           asesoriaRealtimeHolder.imageButton_copy_asesoria = contentView.findViewById(R.id.imageButton_copy_asesoria);
           contentView.setTag(asesoriaRealtimeHolder);

           Bitmap placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.user);
           RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), placeholder);
           circularBitmapDrawable.setCircular(true);
           Glide.with(context)
                   .load(realtimeAsesoria.getImage_asesor())
                   .placeholder(circularBitmapDrawable)
                   .fitCenter()
                   .centerCrop()
                   .apply(RequestOptions.circleCropTransform())
                   .into( asesoriaRealtimeHolder.image);
           asesoriaRealtimeHolder.textView_Informacion_Extra.setSelected(true);
           asesoriaRealtimeHolder.textView_Lugar_Asesoria.setSelected(true);
           asesoriaRealtimeHolder.textView_nombre.setSelected(true);
           asesoriaRealtimeHolder.textView_nombre.setText(realtimeAsesoria.getNombre());
           asesoriaRealtimeHolder.textView_Lugar_Asesoria.setText(realtimeAsesoria.getURL().equals("")?"Lugar de asesoria: "+realtimeAsesoria.getLugar():"Lugar de asesoria: "+realtimeAsesoria.getURL());
           asesoriaRealtimeHolder.textView_Horario.setText("Horario: "+realtimeAsesoria.getHora_inicio()+" a "+realtimeAsesoria.getHora_fin());
           asesoriaRealtimeHolder.textView_Materia.setText("Materia: "+realtimeAsesoria.getMateria());
           String informacion = realtimeAsesoria.getInformacion();
           asesoriaRealtimeHolder.textView_Informacion_Extra.setText(informacion.equals("")?"No hay información extra proporcionada por el asesor":"Información extra: "+informacion);

          if (!realtimeAsesoria.getURL().equals("")){
              asesoriaRealtimeHolder.imageButton_copy_asesoria.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                      ClipData clip = ClipData.newPlainText("URL de Asesoría",  realtimeAsesoria.getURL());
                      clipboard.setPrimaryClip(clip);

                      Toast.makeText(context, "URL Copiado", Toast.LENGTH_SHORT).show();
                  }
              });
          }else {
              asesoriaRealtimeHolder.imageButton_copy_asesoria.setVisibility(View.INVISIBLE);
          }
       }
       else {
           contentView.getTag();
       }
        return contentView;
    }
    static class AsesoriaRealtimeHolder
    {
        public TextView textView_nombre,textView_Lugar_Asesoria,textView_Horario,textView_Materia,textView_Informacion_Extra;
        public ImageView image;
        public ImageButton imageButton_copy_asesoria;
    }


}

