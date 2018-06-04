package tec.fabian.cines35mm;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class EditarPelicula extends AppCompatActivity {

    // Variables
    private ImageView portada_pelicula;
    private EditText nombre_pelicula;
    private EditText director_pelicula;
    private EditText genero_pelicula;
    private EditText anho_estreno;
    private EditText actores_principales;
    private EditText sinopsis;
    private Conexion conexion;
    private Button btnAgregarPelicula;

    private String txt_nombre_pelicula;
    private String txt_director_pelicula;
    private String txt_genero_pelicula;
    private String txt_anho_estreno;
    private String txt_actores_principales;
    private String txt_sinopsis;
    private String txt_portada_pelicula;

    private String id_pelicula,id_usuario;

    private String path_portada;

    private static final int SELECT_PICTURE=3513;
    private static final int PERMISSION_REQUEST_CODE=5468;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_agregar_pelicula);

        View layout= findViewById(R.id.editarPelicula);
        layout.setBackgroundColor(Color.parseColor("#5E5E5E"));

        //Mostrar fecha para regresar a la pantalla anterior en la barra de titulo
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i=getIntent();
        id_pelicula=i.getExtras().getString("id_pelicula");
        id_usuario=i.getExtras().getString("id_usuario");

        txt_nombre_pelicula=i.getExtras().getString("Nombre");
        txt_director_pelicula=i.getExtras().getString("Director");
        txt_genero_pelicula=i.getExtras().getString("Genero");
        txt_anho_estreno=i.getExtras().getString("Anno");
        txt_actores_principales=i.getExtras().getString("Actores");
        txt_sinopsis=i.getExtras().getString("Sipnosis");
        txt_portada_pelicula=i.getExtras().getString("Portada");


        //Se inicializan las variables para los objetos de la interfaz
        portada_pelicula=findViewById(R.id.imgPortada);
        nombre_pelicula = findViewById(R.id.txtNombre);
        director_pelicula = findViewById(R.id.txtDirector);
        genero_pelicula = findViewById(R.id.txtGenero);
        anho_estreno = findViewById(R.id.txtAnnoEstreno);
        actores_principales = findViewById(R.id.txtActoresPrincipales);
        sinopsis = findViewById(R.id.txtSipnosis);
        btnAgregarPelicula = findViewById(R.id.btnAgregarPelicula);
        btnAgregarPelicula.setText("Guardar Cambios");

        if(!TextUtils.isEmpty(txt_portada_pelicula)){
            Picasso.with(this).load(txt_portada_pelicula).into(portada_pelicula);
        }

        nombre_pelicula.setText(txt_nombre_pelicula);
        director_pelicula.setText(txt_director_pelicula);
        genero_pelicula.setText(txt_genero_pelicula);
        anho_estreno.setText(txt_anho_estreno);
        actores_principales.setText(txt_actores_principales);
        sinopsis.setText(txt_sinopsis);

        Button btnAgregarPortada = findViewById(R.id.btnAgregarPortada);
        btnAgregarPortada.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MostrarPortada();
            }
        });

        btnAgregarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // try {
                //TODO Actualizar datos pelicula
               /* } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });
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
                        path_portada =  filePath.getPath();
                        Log.d("PATH", filePath.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE );
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(EditarPelicula.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MostrarPortada();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EditarPelicula.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(this, "No se puede acceder a la imagenes sin permisos", Toast.LENGTH_SHORT).show();
                    }else{
                        new AlertDialog.Builder(this)
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

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
