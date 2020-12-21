package pe.pucp.tel306.firebox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListarArchivosAdapter extends RecyclerView.Adapter<ListarArchivosAdapter.ArchivoViewHolder> {
    private ArrayList<Archivo> listaArchivos;
    private Context context;

    public ListarArchivosAdapter(ArrayList<Archivo> listaArchivos, Context context) {
        this.listaArchivos = listaArchivos;
        this.context = context;
    }

    @NonNull
    @Override
    public ArchivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.item_rv, parent, false);
        ArchivoViewHolder archivoViewHolder = new ArchivoViewHolder(itemview);
        return archivoViewHolder;
    }

    @Override
    public void onBindViewHolder(ArchivoViewHolder holder, int position) {
        Archivo archivo = listaArchivos.get(position);
        String data = "Nombre: "+archivo.getNombre() +"\r\n"
                        +"Bytes: " + archivo.getSizeEnBytes();
        holder.textView.setText(data);
    }

    @Override
    public int getItemCount() {
        return listaArchivos.size();
    }

    public static class ArchivoViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ArchivoViewHolder(View itemview) {
            super(itemview);
            this.textView = itemview.findViewById(R.id.textView2);
        }
    }
}
