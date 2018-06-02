package tec.fabian.cines35mm;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class fAgregarPelicula extends Fragment {

    // Variables
    private View rootView;
    private ImageView portada_pelicula;
    private EditText nombre_pelicula;
    private EditText director_pelicula;
    private EditText generos_pelicula;
    private EditText anho_estreno;
    private EditText actores_principales;
    private EditText sinopsis;
    private Conexion conexion;
    private Button btnAgregarPelicula;
    private Button btnAgregarPortada;

    private String path_portada;

    private static final int SELECT_PICTURE=3513;
    private static final int PERMISSION_REQUEST_CODE=5468;

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
        //Se inicializan las variables para los objetos de la interfaz
        portada_pelicula=rootView.findViewById(R.id.imgPortada);
        nombre_pelicula = rootView.findViewById(R.id.txtNombre);
        director_pelicula = rootView.findViewById(R.id.txtDirector);
        generos_pelicula = rootView.findViewById(R.id.txtGenero);
        anho_estreno = rootView.findViewById(R.id.txtAnnoEstreno);
        actores_principales = rootView.findViewById(R.id.txtActoresPrincipales);
        sinopsis = rootView.findViewById(R.id.txtSipnosis);
        btnAgregarPelicula = rootView.findViewById(R.id.btnAgregarPelicula);
        btnAgregarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onClickedAddMovie();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        btnAgregarPortada = rootView.findViewById(R.id.btnAgregarPortada);
        btnAgregarPortada.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MostrarPortada();
            }
        });

        return rootView;
    }

    //Este metodo agrega la pelicula a la base de datos
    public void onClickedAddMovie() throws JSONException, ExecutionException, InterruptedException {
        String strNombrePelicula = nombre_pelicula.getText().toString();
        String strDirectorPelicula = director_pelicula.getText().toString();
        String strGenerosPelicula = generos_pelicula.getText().toString();
        String strAnhoEstreno = anho_estreno.getText().toString();
        String strActoresPrincipales = actores_principales.getText().toString();
        String strSinopsis = sinopsis.getText().toString();

        //TODO Utilizar path_portada para la imagen

        //Revisa que los Edittext no estén vacios
        if(!strNombrePelicula.isEmpty() && !strDirectorPelicula.isEmpty() && !strGenerosPelicula.isEmpty() && !strAnhoEstreno.isEmpty() && !strActoresPrincipales.isEmpty() && !strSinopsis.isEmpty()){
            conexion = new Conexion();

            //Agrega los parametros al objeto JSON
            JSONObject json_parametros = new JSONObject();
            json_parametros.put("nombre",strNombrePelicula);
            json_parametros.put("director",strDirectorPelicula);
            json_parametros.put("genero",strGenerosPelicula);
            json_parametros.put("anho_estreno",strAnhoEstreno);
            json_parametros.put("actores_principales",strActoresPrincipales);
            json_parametros.put("sinopsis",strSinopsis);
            json_parametros.put("url_imagen","insertar url aqui");
            String  result = conexion.execute("https://cines35mm.herokuapp.com/movies","POST",json_parametros.toString()).get();

            if(result.equals("Created")) {
                Toast.makeText(rootView.getContext(), "Se creó exitosamente la cuenta.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(rootView.getContext(), "Ocurrió un error inesperado."+result.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(rootView.getContext(), json_parametros.toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(rootView.getContext(), "Una de las casillas está vacia.", Toast.LENGTH_LONG).show();
        }
    }

    // ------------------ Elegir imagen para mostrar portada ----------
    public void MostrarPortada(){
        if(Build.VERSION.SDK_INT >=23) {
            if (checkPermission()){
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

            }else{
                requestPermission();
            }
        }else{
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri filePath = data.getData();
                if (null != filePath) {
                    try {
                        ContentResolver contentResolver=getActivity().getApplicationContext().getContentResolver();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath);
                        portada_pelicula.setImageBitmap(bitmap);

                        path_portada=filePath.getPath();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE );
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MostrarPortada();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(rootView.getContext(), "No se puede acceder a la imagenes sin permisos", Toast.LENGTH_SHORT).show();
                    }else{
                        new AlertDialog.Builder(rootView.getContext())
                                .setTitle("Acceso denegado")
                                .setMessage("No se puede acceder a la imagenes sin permisos. Por favor active los permisos en la configuración de la aplicación.")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();//*/
                    }
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
            }
        }
    }


    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //Query bitmap without allocating memory
        options.inJustDecodeBounds = true;
        //decode file from path
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        //decode according to configuration or according best match
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;
        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;
        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }
        //if value is greater than 1,sub sample the original image
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public void onDetach() {
        super.onDetach();
    }

}
