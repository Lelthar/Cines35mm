package tec.fabian.cines35mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class fAgregarPelicula extends Fragment {

    // Variables
    private View rootView;

    public fAgregarPelicula() {
        // Required empty public constructor
    }

    public static fAgregarPelicula newInstance() {
        fAgregarPelicula fragment = new fAgregarPelicula();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_agregar_pelicula, container, false);

        Toast.makeText(rootView.getContext(),"-- WIP Agregar Pelicula --",Toast.LENGTH_SHORT).show();

        return rootView;
    }

}
