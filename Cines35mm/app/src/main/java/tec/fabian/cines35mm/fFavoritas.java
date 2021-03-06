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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class fFavoritas extends Fragment {
    // Variables
    private View rootView;
    private static final String ARG_ID_FAV = "ID";
    private String id_usuario;
    private ListView listView;
    TextView labelNoFavoritas;

    static String Nick, tipoUsuario;

    public fFavoritas() {
        // Required empty public constructor
    }

    public static fFavoritas newInstance(String TipoUsuario,String pIdUsuario) {
        fFavoritas fragment = new fFavoritas();

        Bundle args = new Bundle();
        args.putString(ARG_ID_FAV, pIdUsuario);
        fragment.setArguments(args);

        tipoUsuario=TipoUsuario;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recomendaciones, container, false);

        labelNoFavoritas = (TextView) rootView.findViewById(R.id.labelNoRecomendaciones);
        labelNoFavoritas.setText("No se han seleccionado películas favoritas");
        listView = rootView.findViewById(R.id.listRecomendaciones);
        id_usuario = this.getArguments().getString(ARG_ID_FAV);
        labelNoFavoritas = (TextView) rootView.findViewById(R.id.labelNoRecomendaciones);

        Actualizar_Peliculas();
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void Actualizar_Peliculas() {
        Conexion user_extendeds = new Conexion();
        Conexion favorite_movies = new Conexion();
        try {
            String result = user_extendeds.execute("https://cines35mm.herokuapp.com/movies.json", "GET").get();
            String fmovies = favorite_movies.execute("https://cines35mm.herokuapp.com/favorite_movies.json", "GET").get();
            JSONArray datos_favoritos = new JSONArray(fmovies);
            ArrayList<String> lista_peliculas_usuario = new ArrayList<>();

            Conexion cali = new Conexion();
            String result1 = cali.execute("https://cines35mm.herokuapp.com/rating_movies.json", "GET").get();
            JSONArray calis = new JSONArray(result1);

            for (int i = 0; i < datos_favoritos.length(); i++) {
                JSONObject elemento = datos_favoritos.getJSONObject(i);
                if (elemento.getString("user_id").equals(id_usuario)) {
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
            List<String> id_pelicula_list = new ArrayList<>();



            JSONObject elemento;
            for (int i = 0; i < datos.length(); i++) {
                elemento = datos.getJSONObject(i);
                if (lista_peliculas_usuario.contains(elemento.getString("id"))) {
                    nombres.add(elemento.getString("nombre"));
                    generos.add(elemento.getString("genero"));
                    directores.add(elemento.getString("director"));
                    annos.add(elemento.getString("anho_estreno"));
                    actores.add(elemento.getString("actores_principales"));
                    sinopsis.add(elemento.getString("sinopsis"));
                    portadas.add(elemento.getString("url_imagen"));
                    id_pelicula_list.add(elemento.getString("id"));
                    JSONObject elemento1;
                    int calificacion1=0;
                    int cont = 0;
                    for (int k = 0; k < calis.length(); k++) {
                        elemento1 = calis.getJSONObject(k);

                        if(elemento.getString("id").equals(elemento1.getString("movie_id"))) {
                            calificacion1 = calificacion1 + elemento1.getInt("calificacion");
                            cont=cont+1;
                        }

                    }

                    if(cont!=0)
                        calificacion.add(Integer.toString((calificacion1/cont)));
                    else
                        calificacion.add("-");
                }
            }

            if (nombres!=null) {
                if (!nombres.isEmpty()) {
                    labelNoFavoritas.setVisibility(View.INVISIBLE);

                    String[] Nombre = nombres.toArray(new String[0]);
                    String[] Genero = generos.toArray(new String[0]);
                    String[] Director = directores.toArray(new String[0]);
                    String[] Anno = annos.toArray(new String[0]);
                    String[] Actores = actores.toArray(new String[0]);
                    String[] Sipnosis = sinopsis.toArray(new String[0]);
                    String[] Calificacion = calificacion.toArray(new String[0]);
                    String[] ImgPortada = portadas.toArray(new String[0]);
                    String[] IdPelicula = id_pelicula_list.toArray(new String[0]);


                    CustomListPeliculas adapter = new CustomListPeliculas(this.getActivity(), Nombre, ImgPortada, Genero, Director, Anno, Sipnosis, Actores, Calificacion, Nick, tipoUsuario,id_usuario,IdPelicula);

                    if (adapter != null) {
                        listView.setAdapter(adapter);
                        labelNoFavoritas.setVisibility(View.INVISIBLE);
                    } else {
                        labelNoFavoritas.setVisibility(View.VISIBLE);
                    }
                } else
                    labelNoFavoritas.setVisibility(View.VISIBLE);
            }
            else
                labelNoFavoritas.setVisibility(View.VISIBLE);


        } catch (InterruptedException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }


    }
}
