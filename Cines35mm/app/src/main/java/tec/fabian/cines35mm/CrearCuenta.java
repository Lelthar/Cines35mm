package tec.fabian.cines35mm;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class CrearCuenta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        //Mostrar fecha para regresar a la pantalla anterior en la barra de titulo
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Poner de título 'Crear Cuenta'
        actionBar.setTitle("Crear Cuenta");

        // TODO
        // Validar formato correo
        // Validar contraseñas iguales
        // Validar usuario no repetidos en BD
        // Registrar en la BD el usuario
        Toast.makeText(this,"-- WIP Crear cuenta --",Toast.LENGTH_SHORT).show();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
