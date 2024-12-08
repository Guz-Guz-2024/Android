package com.example.dos_login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrarProductoActivity extends AppCompatActivity {

    private EditText etNombre, etPrecio, etDescripcion, etCategoria, etCantidad;
    private Button btnGuardar, btnActualizar, btnEliminar, btnBuscar, btnCerrarSesion, btnLimpiar, btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etDescripcion = findViewById(R.id.etDescripcion);
        etCategoria = findViewById(R.id.etCategoria);
        etCantidad = findViewById(R.id.etCantidad);
        btnGuardar = findViewById(R.id.btn_guardar);
        btnActualizar = findViewById(R.id.btn_actualizar);
        btnEliminar = findViewById(R.id.btn_eliminar);
        btnBuscar = findViewById(R.id.btn_buscar);
        btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion);
        btnLimpiar = findViewById(R.id.btn_limpiar);
        btnRegresar = findViewById(R.id.btn_regresar); // Botón de regreso

        // Configurar botones
        btnGuardar.setOnClickListener(view -> guardarProducto());
        btnActualizar.setOnClickListener(view -> actualizarProducto());
        btnEliminar.setOnClickListener(view -> eliminarProducto());
        btnBuscar.setOnClickListener(view -> buscarProducto());
        btnCerrarSesion.setOnClickListener(view -> cerrarSesion());
        btnLimpiar.setOnClickListener(view -> limpiarCampos());

        // Configurar el botón de regreso
        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(RegistrarProductoActivity.this, AdminActivity.class);
            startActivity(intent);
            finish(); // Finaliza esta actividad
        });
    }

    private void cerrarSesion() {
        Intent intent = new Intent(RegistrarProductoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void guardarProducto() {
        String nombre = etNombre.getText().toString();
        String precioStr = etPrecio.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String categoria = etCategoria.getText().toString();
        String cantidadStr = etCantidad.getText().toString();

        if (nombre.isEmpty() || precioStr.isEmpty() || descripcion.isEmpty() || categoria.isEmpty() || cantidadStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int cantidad = Integer.parseInt(cantidadStr);

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // Verificar si el producto ya existe
            Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTO,
                    null,
                    DatabaseHelper.COL_NOMBRE + "=?",
                    new String[]{nombre},
                    null,
                    null,
                    null);

            if (cursor.moveToFirst()) {
                // Si se encuentra el producto
                Toast.makeText(this, "Este plato ya existe", Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                return;
            }
            cursor.close();

            // Si no existe, insertar el nuevo producto
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_NOMBRE, nombre);
            values.put(DatabaseHelper.COL_PRECIO, precio);
            values.put(DatabaseHelper.COL_DESCRIPCION, descripcion);
            values.put(DatabaseHelper.COL_CATEGORIA, categoria);
            values.put(DatabaseHelper.COL_CANTIDAD, cantidad);

            long result = db.insert(DatabaseHelper.TABLE_PRODUCTO, null, values);
            db.close();

            if (result != -1) {
                Toast.makeText(this, "Plato exitosamente registrado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            } else {
                Toast.makeText(this, "Error al registrar el plato", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Formato de precio o cantidad inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarProducto() {
        String nombre = etNombre.getText().toString();
        String precioStr = etPrecio.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String categoria = etCategoria.getText().toString();
        String cantidadStr = etCantidad.getText().toString();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el nombre del plato a actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PRECIO, precioStr);
        values.put(DatabaseHelper.COL_DESCRIPCION, descripcion);
        values.put(DatabaseHelper.COL_CATEGORIA, categoria);
        values.put(DatabaseHelper.COL_CANTIDAD, cantidadStr);

        int rowsAffected = db.update(DatabaseHelper.TABLE_PRODUCTO, values, "nombre=?", new String[]{nombre});
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "Plato exitosamente actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Plato no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarProducto() {
        String nombre = etNombre.getText().toString();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el nombre del plato a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted = db.delete(DatabaseHelper.TABLE_PRODUCTO, "nombre=?", new String[]{nombre});
        db.close();

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Plato exitosamente eliminado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Plato no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarProducto() {
        String nombre = etNombre.getText().toString().toLowerCase(); // Convertir a minúsculas

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el nombre del plato a buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consultar productos en la base de datos
        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTO, null, null, null, null, null, null);

        boolean encontrado = false;

        // Recorrer el cursor y comparar los nombres de productos ignorando mayúsculas/minúsculas
        while (cursor.moveToNext()) {
            String productoNombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOMBRE));

            // Comparar el nombre del producto convertido a minúsculas con la búsqueda
            if (productoNombre.toLowerCase().contains(nombre)) {  // Ignorar mayúsculas/minúsculas
                // Mostrar los detalles del producto
                etPrecio.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRECIO)));
                etDescripcion.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPCION)));
                etCategoria.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORIA)));
                etCantidad.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CANTIDAD)));
                encontrado = true;
                break;
            }
        }

        cursor.close();
        db.close();

        if (!encontrado) {
            Toast.makeText(this, "Plato no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etPrecio.setText("");
        etDescripcion.setText("");
        etCategoria.setText("");
        etCantidad.setText("");
    }
}
