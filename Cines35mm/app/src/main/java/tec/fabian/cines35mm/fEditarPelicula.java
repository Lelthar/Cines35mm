package tec.fabian.cines35mm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class fEditarPelicula extends Fragment {

    // Variables
    private View rootView;
    private ImageView portada_pelicula;
    private EditText nombre_pelicula;
    private EditText director_pelicula;
    private EditText generos_pelicula;
    private EditText anho_estreno;
    private EditText actores_principales;
    private EditText sinopsis;
    private Conexion conexion;
    private Button btnAgregarPelicula;


    public fEditarPelicula() {
        // Required empty public constructor

    }

    public static fEditarPelicula newInstance() {
        fEditarPelicula fragment = new fEditarPelicula();
        //TODO recibir id pelicula o datos pelicula
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_agregar_pelicula, container, false);

        Toast.makeText(rootView.getContext(),"-- WIP Agregar Pelicula --",Toast.LENGTH_SHORT).show();
        //Se inicializan las variables para los objetos de la interfaz
        portada_pelicula=rootView.findViewById(R.id.imgPortada);
        nombre_pelicula = rootView.findViewById(R.id.txtNombre);
        director_pelicula = rootView.findViewById(R.id.txtDirector);
        generos_pelicula = rootView.findViewById(R.id.txtGenero);
        anho_estreno = rootView.findViewById(R.id.txtAnnoEstreno);
        actores_principales = rootView.findViewById(R.id.txtActoresPrincipales);
        sinopsis = rootView.findViewById(R.id.txtSipnosis);
        btnAgregarPelicula = rootView.findViewById(R.id.btnAgregarPelicula);
        btnAgregarPelicula.setText("Guardar Cambios");

        //TODO Recibir y mostrar datos de la pelicula

        btnAgregarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // try {
                    //TODO Actualizar datos pelicula
               /* } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });
        return rootView;
    }



    public void onDetach() {
        super.onDetach();
    }
}
