package tec.fabian.cines35mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class fAgregarPelicula extends Fragment {

    // Variables
    private View rootView;
    private EditText nombre_pelicula;
    private EditText director_pelicula;
    private EditText generos_pelicula;
    private EditText anho_estreno;
    private EditText actores_principales;
    private EditText sinopsis;
    private Conexion conexion;
    private Button btnAgregarPelicula;

    public fAgregarPelicula() {
        // Required empty public constructor

    }

    public static fAgregarPelicula newInstance() {
        fAgregarPelicula fragment = new fAgregarPelicula();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_agregar_pelicula, container, false);

        Toast.makeText(rootView.getContext(),"-- WIP Agregar Pelicula --",Toast.LENGTH_SHORT).show();
        //Se inicializan las variables para los objetos de la interfaz
        nombre_pelicula = rootView.findViewById(R.id.txtNombre);
        director_pelicula = rootView.findViewById(R.id.txtDirector);
        generos_pelicula = rootView.findViewById(R.id.txtGenero);
        anho_estreno = rootView.findViewById(R.id.txtAnnoEstreno);
        actores_principales = rootView.findViewById(R.id.txtActoresPrincipales);
        sinopsis = rootView.findViewById(R.id.txtSipnosis);
        btnAgregarPelicula = rootView.findViewById(R.id.btnAgregarPelicula);
        btnAgregarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onClickedAddMovie();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    //Este metodo agrega la pelicula a la base de datos
    public void onClickedAddMovie() throws JSONException, ExecutionException, InterruptedException {
        String strNombrePelicula = nombre_pelicula.getText().toString();
        String strDirectorPelicula = director_pelicula.getText().toString();
        String strGenerosPelicula = generos_pelicula.getText().toString();
        String strAnhoEstreno = anho_estreno.getText().toString();
        String strActoresPrincipales = actores_principales.getText().toString();
        String strSinopsis = sinopsis.getText().toString();

        //Revisa que los Edittext no estén vacios
        if(!strNombrePelicula.isEmpty() && !strDirectorPelicula.isEmpty() && !strGenerosPelicula.isEmpty() && !strAnhoEstreno.isEmpty() && !strActoresPrincipales.isEmpty() && !strSinopsis.isEmpty()){
            conexion = new Conexion();

            //Agrega los parametros al objeto JSON
            JSONObject json_parametros = new JSONObject();
            json_parametros.put("nombre",strNombrePelicula);
            json_parametros.put("director",strDirectorPelicula);
            json_parametros.put("genero",strGenerosPelicula);
            json_parametros.put("anho_estreno",strAnhoEstreno);
            json_parametros.put("actores_principales",strActoresPrincipales);
            json_parametros.put("sinopsis",strSinopsis);
            json_parametros.put("url_imagen","insertar url aqui");
            String  result = conexion.execute("https://cines35mm.herokuapp.com/movies","POST",json_parametros.toString()).get();

            if(result.equals("Created")) {
                Toast.makeText(rootView.getContext(), "Se creó exitosamente la cuenta.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(rootView.getContext(), "Ocurrió un error inesperado."+result.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(rootView.getContext(), json_parametros.toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(rootView.getContext(), "Una de las casillas está vacia.", Toast.LENGTH_LONG).show();
        }
    }

}
