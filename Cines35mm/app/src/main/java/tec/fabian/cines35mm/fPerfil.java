package tec.fabian.cines35mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class fPerfil extends Fragment {

    ///Variables
    View rootView;

    public fPerfil() {
        // Required empty public constructor
    }

    public static fPerfil newInstance() {
        fPerfil fragment = new fPerfil();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_perfil, container, false);
        Toast.makeText(rootView.getContext(), "------ WIP Perfil ------", Toast.LENGTH_SHORT).show();

        return rootView;
    }

}
