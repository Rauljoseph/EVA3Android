package com.example.eva3.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eva3.R;
import com.example.eva3.model.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditarProductoActivity extends AppCompatActivity {

    private EditText etEditarNombre, etEditarDescripcion, etEditarPrecio, etEditarStock;
    private Button btnGuardarCambios;
    private DatabaseReference databaseReference;
    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        // Inicializar vistas
        etEditarNombre = findViewById(R.id.etEditarNombre);
        etEditarDescripcion = findViewById(R.id.etEditarDescripcion);
        etEditarPrecio = findViewById(R.id.etEditarPrecio);
        etEditarStock = findViewById(R.id.etEditarStock);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        // Obtener datos del producto seleccionado
        producto = (Producto) getIntent().getSerializableExtra("producto");
        if (producto != null) {
            etEditarNombre.setText(producto.getNombre());
            etEditarDescripcion.setText(producto.getDescripcion());
            etEditarPrecio.setText(String.valueOf(producto.getPrecio()));
            etEditarStock.setText(String.valueOf(producto.getStock()));
        }

        // Inicializar referencia de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Configurar botón de guardar cambios
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarCambios();
            }
        });
    }

    private void guardarCambios() {
        // Validaciones
        String nombre = etEditarNombre.getText().toString().trim();
        String descripcion = etEditarDescripcion.getText().toString().trim();
        String precioTexto = etEditarPrecio.getText().toString().trim();
        String stockTexto = etEditarStock.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            etEditarNombre.setError("El nombre no puede estar vacío");
            return;
        }

        if (TextUtils.isEmpty(descripcion)) {
            etEditarDescripcion.setError("La descripción no puede estar vacía");
            return;
        }

        double precio;
        int stock;

        try {
            precio = Double.parseDouble(precioTexto);
            if (precio < 0) {
                etEditarPrecio.setError("El precio no puede ser negativo");
                return;
            }
        } catch (NumberFormatException e) {
            etEditarPrecio.setError("El precio debe ser un número válido");
            return;
        }

        try {
            stock = Integer.parseInt(stockTexto);
            if (stock < 0) {
                etEditarStock.setError("El stock no puede ser negativo");
                return;
            }
        } catch (NumberFormatException e) {
            etEditarStock.setError("El stock debe ser un número válido");
            return;
        }

        // Actualizar el producto en Firebase
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);

        databaseReference.child(producto.getId()).setValue(producto).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditarProductoActivity.this, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad
            } else {
                Toast.makeText(EditarProductoActivity.this, "Error al actualizar producto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
