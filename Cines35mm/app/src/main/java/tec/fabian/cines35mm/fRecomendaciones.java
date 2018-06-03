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


public class fRecomendaciones extends Fragment {
    // Variables
    private View rootView;
    private TextView NoPeliculas;
    ListView ListaPeliculas;

    CustomListPeliculas adapter;

    public fRecomendaciones() {
        // Required empty public constructor
    }

    public static fRecomendaciones newInstance(String param1, String param2) {
        fRecomendaciones fragment = new fRecomendaciones();
        //TODO recibir correo para buscar las favoritas y calcular recomendaciones

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recomendaciones, container, false);
        Toast.makeText(rootView.getContext(),"-- WIP Recomendaciones --",Toast.LENGTH_SHORT).show();

        NoPeliculas = (TextView) rootView.findViewById(R.id.labelNoRecomendaciones);
        ListaPeliculas=(ListView) rootView.findViewById(R.id.listRecomendaciones);

        //TODO metodo para calcular recomendaciones
        Actualizar_Peliculas();

        return rootView;
    }

    private void Actualizar_Peliculas() {
        // TODO Mae Gerald yo metia las varas de la BD en una variable tipo cursor entonces asi verificaba esto, y mas adelante definia el tamaño de los arrays de Nombre, Genero... con .getCount() del cursor
        if (!true) { //TODO verificar que existan recomendaciones (Si la BD devuelve peliculas)
            NoPeliculas.setVisibility(View.VISIBLE);
        } else {
            NoPeliculas.setVisibility(View.INVISIBLE);

            //TODO cambiar este Custom List por los datos recuperados de la BD
            String[] Nombre={"Prueba 1","Nombre prueba 2"};
            String[] Genero={"Prueba 1 G","Genero prueba 2"};
            String[] Director={"Prueba 1 D","Director prueba 2"};
            String[] Calificacion={null,null};
            String[] ImgPortada={null,null};

            adapter = new CustomListPeliculas(this.getActivity(),Nombre,ImgPortada,Genero,Director,Calificacion);

            //TODO
            ListaPeliculas.setAdapter(adapter);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}
