package tec.fabian.cines35mm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

public class MainActivity extends AppCompatActivity {

    //----------------Inicio Declaracion de variables-----------------
    public String nick;
    public String correo;
    public String tipo;
    public String id_usuario;

    // Menu desplegable
    private String[] mMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    android.support.v4.app.FragmentTransaction fragTras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        Intent i=getIntent();
        nick=i.getExtras().getString("nick");
        correo=i.getExtras().getString("correo");
        tipo=i.getExtras().getString("tipoUsuario");
        id_usuario = i.getExtras().getString("id");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //--------------------------Funcionalidad para el menu-------------------------


        if(tipo.equals("Administrador"))
            mMenuTitles = new String[]{"Películas Disponibles", "Agregar Película", "Bloquear/Desbloquear Usuario", "Perfil", "Salir"};
        else
            mMenuTitles = new String[]{"Películas Disponibles", "Recomendaciones", "Favoritas", "Perfil", "Salir"};

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.menu_list, mMenuTitles));

        LayoutInflater inflater = getLayoutInflater();
        // Add header news title
        ViewGroup header_menu= (ViewGroup)inflater.inflate(R.layout.menu_header, mDrawerList, false);
        TextView lbNombre=(TextView) header_menu.findViewById(R.id.labelNombreUsuario);
        lbNombre.setText(nick);
        mDrawerList.addHeaderView(header_menu, null, false);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  // host Activity
                mDrawerLayout,         // DrawerLayout object
                toolbar,//toolbar,
                R.string.drawer_open,  // "open drawer" description
                R.string.drawer_close  // "close drawer" description
        ) {

            // Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
            }

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(tipo.equals("Administrador")) {
            setTitle("Películas Disponibles");
            fDisponibles disponibles = fDisponibles.newInstance(nick,tipo);
            ChangeFrag(disponibles);
        }
        else {
            setTitle("Recomendaciones");
            fRecomendaciones recomendaciones = fRecomendaciones.newInstance(nick,tipo,id_usuario);
            ChangeFrag(recomendaciones);
        }

    }


    // ------------- Menu desplegable -------------
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    private void selectItem(int position) {

        int item=position;
        if(tipo.equals("Administrador")) {
            switch (item) {
                case 1:
                    fDisponibles disponibles = fDisponibles.newInstance(nick,tipo);
                    ChangeFrag(disponibles);
                    break;
                case 2:
                    fAgregarPelicula agregarPelicula= fAgregarPelicula.newInstance();
                    ChangeFrag(agregarPelicula);
                    break;
                case 3:
                    fBloquear_Desbloquear bloquear_desbloquear=fBloquear_Desbloquear.newInstance();
                    ChangeFrag(bloquear_desbloquear);
                    break;
                case 4:
                    fPerfil perfil=fPerfil.newInstance(this.nick, this.correo);
                    ChangeFrag(perfil);
                    break;
                case 5:
                    finish();
                    break;

            }
        }else {
            switch (item) {
                case 1:
                    fDisponibles disponibles = fDisponibles.newInstance(nick,tipo);
                    ChangeFrag(disponibles);
                    break;
                case 2:
                    fRecomendaciones recomendaciones = fRecomendaciones.newInstance(nick,tipo,id_usuario);
                    ChangeFrag(recomendaciones);
                    break;
                case 3:
                    fFavoritas favoritas = fFavoritas.newInstance(nick,tipo);
                    ChangeFrag(favoritas);
                    break;
                case 4:
                    fPerfil perfil=fPerfil.newInstance(this.nick, this.correo);
                    ChangeFrag(perfil);
                    break;
                case 5:
                    finish();
                    break;
            }
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mMenuTitles[position-1]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void ChangeFrag(android.support.v4.app.Fragment F){
        fragTras=getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,F);
        fragTras.commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

}
