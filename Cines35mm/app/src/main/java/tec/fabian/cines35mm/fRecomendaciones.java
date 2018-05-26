package tec.fabian.cines35mm;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class fRecomendaciones extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Variables
    private View rootView;
    private String mParam2;

    public fRecomendaciones() {
        // Required empty public constructor
    }

    public static fRecomendaciones newInstance(String param1, String param2) {
        fRecomendaciones fragment = new fRecomendaciones();
        //TODO recibir correo para buscar las favoritas y calcular recomendaciones
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recomendaciones, container, false);
        Toast.makeText(rootView.getContext(),"-- WIP Recomendaciones --",Toast.LENGTH_SHORT).show();

        //TODO metodo para calcular recomendaciones
        //TODO mostrar recomendaciones

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
