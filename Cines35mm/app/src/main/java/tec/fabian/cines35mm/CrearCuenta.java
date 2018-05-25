package tec.fabian.cines35mm;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        //Validar cuenta en action done de EditText Contrasenna
        ((EditText)findViewById(R.id.txtConfirmarContrasenna)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            try {
                                validacionCrearCuenta();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                });

        //Validar cuenta en el boton crear cuenta
        ((Button)findViewById(R.id.btnCrearCuenta)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            validacionCrearCuenta();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    //Validar formato de correo
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void validacionCrearCuenta() throws JSONException, ExecutionException, InterruptedException{
        EditText ETCorreo = findViewById(R.id.txtCorreo);
        EditText ETNick = findViewById(R.id.txtNick);
        EditText ETContrasenna = findViewById(R.id.txtContrasenna);
        EditText ETConfirmarContrasenna = findViewById(R.id.txtConfirmarContrasenna);

        Conexion user_extendeds = new Conexion();

        String result="";
        //


        if( ETCorreo.getText().toString().length() == 0 ) {
            ETCorreo.setError("Ingrese el correo");
        }
        else  if(!isEmailValid(ETCorreo.getText().toString())) {
            ETCorreo.setError("Ingrese un correo válido");
        }
        else if(ETNick.getText().toString().length() == 0) {
            ETNick.setError("Ingrese un nick");
        }
        else if(ETContrasenna.getText().toString().length() == 0) {
            ETContrasenna.setError("Ingrese la contraseña");
        }
        else if(!ETContrasenna.getText().toString().equals(ETConfirmarContrasenna.getText().toString())) {
            ETConfirmarContrasenna.setError("Las contraseñas deben coincidir");
        }
        else {
            result = user_extendeds.execute("https://cines35mm.herokuapp.com/users.json","GET").get();
            String correo = ETCorreo.getText().toString().trim();
            String nick = ETNick.getText().toString().trim();

            if(!UserExist(result,correo,nick)){
                //Crear cuenta
                registrarUsuario();
            }else{
                Toast.makeText(this,"Error: Ya existe un correo o nick igual registrado",Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(this,result,Toast.LENGTH_LONG).show();
        }
    }
    private boolean UserExist(String jsonDatos,String correo, String nick) throws JSONException {
        JSONArray datos = new JSONArray(jsonDatos);

        for(int i = 0; i < datos.length(); i++){
            JSONObject elemento = datos.getJSONObject(i);
            if(elemento.getString("correo").equals(correo) || elemento.getString("nick").equals(nick)){
                return true;
            }
        }
        return false;
    }

    public void registrarUsuario() throws JSONException, ExecutionException, InterruptedException {
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
                 finish();
             }
         }
    }
    
}
