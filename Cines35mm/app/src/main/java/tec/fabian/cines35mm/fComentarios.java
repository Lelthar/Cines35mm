package tec.fabian.cines35mm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class fComentarios extends Fragment {
    // Variables
    private View rootView;
    String Nick, NombrePelicula;
    EditText Comentario;
    TextView labelNoComentarios;
    ListView listaComentarios;

    public fComentarios() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_comentarios, container, false);

        if (getArguments() != null) {
            Nick=getArguments().getString("Nick");
            NombrePelicula=getArguments().getString("Nombre");
        }

        ImageView btnAgregarComentario=rootView.findViewById(R.id.btnAgregarComentario);
        Comentario=rootView.findViewById(R.id.txtComentario);
        labelNoComentarios=rootView.findViewById(R.id.labelNoComentarios);
        listaComentarios=rootView.findViewById(R.id.listComentarios);

        btnAgregarComentario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AgregarComentario();
            }
        });

        ActualizarComentarios();

        return rootView;
    }

    private void ActualizarComentarios(){
        if(!true) {//TODO validar que existan comentarios
            labelNoComentarios.setVisibility(View.VISIBLE);
        }
        else{
            labelNoComentarios.setVisibility(View.INVISIBLE);

            //TODO buscar comentarios de la pelicula con la variable NombrePelicula

            String[] Nicks={"NombrePrueba1","NickPrueba2"};
            String[] Comentarios={"Comentario de prueba","Segundo comentario de prueba para ver si esto funciona correctamente con un comentario relativamente largo, probando probando probando probando probando probando probando probando probando probando probando probando probando probando probando ok ok"};


            CustomListComentarios customListComentarios = new CustomListComentarios(getActivity(),Comentarios,Nicks);

            listaComentarios.setAdapter(customListComentarios);
        }
    }

    private void AgregarComentario(){
        //TODO agregar comentario con las variables Nick, NombrePelicula y Comentario
        Toast.makeText(getContext(),"Agregar comentario",Toast.LENGTH_SHORT).show();
    }
}
