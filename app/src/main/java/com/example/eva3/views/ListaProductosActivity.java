package com.example.eva3.views;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eva3.adapter.ProductoAdapter;
import com.example.eva3.model.Producto;
import com.example.eva3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ListaProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerProductos;
    private ProductoAdapter productoAdapter;
    private List<Producto> listaProductos;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        // Inicializar vistas
        recyclerProductos = findViewById(R.id.recyclerProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));
        listaProductos = new ArrayList<>();
        productoAdapter = new ProductoAdapter(listaProductos);
        recyclerProductos.setAdapter(productoAdapter);

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
                productoAdapter.notifyDataSetChanged(); // Actualizar lista
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListaProductosActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
