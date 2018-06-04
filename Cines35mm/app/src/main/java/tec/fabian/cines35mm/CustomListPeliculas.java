package tec.fabian.cines35mm;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomListPeliculas extends ArrayAdapter {
    private final Activity context;
    String[] Nombre,Portada,Genero,Director, Anno, Sipnosis, Actores, Calificacion;
    int[] NumeroPelicula;

    ImageView ImViewPortada;
    String Nick;

    int posicion;


    public CustomListPeliculas(Activity context,
                     String[] Nombre,String[] Portada,String[] Genero,String[] Director, String[] Anno, String[] Sipnosis, String[] Actores, String[] Calificacion, String Nick) {
        super(context, R.layout.item_lista_pelicula, Nombre);
        this.context = context;
        this.Nombre = Nombre;
        this.Portada = Portada;
        this.Genero = Genero;
        this.Director = Director;
        this.Anno = Anno;
        this.Sipnosis = Sipnosis;
        this.Actores = Actores;
        this.Calificacion = Calificacion;
        this.Nick = Nick;
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
            Picasso.with(this.getContext()).load(Portada[position]).into(ImViewPortada);
        }

        //Abrir información de pelicula y comentarios
        lbNombre.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                posicion=(Integer) view.getTag();
                Abrir_Info();
            }
        });//*/

        lbGenero.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                posicion=(Integer) view.getTag();
                Abrir_Info();
            }
        });//*/
        lbDirector.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                posicion=(Integer) view.getTag();
                Abrir_Info();
            }
        });//*/
        ImViewPortada.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                posicion=(Integer) view.getTag();
                Abrir_Info();
            }
        });//*/

        return rowView;
    }//*/

    private void Abrir_Info(){
        Intent i = new Intent(context, Tabs_InfoPeli_Comentarios.class);
        i.putExtra("Nick",Nick);
        i.putExtra("Nombre",Nombre[posicion]);
        i.putExtra("Genero", Genero[posicion]);
        i.putExtra("Director", Director[posicion]);
        i.putExtra("Anno", Anno[posicion]);
        i.putExtra("Sipnosis", Sipnosis[posicion]);
        i.putExtra("Actores", Actores[posicion]);
        i.putExtra("Portada", Portada[posicion]);
        getContext().startActivity(i);
    }


}
