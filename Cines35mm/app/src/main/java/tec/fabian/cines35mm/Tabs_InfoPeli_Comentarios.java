package tec.fabian.cines35mm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Tabs_InfoPeli_Comentarios extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    String Nick,tipoUsuario;
    String Nombre,Genero, Director,Anno,Sipnosis,Actores,Portada,Calificacion;
    private String id_usuario;
    private String id_pelicula;
    private Conexion conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_info_peli_comentarios);

        //Mostrar fecha para regresar a la pantalla anterior en la barra de titulo
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Se reciben las variables necesarias
        Intent i=getIntent();
        id_pelicula = i.getExtras().getString("id_pelicula");
        id_usuario = i.getExtras().getString("id_usuario");
        Nick = i.getExtras().getString("Nick");
        Nombre = i.getExtras().getString("Nombre");
        Genero = i.getExtras().getString("Genero");
        Director = i.getExtras().getString("Director");
        Anno = i.getExtras().getString("Anno");
        Sipnosis = i.getExtras().getString("Sipnosis");
        Actores = i.getExtras().getString("Actores");
        Portada = i.getExtras().getString("Portada");
        tipoUsuario=i.getExtras().getString("tipoUsuario");
        Calificacion=i.getExtras().getString("Calificacion");

        Bundle args1 = new Bundle();
        args1.putString("id_usuario", id_usuario);
        args1.putString("id_pelicula", id_pelicula);

        Bundle args2 = new Bundle();
        args2.putString("tipoUsuario", tipoUsuario);
        args2.putString("Nombre", Nombre);
        args2.putString("Genero", Genero);
        args2.putString("Director", Director);
        args2.putString("Anno", Anno);
        args2.putString("Sipnosis", Sipnosis);
        args2.putString("Actores", Actores);
        args2.putString("Portada", Portada);
        args2.putString("Calificacion", Calificacion);

        //Poner de título
        actionBar.setTitle(Nombre);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contenido_tab);

        mTabHost.addTab(mTabHost.newTabSpec("Información Película").setIndicator("Información Película"),
                fInformacionPelicula.class, args2);
        mTabHost.addTab(mTabHost.newTabSpec("Comentarios").setIndicator("Comentarios"),
                fComentarios.class, args1);



        //Cambiar de color los tabs
        mTabHost.getTabWidget().setBackgroundColor(Color.BLACK);
        mTabHost.getTabWidget().setDividerDrawable(R.mipmap.divider);
        for (int x = 0; x < mTabHost.getTabWidget().getChildCount(); x++) {
            View v=mTabHost.getTabWidget().getChildAt(x);
            final TextView tv = (TextView) v.findViewById(android.R.id.title);
            // Look for the title view to ensure this is an indicator and not a divider.(I didn't know, it would return divider too, so I was getting an NPE)
            if (tv == null)
                continue;
            else {
                tv.setTextColor(Color.WHITE);
            }
        }//*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.agregarFavoritas: {
                AgregarFavoritos();
                return true;
            }
            case R.id.agregarCalificacion: {
                final CharSequence calificaciones[] = new CharSequence[]{"1", "2", "3", "4", "5"};
                new AlertDialog.Builder(this)
                        .setItems(calificaciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // el usuario selecciona calificaciones[which]
                                try {
                                    CalificarPelicula(calificaciones[which]);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .show();//*/
                return true;
            }
            case R.id.editarPelicula:{
                Abrir_Editar_Pelicula();
                return true;
            }
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CalificarPelicula(CharSequence Calificacion)throws JSONException, ExecutionException, InterruptedException {
        int calificacion=Integer.parseInt(Calificacion.toString());

        Conexion conexion = new Conexion();

        JSONObject json_parametros = new JSONObject();
        json_parametros.put("user_id", id_usuario);
        json_parametros.put("calificacion", calificacion);
        json_parametros.put("movie_id", id_pelicula);
        String result = conexion.execute("https://cines35mm.herokuapp.com/rating_movies", "POST", json_parametros.toString()).get();

        if(result.equals("Created")) {
            Toast.makeText(this, "Se califico exitosamente la pelicula.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Ocurrió un error inesperado."+result.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, json_parametros.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void AgregarFavoritos(){
        //Datos de peliculas favoritas

        conexion = new Conexion();
        try {
            String result_fav = conexion.execute("https://cines35mm.herokuapp.com/favorite_movies.json","GET").get();
            JSONArray datos_favoritos = new JSONArray(result_fav);
            if(esFavorito(datos_favoritos)){
                String id_consulta = getIdMovie(datos_favoritos);
                Conexion modificar_favoritos = new Conexion();
                String resultado = modificar_favoritos.execute("https://cines35mm.herokuapp.com/favorite_movies/"+id_consulta,"DELETE").get();

                if(resultado.equals("OK")) {
                    Toast.makeText(this, "Se eliminó la pelicula de la lista de favoritos.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Ocurrió un error inesperado."+resultado.toString(), Toast.LENGTH_LONG).show();

                }
            }else{
                Conexion modificar_favoritos = new Conexion();
                JSONObject json_parametros = new JSONObject();
                json_parametros.put("user_id",id_usuario);
                json_parametros.put("movie_id",id_pelicula);

                String  result_consulta = modificar_favoritos.execute("https://cines35mm.herokuapp.com/favorite_movies","POST",json_parametros.toString()).get();

                if(result_consulta.equals("Created")) {
                    Toast.makeText(this, "Se agregó exitosamente la pelicula a los favoritos.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Ocurrió un error inesperado."+result_consulta.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(this, json_parametros.toString(), Toast.LENGTH_LONG).show();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private boolean esFavorito(JSONArray jsonArreglo) throws JSONException {
        for(int i = 0; i < jsonArreglo.length(); i++){
            JSONObject elemento = jsonArreglo.getJSONObject(i);
            if(elemento.getString("user_id").equals(id_usuario) &&elemento.getString("movie_id").equals(id_pelicula)){
                return true;
            }
        }
        return false;
    }

    private String getIdMovie(JSONArray jsonArreglo) throws JSONException {
        String id_movie = "0";
        for(int i = 0; i < jsonArreglo.length(); i++){
            JSONObject elemento = jsonArreglo.getJSONObject(i);
            if(elemento.getString("user_id").equals(id_usuario) &&elemento.getString("movie_id").equals(id_pelicula)){
                id_movie = elemento.getString("id");
                return id_movie;
            }
        }
        return id_movie;
    }

    private void Abrir_Editar_Pelicula(){
        Intent i = new Intent(this, EditarPelicula.class);
        i.putExtra("id_pelicula",id_pelicula);
        i.putExtra("id_usuario", id_usuario);
        i.putExtra("Nombre",Nombre);
        i.putExtra("Genero", Genero);
        i.putExtra("Director", Director);
        i.putExtra("Anno", Anno);
        i.putExtra("Sipnosis", Sipnosis);
        i.putExtra("Actores", Actores);
        i.putExtra("Portada", Portada);
        this.startActivity(i);

    }

}
