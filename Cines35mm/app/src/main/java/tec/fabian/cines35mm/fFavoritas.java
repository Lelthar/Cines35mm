package tec.fabian.cines35mm;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class fFavoritas extends Fragment {
    // Variables
    private View rootView;
    private static final String ARG_ID = "ID";
    private String id_usuario;
    private ListView listView;
    public fFavoritas() {
        // Required empty public constructor
    }

    public static fFavoritas newInstance(String user_id) {
        fFavoritas fragment = new fFavoritas();
        //TODO recibir correo para buscar las peliculas favoritas
        Bundle args = new Bundle();
        args.putString(ARG_ID, user_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recomendaciones, container, false);
        Toast.makeText(rootView.getContext(),"-- WIP Favoritas --",Toast.LENGTH_SHORT).show();

        //TextView labelNoFavoritas=(TextView) rootView.findViewById(R.id.labelNoRecomendaciones);
        //labelNoFavoritas.setText("No se han seleccionado películas favoritas");
        listView = rootView.findViewById(R.id.listRecomendaciones);
        id_usuario = this.getArguments().getString(ARG_ID);
        //TODO crear metodo para recuperar peliculas favoritas
        Actualizar_Peliculas();
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void Actualizar_Peliculas(){
        //TODO cambiar este Custom List por los datos recuperados de la BD

        Conexion user_extendeds = new Conexion();
        Conexion favorite_movies = new Conexion();
        try {
            String result = user_extendeds.execute("https://cines35mm.herokuapp.com/movies.json","GET").get();
            String fmovies = favorite_movies.execute("https://cines35mm.herokuapp.com/favorite_movies.json","GET").get();
            JSONArray datos_favoritos = new JSONArray(fmovies);
            ArrayList<String> lista_peliculas_usuario = new ArrayList<>();

            for(int i = 0; i < datos_favoritos.length(); i++){
                JSONObject elemento = datos_favoritos.getJSONObject(i);
                if(elemento.getString("user_id").equals(id_usuario)){
                    lista_peliculas_usuario.add(elemento.getString("movie_id"));
                }
            }

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
                if(lista_peliculas_usuario.contains(elemento.getString("id"))){
                    nombres.add(elemento.getString("nombre"));
                    generos.add(elemento.getString("genero"));
                    directores.add(elemento.getString("director"));
                    annos.add(elemento.getString("anho_estreno"));
                    actores.add(elemento.getString("actores_principales"));
                    sinopsis.add(elemento.getString("sinopsis"));
                    portadas.add(elemento.getString("url_imagen"));
                }
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
            if(adapter != null){
                listView.setAdapter(adapter);
                //TextView NoPeliculas = (TextView) rootView.findViewById(R.id.labelNoDisponibles);
                //NoPeliculas.setVisibility(View.INVISIBLE);
            }else{
                Toast.makeText(this.getContext(),"ERROR.",Toast.LENGTH_SHORT).show();
            }


        } catch (InterruptedException e) {
            Toast.makeText(this.getContext(),e.toString(),Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this.getContext(),e.toString(),Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this.getContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }


    }

}
