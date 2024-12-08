package com.example.dos_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdminActivity extends AppCompatActivity {

    private Button btnLogoutAdmin, btnRegistrarProducto, btnRegistrarCliente;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Configurar la barra de herramientas
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.black)); // Forzar el color del título
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.black)); // Si hay subtítulo
        }


        // Inicializar botones
        btnLogoutAdmin = findViewById(R.id.btnLogoutAdmin);
        btnRegistrarProducto = findViewById(R.id.btn_registrar_producto);
        btnRegistrarCliente = findViewById(R.id.btn_registrar_cliente);

        // Obtener el nombre de usuario que se pasó desde MainActivity
        username = getIntent().getStringExtra("username");
        if (username != null) {
            Toast.makeText(this, "Bienvenido, Administrador: " + username, Toast.LENGTH_LONG).show();
        }

        // Configurar el botón para cerrar sesión
        btnLogoutAdmin.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Configurar el botón para registrar producto
        btnRegistrarProducto.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, RegistrarProductoActivity.class);
            startActivity(intent);
        });

        // Configurar el botón para registrar cliente
        btnRegistrarCliente.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, RegistrarClienteActivity.class);
            startActivity(intent);
        });
    }



  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu); // Cambia a menu_admin
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_list_users) {
            Intent intent = new Intent(this, UserListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    // Inflar el menú de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    // Manejar la selección de opciones del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_profile) {
            // Navegar a EditProfileActivity para editar los datos del administrador
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_list_users) {
            Intent intent = new Intent(this, UserListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
