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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;


public class fRecomendaciones extends Fragment {
    // Variables
    private View rootView;
    private TextView NoPeliculas;
    ListView ListaPeliculas;

    static String Nick,tipoUsuario, ARG_ID;

    CustomListPeliculas adapter;

    public fRecomendaciones() {
        // Required empty public constructor
    }

    public static fRecomendaciones newInstance(String nick, String TipoUsuario, String id) {
        fRecomendaciones fragment = new fRecomendaciones();
        Nick=nick;
        tipoUsuario=TipoUsuario;
        ARG_ID = id;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recomendaciones, container, false);

        NoPeliculas = (TextView) rootView.findViewById(R.id.labelNoRecomendaciones);
        ListaPeliculas=(ListView) rootView.findViewById(R.id.listRecomendaciones);

        Actualizar_Peliculas();

        return rootView;
    }

    private void Actualizar_Peliculas() {

            Conexion user_extendeds = new Conexion();
            Conexion favorite_movies = new Conexion();
            try {
                String result = user_extendeds.execute("https://cines35mm.herokuapp.com/movies.json", "GET").get();
                String fmovies = favorite_movies.execute("https://cines35mm.herokuapp.com/favorite_movies.json", "GET").get();
                ArrayList<String> lista_peliculas_usuario = new ArrayList<>();
                JSONArray favoritos = new JSONArray(fmovies);
                JSONArray datos = new JSONArray(result);

                //saca los favoritos del usuario
                for (int i = 0; i < favoritos.length(); i++) {
                    JSONObject elemento = favoritos.getJSONObject(i);
                    if (elemento.getString("user_id").equals(ARG_ID)) {
                        lista_peliculas_usuario.add(elemento.getString("movie_id"));
                    }
                }

                //de los favoritos saca los "objetos" tipo película
                JSONArray datos_favoritos = new JSONArray();
                for (int i = 0; i < datos.length(); i++) {
                    JSONObject elemento = datos.getJSONObject(i);
                    for(int j=0; j<lista_peliculas_usuario.size(); j++) {
                        if (elemento.getString("id").equals(lista_peliculas_usuario.get(j))) {
                            datos_favoritos.put(elemento);
                        }
                    }
                }

                //comienza a sacar las recomendaciones
                TreeMap<Integer, Integer> recomendadas_usuario = new TreeMap<>();

                JSONObject favorito, pelicula;
                for (int i = 0; i < datos_favoritos.length(); i++) {
                    favorito = datos_favoritos.getJSONObject(i);

                    String movie_id = favorito.getString("id");
                    String director = favorito.getString("director");
                    String genero = favorito.getString("genero");
                    String[] tags = (favorito.getString("tag")).split(", ");

                    int prioridad;
                    for(int j=0; j<datos.length(); j++){
                        prioridad = 0;
                        pelicula = datos.getJSONObject(j);
                        if(!movie_id.equals(pelicula.getString("id"))){
                            if(director.equals(pelicula.getString("director"))){
                                prioridad++;
                            }
                            if(genero.equals(pelicula.getString("genero"))){
                                prioridad++;
                            }

                            String[] tags2 = (pelicula.getString("tag")).split(", ");
                            prioridad += compararTags(tags, tags2);
                        }

                        if(prioridad > 0 && !recomendadas_usuario.containsKey(j)){
                            recomendadas_usuario.put(j, prioridad);
                        }
                    }
                }

                if (recomendadas_usuario != null) {
                    if (!recomendadas_usuario.isEmpty()) {
                        NoPeliculas.setVisibility(View.INVISIBLE);

                        //ordenar por valor
                        Object[] ordenados = recomendadas_usuario.entrySet().toArray();
                        Arrays.sort(ordenados, new Comparator() {
                            public int compare(Object o1, Object o2) {
                                return ((Map.Entry<Integer, Integer>) o2).getValue()
                                        .compareTo(((Map.Entry<Integer, Integer>) o1).getValue());
                            }
                        });


                        List<String> nombres = new ArrayList<>();
                        List<String> generos = new ArrayList<>();
                        List<String> directores = new ArrayList<>();
                        List<String> annos = new ArrayList<>();
                        List<String> actores = new ArrayList<>();
                        List<String> sinopsis = new ArrayList<>();
                        List<String> calificacion = new ArrayList<>();
                        List<String> portadas = new ArrayList<>();
                        List<String> id_pelicula = new ArrayList<>();

                        JSONObject elemento;
                        //se agregan ya ordenados por el valor
                        for (Object a : ordenados) {
                            int key = ((Map.Entry<Integer, Integer>) a).getKey();
                            elemento = datos.getJSONObject(key);

                            nombres.add(elemento.getString("nombre"));
                            generos.add(elemento.getString("genero"));
                            directores.add(elemento.getString("director"));
                            annos.add(elemento.getString("anho_estreno"));
                            actores.add(elemento.getString("actores_principales"));
                            sinopsis.add(elemento.getString("sinopsis"));
                            portadas.add(elemento.getString("url_imagen"));
                            id_pelicula.add(elemento.getString("id"));
                        }

                        String[] Nombre = nombres.toArray(new String[0]);
                        String[] Genero = generos.toArray(new String[0]);
                        String[] Director = directores.toArray(new String[0]);
                        String[] Anno = annos.toArray(new String[0]);
                        String[] Actores = actores.toArray(new String[0]);
                        String[] Sipnosis = sinopsis.toArray(new String[0]);
                        String[] Calificacion = {null, null};
                        String[] ImgPortada = portadas.toArray(new String[0]);
                        String[] IdPelicula = id_pelicula.toArray(new String[0]);

                        adapter = new CustomListPeliculas(this.getActivity(), Nombre, ImgPortada, Genero, Director, Anno, Sipnosis, Actores, Calificacion, Nick, tipoUsuario,ARG_ID,IdPelicula);

                        if (adapter == null) {
                            NoPeliculas.setVisibility(View.VISIBLE);
                        } else {
                            NoPeliculas.setVisibility(View.INVISIBLE);
                            ListaPeliculas.setAdapter(adapter);
                        }
                    }
                    else
                        NoPeliculas.setVisibility(View.VISIBLE);
                }
                else
                    NoPeliculas.setVisibility(View.VISIBLE);


            }catch (InterruptedException e) {
                Toast.makeText(this.getContext(),e.toString(),Toast.LENGTH_SHORT).show();
            } catch (ExecutionException e) {
                Toast.makeText(this.getContext(),e.toString(),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Toast.makeText(this.getContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }


    }

    private int compararTags(String[] arr1, String[] arr2){
        int coincidencias = 0;
        for(int i = 0, j = 0;i < arr1.length && j < arr2.length;){
            int res = arr1[i].compareTo(arr2[j]);
            if(res == 0){
                coincidencias++;
                i++;
                j++;
            }else if(res < 0){
                i++;
            }else{
                j++;
            }
        }
        return coincidencias;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
