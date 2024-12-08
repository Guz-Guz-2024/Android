package com.example.dos_login;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddUserActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etUsername, etPassword, etRole;
    Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Inicializar base de datos y elementos del layout
        db = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etRole = findViewById(R.id.etRole);
        btnAddUser = findViewById(R.id.btnAddUser);

        // Configurar el botón de agregar usuario
        btnAddUser.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String role = etRole.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_USERNAME, username);
            values.put(DatabaseHelper.COL_PASSWORD, password);
            values.put(DatabaseHelper.COL_ROLE, role);

            long result = db.getWritableDatabase().insert(DatabaseHelper.TABLE_USERS, null, values);

            if (result != -1) {
                Toast.makeText(this, "Usuario agregado correctamente.", Toast.LENGTH_SHORT).show();
                finish(); // Regresar a la lista de usuarios
            } else {
                Toast.makeText(this, "Error al agregar usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
