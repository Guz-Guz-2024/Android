package com.example.dos_login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class PedidoActivity extends AppCompatActivity {
    private TextView textViewPedido;
    private Button btnRegresar;
    private ArrayList<String> productosSeleccionados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        textViewPedido = findViewById(R.id.textViewPedido);
        btnRegresar = findViewById(R.id.btn_regresar);

        // Obtener los productos seleccionados desde ClientActivity
        productosSeleccionados = getIntent().getStringArrayListExtra("productosSeleccionados");

        // Mostrar los productos acumulados
        if (productosSeleccionados != null && !productosSeleccionados.isEmpty()) {
            StringBuilder pedidoTexto = new StringBuilder();
            for (String producto : productosSeleccionados) {
                pedidoTexto.append(producto).append("\n");
            }
            textViewPedido.setText(pedidoTexto.toString());
        } else {
            textViewPedido.setText("Plato no seleccionado");
        }

        // Configurar el botón de regreso
        btnRegresar.setOnClickListener(v -> {
            // Crear un Intent para regresar a ClientActivity
            Intent intent = new Intent(PedidoActivity.this, ClientActivity.class);
            startActivity(intent);
            finish(); // Finaliza la actividad actual
        });
    }
}
