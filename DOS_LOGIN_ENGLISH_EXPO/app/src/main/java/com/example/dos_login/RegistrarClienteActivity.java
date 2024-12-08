package com.example.dos_login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrarClienteActivity extends AppCompatActivity {

    private EditText edtNombreCliente, edtCorreoCliente, edtTelefonoCliente, edtDniCliente;
    private Button btnRegistrarCliente, btnBuscarCliente, btnActualizarCliente, btnEliminarCliente, btnLimpiarCliente, btnCerrarSesion, btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cliente);

        // Inicializar campos y botones
        edtNombreCliente = findViewById(R.id.edtNombreCliente);
        edtCorreoCliente = findViewById(R.id.edtCorreoCliente);
        edtTelefonoCliente = findViewById(R.id.edtTelefonoCliente);
        edtDniCliente = findViewById(R.id.edtDniCliente);

        btnRegistrarCliente = findViewById(R.id.btn_guardar);
        btnBuscarCliente = findViewById(R.id.btn_buscar);
        btnActualizarCliente = findViewById(R.id.btn_actualizar);
        btnEliminarCliente = findViewById(R.id.btn_eliminar);
        btnLimpiarCliente = findViewById(R.id.btn_limpiar);
        btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion);
        btnRegresar = findViewById(R.id.btn_regresar);

        // Configurar botones
        btnRegistrarCliente.setOnClickListener(view -> registrarCliente());
        btnBuscarCliente.setOnClickListener(view -> buscarCliente());
        btnActualizarCliente.setOnClickListener(view -> actualizarCliente());
        btnEliminarCliente.setOnClickListener(view -> eliminarCliente());
        btnLimpiarCliente.setOnClickListener(view -> limpiarCampos());
        btnCerrarSesion.setOnClickListener(view -> cerrarSesion());

        // Configuración del botón Regresar
        btnRegresar.setOnClickListener(v -> {
            finish(); // Cierra la actividad actual y regresa a la anterior
        });
    }

    private void registrarCliente() {
        String nombre = edtNombreCliente.getText().toString();
        String correo = edtCorreoCliente.getText().toString();
        String telefono = edtTelefonoCliente.getText().toString();
        String dni = edtDniCliente.getText().toString(); // Obtenemos el DNI correctamente

        if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el cliente ya existe
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_CLIENTES, null, "dni=?", new String[]{dni}, null, null, null);
        if (cursor.moveToFirst()) {
            // Si el cliente con ese DNI ya existe
            Toast.makeText(this, "El cliente con ese Nombre o DNI ya existe", Toast.LENGTH_SHORT).show();
            cursor.close();
            db.close();
            return;
        }
        cursor.close();

        // Si no existe el cliente, proceder a registrarlo
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NOMBRE_CLIENTE, nombre);
        values.put(DatabaseHelper.COL_CORREO_CLIENTE, correo);
        values.put(DatabaseHelper.COL_TELEFONO_CLIENTE, telefono);
        values.put(DatabaseHelper.COL_DNI_CLIENTE, dni); // Ahora el DNI se pasa correctamente

        long result = db.insert(DatabaseHelper.TABLE_CLIENTES, null, values);
        db.close();

        if (result != -1) {
            Toast.makeText(this, "Cliente exitosamente registrado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al registrar el cliente", Toast.LENGTH_SHORT).show();
        }
    }

    private void cerrarSesion() {
        Intent intent = new Intent(RegistrarClienteActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void buscarCliente() {
        String nombre = edtNombreCliente.getText().toString();  // Obtener el nombre desde el campo de texto
        String dni = edtDniCliente.getText().toString();  // Obtener el DNI desde el campo de texto

        // Verificar que al menos uno de los dos campos no esté vacío
        if (nombre.isEmpty() && dni.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el nombre o el DNI del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        // Abrir la base de datos en modo lectura
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Si se ha introducido un nombre, buscar por nombre
        Cursor cursor = null;
        if (!nombre.isEmpty()) {
            cursor = db.query(DatabaseHelper.TABLE_CLIENTES, null, "nombre=?", new String[]{nombre}, null, null, null);
        }

        // Si no se ha introducido un nombre pero sí un DNI, buscar por DNI
        if (!dni.isEmpty() && cursor == null) {
            cursor = db.query(DatabaseHelper.TABLE_CLIENTES, null, "dni=?", new String[]{dni}, null, null, null);
        }

        // Verificar si se encontró el cliente
        if (cursor != null && cursor.moveToFirst()) {
            // Mostrar los detalles del cliente encontrado
            edtNombreCliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOMBRE_CLIENTE)));
            edtCorreoCliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CORREO_CLIENTE)));
            edtTelefonoCliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TELEFONO_CLIENTE)));
            edtDniCliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DNI_CLIENTE))); // Mostrar el DNI recuperado
            Toast.makeText(this, "Cliente encontrado", Toast.LENGTH_SHORT).show();
        } else {
            // Si no se encontró el cliente
            Toast.makeText(this, "El cliente con el nombre o DNI especificado no existe", Toast.LENGTH_SHORT).show();
        }

        // Cerrar el cursor y la base de datos
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    private void actualizarCliente() {
        String nombre = edtNombreCliente.getText().toString();
        String correo = edtCorreoCliente.getText().toString();
        String telefono = edtTelefonoCliente.getText().toString();
        String dni = edtDniCliente.getText().toString(); // Obtener el DNI para actualizarlo

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el nombre del cliente a actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CORREO_CLIENTE, correo);
        values.put(DatabaseHelper.COL_TELEFONO_CLIENTE, telefono);
        values.put(DatabaseHelper.COL_DNI_CLIENTE, dni); // Actualizar también el DNI

        int rowsAffected = db.update(DatabaseHelper.TABLE_CLIENTES, values, "nombre=?", new String[]{nombre});
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "Cliente exitosamente actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarCliente() {
        String nombre = edtNombreCliente.getText().toString();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el nombre del cliente a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted = db.delete(DatabaseHelper.TABLE_CLIENTES, "nombre=?", new String[]{nombre});
        db.close();

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Cliente exitosamente eliminado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        edtNombreCliente.setText("");
        edtCorreoCliente.setText("");
        edtTelefonoCliente.setText("");
        edtDniCliente.setText("");
    }
}
