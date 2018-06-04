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

public class Tabs_InfoPeli_Comentarios extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    String Nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_info_peli_comentarios);

        //Mostrar fecha para regresar a la pantalla anterior en la barra de titulo
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Se reciben las variables necesarias
        Intent i=getIntent();
        Nick = i.getExtras().getString("Nick");
        String Nombre = i.getExtras().getString("Nombre");
        String Genero = i.getExtras().getString("Genero");
        String Director = i.getExtras().getString("Director");
        String Anno = i.getExtras().getString("Anno");
        String Sipnosis = i.getExtras().getString("Sipnosis");
        String Actores = i.getExtras().getString("Actores");
        String Portada = i.getExtras().getString("Portada");
        String tipoUsuario=i.getExtras().getString("tipoUsuario");

        Bundle args1 = new Bundle();
        args1.putString("Nick", Nick);
        args1.putString("Nombre", Nombre);

        Bundle args2 = new Bundle();
        args2.putString("tipoUsuario", tipoUsuario);
        args2.putString("Nombre", Nombre);
        args2.putString("Genero", Genero);
        args2.putString("Director", Director);
        args2.putString("Anno", Anno);
        args2.putString("Sipnosis", Sipnosis);
        args2.putString("Actores", Actores);
        args2.putString("Portada", Portada);

        //Poner de título 'Crear Cuenta'
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
                //TODO agregar metodo para agregar a favoritas (con Nick puede buscar el usuario)
                return true;
            }
            case R.id.agregarCalificacion: {
                CharSequence calificaciones[] = new CharSequence[]{"1", "2", "3", "4", "5"};
                new AlertDialog.Builder(this)
                        .setItems(calificaciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // el usuario selecciona calificaciones[which]
                                //TODO agregar metodo para calificar pelicula (con Nick puede buscar el usuario)
                            }
                        })
                        .show();//*/
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
}
