package com.example.eva3.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eva3.model.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.eva3.R;

public class AgregarProductoActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etPrecio, etStock;
    private Button btnGuardar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Inicializar referencia de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Configurar botón de guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarProducto();
            }
        });
    }

    private void guardarProducto() {
        // Validaciones
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String precioTexto = etPrecio.getText().toString().trim();
        String stockTexto = etStock.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            etNombre.setError("El nombre no puede estar vacío");
            return;
        }

        if (TextUtils.isEmpty(descripcion)) {
            etDescripcion.setError("La descripción no puede estar vacía");
            return;
        }

        if (TextUtils.isEmpty(precioTexto)) {
            etPrecio.setError("El precio no puede estar vacío");
            return;
        }

        if (TextUtils.isEmpty(stockTexto)) {
            etStock.setError("El stock no puede estar vacío");
            return;
        }

        double precio;
        int stock;

        try {
            precio = Double.parseDouble(precioTexto);
            if (precio < 0) {
                etPrecio.setError("El precio no puede ser negativo");
                return;
            }
        } catch (NumberFormatException e) {
            etPrecio.setError("El precio debe ser un número válido");
            return;
        }

        try {
            stock = Integer.parseInt(stockTexto);
            if (stock < 0) {
                etStock.setError("El stock no puede ser negativo");
                return;
            }
        } catch (NumberFormatException e) {
            etStock.setError("El stock debe ser un número válido");
            return;
        }

        // Guardar en Firebase
        String id = databaseReference.push().getKey();
        Producto producto = new Producto(id, nombre, descripcion, precio, stock);
        databaseReference.child(id).setValue(producto).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AgregarProductoActivity.this, "Producto guardado con éxito", Toast.LENGTH_SHORT).show();
                finish(); // Cierra esta actividad
            } else {
                Toast.makeText(AgregarProductoActivity.this, "Error al guardar el producto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
