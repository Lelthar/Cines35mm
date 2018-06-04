package tec.fabian.cines35mm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

public class LoginActivity extends AppCompatActivity {

    private Conexion conexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Llamar verificar usuario en action done de EditText Contrasenna
        ((EditText)findViewById(R.id.txtContrasenna)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            try {
                                VerificarUsuario();
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

        //Boton ingresar
        Button Ingresar=(Button) findViewById(R.id.btnIngresar);
        Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    VerificarUsuario();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    protected void VerificarUsuario() throws ExecutionException, InterruptedException, JSONException {
        EditText ETCorreo = (EditText)findViewById(R.id.txtCorreo);
        EditText ETContrasenna = (EditText)findViewById(R.id.txtContrasenna);

        //conexion = new Conexion(); //Esta clase realiza la conexion al backend de la appp
        //JSONObject jsonObject = new JSONObject(); //Se crea un json object para pasarselo al metodo de conexion
        Conexion user_extendeds = new Conexion();

        String result="";
        //
        result = user_extendeds.execute("https://cines35mm.herokuapp.com/users.json","GET").get();

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

            if(UserExist(result,correo,contrasenna)){
                //Abrir ventana principal
                LlamarMainActivity(ObtenerJSon(result,correo));
            }else{
                Toast.makeText(this,"Correo o contraseña incorrectos",Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(this,result,Toast.LENGTH_LONG).show();
        }
    }

    private boolean UserExist(String jsonDatos,String correo, String password) throws JSONException {
        JSONArray datos = new JSONArray(jsonDatos);

        for(int i = 0; i < datos.length(); i++){
            JSONObject elemento = datos.getJSONObject(i);
            if(elemento.getString("correo").equals(correo) && elemento.getString("contrasenha").equals(password)){
                return true;
            }
        }
        return false;
    }

    private JSONObject ObtenerJSon(String JSonDatos, String correo) throws JSONException{
        JSONArray datos = new JSONArray(JSonDatos);

        for(int i = 0; i < datos.length(); i++){
            JSONObject elemento = datos.getJSONObject(i);
            if(elemento.getString("correo").equals(correo)){
                return elemento;
            }
        }
        return null;
    }

    private String ObtenerDatoJSon(JSONObject JSonDatos, String nombreDato) throws JSONException {
        return JSonDatos.getString(nombreDato);
    }

    protected void LlamarMainActivity(JSONObject result) throws JSONException {
        //Vaciar edittexts
        EditText ETCorreo = (EditText)findViewById(R.id.txtCorreo);
        EditText ETContrasenna = (EditText)findViewById(R.id.txtContrasenna);

        ETCorreo.setText("");
        ETContrasenna.setText("");

        //Abrir ventana principal
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("id",ObtenerDatoJSon(result,"id"));
        i.putExtra("correo",ObtenerDatoJSon(result,"correo"));
        i.putExtra("nick",ObtenerDatoJSon(result,"nick"));
        i.putExtra("tipoUsuario",ObtenerDatoJSon(result,"tipo_cuenta"));
        startActivity(i);
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
