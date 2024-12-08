package com.example.dos_login;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private Cursor cursor;
    private OnProductSelectedListener listener;

    // Interfaz para manejar la selección de productos
    public interface OnProductSelectedListener {
        void onProductSelected(String nombreProducto, double precioProducto);
    }

    public ProductAdapter(Context context, Cursor cursor, OnProductSelectedListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            double precio = cursor.getDouble(cursor.getColumnIndex("precio"));

            holder.textViewNombre.setText(nombre);
            holder.textViewPrecio.setText("S/" + precio);

            holder.btnSeleccionar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductSelected(nombre, precio);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombre, textViewPrecio;
        public Button btnSeleccionar;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.nombreProducto);
            textViewPrecio = itemView.findViewById(R.id.precioProducto);
            btnSeleccionar = itemView.findViewById(R.id.btnSeleccionar);
        }
    }
}
