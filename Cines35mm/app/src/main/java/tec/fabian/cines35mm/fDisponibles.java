package tec.fabian.cines35mm;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class fDisponibles extends Fragment {

    // Variables
    private View rootView;

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
        Toast.makeText(rootView.getContext(),"-- WIP Disponibles --",Toast.LENGTH_SHORT).show();

        //TODO metodo para mostrar las peliculas disponibles
        //TODO metodo para buscar peliculas disponibles
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
