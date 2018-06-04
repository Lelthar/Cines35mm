package tec.fabian.cines35mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class fComentarios extends Fragment {
    // Variables
    private View rootView;
    String id_usuario, id_pelicula;
    EditText Comentario;
    TextView labelNoComentarios;
    ListView listaComentarios;
    JSONArray TodosComentarios;
    JSONArray TodosUsuarios;

    public fComentarios() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_comentarios, container, false);

        if (getArguments() != null) {
            id_usuario=getArguments().getString("id_usuario");
            id_pelicula=getArguments().getString("id_pelicula");
        }

        ImageView btnAgregarComentario=rootView.findViewById(R.id.btnAgregarComentario);
        Comentario=rootView.findViewById(R.id.txtComentario);
        labelNoComentarios=rootView.findViewById(R.id.labelNoComentarios);
        listaComentarios=rootView.findViewById(R.id.listComentarios);

        btnAgregarComentario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    AgregarComentario();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ActualizarComentarios();

        return rootView;
    }



    private void Actualizar_Datos() {
        try {
            Conexion comentariosConexion = new Conexion();
            String result = comentariosConexion.execute("https://cines35mm.herokuapp.com/commentaries.json", "GET").get();
            TodosComentarios = new JSONArray(result);

            Conexion user_extendeds = new Conexion();
            String result1 = user_extendeds.execute("https://cines35mm.herokuapp.com/users.json", "GET").get();
            TodosUsuarios = new JSONArray(result1);
        } catch (InterruptedException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ActualizarComentarios() {
        Actualizar_Datos();

        JSONArray datosComentarios = TodosComentarios;
        JSONArray datosUsuarios = TodosUsuarios;

        JSONArray comentariosFiltrados = new JSONArray();

        try {
            if (datosComentarios !=null) {
                if (datosComentarios.length() > 0) {

                    for (int i = 0; i < datosComentarios.length(); i++) {
                        JSONObject elemento = datosComentarios.getJSONObject(i);
                        if (elemento.getString("movie_id").equals(id_pelicula)) {
                            comentariosFiltrados.put(elemento);
                        }
                    }

                    if (comentariosFiltrados != null) {
                        if (comentariosFiltrados.length() > 0) {
                            labelNoComentarios.setVisibility(View.INVISIBLE);

                            List<String> nicks = new ArrayList<>();
                            List<String> comentarios = new ArrayList<>();

                            JSONObject elemento;
                            for (int i = 0; i < comentariosFiltrados.length(); i++) {
                                elemento = comentariosFiltrados.getJSONObject(i);

                                JSONObject usuario;
                                for (int k = 0; k < datosUsuarios.length(); k++) {
                                    usuario = datosUsuarios.getJSONObject(k);
                                    if (elemento.get("user_id").equals(usuario.get("id")))
                                        nicks.add(usuario.getString("nick"));
                                }

                                comentarios.add(elemento.getString("comentario"));
                            }


                            String[] Nicks = nicks.toArray(new String[0]);
                            String[] Comentarios = comentarios.toArray(new String[0]);


                            CustomListComentarios customListComentarios = new CustomListComentarios(getActivity(), Comentarios, Nicks);

                            listaComentarios.setAdapter(customListComentarios);
                        } else
                            labelNoComentarios.setVisibility(View.VISIBLE);
                    } else
                        labelNoComentarios.setVisibility(View.VISIBLE);
                } else
                    labelNoComentarios.setVisibility(View.VISIBLE);
            } else
                labelNoComentarios.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            Toast.makeText(this.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void AgregarComentario() throws JSONException, ExecutionException, InterruptedException {
        EditText comentario = rootView.findViewById(R.id.txtComentario);
        String strComentario = comentario.getText().toString();

        if (!strComentario.isEmpty()) {
            Conexion conexion = new Conexion();

            JSONObject json_parametros = new JSONObject();
            json_parametros.put("user_id", id_usuario);
            json_parametros.put("comentario", strComentario);
            json_parametros.put("movie_id", id_pelicula);
            String result = conexion.execute("https://cines35mm.herokuapp.com/commentaries", "POST", json_parametros.toString()).get();

            if(result.equals("Created")) {
                Toast.makeText(rootView.getContext(), "Se publicó exitosamente el comentario.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(rootView.getContext(), "Ocurrió un error inesperado."+result.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(rootView.getContext(), json_parametros.toString(), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(rootView.getContext(), "El comentario no puede estar vacío", Toast.LENGTH_LONG).show();
        }
    }
}
