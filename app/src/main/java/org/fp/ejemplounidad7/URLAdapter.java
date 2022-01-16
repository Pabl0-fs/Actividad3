package org.fp.ejemplounidad7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class URLAdapter extends RecyclerView.Adapter<URLAdapter.Holder>{

    private final LinkedList<URLdescarga> datos = new LinkedList<>();

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.rc, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.add(datos.get(position));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void add(URLdescarga url) {
        datos.add(url);
        notifyItemInserted(datos.size() - 1);
    }

    public void sustituir(URLdescarga url){
        for(URLdescarga dato :datos){
            if(dato.getUrl().equalsIgnoreCase(url.getUrl())){
                dato.setEstado(url.getEstado());
            }
        }
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView imagen;
        ProgressBar progreso;
        public Holder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textView);
            imagen = itemView.findViewById(R.id.imageView);
            progreso = itemView.findViewById(R.id.progressBar);
        }

        public void add(URLdescarga url) {
            text.setText(url.getUrl());
            System.out.println("URL: " + url);
            progreso.setVisibility(View.VISIBLE);
            if(url.getEstado() == 1) todoBien();
            if(url.getEstado() == -1) todoMal();
        }

        public void todoBien(){
            progreso.setVisibility(View.GONE);
            imagen.setVisibility(View.VISIBLE);
            imagen.setImageResource(R.drawable.ic_ok);
        }

        public void todoMal(){
            progreso.setVisibility(View.GONE);
            imagen.setVisibility(View.VISIBLE);
            imagen.setImageResource(R.drawable.ic_error);
        }

    }
    public static class URLdescarga {

        private String url;
        private int estado;

        public URLdescarga(String url, int estado) {
            this.url = url;
            this.estado = estado;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getEstado() {
            return estado;
        }

        public void setEstado(int estado) {
            this.estado = estado;
        }
    }
}
