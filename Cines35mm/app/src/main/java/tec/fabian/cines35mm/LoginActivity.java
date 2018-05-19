package tec.fabian.cines35mm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new AlertDialog.Builder(this)
                .setTitle("¡Importante!")
                .setMessage("Datos de prueba" +
                        "\n\n" +
                        "Correo: prueba@correo.com\n" +
                        "Contraseña: 123\n")
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_info)
                .show();//*/
        // TODO
        // Verificar el correo y contraseña con la BD
        // Validar formato del correo
        Toast.makeText(this,"-- WIP Login --",Toast.LENGTH_SHORT).show();

        //Llamar verificar usuario en action done de EditText Contrasenna
        ((EditText)findViewById(R.id.txtContrasenna)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            VerificarUsuario();
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                });

        //Boton ingresar
        Button Ingresar=(Button) findViewById(R.id.btnIngresar);
        Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarUsuario();
            }
        });

        //Boton crear cuenta
        Button CrearCuenta=(Button) findViewById(R.id.btnCrearCuenta);
        CrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirCrearCuenta();
            }
        });
    }

    //Boton ingresar
    protected void VerificarUsuario(){
        EditText ETCorreo = (EditText)findViewById(R.id.txtCorreo);
        EditText ETContrasenna = (EditText)findViewById(R.id.txtContrasenna);

        if( ETCorreo.getText().toString().length() == 0 ) {
            ETCorreo.setError("Ingrese el correo");
        }
        else  if(!isEmailValid(ETCorreo.getText().toString())) {
            ETCorreo.setError("Ingrese un correo válido");
        }
        else if(ETContrasenna.getText().toString().length() == 0) {
            ETContrasenna.setError("Ingrese la contraseña");
        }
        else {

            String correo = ETCorreo.getText().toString().trim();
            String contrasenna = ((EditText) findViewById(R.id.txtContrasenna)).getText().toString().trim();

            if(correo.trim().equals("prueba@correo.com") && contrasenna.trim().equals("123")){
                //Abrir ventana principal
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("correo",correo);
                startActivity(i);
            }else{
                Toast.makeText(this,"Correo o contraseña incorrectos",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Boton crear cuenta
    protected void AbrirCrearCuenta(){
        //Abrir ventana para crear cuenta de usuario
        Intent i =new Intent(getApplicationContext(),CrearCuenta.class);
        startActivity(i);
    }

    //Validar formato de correo
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
