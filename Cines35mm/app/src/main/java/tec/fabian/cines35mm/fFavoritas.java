package tec.fabian.cines35mm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class fFavoritas extends Fragment {
    // Variables
    private View rootView;

    public fFavoritas() {
        // Required empty public constructor
    }

    public static fFavoritas newInstance() {
        fFavoritas fragment = new fFavoritas();
        //TODO recibir correo para buscar las peliculas favoritas
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recomendaciones, container, false);
        Toast.makeText(rootView.getContext(),"-- WIP Favoritas --",Toast.LENGTH_SHORT).show();

        TextView labelNoFavoritas=(TextView) rootView.findViewById(R.id.labelNoRecomendaciones);
        labelNoFavoritas.setText("No se han seleccionado películas favoritas");

        //TODO crear metodo para recuperar peliculas favoritas
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
