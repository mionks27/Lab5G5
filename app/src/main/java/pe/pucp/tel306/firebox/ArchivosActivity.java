package pe.pucp.tel306.firebox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ArchivosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivos);
    }

    public void agregarArchivo(View view){
        startActivity(new Intent(ArchivosActivity.this, AgregarFileActivity.class));
    }



    public void pickFile(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent,"Seleccione Archivo para subir"),10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    Uri filePath = data.getData();
                    System.out.println(filePath);
                    subirArchivoConPutFile(filePath);
                }
                break;
        }
    }

    ///METODOS STORAGE - ROYER

    public void subirArchivoConPutFile(Uri uri) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageMetadata storageMetadata = new StorageMetadata.Builder()
                    .setCustomMetadata("autor", "Grupo 5")
                    .build();

            UploadTask task = storageReference
                    .child(firebaseUser.getUid()).child("Prueba.jpg") ///nombre a colocar en firebase
                    .putFile(uri, storageMetadata);


            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("infoApp", "subida exitosa");
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("infoApp", "error en la subida");
                    e.printStackTrace();
                }
            });
            task.addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {

                }
            });
            task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    long bytesTransferred = snapshot.getBytesTransferred();
                    long totalByteCount = snapshot.getTotalByteCount();

                    double progreso = (100.0 * bytesTransferred) / totalByteCount;

                    Log.d("progreso", String.valueOf(progreso));

                }
            });

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
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
}