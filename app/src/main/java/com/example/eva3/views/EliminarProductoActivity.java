package com.example.eva3.views;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eva3.R;
import com.example.eva3.adapter.EliminarProductoAdapter;
import com.example.eva3.model.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class EliminarProductoActivity extends AppCompatActivity {

    private RecyclerView recyclerEliminarProductos;
    private EliminarProductoAdapter eliminarProductoAdapter;
    private List<Producto> listaProductos;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_producto);

        // Inicializar vistas
        recyclerEliminarProductos = findViewById(R.id.recyclerEliminarProductos);
        recyclerEliminarProductos.setLayoutManager(new LinearLayoutManager(this));
        listaProductos = new ArrayList<>();
        eliminarProductoAdapter = new EliminarProductoAdapter(listaProductos);
        recyclerEliminarProductos.setAdapter(eliminarProductoAdapter);

        // Conexión con Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Obtener productos de Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaProductos.clear(); // Limpiar lista para evitar duplicados
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Producto producto = dataSnapshot.getValue(Producto.class);
                    if (producto != null) {
                        listaProductos.add(producto);
                    }
                }
                eliminarProductoAdapter.notifyDataSetChanged(); // Actualizar lista
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EliminarProductoActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
