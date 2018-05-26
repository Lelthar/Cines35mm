package tec.fabian.cines35mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class fBloquear_Desbloquear extends Fragment {

    //Variables
    View rootView;

    public fBloquear_Desbloquear() {
        // Required empty public constructor
    }

    public static fBloquear_Desbloquear newInstance() {
        fBloquear_Desbloquear fragment = new fBloquear_Desbloquear();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_bloquear__desbloquear, container, false);
        Toast.makeText(rootView.getContext(), "------ WIP Bloquear/Desbloquear ------", Toast.LENGTH_SHORT).show();
        //TODO metodo para buscar usuario
        //TODO mostrar el boton de desbloquear o bloquear segun el usuario encontrado
        //TODO metodo para bloquear/desbloquear usuario
        return rootView;
    }

}
