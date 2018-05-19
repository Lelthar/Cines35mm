package tec.fabian.cines35mm;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class CrearCuenta extends AppCompatActivity {
    private EditText correo;
    private EditText nickname;
    private EditText contrasenna;
    private EditText contrasenna_confirmacion;
    private Conexion conexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        //Mostrar fecha para regresar a la pantalla anterior en la barra de titulo
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Poner de título 'Crear Cuenta'
        actionBar.setTitle("Crear Cuenta");

        correo = findViewById(R.id.txtCorreo);
        nickname = findViewById(R.id.txtNick);
        contrasenna = findViewById(R.id.txtContrasenna);
        contrasenna_confirmacion = findViewById(R.id.txtConfirmarContrasenna);
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

    public void registrarUsuario(View view) throws JSONException, ExecutionException, InterruptedException {
         String mail = this.correo.getText().toString();
         String nick = this.nickname.getText().toString();
         String password = this.contrasenna.getText().toString();
         String password_confirmation = this.contrasenna_confirmacion.getText().toString();
         if(!mail.isEmpty() && !nick.isEmpty() && !password.isEmpty() && !password_confirmation.isEmpty() && password.equals(password_confirmation)){
             conexion = new Conexion();
             JSONObject json_parametros = new JSONObject();
             json_parametros.put("correo",mail);
             json_parametros.put("nick",nick);
             json_parametros.put("contrasenha",password);
             json_parametros.put("tipo_cuenta","Cliente");
             json_parametros.put("disponible",true);
             json_parametros.put("url_imagen","");
             String  result = conexion.execute("https://cines35mm.herokuapp.com/users","POST",json_parametros.toString()).get();

             if(result.equals("Created")) {
                 Toast.makeText(this, "Se creó exitosamente la cuenta", Toast.LENGTH_LONG).show();
                 Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                 startActivity(i);
             }
         }
    }
}
