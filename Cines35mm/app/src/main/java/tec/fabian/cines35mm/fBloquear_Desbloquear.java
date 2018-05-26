package tec.fabian.cines35mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class fBloquear_Desbloquear extends Fragment {

    //Variables
    View rootView;
    private Button boton_bloquear;
    private Button boton_desbloquear;
    private Button boton_buscar;
    private EditText text_busqueda;
    private Conexion conexion;
    private TextView texto_correo;
    private TextView texto_nickname;

    public fBloquear_Desbloquear() {
        // Required empty public constructor
    }

    public static fBloquear_Desbloquear newInstance() {
        fBloquear_Desbloquear fragment = new fBloquear_Desbloquear();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_bloquear__desbloquear, container, false);
        Toast.makeText(rootView.getContext(), "------ WIP Bloquear/Desbloquear ------", Toast.LENGTH_SHORT).show();
        //TODO metodo para buscar usuario
        //TODO mostrar el boton de desbloquear o bloquear segun el usuario encontrado
        //TODO metodo para bloquear/desbloquear usuario
        //Se inicializan los objetos de la interfaz para acceder a ellos
        boton_bloquear = rootView.findViewById(R.id.btnBloquear);
        boton_desbloquear = rootView.findViewById(R.id.btnDesbloquear);
        boton_buscar = rootView.findViewById(R.id.btnBuscarUsuario);
        text_busqueda = rootView.findViewById(R.id.txtNickUsuario);

        texto_correo = rootView.findViewById(R.id.lblCorreo);
        texto_nickname = rootView.findViewById(R.id.lblNick);

        //Se agregan los metodos a los botones
        boton_bloquear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeAvailable(true);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        boton_desbloquear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeAvailable(false);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        boton_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    buscar_usuario();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    public void onDetach() {
        super.onDetach();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void buscar_usuario() throws ExecutionException, InterruptedException, JSONException {
        String nickname = text_busqueda.getText().toString().replaceAll("\\s","");
        conexion = new Conexion();
        String jsonResult = "";
        jsonResult = conexion.execute("https://cines35mm.herokuapp.com/users.json","GET").get();

        if(UserExists(jsonResult,nickname)){//Revisa que el usuario exista
            JSONObject jsonUsuario = obtenerJsonObject(jsonResult,nickname);

            if(isEmailValid(nickname)){ //Revisa si es correo o no con lo que realiza la busqueda
                if(UserAvailable(jsonUsuario)){
                    boton_bloquear.setVisibility(View.VISIBLE);
                }else{
                    boton_desbloquear.setVisibility(View.VISIBLE);
                }
                texto_correo.setText(nickname);
            }else{
                if(UserAvailable(jsonUsuario)){
                    boton_bloquear.setVisibility(View.VISIBLE);
                }else{
                    boton_desbloquear.setVisibility(View.VISIBLE);
                }
                texto_nickname.setText(nickname);
            }
        }else{
            //Mostrar mensaje de que el usuario no existe
        }

    }

    //Este metodo revisa si el usuario existe o no
    private boolean UserExists(String jsonDatos,String nickname) throws JSONException {
        JSONArray datos = new JSONArray(jsonDatos);

        for(int i = 0; i < datos.length(); i++){
            JSONObject elemento = datos.getJSONObject(i);
            if(elemento.getString("correo").equals(nickname) || elemento.getString("nick").equals(nickname)){
                return true;
            }
        }
        return false;
    }

    private boolean UserAvailable(JSONObject jsonDatos) throws JSONException {
        return jsonDatos.getBoolean("disponible");
    }

    private JSONObject obtenerJsonObject(String jsonDatos, String nickname) throws JSONException {
        JSONObject elemento = null;
        JSONArray datos = new JSONArray(jsonDatos);
        for(int i = 0; i < datos.length(); i++){
            elemento = datos.getJSONObject(i);
            if(elemento.getString("correo").equals(nickname) || elemento.getString("nick").equals(nickname)){
                break;
            }
        }
        return elemento;
    }

    private void changeAvailable(boolean tipo) throws ExecutionException, InterruptedException, JSONException {
        if(tipo){
            String nickname = text_busqueda.getText().toString();
            conexion = new Conexion();
            String jsonResult = "";
            jsonResult = conexion.execute("https://cines35mm.herokuapp.com/users.json","GET").get();

            JSONObject jsonUsuario = obtenerJsonObject(jsonResult,nickname);
            jsonUsuario.remove("disponible");
            jsonUsuario.put("disponible",false);
            Conexion modificar_usuario = new Conexion();
            String  result = modificar_usuario.execute("https://cines35mm.herokuapp.com/users/"+jsonUsuario.getString("id"),"PATCH",jsonUsuario.toString()).get();

            if(result.equals("OK")) {
                Toast.makeText(rootView.getContext(), "Se bloqueo el usuario.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(rootView.getContext(), "Ocurrió un error inesperado."+result.toString(), Toast.LENGTH_LONG).show();
            }
            boton_bloquear.setVisibility(View.INVISIBLE);
            texto_nickname.setText("");
            texto_correo.setText("");
        }else{
            String nickname = text_busqueda.getText().toString();
            conexion = new Conexion();
            String jsonResult = "";
            jsonResult = conexion.execute("https://cines35mm.herokuapp.com/users.json","GET").get();

            JSONObject jsonUsuario = obtenerJsonObject(jsonResult,nickname);
            jsonUsuario.remove("disponible");
            jsonUsuario.put("disponible",true);

            Conexion modificar_usuario = new Conexion();
            String  result = modificar_usuario.execute("https://cines35mm.herokuapp.com/users/"+jsonUsuario.getString("id"),"PATCH",jsonUsuario.toString()).get();

            if(result.equals("OK")) {
                Toast.makeText(rootView.getContext(), "Se desbloqueo el usuario.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(rootView.getContext(), "Ocurrió un error inesperado."+result.toString(), Toast.LENGTH_LONG).show();
            }
            boton_desbloquear.setVisibility(View.INVISIBLE);
            texto_nickname.setText("");
            texto_correo.setText("");
        }
    }
}
