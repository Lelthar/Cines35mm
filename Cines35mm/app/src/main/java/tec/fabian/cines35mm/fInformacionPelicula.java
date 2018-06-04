package tec.fabian.cines35mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class fInformacionPelicula extends Fragment {
    // Variables
    private View rootView;

    public fInformacionPelicula() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_informacion_pelicula, container, false);


        String Nick=new String();
        String Nombre=new String();
        String Genero=new String();
        String Director=new String();
        String Anno=new String();
        String Sipnosis=new String();
        String Actores=new String();
        String Portada=new String();

        if (getArguments() != null) {
            Nick = getArguments().getString("Nick");
            Nombre = getArguments().getString("Nombre");
            Genero = getArguments().getString("Genero");
            Director = getArguments().getString("Director");
            Anno = getArguments().getString("Anno");
            Sipnosis = getArguments().getString("Sipnosis");
            Actores = getArguments().getString("Actores");
            Portada = getArguments().getString("Portada");

        }

        ImageView imgPortada = rootView.findViewById(R.id.imgPortada);
        TextView lbNombrePelicula = rootView.findViewById(R.id.lbNombrePelicula);
        TextView lbGenero = rootView.findViewById(R.id.lbGenero);
        TextView lbDirector = rootView.findViewById(R.id.lbDirector);
        TextView lbAnno = rootView.findViewById(R.id.lbAnno);
        TextView lbActores = rootView.findViewById(R.id.lbActores);
        TextView lbSipnosis = rootView.findViewById(R.id.lbSipnosis);

        if(!TextUtils.isEmpty(Portada)){
            Picasso.with(this.getContext()).load(Portada).into(imgPortada);
        }

        lbNombrePelicula.setText(Nombre);
        lbGenero.setText("Género: "+Genero);
        lbDirector.setText("Director: "+Director);
        lbAnno.setText("Año: "+Anno);
        lbActores.setText("Actores: "+Actores);
        lbSipnosis.setText("Sipnosis: "+Sipnosis);


        return rootView;
    }

}
