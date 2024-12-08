package com.example.dos_login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dos_login.R;

public class RegisterActivity extends AppCompatActivity {
    com.example.dos_login.DatabaseHelper db;
    EditText etUsername, etPassword;
    RadioGroup rgRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new com.example.dos_login.DatabaseHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        rgRole = findViewById(R.id.rgRole);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String role = rgRole.getCheckedRadioButtonId() == R.id.rbAdmin ? "admin" : "client";

            // Intentar registrar al usuario
            long result = db.registerUser(username, password, role);

            if (result == -2) { // Usuario ya existente
                Toast.makeText(this, "Usuario ya existente", Toast.LENGTH_SHORT).show();
                // Redirigir a MainActivity
                Intent intent = new Intent(this, com.example.dos_login.MainActivity.class);
                startActivity(intent);
                finish();
            } else if (result == -1) { // Error genérico
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            } else { // Registro exitoso
                Toast.makeText(this, "exitosamente registrado", Toast.LENGTH_SHORT).show();
                // Redirigir a MainActivity
                Intent intent = new Intent(this, com.example.dos_login.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
