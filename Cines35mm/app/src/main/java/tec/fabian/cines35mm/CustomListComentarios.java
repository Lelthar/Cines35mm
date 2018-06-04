package tec.fabian.cines35mm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListComentarios extends ArrayAdapter {
    private final Activity context;
    String[] Nick,Comentario;


    public CustomListComentarios(Activity context,
                               String[] Comentario, String[] Nick) {
        super(context, R.layout.item_lista_pelicula, Nick);
        this.context = context;
        this.Comentario = Comentario;
        this.Nick = Nick;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_lista_comentarios, null, true);
        TextView lbNick = rowView.findViewById(R.id.lbNick);
        TextView lbComentario = rowView.findViewById(R.id.lbComentario);

        lbNick.setText(Nick[position]);
        lbComentario.setText(Comentario[position]);

        return rowView;
    }//*/
}
