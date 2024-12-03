package com.example.eva3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eva3.R;
import com.example.eva3.model.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class EliminarProductoAdapter extends RecyclerView.Adapter<EliminarProductoAdapter.ProductoViewHolder> {

    private List<Producto> listaProductos;

    public EliminarProductoAdapter(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_eliminar_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.tvNombreProductoEliminar.setText(producto.getNombre());

        holder.btnEliminarProducto.setOnClickListener(view -> {
            DatabaseReference productoRef = FirebaseDatabase.getInstance()
                    .getReference("productos")
                    .child(producto.getId());
            productoRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listaProductos.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(view.getContext(), "Producto eliminado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Error al eliminar producto", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProductoEliminar;
        Button btnEliminarProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProductoEliminar = itemView.findViewById(R.id.tvNombreProductoEliminar);
            btnEliminarProducto = itemView.findViewById(R.id.btnEliminarProducto);
        }
    }
}
