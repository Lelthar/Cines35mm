package tec.fabian.cines35mm;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class fDisponibles extends Fragment {

    // Variables
    private View rootView;
    ListView ListaPeliculas;

    public fDisponibles() {
        // Required empty public constructor
    }

    public static fDisponibles newInstance() {
        fDisponibles fragment = new fDisponibles();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_disponibles, container, false);
        Toast.makeText(rootView.getContext(),"-- WIP Disponibles --",Toast.LENGTH_SHORT).show();

        //TODO metodo para mostrar las peliculas disponibles
        Actualizar_Peliculas();
        //TODO metodo para buscar peliculas disponibles
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void Actualizar_Peliculas(){
        //TODO cambiar este Custom List por los datos recuperados de la BD

        Conexion user_extendeds = new Conexion();
        try {
            String result = user_extendeds.execute("https://cines35mm.herokuapp.com/users.json","GET").get();

            JSONArray datos = new JSONArray(result);

            List<String> nombres = new ArrayList<>();
            List<String> generos = new ArrayList<>();
            List<String> directores = new ArrayList<>();
            List<String> annos = new ArrayList<>();
            List<String> actores = new ArrayList<>();
            List<String> sinopsis = new ArrayList<>();
            List<String> calificacion = new ArrayList<>();
            List<String> portadas = new ArrayList<>();

            JSONObject elemento;
            for(int i = 0; i < datos.length(); i++){
                elemento = datos.getJSONObject(i);

                nombres.add(elemento.getString("nombre"));
                generos.add(elemento.getString("genero"));
                directores.add(elemento.getString("director"));
                annos.add(elemento.getString("año_estreno"));
                actores.add(elemento.getString("actores_principales"));
                sinopsis.add(elemento.getString("sinopsis"));
                portadas.add(elemento.getString("url_imagen"));
            }

            String[] Nombre = nombres.toArray(new String[0]);
            String[] Genero = generos.toArray(new String[0]);
            String[] Director = directores.toArray(new String[0]);
            String[] Anno = annos.toArray(new String[0]);
            String[] Actores = actores.toArray(new String[0]);;
            String[] Sipnosis = sinopsis.toArray(new String[0]);;
            String[] Calificacion = {null,null};
            String[] ImgPortada = portadas.toArray(new String[0]);

            CustomListPeliculas adapter = new CustomListPeliculas(this.getActivity(),Nombre,ImgPortada,Genero,Director,Anno,Sipnosis,Actores,Calificacion,null);

            //TODO
            ListaPeliculas.setAdapter(adapter);
            TextView NoPeliculas = (TextView) rootView.findViewById(R.id.labelNoDisponibles);
            NoPeliculas.setVisibility(View.INVISIBLE);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
