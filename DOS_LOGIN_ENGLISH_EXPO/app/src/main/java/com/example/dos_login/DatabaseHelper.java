package com.example.dos_login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";

    // Tabla users
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";

    // Tabla productos
    public static final String TABLE_PRODUCTO = "producto";
    public static final String COL_PRODUCTO_ID = "id";
    public static final String COL_NOMBRE = "nombre";
    public static final String COL_PRECIO = "precio";
    public static final String COL_DESCRIPCION = "descripcion";
    public static final String COL_CATEGORIA = "categoria";
    public static final String COL_CANTIDAD = "cantidad";

    // Tabla clientes
    public static final String TABLE_CLIENTES = "clientes";
    public static final String COL_CLIENTE_ID = "id";
    public static final String COL_NOMBRE_CLIENTE = "nombre";
    public static final String COL_CORREO_CLIENTE = "correo";
    public static final String COL_TELEFONO_CLIENTE = "telefono";
    public static final String COL_DNI_CLIENTE = "DNI";


    // Tabla pedidos (nueva)
    public static final String TABLE_PEDIDO = "pedido";
    public static final String COL_PEDIDO_ID = "id";
    public static final String COL_CLIENTE_ID_PEDIDO = "cliente_id";  // Relación con la tabla de clientes
    public static final String COL_PRODUCTO_ID_PEDIDO = "producto_id";  // Relación con la tabla de productos
    public static final String COL_CANTIDAD_PEDIDO = "cantidad";  // Cantidad del producto

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de usuarios
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)");

        // Crear tabla de productos
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTO + " (" +
                COL_PRODUCTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOMBRE + " TEXT, " +
                COL_PRECIO + " REAL, " +
                COL_DESCRIPCION + " TEXT, " +
                COL_CATEGORIA + " TEXT, " +
                COL_CANTIDAD + " INTEGER)");

        // Crear tabla de clientes
        db.execSQL("CREATE TABLE " + TABLE_CLIENTES + " (" +
                COL_CLIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOMBRE_CLIENTE + " TEXT, " +
                COL_CORREO_CLIENTE + " TEXT, " +
                COL_TELEFONO_CLIENTE + " TEXT, " +
                COL_DNI_CLIENTE + " TEXT UNIQUE)"); // Agregar campo DNI con restricción única


        // Crear tabla de pedidos
        db.execSQL("CREATE TABLE " + TABLE_PEDIDO + " (" +
                COL_PEDIDO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CLIENTE_ID_PEDIDO + " INTEGER, " +
                COL_PRODUCTO_ID_PEDIDO + " INTEGER, " +
                COL_CANTIDAD_PEDIDO + " INTEGER, " +
                "FOREIGN KEY(" + COL_CLIENTE_ID_PEDIDO + ") REFERENCES " + TABLE_CLIENTES + "(" + COL_CLIENTE_ID + "), " +
                "FOREIGN KEY(" + COL_PRODUCTO_ID_PEDIDO + ") REFERENCES " + TABLE_PRODUCTO + "(" + COL_PRODUCTO_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si se necesita actualizar la base de datos, eliminamos las tablas
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDO);

        // Llamamos a onCreate para recrear las tablas según la nueva versión
        onCreate(db);

        // Si la nueva versión requiere que se agregue el campo DNI a la tabla clientes
        // Esto se hace solo si la tabla ya existe en una versión anterior
        if (oldVersion < 2) {  // Cambia el número de versión según corresponda
            db.execSQL("ALTER TABLE " + TABLE_CLIENTES + " ADD COLUMN " + COL_DNI_CLIENTE + " TEXT UNIQUE");
        }
    }

    // Verificar si el usuario ya existe
    public boolean usuarioExistente(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_USERNAME + "=?", new String[]{username}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    // Registrar un nuevo usuario con validación
    public String registerUser(String username, String password, String role, Context context) {
        if (usuarioExistente(username)) {
            return "El usuario ya existe";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        if (result == -1) {
            return "Error al registrar usuario";
        } else {
            return "Usuario exitosamente registrado";
        }
    }


    public String insertarCliente(String nombre, String correo, String telefono, String dni) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Comprobar si el cliente ya existe por DNI
        Cursor cursor = db.query(TABLE_CLIENTES, null, COL_DNI_CLIENTE + " = ?", new String[]{dni}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return "Cliente ya registrado con este Dni";
        }

        // Si el cliente no existe, proceder a insertarlo
        ContentValues values = new ContentValues();
        values.put(COL_NOMBRE_CLIENTE, nombre);
        values.put(COL_CORREO_CLIENTE, correo);
        values.put(COL_TELEFONO_CLIENTE, telefono);
        values.put(COL_DNI_CLIENTE, dni);

        // Insertar el cliente en la base de datos
        long result = db.insert(TABLE_CLIENTES, null, values);

        // Cerrar la base de datos
        db.close();

        if (result == -1) {
            return "Error al registrar cliente";
        } else {
            return "Cliente exitosamente registrado";
        }
    }



    /// Métodos para la tabla productos
    public String insertarProducto(String nombre, double precio, String descripcion, String categoria, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Comprobar si el producto ya existe por nombre
        Cursor cursor = db.query(TABLE_PRODUCTO, null, COL_NOMBRE + " = ?", new String[]{nombre}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return "Plato ya existente";
        }

        // Si el producto no existe, proceder a insertarlo
        ContentValues values = new ContentValues();
        values.put(COL_NOMBRE, nombre);
        values.put(COL_PRECIO, precio);
        values.put(COL_DESCRIPCION, descripcion);
        values.put(COL_CATEGORIA, categoria);
        values.put(COL_CANTIDAD, cantidad);

        long result = db.insert(TABLE_PRODUCTO, null, values);
        cursor.close();

        if (result == -1) {
            return "Error al registrar plato";
        } else {
            return "Plato exitosamente registrado";
        }
    }


    public int eliminarProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PRODUCTO, COL_PRODUCTO_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int actualizarProducto(int id, String nombre, double precio, String descripcion, String categoria, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NOMBRE, nombre);
        values.put(COL_PRECIO, precio);
        values.put(COL_DESCRIPCION, descripcion);
        values.put(COL_CATEGORIA, categoria);
        values.put(COL_CANTIDAD, cantidad);

        return db.update(TABLE_PRODUCTO, values, COL_PRODUCTO_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor buscarProducto(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PRODUCTO, null, COL_NOMBRE + " LIKE ?", new String[]{"%" + nombre + "%"}, null, null, null);
    }

    // Método para verificar el inicio de sesión de un usuario
    public Cursor loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para seleccionar un usuario basado en el nombre de usuario y la contraseña
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?" ;

        // Ejecutar la consulta y devolver el cursor con los resultados
        return db.rawQuery(query, new String[]{username, password});
    }

    public long registerUser(String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            return -2; // Código para usuario existente
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("role", role);

        return db.insert("users", null, values); // Retorna -1 si ocurre un error
    }




    // Métodos para la tabla pedidos
    public long insertarPedido(int clienteId, int productoId, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_CLIENTE_ID_PEDIDO, clienteId);
        values.put(COL_PRODUCTO_ID_PEDIDO, productoId);
        values.put(COL_CANTIDAD_PEDIDO, cantidad);

        return db.insert(TABLE_PEDIDO, null, values);
    }

    public Cursor obtenerPedidos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PEDIDO, null);
    }

    // Método para obtener todos los productos
    public Cursor obtenerProductos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTO, null);
    }
}
