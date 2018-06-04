package tec.fabian.cines35mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class fPerfil extends Fragment {

    ///Variables
    private static final String ARG_NICK = "NICK";
    private static final String ARG_CORREO = "CORREO";
    View rootView;
    private String nickname;
    private String correo;
    private EditText textoNick;
    private EditText textoCorreo;
    private EditText textoPassword;
    private EditText textoConfirm_password;
    private Button boton_editar_informacion;
    private Button boton_editar_contrasena;
    private Conexion conexion;

    public fPerfil() {
        // Required empty public constructor
    }

    public static fPerfil newInstance(String nick, String correo) {
        fPerfil fragment = new fPerfil();
        Bundle args = new Bundle();
        args.putString(ARG_NICK, nick);
        args.putString(ARG_CORREO, correo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_perfil, container, false);

        //Se agregan los objetos de la interfaz
        nickname = this.getArguments().getString(ARG_NICK);
        correo = this.getArguments().getString(ARG_CORREO);

        textoNick = rootView.findViewById(R.id.txtCorreo);
        textoCorreo = rootView.findViewById(R.id.txtNick);
        textoPassword = rootView.findViewById(R.id.txtContrasenna);
        textoConfirm_password = rootView.findViewById(R.id.txtConfirmarContrasenna);
        boton_editar_informacion = rootView.findViewById(R.id.btnEditarInformacion);
        boton_editar_contrasena = rootView.findViewById(R.id.btnCambiarContrasenna);

        boton_editar_informacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    modificarUsuario();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        boton_editar_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    modificarUsuarioPassword();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        agregarTextoCasillas();

        return rootView;
    }

    public void onDetach() {
        super.onDetach();
    }

    private JSONObject obtenerJsonObject(String jsonDatos, String nickname) throws JSONException {
        JSONObject elemento = null;
        JSONArray datos = new JSONArray(jsonDatos);
        for(int i = 0; i < datos.length(); i++){
            elemento = datos.getJSONObject(i);
            if(elemento.getString("nick").equals(nickname)){
                break;
            }
        }
        return elemento;
    }

    private void modificarUsuario() throws ExecutionException, InterruptedException, JSONException {
        String strCorreo = textoCorreo.getText().toString();
        String strNickName = textoNick.getText().toString();

        if(!strCorreo.isEmpty() && !strNickName.isEmpty() && isEmailValid(strCorreo)){
            conexion = new Conexion();
            String jsonResult = "";
            jsonResult = conexion.execute("https://cines35mm.herokuapp.com/users.json","GET").get();
            JSONObject jsonUser = obtenerJsonObject(jsonResult,nickname);
            jsonUser.remove("correo");
            jsonUser.put("correo",strCorreo);

            jsonUser.remove("nick");
            jsonUser.put("nick",strNickName);
            conexion = new Conexion();

            String  result = conexion.execute("https://cines35mm.herokuapp.com/users/"+jsonUser.getString("id"),"PATCH",jsonUser.toString()).get();

            if(result.equals("OK")) {
                Toast.makeText(rootView.getContext(), "Se modificó exitosamente los datos del usuario.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(rootView.getContext(), "Ocurrió un error inesperado."+result.toString(), Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(rootView.getContext(), "Error en los datos.", Toast.LENGTH_LONG).show();
            //Mensaje de contraseñas diferentes
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void modificarUsuarioPassword() throws ExecutionException, InterruptedException, JSONException {
        String strNickName = nickname;
        String strContrasena = textoPassword.getText().toString();
        String strConfirmarContrasena = textoConfirm_password.getText().toString();

        if(strContrasena.equals(strConfirmarContrasena) && !strContrasena.isEmpty()){
            conexion = new Conexion();
            String jsonResult = "";
            jsonResult = conexion.execute("https://cines35mm.herokuapp.com/users.json","GET").get();
            JSONObject jsonUser = obtenerJsonObject(jsonResult,strNickName);
            jsonUser.remove("contrasenha");
            jsonUser.put("contrasenha",strContrasena);
            conexion = new Conexion();

            String  result = conexion.execute("https://cines35mm.herokuapp.com/users/"+jsonUser.getString("id"),"PATCH",jsonUser.toString()).get();

            if(result.equals("OK")) {
                Toast.makeText(rootView.getContext(), "Se modificó exitosamente la contraseña.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(rootView.getContext(), "Ocurrió un error inesperado."+result.toString(), Toast.LENGTH_LONG).show();
            }
            textoConfirm_password.setText("");
            textoPassword.setText("");
        }else{
            Toast.makeText(rootView.getContext(), "Error en los datos.", Toast.LENGTH_LONG).show();
            //Mensaje de contraseñas diferentes
        }

    }

    private void agregarTextoCasillas(){
        textoCorreo.setText(correo);
        textoNick.setText(nickname);
    }

}
