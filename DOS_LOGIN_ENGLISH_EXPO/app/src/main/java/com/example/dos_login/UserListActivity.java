package com.example.dos_login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    DatabaseHelper db;
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User> userList;
    FloatingActionButton fabAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        // Inicializar base de datos y cargar usuarios
        db = new DatabaseHelper(this);
        cargarUsuarios();

        // Configurar adaptador
        userAdapter = new UserAdapter(userList, user -> {
            // Manejar clic en usuario
            Intent intent = new Intent(UserListActivity.this, EditProfileActivity.class);
            intent.putExtra("username", user.getUsername());
            startActivity(intent);
        });

        recyclerView.setAdapter(userAdapter);

        // Configurar el FAB
        fabAddUser = findViewById(R.id.fabAddUser);
        fabAddUser.setOnClickListener(v -> {
            // Navegar al Activity de agregar usuario
            Intent intent = new Intent(UserListActivity.this, AddUserActivity.class);
            startActivity(intent);
        });
    }

    private void cargarUsuarios() {
        Cursor cursor = db.getReadableDatabase().query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COL_USERNAME, DatabaseHelper.COL_ROLE},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROLE));
                userList.add(new User(username, role));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No hay usuarios registrados.", Toast.LENGTH_SHORT).show();
        }
    }
}
