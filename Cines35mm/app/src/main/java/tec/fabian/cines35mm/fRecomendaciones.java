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
        //TODO mostrar recomendaciones

        return rootView;
    }

    private void Actualizar_Peliculas() {
        if (true) { //TODO verificar que existan recomendaciones
            NoPeliculas.setVisibility(View.VISIBLE);
            return;
        } else {
            NoPeliculas.setVisibility(View.INVISIBLE);

            CustomListPeliculas adapter = new CustomListPeliculas();//TODO

            //TODO
            //ListaPeliculas.setAdapter(adapter);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}
