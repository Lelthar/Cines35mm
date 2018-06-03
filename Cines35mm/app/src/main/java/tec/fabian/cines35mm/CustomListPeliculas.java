package tec.fabian.cines35mm;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import static java.security.AccessController.getContext;

public class CustomListPeliculas extends ArrayAdapter {
    private final Activity context;
    String[] Nombre,Portada,Genero,Director,Calificacion;
    int[] NumeroPelicula;

    ImageView ImViewPortada;

    int posicion;


    public CustomListPeliculas(Activity context,
                     String[] Nombre,String[] Portada,String[] Genero,String[] Director,String[] Calificacion) {
        super(context, R.layout.item_lista_pelicula, Nombre);
        this.context = context;
        this.Nombre = Nombre;
        this.Portada = Portada;
        this.Genero = Genero;
        this.Director = Director;
        this.Calificacion = Calificacion;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_lista_pelicula, null, true);
        TextView lbNombre = (TextView) rowView.findViewById(R.id.lbNombrePelicula);
        TextView lbGenero = (TextView) rowView.findViewById(R.id.lbGenero);
        TextView lbDirector = (TextView) rowView.findViewById(R.id.lbDirector);
        //TextView lbCalificacion = (TextView) rowView.findViewById(R.id.lbCalificacion);
        ImViewPortada = (ImageView) rowView.findViewById(R.id.imgPortada);

        lbNombre.setText(Nombre[position]);
        lbGenero.setText(Genero[position]);
        lbDirector.setText(Director[position]);

        lbNombre.setTag(position);
        lbGenero.setTag(position);
        lbDirector.setTag(position);
        ImViewPortada.setTag(position);



        if (!TextUtils.isEmpty(Portada[position])) {
            //TODO poner imagen en el ImageView
            //ImViewPortada.;
        }

        return rowView;
    }//*/
}
