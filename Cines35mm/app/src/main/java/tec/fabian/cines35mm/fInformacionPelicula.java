package tec.fabian.cines35mm;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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


        String Nombre=new String();
        String Genero=new String();
        String Director=new String();
        String Anno=new String();
        String Sipnosis=new String();
        String Actores=new String();
        String Portada=new String();
        String Calificacion=new String();

        if (getArguments() != null) {

            Nombre = getArguments().getString("Nombre");
            Genero = getArguments().getString("Genero");
            Director = getArguments().getString("Director");
            Anno = getArguments().getString("Anno");
            Sipnosis = getArguments().getString("Sipnosis");
            Actores = getArguments().getString("Actores");
            Portada = getArguments().getString("Portada");
            Calificacion = getArguments().getString("Calificacion");

        }

        ImageView imgPortada = rootView.findViewById(R.id.imgPortada);
        TextView lbNombrePelicula = rootView.findViewById(R.id.lbNombrePelicula);
        TextView lbGenero = rootView.findViewById(R.id.lbGenero);
        TextView lbDirector = rootView.findViewById(R.id.lbDirector);
        TextView lbAnno = rootView.findViewById(R.id.lbAnno);
        TextView lbActores = rootView.findViewById(R.id.lbActores);
        TextView lbSipnosis = rootView.findViewById(R.id.lbSipnosis);
        TextView lbCalificacion = rootView.findViewById(R.id.lbCalificacion);

        if(!TextUtils.isEmpty(Portada)){
            Picasso.with(this.getContext()).load(Portada).into(imgPortada);
        }

        lbNombrePelicula.setText(Nombre);
        lbGenero.setText("Género: "+Genero);
        lbDirector.setText("Director: "+Director);
        lbAnno.setText("Año: "+Anno);
        lbActores.setText("Actores: "+Actores);
        lbSipnosis.setText("Sipnosis: "+Sipnosis);
        lbCalificacion.setText("Calificación: "+Calificacion);


        return rootView;
    }


    //------- Agregar menu de opciones --------
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        String tipoUsuario=null;
        if (getArguments() != null) {
            tipoUsuario = getArguments().getString("tipoUsuario");
        }

        if(tipoUsuario!=null)
            if (tipoUsuario.equals("Cliente"))
                inflater.inflate(R.menu.menu_opciones, menu);
            else
                inflater.inflate(R.menu.menu_opciones_administrador, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

}
