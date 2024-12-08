package com.example.dos_login;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etEditUsername, etEditPassword;
    CheckBox cbShowPassword;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inicializar la base de datos y los elementos del layout
        db = new DatabaseHelper(this);
        etEditUsername = findViewById(R.id.etEditUsername);
        etEditPassword = findViewById(R.id.etEditPassword);
        cbShowPassword = findViewById(R.id.cbShowPassword);
        Button btnUpdate = findViewById(R.id.btnUpdate);

        // Obtener el nombre de usuario desde el Intent
        username = getIntent().getStringExtra("username");

        if (username != null) {
            cargarDatosUsuario(username);
        } else {
            Toast.makeText(this, "Error: No se pudo obtener el nombre de usuario.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Manejar el evento de mostrar/ocultar contraseña
        cbShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etEditPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etEditPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            etEditPassword.setSelection(etEditPassword.getText().length()); // Mover el cursor al final
        });

        // Manejar el evento de actualización
        btnUpdate.setOnClickListener(v -> {
            String newUsername = etEditUsername.getText().toString().trim();
            String newPassword = etEditPassword.getText().toString().trim();

            // Validar que los campos no estén vacíos
            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Intentar actualizar los datos del usuario
            actualizarDatosUsuario(username, newUsername, newPassword);
        });
    }

    /**
     * Método para cargar los datos del usuario desde la base de datos.
     */
    private void cargarDatosUsuario(String username) {
        // Consultar los datos del usuario
        Cursor cursor = db.getReadableDatabase().query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COL_USERNAME, DatabaseHelper.COL_PASSWORD},
                DatabaseHelper.COL_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Rellenar los campos con los datos del usuario
            etEditUsername.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME)));
            etEditPassword.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD)));
            cursor.close();
        } else {
            Toast.makeText(this, "No se encontraron datos para este usuario.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Método para actualizar los datos del usuario en la base de datos.
     */
    private void actualizarDatosUsuario(String oldUsername, String newUsername, String newPassword) {
        // Verificar si el nuevo nombre de usuario ya existe
        Cursor existingUserCursor = db.getReadableDatabase().query(
                DatabaseHelper.TABLE_USERS,
                null,
                DatabaseHelper.COL_USERNAME + " = ? AND " + DatabaseHelper.COL_USERNAME + " != ?",
                new String[]{newUsername, oldUsername},
                null,
                null,
                null
        );

        if (existingUserCursor != null && existingUserCursor.getCount() > 0) {
            Toast.makeText(this, "El nuevo nombre de usuario ya está en uso.", Toast.LENGTH_SHORT).show();
            existingUserCursor.close();
            return;
        }

        if (existingUserCursor != null) {
            existingUserCursor.close();
        }

        // Crear los valores para actualizar
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USERNAME, newUsername);
        values.put(DatabaseHelper.COL_PASSWORD, newPassword);

        // Realizar la actualización en la base de datos
        int rowsUpdated = db.getWritableDatabase().update(
                DatabaseHelper.TABLE_USERS,
                values,
                DatabaseHelper.COL_USERNAME + " = ?",
                new String[]{oldUsername}
        );

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar los datos.", Toast.LENGTH_SHORT).show();
        }
    }
}
