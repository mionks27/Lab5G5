package pe.pucp.tel306.firebox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AgregarFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_file);
    }

    public void pickFile(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

            startActivityForResult(Intent.createChooser(intent,"Seleccione un archivo para subir"),1);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    String filePath = data.getData().getPath();
                    TextView archivo = findViewById(R.id.textViewPath);
                    archivo.setText(filePath);
                }
                break;
        }
    }
}