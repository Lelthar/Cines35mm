package tec.fabian.cines35mm;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
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

        AWSMobileClient.getInstance().initialize(rootView.getContext(), new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                //Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();
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

        //Revisa que los Edittext no estén vacios
        if(!strNombrePelicula.isEmpty() && !strDirectorPelicula.isEmpty() && !strGenerosPelicula.isEmpty() && !strAnhoEstreno.isEmpty() && !strActoresPrincipales.isEmpty() && !strSinopsis.isEmpty()){
            conexion = new Conexion();
            uploadImageS3(strNombrePelicula.replaceAll("\\s",""));
            String urlPelicula = "https://s3.amazonaws.com/cinesmmapp-userfiles-mobilehub-1517008059/uploads/"+strNombrePelicula.replaceAll("\\s","")+".jpg";
            //Agrega los parametros al objeto JSON
            JSONObject json_parametros = new JSONObject();
            json_parametros.put("nombre",strNombrePelicula);
            json_parametros.put("director",strDirectorPelicula);
            json_parametros.put("genero",strGenerosPelicula);
            json_parametros.put("anho_estreno",strAnhoEstreno);
            json_parametros.put("actores_principales",strActoresPrincipales);
            json_parametros.put("sinopsis",strSinopsis);
            json_parametros.put("url_imagen",urlPelicula);
            String  result = conexion.execute("https://cines35mm.herokuapp.com/movies","POST",json_parametros.toString()).get();

            if(result.equals("Created")) {
                Toast.makeText(rootView.getContext(), "Se creó exitosamente la pelicula.", Toast.LENGTH_LONG).show();
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
                        portada_pelicula.setImageURI(filePath);
                        path_portada = getFilePath(rootView.getContext(),filePath);
                        Log.d("PATH", filePath.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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

    public void onDetach() {
        super.onDetach();
    }

    private void uploadImageS3(String nombreImagen){
	//Agregar el keypublico y local cuando se vaya a correr, borrarlo cuando se vaya a subir a github
        BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAI3DJRR55SRNPI2DA", "6W0RL34aDupHFlTLDSnQYgw5+5KFW0WhWWUB5ALq");
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(rootView.getContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        // "jsaS3" will be the folder that contains the file
        TransferObserver uploadObserver =
                transferUtility.upload("uploads/" + nombreImagen+".jpg",new File(path_portada));

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed download.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        // If your upload does not trigger the onStateChanged method inside your
        // TransferListener, you can directly check the transfer state as shown here.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }
    }

}
