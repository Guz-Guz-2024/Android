package com.example.dos_login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    com.example.dos_login.DatabaseHelper db;
    EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new com.example.dos_login.DatabaseHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            Cursor cursor = db.loginUser(username, password);

            if (cursor != null && cursor.moveToFirst()) {
                // Comprobar el rol y enviar el nombre de usuario a la actividad correspondiente
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                if (role.equals("admin")) {

                    Intent intentadmin = new Intent(this, AdminActivity.class);
                    intentadmin.putExtra("username", username);  // Pasar el nombre de usuario
                    startActivity(intentadmin);
                } else {
                    // Enviar el nombre de usuario a ClientActivity
                    Intent intent = new Intent(this, ClientActivity.class);
                    intent.putExtra("username", username);  // Pasar el nombre de usuario
                    startActivity(intent);
                }
                finish();
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}

