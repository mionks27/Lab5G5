package pe.pucp.tel306.firebox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AgregarFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_file);
    }

    public void pickFile(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    String filePath = String.valueOf(data.getData());
                    TextView archivo = findViewById(R.id.textViewPath);
                    archivo.setText(filePath);
                }
                break;
        }
    }

    ///METODOS STORAGE - ROYER

    public void subirArchivoConPutStream(View view) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView textView = findViewById(R.id.textViewPath);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(externalStoragePublicDirectory, "pucp.jpg"); //pucp.jpg es el archivo que está en la carpeta PICTURES en android.
            try {
                InputStream inputStream = new FileInputStream(file);

                storageReference.child(firebaseUser.getUid() +"/"+ textView.getText()).putStream(inputStream) ///el nombre con el que va a aparecer en firebase storage
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("infoApp", "subida exitosa");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("infoApp", "error en la subida");
                                e.printStackTrace();
                            }
                        });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

    }

    //en caso el permiso sea exitoso o denegado:
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1) {
                //subirArchivoPutStream(null);
            } else if (requestCode == 2) {
                //subirArchivoConPutFile(null);
            } else if (requestCode == 3) {
                //descargarDocumento(null);
            }
        }
    }

    ///fin stream

}