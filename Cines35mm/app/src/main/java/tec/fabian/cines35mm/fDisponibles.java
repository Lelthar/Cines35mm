package tec.fabian.cines35mm;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

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
    TextView NoPeliculasDisponibles;
    JSONArray TodasPeliculas;

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
        NoPeliculasDisponibles=rootView.findViewById(R.id.labelNoDisponibles);
        ListaPeliculas = rootView.findViewById(R.id.listDisponibles);
        final EditText Busqueda=rootView.findViewById(R.id.txtBusqueda);
        Busqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {            }

            @Override
            public void afterTextChanged(Editable s) {
                Actualizar_Peliculas(Busqueda.getText().toString().trim());
            }
        });
        Actualizar_Datos();
        Actualizar_Peliculas("Todo");
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void Actualizar_Datos() {
        try {
            Conexion user_extendeds = new Conexion();
            String result = user_extendeds.execute("https://cines35mm.herokuapp.com/movies.json", "GET").get();
            TodasPeliculas = new JSONArray(result);
        } catch (InterruptedException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Actualizar_Peliculas(String Buscar){
        try {
            if(!Buscar.equals("Todo"))
                Actualizar_Datos();

            JSONArray datos = TodasPeliculas;

            if(!Buscar.equals("Todo"))
                datos=BuscarPeliculas(Buscar,datos);

            if (datos==null) {
                NoPeliculasDisponibles.setVisibility(View.VISIBLE);
            } else {
                NoPeliculasDisponibles.setVisibility(View.INVISIBLE);
                List<String> nombres = new ArrayList<>();
                List<String> generos = new ArrayList<>();
                List<String> directores = new ArrayList<>();
                List<String> annos = new ArrayList<>();
                List<String> actores = new ArrayList<>();
                List<String> sinopsis = new ArrayList<>();
                List<String> calificacion = new ArrayList<>();
                List<String> portadas = new ArrayList<>();

                JSONObject elemento;
                for (int i = 0; i < datos.length(); i++) {
                    elemento = datos.getJSONObject(i);

                    nombres.add(elemento.getString("nombre"));
                    generos.add(elemento.getString("genero"));
                    directores.add(elemento.getString("director"));
                    annos.add(elemento.getString("anho_estreno"));
                    actores.add(elemento.getString("actores_principales"));
                    sinopsis.add(elemento.getString("sinopsis"));
                    portadas.add(elemento.getString("url_imagen"));
                }

                String[] Nombre = nombres.toArray(new String[0]);
                String[] Genero = generos.toArray(new String[0]);
                String[] Director = directores.toArray(new String[0]);
                String[] Anno = annos.toArray(new String[0]);
                String[] Actores = actores.toArray(new String[0]);
                ;
                String[] Sipnosis = sinopsis.toArray(new String[0]);
                ;
                String[] Calificacion = {null, null};
                String[] ImgPortada = portadas.toArray(new String[0]);

                CustomListPeliculas adapter = new CustomListPeliculas(this.getActivity(), Nombre, ImgPortada, Genero, Director, Anno, Sipnosis, Actores, Calificacion, null);

                if (adapter != null) {
                    ListaPeliculas.setAdapter(adapter);
                    TextView NoPeliculas = (TextView) rootView.findViewById(R.id.labelNoDisponibles);
                    NoPeliculas.setVisibility(View.INVISIBLE);
                } else {
                    TextView NoPeliculas = (TextView) rootView.findViewById(R.id.labelNoDisponibles);
                    NoPeliculas.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            Toast.makeText(this.getContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private JSONArray BuscarPeliculas(String Buscar, JSONArray datos) throws JSONException {
        JSONArray datosFiltrados=new JSONArray();
        JSONObject elemento;

        for (int i = 0; i < datos.length(); i++) {
            elemento = datos.getJSONObject(i);
            if(elemento.getString("nombre").toLowerCase().contains(Buscar.toLowerCase()))
                datosFiltrados.put(elemento);
        }

        return datosFiltrados;
    }
}
