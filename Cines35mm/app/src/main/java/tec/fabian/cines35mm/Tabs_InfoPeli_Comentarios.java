package tec.fabian.cines35mm;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class Tabs_InfoPeli_Comentarios extends AppCompatActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_info_peli_comentarios);

        //Mostrar fecha para regresar a la pantalla anterior en la barra de titulo
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Se reciben las variables necesarias
        Intent i=getIntent();
        String Nick = i.getExtras().getString("Nick");
        String Nombre = i.getExtras().getString("Nombre");
        String Genero = i.getExtras().getString("Genero");
        String Director = i.getExtras().getString("Director");
        String Anno = i.getExtras().getString("Anno");
        String Sipnosis = i.getExtras().getString("Sipnosis");
        String Actores = i.getExtras().getString("Actores");
        String Portada = i.getExtras().getString("Portada");

        Bundle args = new Bundle();
        args.putString("Nick", Nick);
        args.putString("Nombre", Nombre);
        args.putString("Genero", Genero);
        args.putString("Director", Director);
        args.putString("Anno", Anno);
        args.putString("Sipnosis", Sipnosis);
        args.putString("Actores", Actores);
        args.putString("Portada", Portada);

        //Poner de título 'Crear Cuenta'
        actionBar.setTitle(Nombre);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contenido_tab);

        mTabHost.addTab(mTabHost.newTabSpec("Información Película").setIndicator("Información Película"),
                fInformacionPelicula.class, args);
        mTabHost.addTab(mTabHost.newTabSpec("Comentarios").setIndicator("Comentarios"),
                fComentarios.class, args);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
