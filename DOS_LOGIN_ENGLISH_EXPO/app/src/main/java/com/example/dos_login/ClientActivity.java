package com.example.dos_login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ClientActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private DatabaseHelper dbHelper;
    private Button btnVerPedido, btnCerrarSesion;
    private ArrayList<String> productosSeleccionados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        // Inicializar la lista para acumular los productos seleccionados
        productosSeleccionados = new ArrayList<>();

        // Obtener el nombre del cliente desde el Intent
        String username = getIntent().getStringExtra("username");

        // Inicializar UI
        TextView textView = findViewById(R.id.textView);
        if (username != null && !username.isEmpty()) {
            textView.setText("¡Bienvenido a la Cevichería Conchita Rica " + username + "!");
        }
        textView.setGravity(Gravity.CENTER);

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar base de datos y adaptador
        dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.obtenerProductos();

        productAdapter = new ProductAdapter(this, cursor, new ProductAdapter.OnProductSelectedListener() {
            @Override
            public void onProductSelected(String nombreProducto, double precioProducto) {
                // Acumular el producto seleccionado en la lista
                String textoProducto = "Plato: " + nombreProducto + " | Precio: S/" + precioProducto;
                productosSeleccionados.add(textoProducto);
            }
        });

        recyclerView.setAdapter(productAdapter);

        // Configurar el botón para ver el pedido
        btnVerPedido = findViewById(R.id.btnAgregarPedido);
        btnVerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pasar los productos seleccionados a PedidoActivity
                Intent intent = new Intent(ClientActivity.this, PedidoActivity.class);
                intent.putStringArrayListExtra("productosSeleccionados", productosSeleccionados);
                startActivity(intent);
            }
        });

        // Configurar el botón de Cerrar sesión
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la pantalla de inicio de sesión
                Intent intent = new Intent(ClientActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finaliza la actividad actual
            }
        });
    }
}
